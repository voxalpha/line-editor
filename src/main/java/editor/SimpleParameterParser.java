package editor;

import java.util.Optional;

public class SimpleParameterParser implements ParameterParser{

    @Override
    public EditorParameters parse(String[] args) throws CheckedIllegalArgumentException {
        Optional<Integer> lineNumber = Optional.empty();
        Optional<String> lineText = Optional.empty();
        if (args.length > 0)
        {
            try {
                lineNumber = Optional.of(Integer.parseInt(args[0]));
            }
            catch (NumberFormatException e){
                throw new CheckedIllegalArgumentException(args[0] + " is not valid integer");
            }

            if (args.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i + 1 < args.length) sb.append(" ");
                }
                lineText = Optional.of(sb.toString());
            }
        }
        return new EditorParameters(lineNumber, lineText);
    }
}
