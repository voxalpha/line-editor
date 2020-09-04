package editor;

/**
 * List of supported operations
 */
public enum OperationType {

   LIST("list"),
   DELETE("del"),
   INSERT("ins"),
   SAVE("save"),
   QUIT("quit");

   public String command;
   OperationType(String command){
       this.command = command;
   }


}
