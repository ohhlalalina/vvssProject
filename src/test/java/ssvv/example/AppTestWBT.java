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


/**
 * Unit test for simple App.
 */
public class AppTestWBT
{
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
    public void addAssignment_Description_empty(){
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());

        try{
            service.saveTema("1000", "", 7, 5);
        }
        catch (ValidationException e) {
            assertEquals("Descriere nula! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void addAssignment_Description_null() {
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
        try{
            service.saveTema("1000", null, 7, 5);
        }
        catch (ValidationException e) {
            assertEquals("Descriere invalida! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void addAssignment_Description_valid() {
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());

        service.saveTema("1000", "o descriere", 7, 5);
        assertEquals(1, service.findAllTeme().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void addAssignment_Deadlines() {
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());

        try{
            service.saveTema("1000", "Tema1", 0, 10);
        }
        catch (ValidationException e) {
            assertEquals("Deadline invalid! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
        try{
            service.saveTema("1000", "Tema1", 15, 10);
        }
        catch (ValidationException e) {
            assertEquals("Deadline invalid! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
        try{
            service.saveTema("1000", "Tema1", 7, 8);
        }
        catch (ValidationException e) {
            assertEquals("Deadline invalid! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
        try{
            service.saveTema("1000", "Tema1", 13, 0);
        }
        catch (ValidationException e) {
            assertEquals("Deadline invalid! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());

        try{
            service.saveTema("1000", "Tema1", 13, 15);
        }
        catch (ValidationException e) {
            assertEquals("Deadline invalid! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());


        service.saveTema("1000", "Tema1", 8,3);
        assertEquals(1, service.findAllTeme().spliterator().getExactSizeIfKnown());
        service.deleteTema("1000");

        service.saveTema("1000", "Tema1", 10,7);
        assertEquals(1, service.findAllTeme().spliterator().getExactSizeIfKnown());
        service.deleteTema("1000");

        service.saveTema("1000", "Tema1", 11,9);
        assertEquals(1, service.findAllTeme().spliterator().getExactSizeIfKnown());
        service.deleteTema("1000");

        service.saveTema("1000", "Tema1", 14,10);
        assertEquals(1, service.findAllTeme().spliterator().getExactSizeIfKnown());
        service.deleteTema("1000");

        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
    }
}
