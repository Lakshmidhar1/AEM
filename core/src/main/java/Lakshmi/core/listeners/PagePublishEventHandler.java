package Lakshmi.core.listeners;

import org.apache.sling.api.resource.ModifiableValueMap;
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
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication"
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

            String type = (String) event.getProperty("type");    // ACTIVATE, DEACTIVATE etc.
            String[] paths = (String[]) event.getProperty("paths");

            if (!"ACTIVATE".equals(type) || paths == null || paths.length == 0) {
                return;
            }

            String path = paths[0];   // get first published path

            LOG.info("🔥 Page Published: {}", path);

            Map<String, Object> authInfo = new HashMap<>();
            authInfo.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);

            try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(authInfo)) {

                Resource jcrContent = resolver.getResource(path + "/jcr:content");

                if (jcrContent != null) {
                    ModifiableValueMap mvm = jcrContent.adaptTo(ModifiableValueMap.class);
                    mvm.put("changed", true);
                    resolver.commit();

                    LOG.info("🔥 Added changed=true to {}", path + "/jcr:content");
                }
            }

        } catch (Exception e) {
            LOG.error("Error in Event Handler", e);
        }
    }
}
