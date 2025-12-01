package Lakshmi.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Date;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Solo {

    @ValueMapValue
    private String title;
    @ValueMapValue
    private String image;
    @ValueMapValue
    private Date date;

    public String getTitle() {
        return title;
    }
    public String getImage() {
        return image;
    }
    public Date getDate() {
        return date;
    }

    public int y = 100;
}
