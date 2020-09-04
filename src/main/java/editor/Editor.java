package editor;

import java.io.File;
import java.io.IOException;

public interface Editor {

    //void loadFile(File file) throws IOException;
    void saveFile() throws IOException;
    void insertLine(int lineNumber, String line) throws CheckedIllegalArgumentException;
    void deleteLine(int lineNumber) throws CheckedIllegalArgumentException;
    void listContent();
}
