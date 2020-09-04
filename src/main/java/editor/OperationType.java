package editor;

public enum OperationType {

   LIST("list"),
   DELETE("delete"),
   INSERT("insert"),
   SAVE("save"),
   QUIT("quit");

   public String command;
   OperationType(String command){
       this.command = command;
   }


}
