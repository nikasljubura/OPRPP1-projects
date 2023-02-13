package hr.fer.oprpp1.fractals;


import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Character.isDigit;

public class Newton {

    public static void main(String[] args) throws IOException {

        Complex[] complexes;

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int rootNum = 1;
        String line;
        List<Complex> roots = new ArrayList<>();


        System.out.print("Root " +  String.valueOf(rootNum) + "> ");

        while(!(line = reader.readLine()).equals("done")){
            String re = "";
            String im = "";
            line = line.strip();

            if(line.length() == 0){
                System.out.println("Illegal root entered. Exiting program.");
                System.exit(-1);
            }

            char[] tokens = line.toCharArray();

            for(int i = 0; i < tokens.length; i++){

                if(isDigit(tokens[i])){//prvi znak broj
                    int j = i;
                    while(j < tokens.length && (tokens[j] != '+' && tokens[j] != '-'
                            && tokens[j] != ' ')){
                        re += tokens[j];
                        j++;
                    }

                    i = j - 1;
                }

                else if(tokens[i] == 'i'){ //prvi znak je i
                    if(i+1 < tokens.length){
                        int j = i + 1;
                        while(j < tokens.length){
                            im += tokens[j];
                            j++;
                        }
                        i = j - 1;
                    }else{
                        im += '1';
                    }
                }

                else if(tokens[i] == '-'){ //prvi znak je -
                    if(i+1 < tokens.length){
                        //jel postoji iduci
                        while(tokens[i+1] == ' '){
                            i++;
                        }
                        int j;
                        if(isDigit(tokens[i+1])){ //on je broj
                            j = i;
                            re += "-";
                            j++;
                            while(j < tokens.length && (tokens[j] != '+' && tokens[j] != '-'
                                    && tokens[j] != ' ')){
                                re += tokens[j];
                                j++;
                            }
                            i = j - 1;
                        }else if(tokens[i+1] == 'i'){ //on je i
                            im += "-";
                            i++;
                            if(i+1 < tokens.length){ //jel postoji iduci
                                j = i + 1;
                                while(j < tokens.length){ //da -> dodajemo sve
                                    im += tokens[j];
                                    j++;
                                }
                                i = j;
                            }else{ //ne -> im je 1
                                im += '1';
                            }
                        }else{ //nije ni i ni znamenka -> greska
                            System.out.println("Pogrešan unos. Završavam program.");
                            System.exit(-1);
                        }
                    }else{ //minus je a ne postoji iduci -> greska
                        System.out.println("Pogrešan unos. Završavam program.");
                        System.exit(-1);
                    }
                }

            }
            if(im.equals("")){
                im += '0';
            }
            if(re.equals("")) {
                re += '0';
            }
            try{
                Complex complex = new Complex(Double.parseDouble(re), Double.parseDouble(im));
                roots.add(complex);
            }catch(Exception e){
                System.out.println("Greška u parsiranju. Završavam program.");
                System.exit(-1);
            }

            rootNum ++;

            System.out.print("Root " +  String.valueOf(rootNum) + "> ");

        }

        //prebacivanje u polje
        complexes = new Complex[roots.size()];
        int i = 0;
        for(Complex root: roots){
            complexes[i++] = root;
        }

        System.out.println("Image of fractal will appear shortly. Thank you.");
        FractalViewer.show(new MojProducer(complexes));


    }

    public static class MojProducer implements IFractalProducer {

        private Complex[] complexes;
        public MojProducer(Complex[] complexes) throws IOException {
            this.complexes = complexes;
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo,
                            IFractalResultObserver observer, AtomicBoolean cancel) {



            ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(new Complex(1,0), this.complexes);
            ComplexPolynomial poly = polynomial.toComplexPolynom();

            int m = 16*16;
            int offset = 0;

            short[] data = new short[width*height];

            for(int y = 0; y < height; y++){
                if(cancel.get()) break;

                for(int x = 0; x < width; x++){

                    double cre = x / (width-1.0) * (reMax - reMin) + reMin;
                    double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;

                    Complex zn = new Complex(cre, cim);
                    int iter = 0;

                    double module;
                    do{
                        Complex numerator = poly.apply(zn);

                        Complex denominator = poly.derive().apply(zn);

                        Complex znold = new Complex(zn.getRe(), zn.getIm());
                        Complex fraction = numerator.divide(denominator);

                        zn = zn.sub(fraction);
                        module = znold.sub(zn).module();
                        iter++;
                    }while(module > 0.001 && iter < m);
                    int index = polynomial.indexOfClosestRootFor(zn, 0.002);
                    data[offset++] =(short) (index + 1);
                }

                observer.acceptResult(data, (short)(poly.order() + 1), requestNo);
            }



        }
    }
}
