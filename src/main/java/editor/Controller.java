package editor;

/**
 * Interface supporting control interactions
 */
public interface Controller {
    /**
     * Callback registration for particular editor operation
     * @param opType - type of commad
     * @param operation - call back function
     */
    void registerOperation(OperationType opType, Operation operation);

    /**
     * Error registration
     * @param message - error message
     */
    void error(String message);

    /**
     *  @return true if conntroller teminated and doesn't accept input
     */
    boolean terminated();

    /**
     *  @return true if last command was successful
     */
    boolean successful();

    /**
     *  @return last error message
     */
    String getLastError();
}
