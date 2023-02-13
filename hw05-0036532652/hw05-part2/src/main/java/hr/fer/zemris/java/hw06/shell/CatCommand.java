package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.argumentparsers.Parser1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class CatCommand implements ShellCommand{
    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        if(arguments.equals("")){
            env.writeln("Command should have one or two arguments.");
            return ShellStatus.CONTINUE;
        }


        try{

            //argumenti
            String[] args = Parser1.parse(arguments);//dobit argumente od parsera

            if(args.length == 1){
                //samo je ime datoteke
                Path path = Paths.get(args[0]);
                Charset c = Charset.defaultCharset();
                String line = "";
                try{
                    BufferedReader br = Files.newBufferedReader(path, c);
                    while((line = br.readLine()) != null){
                        env.writeln(line);
                    }
                    return ShellStatus.CONTINUE;
                }catch(IOException e){
                    env.writeln("Couldn't open given file.");
                    return ShellStatus.CONTINUE;
                }

            }else if(args.length == 2){
                //samo je ime datoteke
                Path path = Paths.get(args[0]);
                try {
                    Charset c = Charset.forName(args[1].toString());
                } catch (UnsupportedCharsetException exception) {
                    env.writeln("Charset isn't supported!");
                    return ShellStatus.CONTINUE;
                }
                Charset c = Charset.forName(args[1].toString());
                String line = "";
                try{
                    BufferedReader br = Files.newBufferedReader(path, c);
                    while((line = br.readLine()) != null){
                        env.writeln(line);
                    }
                    return ShellStatus.CONTINUE;
                }catch(IOException e){
                    env.writeln("Couldn't open given file.");
                    return ShellStatus.CONTINUE;
                }
            }else{
                env.writeln("Command should have one or two arguments.");
                return ShellStatus.CONTINUE;
            }


        }catch(InvalidParameterException ex){
            env.writeln("Invalid parameters entered. Please enter parameters in a correct way.");
            return ShellStatus.CONTINUE;
        }



    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "cat";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> cat = new ArrayList<>();
        cat.add("This command opens given file and writes its content to console.");
        cat.add("Command cat takes one or two arguments.");
        cat.add("The first argument is path to some file and is mandatory.");
        cat.add("The second argument is charset name that should be used to interpret chars from bytes. If not provided," +
                " a defaultplatform charset should be used.");
        return cat;
    }
}
