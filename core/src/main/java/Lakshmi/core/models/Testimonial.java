package Lakshmi.core.models;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Testimonial {

    @ValueMapValue
    private String name;

    @ValueMapValue
    private String role;

    @ValueMapValue
    private String feedback;

    @ValueMapValue
    private String image;

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getImage() {
        return image;
    }
}
