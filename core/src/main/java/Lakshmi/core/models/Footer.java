package Lakshmi.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Footer  {

    @ValueMapValue
    private String text;

    @ValueMapValue
    private String link;

    @ValueMapValue
    private String text1;

    public String getText1() {
        return text1;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }
}


