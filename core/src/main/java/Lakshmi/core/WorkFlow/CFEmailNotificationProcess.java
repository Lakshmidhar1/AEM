package Lakshmi.core.WorkFlow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label=CF Email Notification Process"
        }
)
public class CFEmailNotificationProcess implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(CFEmailNotificationProcess.class);

    private static final String SUBSERVICE = "cf-workflow-service";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private MessageGatewayService messageGatewayService;

    @Reference
    private Replicator replicator;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args)
            throws WorkflowException {

        log.info("CFEmailNotificationProcess started for workflow ID: {}", workItem.getWorkflow().getId());

        ResourceResolver resolver = null;

        try {
            // 1) Get ResourceResolver using service user
            Map<String, Object> authInfo = new HashMap<>();
            authInfo.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
            resolver = resolverFactory.getServiceResourceResolver(authInfo);
            log.info("Service ResourceResolver obtained successfully.");

            // 2) Extract payload path
            WorkflowData workflowData = workItem.getWorkflowData();
            String payload = workflowData.getPayload().toString();
            String path = payload.startsWith("jcr:") ? payload.substring(payload.indexOf(":") + 1) : payload;
            log.info("Workflow payload path: {}", path);

            // 3) Get resource
            Resource resource = resolver.getResource(path);
            String title = "";
            String author = "";

            if (resource != null) {
                Resource contentNode = resource.getChild("jcr:content");
                ValueMap props = (contentNode != null) ? contentNode.getValueMap() : resource.getValueMap();

                title = props.get("jcr:title", props.get("title", ""));
                author = props.get("cq:lastModifiedBy", props.get("cq:createdBy", ""));

                log.info("Resource found. Title: {}, Author: {}", title, author);
            } else {
                log.warn("Resource not found at path: {}", path);
            }

            // 4) Get recipient email from workflow arguments (configurable in workflow step), fallback to hardcoded
            String recipientEmail = args.get("recipientEmail", "somamanojshm.22@gmail.com");
            log.info("Using recipient email: {}", recipientEmail);

            // 5) Prepare email
            HtmlEmail email = new HtmlEmail();
            email.setCharset("UTF-8");
            email.addTo(recipientEmail);
            email.setFrom("lakshmidharreddykuruguntla8742@gmail.com");
            email.setSubject("Content Fragment Approved: " + (title != null && !title.isEmpty() ? title : path));

            StringBuilder body = new StringBuilder();
            body.append("<p>The Content Fragment has been <strong>approved</strong>.</p>");
            body.append("<p><b>Path:</b> ").append(path).append("</p>");
            if (title != null && !title.isEmpty()) {
                body.append("<p><b>Title:</b> ").append(title).append("</p>");
            }
            if (author != null && !author.isEmpty()) {
                body.append("<p><b>Author:</b> ").append(author).append("</p>");
            }
            body.append("<p><a href=\"").append(getAuthorUrl(path)).append("\">Open in AEM</a></p>");
            email.setHtmlMsg(body.toString());

            log.info("Email prepared successfully for path: {}", path);

            // 6) Send email
            MessageGateway<HtmlEmail> gateway = (MessageGateway<HtmlEmail>) messageGatewayService.getGateway(HtmlEmail.class);
            if (gateway != null) {
                gateway.send(email);
                log.info("Email sent successfully to recipient: {}", recipientEmail);
            } else {
                log.error("MessageGateway not available.");
                throw new WorkflowException("MessageGateway not available");
            }
            // 7) Approve for publication by replicating (activating) the Content Fragment
            if (resource != null) {
                try {
                    Session session = resolver.adaptTo(Session.class);
                    if (session != null) {
                        replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
                        log.info("Content Fragment replicated (activated) for publication at path: {}", path);
                    } else {
                        log.error("Failed to adapt ResourceResolver to JCR Session, replication skipped.");
                    }
                } catch (ReplicationException e) {
                    log.error("Replication failed for path: {}", path, e);
                    // optional: throw new WorkflowException("Replication failed", e);
                }
            } else {
                log.warn("Skipping replication as resource not found at path: {}", path);
            }


        } catch (LoginException e) {
            log.error("Service ResourceResolver login failed", e);
            throw new WorkflowException("Service ResourceResolver login failed", e);
        } catch (EmailException e) {
            log.error("Email sending failed", e);
            throw new WorkflowException("Email sending failed", e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
                log.info("ResourceResolver closed.");
            }
        }

        log.info("CFEmailNotificationProcess completed for workflow ID: {}", workItem.getWorkflow().getId());
    }

    private String getAuthorUrl(String path) {
        return "http://localhost:4502/editor.html" + path;
    }
}