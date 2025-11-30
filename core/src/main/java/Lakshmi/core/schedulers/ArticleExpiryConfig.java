package Lakshmi.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "ArticleExpiryConfig")
public @interface ArticleExpiryConfig {
    @AttributeDefinition(
            name="Iphone 16",
            description="About Iphone 16",
            defaultValue = "Iphone 16"
    )
    public String schedular_name();
    @AttributeDefinition(
            name="schedular_expression",
            description="About expression"
    )
    public String schedular_expression()default "*/3 * * * * ?";
    @AttributeDefinition(
            name="schedular_enabled",
            description="About enabled"
    )
    public boolean schedular_enabled()default true;
    @AttributeDefinition(
            name="schedular_concurrent",
            description="About concurrent"
    )
    public boolean schedular_concurrent()default false;

}
