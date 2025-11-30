package Lakshmi.core.models;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Named;
import java.util.Date;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Digital implements Digital1{
    @ScriptVariable
    Page currentPage;

    @ValueMapValue
    private String text;

    @ValueMapValue
    private String textarea;

    @ValueMapValue
    private int articlenumber;

    @ValueMapValue
    private Date datepicker;

    @Named(value = "jcr:created")
    @ValueMapValue
    String Lakshmidhar;


    @Override
    public String getArticleText() {
        return text;
    }

    @Override
    public String getArticleDescription() {
        return textarea;
    }

    @Override
    public int getArticlenumber() {
        return articlenumber;
    }

    @Override
    public Date getArticleDate() {
        return datepicker;
    }

    @Override
    public String getCurrentPage() {
        return currentPage.getPageTitle();
    }

    @Override
    public String getJcrcreated() {
        return Lakshmidhar;
    }
}
