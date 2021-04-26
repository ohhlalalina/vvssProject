package ssvv.example;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class AppTestIncremental {
    private Service service;
    private StudentXMLRepository studentXMLRepository;
    private TemaXMLRepository temaXMLRepository;
    private NotaXMLRepository notaXMLRepository;
    private String validStudentId;
    private String validStudentName;
    private Integer validStudentGroup;

    private String validAssignmentId;
    private String validAssignmentDescription;
    private Integer validAssignmentDeadline;
    private Integer getValidAssignmentStartLine;

    @Before
    public void beforeTest() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        studentXMLRepository = new StudentXMLRepository(studentValidator, "studenti.xml");
        temaXMLRepository = new TemaXMLRepository(temaValidator, "teme.xml");
        notaXMLRepository = new NotaXMLRepository(notaValidator, "note.xml");
        service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);

        validStudentId = "12";
        validStudentName = "test student name";
        validStudentGroup = 931;

        validAssignmentId = "10";
        validAssignmentDescription = "test big bang assignment";
        validAssignmentDeadline = 10;
        getValidAssignmentStartLine = 5;
    }

    @Test
    public void addStudentIncremental(){
        if(studentXMLRepository.findOne(validStudentId)!=null)
            studentXMLRepository.delete(validStudentId);

        assertEquals(0, service.saveStudent(validStudentId, validStudentName, -100));
    }

    @Test
    public void addAssignmentIncremental(){
            if(studentXMLRepository.findOne(validStudentId)!=null)
                studentXMLRepository.delete(validStudentId);
            assertEquals(1, service.saveStudent(validStudentId, validStudentName, validStudentGroup));
            if(temaXMLRepository.findOne(validAssignmentId)!=null)
                temaXMLRepository.delete(validAssignmentId);
            assertEquals(0, service.saveTema(validAssignmentId, validAssignmentDescription, 10, 11));
    }

    @Test
    public void addGradeIncremental(){
        if(studentXMLRepository.findOne(validStudentId)!=null)
            studentXMLRepository.delete(validStudentId);
        assertEquals(1, service.saveStudent(validStudentId, validStudentName, validStudentGroup));
        if(temaXMLRepository.findOne(validAssignmentId)!=null)
            temaXMLRepository.delete(validAssignmentId);
        assertEquals(1, service.saveTema(validAssignmentId, validAssignmentDescription,
                validAssignmentDeadline, getValidAssignmentStartLine));
        if(notaXMLRepository.findOne(new Pair<>(validStudentId, validAssignmentId))!=null)
            notaXMLRepository.delete(new Pair<>(validStudentId, validAssignmentId));
        assertEquals(1, service.saveNota(validStudentId, validAssignmentId, 10,
                getValidAssignmentStartLine, "GG EZ"));
    }

}
