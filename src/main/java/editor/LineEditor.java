package editor;

import java.io.File;
import java.io.IOException;

public class LineEditor {

    public static void main(String[] args) throws IOException {

        File file = null;
        if (args.length > 0) {
            file = new File(args[0]);
            if (!file.exists() || file.isDirectory()){
                System.out.println(file.getAbsolutePath() + " does not exists or directory");
                System.exit(-1);
            }
        } else {
            System.out.println("Please provide file name as single command line parameter");
            System.exit(-1);
        }

        CommandLineController controller = new CommandLineController(new SimpleParameterParser());
        System.out.println("File " + file.getAbsolutePath() + " loaded");
        CommandLineEditor editor = new CommandLineEditor(controller, controller, file);
        editor.listContent();

        while (!controller.terminated()){
            controller.prompt(System.in);
        }
    }
}
