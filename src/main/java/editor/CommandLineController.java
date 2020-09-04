package editor;
import java.io.InputStream;
import java.util.*;

/**
 * Console controller implementation
 */
public class CommandLineController implements Controller, Display{

    private final ParameterParser parser;
    private final Map<String, Operation> operations = new HashMap<>();
    private boolean isTerminated = false;
    private String lastError;
    private boolean isSuccess = false;


    public CommandLineController(ParameterParser parameterParser){
        this.parser = parameterParser;
        registerOperation(OperationType.QUIT, this::quit);
    }

    private Operation getOperation(String[] commandLine)
    {
        String command = commandLine[0].toLowerCase();

        Operation operation = operations.get(command);
        if (operation == null){
            error("Command " + command + " is not supported");
            help();
        }
        return operation;
    }

    private EditorParameters getParameters(String[] commandLine)
    {
        String[] args = Arrays.copyOfRange(commandLine, 1, commandLine.length);
        try {
            return parser.parse(args);
        }
        catch (CheckedIllegalArgumentException e){
            error(e.getMessage());
            return null;
        }
    }

    /**
     * Encapsulate console interactions
     * @param input - stream to take input
     */
    public void prompt(InputStream input) {
        System.out.print(">> ");
        String commandString = (new Scanner(input)).nextLine();
        if (input != System.in)
          //echo command to system out in case input is not from system in
          System.out.println(commandString);
        String[] commandLine = commandString.split(" ");

        Operation operation = getOperation(commandLine);
        if (operation == null) return;

        EditorParameters parameters = getParameters(commandLine);
        if (parameters == null) return;

        isSuccess = operation.execute(parameters);
    }

    @Override
    public void registerOperation(OperationType opType, Operation operationCall) {
        operations.put(opType.command, operationCall);
    }

    private boolean quit(EditorParameters parameters){
        System.out.println("Bye...");
        isTerminated = true;
        return true;
    }

    @Override
    public boolean terminated() {
        return isTerminated;
    }

    @Override
    public boolean successful() {
        return isSuccess;
    }


    @Override
    public String getLastError() {
        return lastError;
    }

    @Override
    public void error(String message) {
        lastError = message;
        isSuccess = false;
        System.out.println("Error: " + lastError);
    }

    @Override
    public void showContent(List<String> content) {
       int i = 0;
       for(String line: content)
           System.out.println(++i + ": " + line);
    }

    private void help(){
        System.out.println("List of supported commands: ");
        operations.keySet().forEach(System.out::println);
    }
}
