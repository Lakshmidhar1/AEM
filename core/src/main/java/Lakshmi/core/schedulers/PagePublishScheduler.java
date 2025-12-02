package Lakshmi.core.schedulers;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(
        service = Runnable.class,
        immediate = true

)
public class PagePublishScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(PagePublishScheduler.class);

    private static final String SUBSERVICE_NAME = "replication-service";

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Replicator replicator;

    private String schedulerName;
    private String cronExpression;
    private String rootPagePath;
    private boolean enabled;

    @Activate
    protected void activate(PagePublishSchedulerConfig config) {
        this.schedulerName = config.scheduler_name();
        this.cronExpression = config.scheduler_expression();
        this.rootPagePath = config.root_page_path();
        this.enabled = config.enabled();

        LOG.info("Activating PagePublishScheduler with name={}, cron={}, rootPath={}, enabled={}",
                schedulerName, cronExpression, rootPagePath, enabled);

        if (enabled) {
            scheduleJob();
        } else {
            LOG.info("Scheduler is disabled via configuration.");
        }
    }

    @Modified
    protected void modified(PagePublishSchedulerConfig config) {
        // Unschedule old job
        unscheduleJob();

        this.schedulerName = config.scheduler_name();
        this.cronExpression = config.scheduler_expression();
        this.rootPagePath = config.root_page_path();
        this.enabled = config.enabled();

        LOG.info("Modifying PagePublishScheduler with name={}, cron={}, rootPath={}, enabled={}",
                schedulerName, cronExpression, rootPagePath, enabled);

        if (enabled) {
            scheduleJob();
        } else {
            LOG.info("Scheduler is disabled via configuration.");
        }
    }

    @Deactivate
    protected void deactivate() {
        LOG.info("Deactivating PagePublishScheduler {}", schedulerName);
        unscheduleJob();
    }

    private void scheduleJob() {
        try {
            ScheduleOptions options = scheduler.EXPR(cronExpression);
            options.name(schedulerName);
            options.canRunConcurrently(false); // no parallel runs

            scheduler.schedule(this, options);
            LOG.info("Scheduled PagePublishScheduler with expression {}", cronExpression);
        } catch (Exception e) {
            LOG.error("Error scheduling PagePublishScheduler", e);
        }
    }

    private void unscheduleJob() {
        try {
            if (schedulerName != null) {
                scheduler.unschedule(schedulerName);
                LOG.info("Unscheduled job {}", schedulerName);
            }
        } catch (Exception e) {
            LOG.warn("Error unscheduling job {}", schedulerName, e);
        }
    }

    @Override
    public void run() {
        LOG.info("PagePublishScheduler triggered for root path: {}", rootPagePath);

        try (ResourceResolver serviceResolver = getServiceResourceResolver()) {

            if (serviceResolver == null) {
                LOG.error("Could not obtain service ResourceResolver, aborting publish job.");
                return;
            }

            PageManager pageManager = serviceResolver.adaptTo(PageManager.class);
            if (pageManager == null) {
                LOG.error("Could not adapt ResourceResolver to PageManager, aborting publish job.");
                return;
            }

            Page rootPage = pageManager.getPage(rootPagePath);
            if (rootPage == null) {
                LOG.error("Root page {} not found, aborting publish job.", rootPagePath);
                return;
            }

            LOG.info("Starting publishing of pages under {}", rootPagePath);
            Session session = serviceResolver.adaptTo(Session.class);

            if (session == null) {
                LOG.error("Could not adapt ResourceResolver to JCR Session, aborting.");
                return;
            }

            // Publish the root page itself
            publishPage(rootPage, session);

            // Publish all child pages recursively
            Iterator<Page> children = rootPage.listChildren(null, true); // recursive = true
            while (children.hasNext()) {
                Page child = children.next();
                publishPage(child, session);
            }

            LOG.info("Finished publishing pages under {}", rootPagePath);

        } catch (Exception e) {
            LOG.error("Error while running PagePublishScheduler", e);
        }
    }

    private void publishPage(Page page, Session session) {
        String path = page.getPath();
        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
            LOG.info("Published page: {}", path);
        } catch (Exception e) {
            LOG.error("Failed to publish page: {}", path, e);
        }
    }

    private ResourceResolver getServiceResourceResolver() {
        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE_NAME);

        try {
            return resourceResolverFactory.getServiceResourceResolver(authInfo);
        } catch (LoginException e) {
            LOG.error("Failed to get service ResourceResolver using subservice {}", SUBSERVICE_NAME, e);
        }
        return null;
    }
}
