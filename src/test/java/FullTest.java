import editor.CommandLineController;
import editor.Editor;
import editor.SimpleParameterParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FullTest {

   Editor editor;
   CommandLineController controller;

   @BeforeEach
   public void setUp() throws IOException {
       controller = new CommandLineController(new SimpleParameterParser());
               //new ByteArrayInputStream("zzz".getBytes()));
       File file = File.createTempFile("editor", ".tmp");
       Files.write(file.toPath(), Arrays.asList("line one", "line two", "line three"));
       editor = new Editor(controller, controller, file);
   }

   @Test
   @DisplayName("Content manipulations")
   public void testContent(){
       controller.prompt(new ByteArrayInputStream("ins 3 new line".getBytes()));
       assertTrue(controller.successful());
       assertEquals(4, editor.getContentSize());
       assertEquals("new line", editor.getLine(3));
       assertEquals("line three", editor.getLine(4));

       controller.prompt(new ByteArrayInputStream("del 1".getBytes()));
       assertTrue(controller.successful());
       assertEquals(3, editor.getContentSize());
       assertEquals("new line", editor.getLine(2));
       assertEquals("line three", editor.getLine(3));

       controller.prompt(new ByteArrayInputStream("ins 8 aaa".getBytes()));
       assertFalse(controller.successful());
       assertTrue(controller.getLastError().endsWith("Cannot insert line into position 8"));

       controller.prompt(new ByteArrayInputStream("ins 0 aaa ".getBytes()));
       assertTrue(controller.getLastError().endsWith("Cannot insert line into position 0"));
       assertFalse(controller.successful());

       controller.prompt(new ByteArrayInputStream("ins zzz aaa".getBytes()));
       assertTrue(controller.getLastError().endsWith("zzz is not valid integer"));
       assertFalse(controller.successful());

       controller.prompt(new ByteArrayInputStream("ins 8 aaa".getBytes()));
       assertFalse(controller.successful());
       assertTrue(controller.getLastError().endsWith("Cannot insert line into position 8"));

       controller.prompt(new ByteArrayInputStream("ins 0 aaa ".getBytes()));
       assertTrue(controller.getLastError().endsWith("Cannot insert line into position 0"));
       assertFalse(controller.successful());

       controller.prompt(new ByteArrayInputStream("ins zzz aaa".getBytes()));
       assertTrue(controller.getLastError().endsWith("zzz is not valid integer"));
       assertFalse(controller.successful());
   }

    @Test
    @DisplayName("Parameter correctness")
    public void testParams(){
        controller.prompt(new ByteArrayInputStream("ins 1 aaa".getBytes()));
        assertTrue(controller.successful());

        controller.prompt(new ByteArrayInputStream("ins 8 aaa".getBytes()));
        assertFalse(controller.successful());
        assertTrue(controller.getLastError().endsWith("Cannot insert line into position 8"));

        controller.prompt(new ByteArrayInputStream("ins 0 aaa ".getBytes()));
        assertTrue(controller.getLastError().endsWith("Cannot insert line into position 0"));
        assertFalse(controller.successful());

        controller.prompt(new ByteArrayInputStream("ins zzz aaa".getBytes()));
        assertTrue(controller.getLastError().endsWith("zzz is not valid integer"));
        assertFalse(controller.successful());

        controller.prompt(new ByteArrayInputStream("ins 1".getBytes()));
        assertTrue(controller.getLastError().endsWith("insert should have 2 arguments"));
        assertFalse(controller.successful());

        controller.prompt(new ByteArrayInputStream("del 1".getBytes()));
        assertTrue(controller.successful());

        controller.prompt(new ByteArrayInputStream("del 8".getBytes()));
        assertFalse(controller.successful());
        assertTrue(controller.getLastError().endsWith("Cannot delete line 8"));

        controller.prompt(new ByteArrayInputStream("del 0".getBytes()));
        assertTrue(controller.getLastError().endsWith("Cannot delete line 0"));
        assertFalse(controller.successful());

        controller.prompt(new ByteArrayInputStream("del zzz".getBytes()));
        assertTrue(controller.getLastError().endsWith("zzz is not valid integer"));
        assertFalse(controller.successful());

        controller.prompt(new ByteArrayInputStream("del".getBytes()));
        assertTrue(controller.getLastError().endsWith("delete should have 1 argument"));
        assertFalse(controller.successful());

        controller.prompt(new ByteArrayInputStream("list".getBytes()));
        assertTrue(controller.successful());

        controller.prompt(new ByteArrayInputStream("list 10".getBytes()));
        assertTrue(controller.getLastError().endsWith("list accepts no arguments"));
        assertFalse(controller.successful());

        controller.prompt(new ByteArrayInputStream("save 10".getBytes()));
        assertTrue(controller.getLastError().endsWith("save accepts no arguments"));
        assertFalse(controller.successful());
    }

    @Test
    @DisplayName("Commands recognizing")
    public void testCommand(){
        controller.prompt(new ByteArrayInputStream("quit".getBytes()));
        assertTrue(controller.terminated());
        assertTrue(controller.successful());

        controller.prompt(new ByteArrayInputStream("zzz".getBytes()));
        assertTrue(controller.getLastError().endsWith("Command zzz is not supported"));
    }

    @Test
    @DisplayName("Result saving")
    public void testSave()
    {
        controller.prompt(new ByteArrayInputStream("save".getBytes()));
        assertTrue(controller.successful());

        //File actual writing left intentionally untested
        assertTrue(true);
    }

}
