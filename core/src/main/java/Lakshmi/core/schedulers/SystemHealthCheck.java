package Lakshmi.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SystemHealthCheckConfig.class)
public class SystemHealthCheck implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SystemHealthCheck.class);

    @Reference
    private Scheduler scheduler;

    private String jobName;

    @Activate
    public void activate(SystemHealthCheckConfig config) {
        LOG.info(">>>>> Scheduler activate() called");
        jobName = config.schedularName();
        scheduleJob(config);
    }

    @Modified
    public void modified(SystemHealthCheckConfig config) {
        unscheduleJob();               // Stop the old one first
        scheduleJob(config);           // Then reschedule with new config
    }

    @Deactivate
    public void deactivate() {
        unscheduleJob();               // Remove the job during deactivation
    }

    private void scheduleJob(SystemHealthCheckConfig config) {
        if (config.schedularEnabled()) {
            ScheduleOptions options = scheduler.EXPR(config.schedularExpression());
            options.name(jobName);
            options.canRunConcurrently(false);
            scheduler.schedule(this, options);
            LOG.info("Scheduler added successfully with name '{}'", jobName);
        } else {
            LOG.info("Scheduler is disabled");
        }
    }

    private void unscheduleJob() {
        if (scheduler != null && jobName != null) {
            scheduler.unschedule(jobName);
            LOG.info("Scheduler unscheduled: '{}'", jobName);
        }
    }

    @Override
    public void run() {
        Runtime runtime = Runtime.getRuntime();
        long freeMem = runtime.freeMemory();
        long totalMem = runtime.totalMemory();
        long usedMem = totalMem - freeMem;
        int processors = runtime.availableProcessors();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        LOG.info("==== System Health Snapshot ====");
        LOG.info("Time: {}", time);
        LOG.info("Used Memory: {} MB", usedMem / (1024 * 1024));
        LOG.info("Free Memory: {} MB", freeMem / (1024 * 1024));
        LOG.info("Total Memory: {} MB", totalMem / (1024 * 1024));
        LOG.info("Available Processors: {}", processors);
        LOG.info("===============================");
    }
}
