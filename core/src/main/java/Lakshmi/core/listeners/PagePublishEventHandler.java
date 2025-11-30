package Lakshmi.core.listeners;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(
        service = EventHandler.class,
        immediate = true,
        property = {
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication",
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication/job",
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication/job/publish",
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication/event/emit"
        }
)
public class PagePublishEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PagePublishEventHandler.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    private static final String SUBSERVICE = "my-service-user";

    @Override
    public void handleEvent(Event event) {
        try {
            String action = (String) event.getProperty("type");
            String path = (String) event.getProperty("path");

            // We only care about ACTIVATION events
            if (!"ACTIVATE".equals(action) || path == null || !path.startsWith("/content")) {
                return;
            }

            LOG.info("Page published: {}", path);

            Map<String, Object> authInfo = new HashMap<String, Object>();
            authInfo.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);

            ResourceResolver resolver = null;

            try {
                resolver = resolverFactory.getServiceResourceResolver(authInfo);

                String jcrPath = path + "/jcr:content";

                Resource jcrContent = resolver.getResource(jcrPath);
                if (jcrContent != null) {
                    ModifiableValueMap mvm = jcrContent.adaptTo(ModifiableValueMap.class);
                    if (mvm != null) {
                        mvm.put("changed", true);
                        resolver.commit();
                        LOG.info("Added 'changed=true' to {}", jcrPath);
                    }
                }

            } finally {
                if (resolver != null && resolver.isLive()) {
                    resolver.close();
                }
            }

        } catch (PersistenceException e) {
            LOG.error("Error updating jcr:content", e);
        } catch (Exception e) {
            LOG.error("Unexpected error in replication handler", e);
        }
    }
}
