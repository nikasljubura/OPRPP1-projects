package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.argumentparsers.Parser1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HexDumpCommand implements ShellCommand {
    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments){

        String args[] = Parser1.parse(arguments);

        if(args.length != 1){
            env.writeln("hexdump command takes only one argument - file name.");
            return ShellStatus.CONTINUE;

        }else{
            String filename = args[0];

            File file = new File(filename);

            if(file.isDirectory()){
                env.writeln("Argument cannot be a directory.");
                return ShellStatus.CONTINUE;
            }

            try{
                InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
                byte[] buffer = new byte[16];
                int bytesRead = 0;
                int adress_counter = 0;
                while((bytesRead = inputStream.read(buffer)) >= 0) {

                    env.write(String.format("%1$08X: ",adress_counter)); //ispis adrese

                    int i = 0;
                    for(i = 0; i < bytesRead; i++) {

                        if(i==7){
                            env.write(String.format("%1$02X|", buffer[i]));
                        }else{
                            env.write(String.format("%1$02X ", buffer[i]));
                        }

                    }
                    while(i<16){
                        env.write("   ");
                        i++;
                    }

                    env.write("|");

                    for(i = 0; i < bytesRead; i++) {

                      if(buffer[i] < 32 || buffer[i] > 127){
                          env.write(".");
                      }else{
                          char c = (char) buffer[i];
                          env.write(String.valueOf(c));
                      }

                    }
                    adress_counter += 16;

                    env.writeln("");
                }



                inputStream.close();
                return ShellStatus.CONTINUE;
            }catch(IOException e) {
                env.writeln("Error opening a file.");
                return ShellStatus.CONTINUE;
            }





        }

    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "hexdump";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> hexdump = new ArrayList<>();
        hexdump.add("Finally, the hexdump command expects a single argument: file name, and produces hex-output.");
        return hexdump;
    }
}
