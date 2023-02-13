package hr.fer.zemris.java.hw06.shell;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CharsetsCommand implements ShellCommand {
    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

       if(!arguments.equals("")){
           env.writeln("This command takes no arguments.");
           return ShellStatus.CONTINUE;
       }else{
           Map<String, Charset> charsets;
           charsets = Charset.availableCharsets();

           Iterator<Charset> iterator = charsets.values().iterator();
           while (iterator.hasNext()) {
               Charset charset = (Charset)iterator.next();
               env.writeln(charset.displayName());
           }
           return ShellStatus.CONTINUE;
       }
    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
       return "charsets";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
       List<String> charsets = new ArrayList<>();
       charsets.add("This command takes no arguments and lists names of supported charsets for your Java platform.");
       return charsets;
    }
}
