package Lakshmi.core.services;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Student.class, immediate = true,enabled = true)
@Designate(ocd=StudentConfig.class)
public class Student {

    private static Logger LOG = LoggerFactory.getLogger(Student.class);

    private String studentName;
    private int studentRollNo;
    private int studentMarks;

    @Activate
    public void Activate(StudentConfig studentConfig){
        UpdateConfig(studentConfig);
    }
    @Modified
    public void Update(StudentConfig studentConfig){
        UpdateConfig(studentConfig);
    }
    public void UpdateConfig(StudentConfig studentConfig){
        this.studentName = studentConfig.studentName();
        this.studentRollNo = studentConfig.studentRollNo();
        this.studentMarks = studentConfig.studentMarks();
        LOG.info("studentName={},studentRollNo={},studentMarks={}", studentName, studentRollNo, studentMarks);


    }
}
