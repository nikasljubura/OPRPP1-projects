package hr.fer.zemris.java.hw06.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * implements all methods from interface enviroment -> represents an object which will be passed to MyShell and
 * every command (they all share those methods)
 */
public class ShellEnvironment implements Environment {

    private BufferedReader reader;
    private SortedMap<String, ShellCommand> map;
    private Character multilineSymbol;
    private Character promptSymbol;
    private Character morelinesSymbol;

    public ShellEnvironment(){

        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.map = new TreeMap<>();

        map.put("symbol", new SymbolCommand());
        map.put("exit", new ExitCommand());
        map.put("help", new HelpCommand());
        map.put("charsets", new CharsetsCommand());
        map.put("cat", new CatCommand());
        map.put("ls", new LsCommand());
        map.put("tree", new TreeCommand());
        map.put("copy", new CopyCommand());
        map.put("mkdir", new MkdirCommand());
        map.put("hexdump", new HexDumpCommand());

        this.multilineSymbol = '|';
        this.promptSymbol = '>';
        this.morelinesSymbol = '\\';

    }


    /**
     *
     * @return single line read from the command prompt
     * @throws ShellIOException
     */
    @Override
    public String readLine() throws ShellIOException {

       try{
           return this.reader.readLine().trim();
       }catch(IOException exc){
           throw new ShellIOException("reading exception");
       }

    }


    /**
     *
     * @param text that should be written to the console without new line
     * @throws ShellIOException
     */
    @Override
    public void write(String text) throws ShellIOException {
        try{
            System.out.print(text);
        }catch(ShellIOException e){
            throw new ShellIOException("writing exception");
        }
    }


    /**
     *
     * @param text that should be written to the console with a new line
     * @throws ShellIOException
     */
    @Override
    public void writeln(String text) throws ShellIOException {
        try{
            System.out.println(text);
        }catch(ShellIOException e){
            throw new ShellIOException("writing exception");
        }
    }

    /**
     * returns map of sorted commands (an unmodifiable one)
     * @return
     */
    @Override
    public SortedMap<String, ShellCommand> commands() {

        return Collections.unmodifiableSortedMap(this.map);

    }


    /**
     *
     * @return current multilineSymbol
     */
    @Override
    public Character getMultilineSymbol() {
        return this.multilineSymbol;
    }

    /**
     * sets the multilineSymbol to what the user passed
     * @param symbol
     */
    @Override
    public void setMultilineSymbol(Character symbol) {
        this.multilineSymbol = symbol;
    }

    /**
     *
     * @return current prompt symbol
     */
    @Override
    public Character getPromptSymbol() {
        return this.promptSymbol;
    }

    /**
     * sets the prompt symbol to what the user passed
     * @param symbol
     */
    @Override
    public void setPromptSymbol(Character symbol) {
        this.promptSymbol = symbol;
    }

    /**
     *
     * @return the current morelinessymbol
     */
    @Override
    public Character getMorelinesSymbol() {
        return this.morelinesSymbol;
    }


    /**
     * sets the morelinessymbol to what the user passed
     * @param symbol
     */
    @Override
    public void setMorelinesSymbol(Character symbol) {
         this.morelinesSymbol = symbol;
    }
}
