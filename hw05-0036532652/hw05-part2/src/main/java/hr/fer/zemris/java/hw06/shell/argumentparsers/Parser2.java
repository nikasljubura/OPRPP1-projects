package hr.fer.zemris.java.hw06.shell.argumentparsers;

import java.security.InvalidParameterException;

/**
 * Parser works for command that expects 2 file arguments
 */
public class Parser2 {

    public static String[] parse(String arguments){

        //za 2 filea

        String file1 = "";
        String file2 = "";

        String[] args = new String[2];

        arguments = arguments.strip();

        char[] arr = arguments.toCharArray();

        int cnt = 0; //moze ih imat 2 ili 4 (ili ne imat -> onda su fileovibez navodnika -> else dio)

        if(arguments.contains("\"")){ //brojimo navodnike
            for(int i = 0; i < arr.length; i++){
                if(arr[i] == '\"'){
                    cnt++;
                }
            }

            //jel ima tocno 2 ili 4 navodnika
            if(cnt != 2 && cnt != 4){
                throw new InvalidParameterException();
            }

            if(cnt == 2){ //jedan moze bit u navodnicima drugi ne mora bit...

                int k = arr[0];

                if(k == '\"'){ //prvi file je u navodnicima

                    for(int i = 0; i < arr.length; i++) {
                        if (i == 0 && arr[i] == '\"') {
                            int j = i + 1;
                            while (arr[j] != '\"') {
                                file1 += arr[j];
                                j++;
                            }
                            i = j + 1;
                        }

                        if(i < arr.length && arr[i] != ' '){
                            file2 += arr[i];
                        }
                    }

                    if(file1.length() + 2 == arr.length){ //mora imat dva filea
                        //imam samo file bez charseta
                        throw new InvalidParameterException();

                    }else{ //imam i charset
                        args = new String[2];
                        args[0] = file1;
                        args[1] = file2;
                        return args;
                    }


                }else{//drugi file je u navodnicima

                    for(int i = 0; i < arr.length; i++){
                        if(arr[i] == '\"'){
                            int j = i + 1;
                            while(arr[j] != '\"'){
                                file2 += arr[j];
                                j++;
                            }
                            i = j + 1;
                        }

                        if(i != arr.length && arr[i] != ' '){
                            file1 += arr[i];
                        }

                    }

                    args = new String[2];
                    args[0] = file1;
                    args[1] = file2;
                    return args;

                }

            }

            if(cnt == 4){
                for(int i = 0; i < arr.length; i++) {
                    if (i == 0 && arr[i] == '\"') {
                        int j = i + 1;
                        while (arr[j] != '\"') {
                            file1 += arr[j];
                            j++;
                        }
                        i = j + 1;
                    }

                    if(arr[i] != ' '){
                        if(arr[i] == '\"'){
                            int j = i + 1;
                            while(arr[j] != '\"'){
                                file2 += arr[j];
                                j++;
                            }
                            i = j + 1;
                        }
                    }
                }

                args = new String[2];
                args[0] = file1;
                args[1] = file2;
                return args;
            }



        }else{ //nema navodnika
            //ovo je oke
            args = arguments.split(" ");

        }

        return args;
    }
}
