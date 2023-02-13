package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.List;

/**
 * symbol command could mean that we either want to change the passed symbol or write the current symbol to the prompt
 * PROMPT/MORELINES/MULTILINE
 */
public class SymbolCommand implements ShellCommand{


    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        //trimamo i splitamo argumente po spaceu da vidimo jel imamo 1 ili 2 argumenata
        //1 -> u slucaju da imamo 1 moramo samo ispisat current symbol za koji se trazi
        //2 -> u slucaju da imamo 2 drugi nam je onaj u koji prvi zelimo promijenit

        String[] args = arguments.trim().split(" ");
        if(args.length == 2){
            String command = args[0];
            String newSymbol = args[1];

            //moramo mijenjat
            if(command.equals("PROMPT")){
                Character lastSymbol = env.getPromptSymbol();
                env.setPromptSymbol(newSymbol.charAt(0));
                env.writeln("Symbol for PROMPT changed from " + "'" + lastSymbol + "'" + " to " +
                        env.getPromptSymbol());
                return ShellStatus.CONTINUE;
            }else if(command.equals("MORELINES")){
                Character lastSymbol = env.getMorelinesSymbol();
                env.setMorelinesSymbol((newSymbol.charAt(0)));
                env.writeln("Symbol for MORELINES changed from " + "'" + lastSymbol + "'" + " to " +
                        env.getMorelinesSymbol());
                return ShellStatus.CONTINUE;
            }else if(command.equals("MULTILINE")){
                Character lastSymbol = env.getMultilineSymbol();
                env.setMultilineSymbol(newSymbol.charAt(0));
                env.writeln("Symbol for MULTILINE changed from " + "'" + lastSymbol + "'" + " to " +
                        env.getMultilineSymbol());
                return ShellStatus.CONTINUE;
            }else{
                env.writeln("Invalid input"); //shell except
                return ShellStatus.CONTINUE;
            }
        }else if(args.length == 1){
            String command = args[0];
            //moramo ispisat koja je
            if(command.equals("PROMPT")){

                env.writeln("Symbol for PROMPT is " + "'" + env.getPromptSymbol() + "'");

                return ShellStatus.CONTINUE;

            }else if(command.equals("MORELINES")){
                env.writeln("Symbol for MORELINES is " + "'" + env.getMorelinesSymbol() + "'");

                return ShellStatus.CONTINUE;
            }else if(command.equals("MULTILINE")){
                env.writeln("Symbol for MULTILINE is " + "'" + env.getMultilineSymbol() + "'");

                return ShellStatus.CONTINUE;
            }else{
                env.writeln("Invalid input");
                return ShellStatus.CONTINUE;
            }

        }else{
            env.writeln("Only one or two arguments allowed in this command.");
            return ShellStatus.CONTINUE;
        }
    }


    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "symbol";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> listOfDescriptions = new ArrayList<>();
        listOfDescriptions.add("Avaliable command symbols are: PROMPT, MORELINES and MULTILINE.");
        listOfDescriptions.add("To change a specific command symbol, after desired command enter the char " +
                "you want to set it to.");

        return listOfDescriptions;
    }
}
