package Lakshmi.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
@ObjectClassDefinition(name = "EmpConfig" )
public @interface EmpConfig{
    @AttributeDefinition(
            name = "Ravi",
            description = "Good Employee",
            defaultValue = "Ravi",
            type = AttributeType.STRING
    )
    public String empName();

    @AttributeDefinition(
            name = "0008",
            description = "Good Employee",
            defaultValue = "420",
            type =AttributeType.INTEGER
    )
    public int empId();

    @AttributeDefinition(
            name = "50000",
            description = "Good Employee",
            defaultValue = "500000",
            type =AttributeType.DOUBLE
    )
    public double empSalary();

}
