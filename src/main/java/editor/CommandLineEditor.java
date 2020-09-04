package editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class CommandLineEditor /*implements Editor*/{
    private final Display display;
    private final Controller controller;
    private File file = null;
    private List<String> content = new ArrayList<>();

    public CommandLineEditor(Controller controller, Display display, File file) throws IOException {
        this.display = display;
        this.controller = controller;
        controller.registerOperation(OperationType.SAVE, this::saveFile);
        controller.registerOperation(OperationType.INSERT, this::insertLine);
        controller.registerOperation(OperationType.DELETE, this::deleteLine);
        controller.registerOperation(OperationType.LIST, this::listContent);
        loadFile(file);
    }

    //@Override
    private void loadFile(File file) throws IOException {
       this.file = file;
       content = Files.readAllLines(file.toPath());
    }

    private boolean saveFile(EditorParameters parameters){
       if (!parameters.getLineNumber().isPresent() && !parameters.getLineText().isPresent()) {
           try {
               saveFile();
               return true;
           } catch (IOException e) {
               controller.error(e.getMessage());
               return false;
           }
       }
       else {
           controller.error(OperationType.SAVE.command + " accepts no arguments");
           return false;
       }
    }

    //@Override
    public void saveFile() throws IOException {
      Files.write(file.toPath(), content);
    }

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
            controller.error(OperationType.INSERT.command + " should have 2 arguments");
            return false;
        }
    }

    //@Override
    public void insertLine(int lineNumber, String line) throws CheckedIllegalArgumentException {
      if (lineNumber < 1 || lineNumber > content.size() + 1)
            throw new CheckedIllegalArgumentException("Cannot insert line into position " + lineNumber);
      content.add(lineNumber - 1, line);
    }

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
            controller.error(OperationType.DELETE.command + " should have 1 argument");
            return false;
        }
    }
    //@Override
    public void deleteLine(int lineNumber) throws CheckedIllegalArgumentException {
      if (lineNumber < 1 || lineNumber > content.size())
          throw new CheckedIllegalArgumentException("Cannot delete line " + lineNumber);
      content.remove(lineNumber - 1);
    }

    private boolean listContent(EditorParameters parameters){
        if (!parameters.getLineNumber().isPresent() && !parameters.getLineText().isPresent()) {
            listContent();
        }
        else {
            controller.error(OperationType.LIST.command + " accepts no arguments");
            return false;
        }
        return true;
    }

    //@Override
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
