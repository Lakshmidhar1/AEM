package Lakshmi.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "StudentConfig")
public @interface StudentConfig {
    @AttributeDefinition(
            name="studentName",
            defaultValue = "Ravi",
            type = AttributeType.STRING
    )
    public String studentName();
    @AttributeDefinition(
            name="studentRollNo",
            defaultValue = "000",
            type = AttributeType.INTEGER
    )
    public int studentRollNo();
    @AttributeDefinition(
            name="studentMarks",
            defaultValue = "000",
            type = AttributeType.INTEGER
    )
    public int studentMarks();

}
