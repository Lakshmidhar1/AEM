package Lakshmi.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SingleArticleModel {

    @ValueMapValue
    private Boolean enablePii;

    @ValueMapValue
    private String cssClass;

    public Boolean getEnablePii() {
        return enablePii != null ? enablePii : false;
    }

    public String getCssClass() {
        return cssClass;
    }
}
