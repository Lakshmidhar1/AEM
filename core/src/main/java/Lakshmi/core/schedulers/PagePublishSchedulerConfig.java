package Lakshmi.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Page Publish Scheduler Configuration",
        description = "Publishes all pages under a root path based on a cron expression"
)
public @interface PagePublishSchedulerConfig {

    @AttributeDefinition(
            name = "Scheduler Name",
            description = "Unique name for this scheduler",
            type = AttributeType.STRING
    )
    String scheduler_name() default "page-publish-scheduler";

    @AttributeDefinition(
            name = "Enabled",
            description = "Enable/disable this scheduler",
            type = AttributeType.BOOLEAN
    )
    boolean enabled() default true;

    @AttributeDefinition(
            name = "Cron Expression",
            description = "Cron expression (e.g. 0 0/1 * * * ? for every minute)",
            type = AttributeType.STRING
    )
    String scheduler_expression() default "0 0/1 * * * ?";

    @AttributeDefinition(
            name = "Root Page Path",
            description = "Root page path under which pages will be published",
            type = AttributeType.STRING
    )
    String root_page_path() default "/content";
}
