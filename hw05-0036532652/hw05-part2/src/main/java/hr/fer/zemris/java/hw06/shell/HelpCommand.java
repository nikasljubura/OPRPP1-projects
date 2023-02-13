package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements ShellCommand{
    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if(arguments.equals("")){
            env.writeln("Commands supported are:");
            env.writeln("charsets, cat, ls, tree, copy, mkdir, hexdump, symbol, exit." +
                    " Please choose one and continue.");
            return ShellStatus.CONTINUE;
        }else{
            String args[] = arguments.trim().split(" ");
            if(args.length == 1){
                ShellCommand command = env.commands().get(args[0]);
                if(command == null){
                    env.writeln("This command doesn't exist.");
                    return ShellStatus.CONTINUE;
                }else{
                    List<String> descriptions = command.getCommandDescription();
                    for(String s: descriptions){
                        env.writeln(s);
                    }
                    return ShellStatus.CONTINUE;
                }

            }else{
                env.writeln("Only one argument is allowed in this command.");
                return ShellStatus.CONTINUE;
            }
        }
    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "help";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {

        List<String> helpMe = new ArrayList<>();
        helpMe.add("If started with no arguments, help lists names of all supported commands.");
        helpMe.add("If started with single argument, help prints name and the description of selected command.");
        return helpMe;
    }
}
