package Lakshmi.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class ArticalService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticalService.class);

    @Activate
    public void activate(){
        LOG.info("ArticalService activated");
    }
    @Deactivate
    public void deactivate(){
        LOG.info("ArticalService deactivated");
    }
    @Modified
    public void modified(){
        LOG.info("ArticalService modified");
    }

}
