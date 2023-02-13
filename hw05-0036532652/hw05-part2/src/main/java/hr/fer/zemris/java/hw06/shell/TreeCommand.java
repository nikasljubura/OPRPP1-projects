package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.argumentparsers.Parser1;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TreeCommand implements ShellCommand {
    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        String[] args = Parser1.parse(arguments);
        if(args.length != 1){
            env.writeln("tree command takes only one argument.");
            return ShellStatus.CONTINUE;
        }else{
            File folder = new File(args[0]);
            if(!folder.isDirectory()){
                env.writeln("Given file is not a directory. Please enter a valid directory name.");
                return ShellStatus.CONTINUE;
            }else{
                int indent = 0;
                StringBuilder sB = new StringBuilder();
                printDirectoryTree(folder,indent, sB, env);
                env.writeln(sB.toString());
                return ShellStatus.CONTINUE;
            }

        }
    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "tree";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> tree = new ArrayList<>();
        tree.add("The tree command expects a single argument: directory name and prints a tree.");
        return tree;
    }

    private static void printDirectoryTree(File folder, int ind, StringBuilder sB, Environment env){
        if (!folder.isDirectory()) {
            env.writeln("Given folder is not a directory. Please enter valid directory folder.");
            return;
        }
        sB.append(getInd(ind));
        sB.append("+--");
        sB.append(folder.getName());
        sB.append("\n");
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                printDirectoryTree(file, ind + 1, sB, env);
            } else {
                print(file, ind + 1, sB);
            }
        }
    }

    private static String getInd(int ind){
        StringBuilder sB = new StringBuilder();
        for(int i = 0; i < ind; i++){
            sB.append("|  ");
        }

        return sB.toString();
    }

    private static void print(File file, int ind, StringBuilder sB){
        sB.append(getInd(ind));
        sB.append("+--");
        sB.append(file.getName() + "\n");

    }
}
