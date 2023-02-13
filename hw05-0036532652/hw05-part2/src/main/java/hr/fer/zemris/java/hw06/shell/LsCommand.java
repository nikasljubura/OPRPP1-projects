package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.argumentparsers.Parser1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LsCommand implements ShellCommand {
    /**
     * @param env       in which the shell is ran
     * @param arguments passed to the command
     * @return continue or terminate
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        String[] args = Parser1.parse(arguments);

        if(args.length != 1){
            env.writeln("ls command only takes one argument. Please enter a valid argument.");
            return ShellStatus.CONTINUE;
        }else{

            File f = new File(args[0]);
            if(!f.isDirectory()){
                env.writeln("argument can only be a directory. Please enter a directory name.");
                return ShellStatus.CONTINUE;
            }else{
                File[] allFiles = f.listFiles();

                for(File file: allFiles){
                    //drwx
                    if(file.isDirectory()){
                        env.write("d");
                    }else{
                        env.write("-");
                    }

                    if(file.canRead()){
                        env.write("r");
                    }else{
                        env.write("-");
                    }

                    if(file.canWrite()){
                        env.write("w");
                    }else{
                        env.write("-");
                    }

                    if(file.canExecute()){
                        env.write("x");
                    }else{
                        env.write("-");
                    }

                    //size in bytes
                    env.write("%10d ".formatted(file.length()));

                    //date

                    try{

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Path path = Paths.get(file.getPath());
                        BasicFileAttributeView faView = Files.getFileAttributeView(
                                path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
                        );
                        BasicFileAttributes attributes = faView.readAttributes();
                        FileTime fileTime = attributes.creationTime();
                        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
                        env.write(formattedDateTime + " ");

                    }catch(IOException e){
                        env.write("Error formatting time.");
                         return ShellStatus.CONTINUE;
                    }

                    //filename

                    env.writeln(file.getName());

                }
            }
        }
        return ShellStatus.CONTINUE;
    }

    /**
     * @return name of the command
     */
    @Override
    public String getCommandName() {
        return "ls";
    }

    /**
     * @return list of instructions for the command usage
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> ls = new ArrayList<>();
        ls.add("Command ls takes a single argument – directory – and writes a directory listing (not recursive).");
        return ls;
    }
}
