package ssvv.example;

import static org.junit.Assert.assertEquals;

import domain.Nota;
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

import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors


public class AppTestBBOld {
    private static final String IntMaxValuePlusOne = "2147483648";
    private Service service;

    private void initXMLFile(String name) {
        try {
            File myObj = new File(name);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter(name);
            myWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<Entitati>\n" +
                    "\n" +
                    "</Entitati>\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    @Before
    public void initialize() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        initXMLFile("studenti_test.xml");
        initXMLFile("teme_test.xml");
        initXMLFile("note_test.xml");
        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "studenti_test.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme_test.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note_test.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @After
    public void deleteFiles() {
        File myObj = new File("studenti_test.xml");
        myObj.delete();
        myObj = new File("teme_test.xml");
        myObj.delete();
        myObj = new File("note_test.xml");
        myObj.delete();
    }

    @Test
    public void addStudent_Id_null() {
        assertEquals(0, service.findAllStudents().spliterator().getExactSizeIfKnown());
        try {
            service.saveStudent(null, "andrei", 931);
        } catch (ValidationException e) {
            assertEquals("INVALID ENTITY", e.getMessage());
        }
        assertEquals(0, service.findAllStudents().spliterator().getExactSizeIfKnown());

    }

    @Test
    public void addAssignment_good(){
        int added = service.saveTema("1", "Descriere", 12, 10);
        assertEquals(1, added);
    }

    @Test
    public void addGrade_noStudent(){
        int added = service.saveNota("1","1",8.0,11,"Good");
        assertEquals(-1,added);
    }

    @Test
    public void smallerGrade(){
        assertEquals(0, service.findAllStudents().spliterator().getExactSizeIfKnown());
        service.saveStudent("1", "andrei", 931);
        assertEquals(1, service.findAllStudents().spliterator().getExactSizeIfKnown());
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
        service.saveTema("1", "Descriere", 12, 10);
        assertEquals(1, service.findAllTeme().spliterator().getExactSizeIfKnown());
        int added = service.saveNota("1","1",-1.0,11,"Good");
        assertEquals(1,added);
    }
}
