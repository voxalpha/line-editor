package editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Editor {
    private final Display display;
    private final Controller controller;
    private File file;
    private List<String> content = new ArrayList<>();

    public Editor(Controller controller, Display display, File file) throws IOException {
        this.display = display;
        this.controller = controller;
        //registering supported operations in controller
        controller.registerOperation(OperationType.SAVE, this::saveFile);
        controller.registerOperation(OperationType.INSERT, this::insertLine);
        controller.registerOperation(OperationType.DELETE, this::deleteLine);
        controller.registerOperation(OperationType.LIST, this::listContent);
        //making sure content is loaded
        loadFile(file);
    }

    private void loadFile(File file) throws IOException {
       this.file = file;
       //relying on Files implementation to read file
       content = Files.readAllLines(file.toPath());
    }

    /**
     * SaveFile wrapper to get callback from Controller
     * @param parameters - command parameters
     * @return - true if command was executed
     */
    private boolean saveFile(EditorParameters parameters){
       if (!parameters.getLineNumber().isPresent() && !parameters.getLineText().isPresent()) {
           try {
               saveFile();
               return true;
           } catch (IOException e) {
               controller.error(e.getClass().getSimpleName() + " " + e.getMessage());
               return false;
           }
       }
       else {
           controller.error(OperationType.SAVE.toString().toLowerCase() + " accepts no arguments");
           return false;
       }
    }

    /**
     * Save content to file was loaded during construction of class
     */
    public void saveFile() throws IOException {
      //relying on Files implementation to write file
      Files.write(file.toPath(), content);
    }

    /**
     * InsertLine wrapper to get callback from Controller
     */
    private boolean insertLine(EditorParameters parameters){
        if (parameters.getLineNumber().isPresent() && parameters.getLineText().isPresent()) {
            try {
                insertLine(parameters.getLineNumber().get(), parameters.getLineText().get());
                listContent();
                return true;
            } catch (CheckedIllegalArgumentException e) {
                controller.error(e.getMessage());
                return false;
            }
        }
        else {
            controller.error(OperationType.INSERT.toString().toLowerCase() + " should have 2 arguments");
            return false;
        }
    }

    /**
     * Insert element to internal content collection
     * @param lineNumber - where to insert, can be from 1 to last line + 1 (tail insert)
     * @param line - text
     */
    public void insertLine(int lineNumber, String line) throws CheckedIllegalArgumentException {
      if (lineNumber < 1 || lineNumber > content.size() + 1)
            throw new CheckedIllegalArgumentException("Cannot insert line into position " + lineNumber);
      content.add(lineNumber - 1, line);
    }

    /**
     * deleteLine wrapper to get callback from Controller
     */
    private boolean deleteLine(EditorParameters parameters){
        if (parameters.getLineNumber().isPresent() && !parameters.getLineText().isPresent()) {
            try {
                deleteLine(parameters.getLineNumber().get());
                listContent();
                return true;
            } catch (CheckedIllegalArgumentException e) {
                controller.error(e.getMessage());
                return false;
            }

        }
        else {
            controller.error(OperationType.DELETE.toString().toLowerCase() + " should have 1 argument");
            return false;
        }
    }

    /**
     * Delete element from internal content collection
     * @param lineNumber - what to delete, can be from 1 to last existing line number
     */
    public void deleteLine(int lineNumber) throws CheckedIllegalArgumentException {
      if (lineNumber < 1 || lineNumber > content.size())
          throw new CheckedIllegalArgumentException("Cannot delete line " + lineNumber);
      content.remove(lineNumber - 1);
    }

    /**
     * List wrapper to get callback from Controller
     */
    private boolean listContent(EditorParameters parameters){
        if (!parameters.getLineNumber().isPresent() && !parameters.getLineText().isPresent()) {
            listContent();
        }
        else {
            controller.error(OperationType.LIST.toString().toLowerCase() + " accepts no arguments");
            return false;
        }
        return true;
    }

    /**
     * Call Display to show content
     */
    public void listContent() {
      display.showContent(content);
    }


    public String getLine(int lineNumber) {
        if (lineNumber < 1 || lineNumber > content.size())
            throw new IllegalArgumentException("Invalid line number " + lineNumber);
        return content.get(lineNumber - 1);
    }

    public int getContentSize(){
        return content.size();
    }

}
