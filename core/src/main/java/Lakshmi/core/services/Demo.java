package Lakshmi.core.services;

import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true,enabled = true)
public class Demo {
    private static Logger LOG = LoggerFactory.getLogger(Demo.class);

    @Reference
    TestDemo testDemo;

    @Activate
    public void activate(){
        LOG.info("Demo Activated");
    }
    @Deactivate
    public void deactivate(){
        LOG.info("Demo Deactivated");
    }
    @Modified
    public void update(){
        LOG.info("Demo Updated");
    }

}
