package Lakshmi.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = TestDemo.class, immediate = true,enabled = true)
public class TestDemo {
    private static Logger LOG = LoggerFactory.getLogger(TestDemo.class);
    @Activate
    public void updateData(){
        LOG.info("TestDemo::updateData()");
    }

    public void data(){
        LOG.info("TestDemo::data()");
    }
}
