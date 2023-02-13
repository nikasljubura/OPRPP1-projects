package hr.fer.zemris.java.hw06.shell.argumentparsers;

import java.security.InvalidParameterException;

/**
 * parses arguments for commands that expect 2 arguments (file and string) or 1 file argument
 */
public class Parser1 {

    public static String[] parse(String arguments){



        //moramo prvo provjerit dal containa ove znakove ""
        //ako ne containa te znakove vracamo argumente splitane po razmaku

        arguments = arguments.strip();
        char[] arr = arguments.toCharArray();

        int cnt = 0;

        String file = "";
        String charset = "";

        String args[];


        if(arguments.contains("\"")){
            for(int i = 0; i < arr.length; i++){
                if(arr[i] == '\"'){
                    cnt++;
                }
            }

            arguments = arguments.strip();
            //jel ima tocno dva navodnika
            if(cnt != 2){
                throw new InvalidParameterException();
            }

            for(int i = 0; i < arr.length; i++){
                if(i == 0 && arr[i] == '\"'){
                    int j = i + 1;
                    while(arr[j] != '\"'){
                        file+=arr[j];
                        j++;
                    }
                    if((j+1) < arr.length && arr[j+1] != ' '){
                        throw new InvalidParameterException();
                    }
                    i = j+1;
                }
                if(i < arr.length){
                    charset += arr[i];
                }

            }

            if(file.length() + 2 == arr.length){
                //imam samo file bez charseta
                args = new String[1];
                args[0] = file;
                return args;

            }else{ //imam i charset
                args = new String[2];
                args[0] = file;
                args[1] = charset.strip();
                return args;
            }


        }else{

            args = arguments.split(" ");

        }

        return args;
    }


}
