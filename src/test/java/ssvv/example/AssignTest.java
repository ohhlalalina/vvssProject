package ssvv.example;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class AssignTest {
    private static final String IntMaxValuePlusOne = "2147483648";
    Service service;

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
    public void addAssignment_id_empty(){
        int added = service.saveTema("", "Descriere", 10, 8);
        assertEquals(0, added);
    }

    @Test
    public void addAssignment_desc_null(){
        int added = service.saveTema("1", null, 10, 8);
        assertEquals(0, added);
    }

    @Test
    public void addAssignment_desc_empty(){
        int added = service.saveTema("1", "", 10, 8);
        assertEquals(0, added);
    }

    @Test
    public void addAssignment_deadline_lowerStart(){
        int added = service.saveTema("1", "Descriere", 10, 12);
        assertEquals(0, added);
    }

    @Test
    public void addAssignment_deadline_greater14(){
        int added = service.saveTema("1", "Descriere", 16, 12);
        assertEquals(0, added);
    }

    @Test
    public void addAssignment_deadline_equalUpBoundary(){
        service.deleteTema("1");
        int added = service.saveTema("1", "Descriere", 14, 12);
        assertEquals(1, added);
    }

    @Test
    public void addAssignment_startline_lower1(){
        int added = service.saveTema("1", "Descriere", 2, 0);
        assertEquals(0, added);
    }

    @Test
    public void addAssignment_startline_greater14(){
        int added = service.saveTema("1", "Descriere", 10, 15);
        assertEquals(0, added);
    }

    @Test
    public void addAssignment_startline_equalLowBoundary(){
        int added = service.saveTema("1", "Descriere", 3, 1);
        assertEquals(1, added);
    }

    @Test
    public void addAssignment_startline_equaldeadline(){
        int added = service.saveTema("1", "Descriere", 3, 3);
        assertEquals(1, added);
    }

}
