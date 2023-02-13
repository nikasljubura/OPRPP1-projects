package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * provides methods unique to each of the commands, every command will implement this interface
 */
public interface ShellCommand {


    /**
     *
     * @param env in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     *
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     *
     * @return name of the command
     */
    String getCommandName();

    /**
     *
     * @return list of instructions for the command usage
     */
    List<String> getCommandDescription();

}
