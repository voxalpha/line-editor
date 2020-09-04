package editor;


@FunctionalInterface
public interface Operation {
    boolean execute(EditorParameters parameters);
}
