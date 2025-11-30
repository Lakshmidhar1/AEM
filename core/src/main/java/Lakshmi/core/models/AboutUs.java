package Lakshmi.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AboutUs {
    @ValueMapValue
    private String title;

    @ValueMapValue
    private String type;

    @ValueMapValue
    private String aboutcontent;

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getAboutcontent() {
        return aboutcontent;
    }
}
