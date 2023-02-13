package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.List;

/**
 * Command with which the user exits the communication with shell
 */
public class ExitCommand implements ShellCommand {

    /**
     * @param env in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if(!arguments.equals("")){
            env.writeln("Command exit does not have any arguments.");
            return ShellStatus.CONTINUE;
        }else{
            return ShellStatus.TERMINATE;
        }

    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "exit";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> listOfDescriptions = new ArrayList<>();
        listOfDescriptions.add("Command terminates the program.");

        return listOfDescriptions;
    }
}
