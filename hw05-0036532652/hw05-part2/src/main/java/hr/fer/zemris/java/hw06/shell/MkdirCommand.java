package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.argumentparsers.Parser1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MkdirCommand implements ShellCommand {
    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        String[] args = Parser1.parse(arguments);

        if(args.length != 1){
            env.writeln("mkdir command cannot have multiple arguments.");
            return ShellStatus.CONTINUE;
        }else{

            Path newFile = Paths.get(args[0]);

            try{
                Files.createDirectories(newFile);
                return ShellStatus.CONTINUE;
            }catch(IOException e){
                env.writeln("Error making a directory.");
                return ShellStatus.CONTINUE;
            }
        }
    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "mkdir";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> mkdir = new ArrayList<>();
        mkdir.add("The mkdir command takes a single argument: directory name, and creates the appropriate directory structure.");
        return mkdir;
    }
}
