package Lakshmi.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BlogPost {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String date;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String blogcontent;

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getBlogcontent() {
        return blogcontent;
    }
}
