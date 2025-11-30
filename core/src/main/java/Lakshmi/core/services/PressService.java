package Lakshmi.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class PressService {

    private static final Logger LOG = LoggerFactory.getLogger(PressService.class);

    @Activate
    public void activate(){
        LOG.info("PressService activated");
    }
    @Deactivate
    public void deactivate(){
        LOG.info("PressService deactivated");
    }
    @Modified
    public void modified(){
        LOG.info("PressService modified");
    }

}
