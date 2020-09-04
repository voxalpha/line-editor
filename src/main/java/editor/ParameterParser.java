package editor;

public interface ParameterParser {
    EditorParameters parse(String[] args) throws CheckedIllegalArgumentException;
}
