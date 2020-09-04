package editor;


import java.util.Optional;

public class EditorParameters {
    private final Optional<Integer> lineNumber;
    private final Optional<String> lineText;

    public EditorParameters(Optional<Integer> lineNumber, Optional<String> lineText) {
        this.lineNumber = lineNumber;
        this.lineText = lineText;
    }

    public Optional<Integer> getLineNumber() {
        return lineNumber;
    }

    public Optional<String> getLineText() {
        return lineText;
    }
}
