public class Command {
    private String commandType;
    private int index;
    private int val;

    public Command(String commandType, int index, int val){
        this.commandType = commandType;
        this.index = index;
        this.val = val;
    }

    /**
     * A getter of the field commandType.
     *
     * @return the command type that was made.
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * A getter of the field index.
     * 
     * @return the index where the action has been made.
     */
    public int getIndex() {
        return index;
    }

    /**
     * A getter of the field val.
     *
     * @return the value that was in the specific index before the action.
     */
    public int getVal() {
        return val;
    }
}
