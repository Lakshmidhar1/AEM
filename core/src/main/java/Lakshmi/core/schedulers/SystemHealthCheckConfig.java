package Lakshmi.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "SystemHealthCheckConfig")
public @interface SystemHealthCheckConfig {

    @AttributeDefinition(
            name = "Schedular Name",
            defaultValue = "Schedular Name"
    )
    String schedularName();
    @AttributeDefinition(
            name = "Schedular Expression",
            defaultValue = "Schedular Expression"
    )
    String schedularExpression()default "0/30 * * * * ?";
    @AttributeDefinition(
            name = "Schedular Enabled",
            defaultValue = "Checkbox"
    )
    boolean schedularEnabled() default true;
}
