package editor;

public interface Controller {
    void registerOperation(OperationType opType, Operation operation);
    void error(String message);
    boolean terminated();
    boolean successful();
    String getLastError();
}
