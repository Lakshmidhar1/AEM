package Lakshmi.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service = Emp.class, immediate = true,enabled = true)
@Designate(ocd=EmpConfig.class)
public class Emp {

    private static Logger LOG = LoggerFactory.getLogger(Emp.class);

    private String empName;
    private int empId;
    private double empSalary;

    @Activate
    public void activate(EmpConfig empConfig){
        updateConfig(empConfig);

    }
    public void deactivate(EmpConfig empConfig){

    }
    @Modified
    public void update(EmpConfig empConfig){
        updateConfig(empConfig);
    }
    public void updateConfig(EmpConfig empConfig){
        this.empName = empConfig.empName();
        this.empId = empConfig.empId();
        this.empSalary = empConfig.empSalary();
        LOG.info("empName={},empId={},empSalary={}",empName,empId,empSalary);

    }

}
