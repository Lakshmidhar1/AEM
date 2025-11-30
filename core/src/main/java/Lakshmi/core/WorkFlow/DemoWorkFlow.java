package Lakshmi.core.WorkFlow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service = WorkflowProcess.class, immediate = true,enabled = true)
public class DemoWorkFlow implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(DemoWorkFlow.class);
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {

        String payLoadPath = workItem.getWorkflowData().getPayloadType();
        log.info("payLoadType={}", payLoadPath);
        if (payLoadPath.equals("JCR_PATH")) {
            String path = workItem.getWorkflowData().getPayload().toString();
            log.info("path={}", path);
        }

    }
}
