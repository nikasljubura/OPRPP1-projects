package hr.fer.zemris.java.hw06.shell;

import java.sql.SQLOutput;

/**
 * represents a simple shell that is easy to use by user
 */
public class MyShell {

    public static void main(String[] args) {



            ShellEnvironment environment = new ShellEnvironment(); //build environment

        try{
            environment.writeln("Welcome to MyShell v 1.0");
            ShellStatus status = ShellStatus.CONTINUE;


            while(status != ShellStatus.TERMINATE){

                environment.write(environment.getPromptSymbol() + " ");
                String s = environment.readLine();
                while(s.endsWith(environment.getMorelinesSymbol().toString())){
                    environment.write(environment.getMultilineSymbol() + " ");
                    s = s.substring(0,s.length()-1); //trebamo sve osim zadnjeg znaka
                    s += " " + environment.readLine();
                }

                String strings[] = s.split("\\s+");
                String commandName = strings[0];
                String arguments = "";
                for(int i = 1; i < strings.length; i++){
                    arguments += strings[i];
                    arguments += " ";
                }

                ShellCommand command = environment.commands().get(commandName);
                if(command != null){
                    status = command.executeCommand(environment, arguments);
                }else{
                    environment.writeln("Command isn't supported. Please enter one of the supported commands.");
                }



            }
        }catch (ShellIOException exception){
            System.out.println(exception.getMessage());
        }

    }
}
