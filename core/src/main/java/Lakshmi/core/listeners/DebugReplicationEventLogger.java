package Lakshmi.core.listeners;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = EventHandler.class,
        immediate = true,
        property = {
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication",
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication/job",
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication/job/publish",
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication/job_scene7",
                EventConstants.EVENT_TOPIC + "=com/day/cq/replication/job_test_and_target"
        }
)
public class DebugReplicationEventLogger implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DebugReplicationEventLogger.class);

    @Override
    public void handleEvent(Event event) {
        LOG.error("🔥🔥🔥 REPLICATION EVENT RECEIVED 🔥🔥🔥");
        LOG.error("Topic       : {}", event.getTopic());
        LOG.error("Properties  : {}", java.util.Arrays.toString(event.getPropertyNames()));
        for (String key : event.getPropertyNames()) {
            LOG.error(" - {} : {}", key, event.getProperty(key));
        }
    }
}
