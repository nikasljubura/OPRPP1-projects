package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.argumentparsers.Parser2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CopyCommand implements ShellCommand {
    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {


        String[] args = Parser2.parse(arguments);

        if(args.length != 2){
            env.writeln("This command needs two arguments to execute.");
            return ShellStatus.CONTINUE;
        }else{



            Path source = Paths.get(args[0]);
            Path destination = Paths.get(args[1]);

            File src = new File(source.toString());
            File dst = new File(destination.toString());

            //jel postoji src file i je li directory - greske
            if(src.isDirectory()){
                env.writeln("Cannot copy a directory.");
                return ShellStatus.CONTINUE;
            }else if(!src.exists()){
                env.writeln("Source file doesn't exist.");
                return ShellStatus.CONTINUE;
            }

            if(dst.exists() && !dst.isDirectory()){

                env.writeln("Allow to overwrite file? Y/N");

                if(env.readLine().toString().equals("Y")){//allowed to overwrite, destination je file

                    try{
                        //OTVARANJE STREAMOVA
                        //InputStream inputStream = new BufferedInputStream(Files.newInputStream(source));
                        //OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(newFile));

                        BufferedReader br = Files.newBufferedReader(source);
                        BufferedWriter bw = Files.newBufferedWriter(destination);

                        String redak;
                        while((redak = br.readLine()) != null){
                            bw.write(redak + "\n");
                        }

                        br.close();
                        bw.close();

                        return ShellStatus.CONTINUE;

                    }catch(IOException e){
                        env.writeln("Error opening streams.");
                        return ShellStatus.CONTINUE;
                    }


                }else if(env.readLine().toString().equals("N")){
                    return ShellStatus.CONTINUE;
                }else{
                    env.writeln("Please respond with 'Y' for 'yes' or 'N' for 'no'.");
                    return ShellStatus.CONTINUE;
                }

            }else if(dst.exists() && dst.isDirectory()){
                //treba kopirat source u dan direktorij

                String src_filename = src.getName();

                Path newFile = Paths.get(destination.toString(), src_filename);

                try{
                    Files.createFile(newFile);
                }catch(IOException e){
                    env.writeln("Error making a file.");
                    return ShellStatus.CONTINUE;
                }


                try{
                    //OTVARANJE STREAMOVA
                    //InputStream inputStream = new BufferedInputStream(Files.newInputStream(source));
                    //OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(newFile));

                    BufferedReader br = Files.newBufferedReader(source);
                    BufferedWriter bw = Files.newBufferedWriter(newFile);

                    String redak;
                    while((redak = br.readLine()) != null){
                        bw.write(redak + "\n");
                    }

                    br.close();
                    bw.close();

                    return ShellStatus.CONTINUE;

                }catch(IOException e){
                    env.writeln("Error opening streams.");
                    return ShellStatus.CONTINUE;
                }

            }else{

                env.writeln("Invalid arguments.");
                return ShellStatus.CONTINUE;

            }


        }

    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "copy";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> copy = new ArrayList<>();
        copy.add("The copy command expects two arguments: source file name and destination file name");
        copy.add("Command copies the source file into destination file.");
        return copy;
    }
}
