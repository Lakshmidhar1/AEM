package Lakshmi.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service=Runnable.class,immediate = true,enabled = true)
@Designate(ocd = ArticleExpiryConfig.class)
public class ArticleExpiryImp implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(ArticleExpiryImp.class);

    @Reference
    Scheduler scheduler;

    @Override
    public void run() {
        LOG.info("Lakshmidhar is Good BOY");
    }
    @Activate
    public void activate(ArticleExpiryConfig articleExpiryConfig){
        updateArticleConfig(articleExpiryConfig);
    }
    @Deactivate
    public void deactivate(ArticleExpiryConfig articleExpiryConfig){
        updateArticleConfig(articleExpiryConfig);
    }
    @Modified
    public void update(ArticleExpiryConfig articleExpiryConfig){
        updateArticleConfig(articleExpiryConfig);
    }
    public void updateArticleConfig(ArticleExpiryConfig articleExpiryConfig){
       if (articleExpiryConfig.schedular_enabled()){
           ScheduleOptions options =scheduler.EXPR(articleExpiryConfig.schedular_expression());
           options.name(articleExpiryConfig.schedular_name());
           options.canRunConcurrently(false);
           scheduler.schedule(this, options);
           LOG.info("Schedular added sucessfully name='{}'",articleExpiryConfig.schedular_name());
       }else{
           LOG.info("Scheduler disabled");
       }
    }
}
