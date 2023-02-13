package hr.fer.oprpp1.fractals;

import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.mandelbrot.Mandelbrot;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Character.isDigit;

public class NewtonParallel {

    public static void main(String[] args) throws IOException {

        Complex[] complexes;

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int rootNum = 1;
        String line;
        List<Complex> roots = new ArrayList<>();


        System.out.print("Root " + String.valueOf(rootNum) + "> ");

        while (!(line = reader.readLine()).equals("done")) {
            String re = "";
            String im = "";
            line = line.strip();

            if (line.length() == 0) {
                System.out.println("Illegal root entered. Exiting program.");
                System.exit(-1);
            }

            char[] tokens = line.toCharArray();

            for (int i = 0; i < tokens.length; i++) {

                if (isDigit(tokens[i])) {//prvi znak broj
                    int j = i;
                    while (j < tokens.length && (tokens[j] != '+' && tokens[j] != '-'
                            && tokens[j] != ' ')) {
                        re += tokens[j];
                        j++;
                    }

                    i = j - 1;
                } else if (tokens[i] == 'i') { //prvi znak je i
                    if (i + 1 < tokens.length) {
                        int j = i + 1;
                        while (j < tokens.length) {
                            im += tokens[j];
                            j++;
                        }
                        i = j - 1;
                    } else {
                        im += '1';
                    }
                } else if (tokens[i] == '-') { //prvi znak je -
                    if (i + 1 < tokens.length) {
                        //jel postoji iduci
                        while (tokens[i + 1] == ' ') {
                            i++;
                        }
                        int j;
                        if (isDigit(tokens[i + 1])) { //on je broj
                            j = i;
                            re += "-";
                            j++;
                            while (j < tokens.length && (tokens[j] != '+' && tokens[j] != '-'
                                    && tokens[j] != ' ')) {
                                re += tokens[j];
                                j++;
                            }
                            i = j - 1;
                        } else if (tokens[i + 1] == 'i') { //on je i
                            im += "-";
                            i++;
                            if (i + 1 < tokens.length) { //jel postoji iduci
                                j = i + 1;
                                while (j < tokens.length) { //da -> dodajemo sve
                                    im += tokens[j];
                                    j++;
                                }
                                i = j;
                            } else { //ne -> im je 1
                                im += '1';
                            }
                        } else { //nije ni i ni znamenka -> greska
                            System.out.println("Illegal root entered. Exiting program.");
                            System.exit(-1);
                        }
                    } else { //minus je a ne postoji iduci -> greska
                        System.out.println("Illegal root entered. Exiting program..");
                        System.exit(-1);
                    }
                }

            }
            if (im.equals("")) {
                im += '0';
            }
            if (re.equals("")) {
                re += '0';
            }
            try {
                Complex complex = new Complex(Double.parseDouble(re), Double.parseDouble(im));
                roots.add(complex);
            } catch (Exception  e) {
                System.out.println("Parsing error. Exiting program.");
                System.exit(-1);
            }

            rootNum++;

            System.out.print("Root " + String.valueOf(rootNum) + "> ");

        }

        if(rootNum < 3){
            System.out.println("Minimum of 2 roots are required. Exiting program.");
            System.exit(-1);
        }

        //prebacivanje u polje
        complexes = new Complex[roots.size()];
        int j = 0;
        for (Complex root : roots) {
            complexes[j++] = root;
        }

        //---
        List<String> listHolder = new ArrayList<>();
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("--")) {
                listHolder.add(args[i]);
            }else if(args[i].startsWith("-")) {
                if(i + 1 < args.length) {
                    listHolder.add(args[i] + " " + args[i + 1]);
                } else {
                    System.out.println("Ivalid arguments entered. Exiting program.");
                    System.exit(-1);
                }
            }
        }

        String[] copyArr = new String[listHolder.size()];
        for(int i = 0; i < listHolder.size(); i++) {
            copyArr[i] = listHolder.get(i);
        }
        args = copyArr;
        //---

        int workers = 0;
        int tracks = 0;

        if (args.length == 0) {
            workers = Runtime.getRuntime().availableProcessors();
            tracks = 4 * workers;

        } else {
            //length nije 0
            if (args.length == 2) {
                boolean w = false;
                if (args[0].startsWith("--workers=") || args[0].startsWith("-w ")) {
                    w = true;
                    if (args[0].startsWith("--workers=")) {
                        String x = args[0];
                        x = x.substring(10, x.length());
                        workers = Integer.parseInt(x);
                    } else if (args[0].startsWith("-w ")) {
                        String x = args[0];
                        x = x.substring(3, x.length());
                        workers = Integer.parseInt(x);
                    }
                } else if (args[0].startsWith("--tracks=") || args[0].startsWith("-t ")) {
                    if (args[0].startsWith("--tracks=")) {
                        String x = args[0];
                        x = x.substring(9, x.length());
                        tracks = Integer.parseInt(x);
                    } else if (args[0].startsWith("-t ")) {
                        String x = args[0];
                        x = x.substring(3, x.length());
                        tracks = Integer.parseInt(x);
                    }
                } else {
                    System.out.println("Ivalid arguments entered. Exiting program.");
                    System.exit(-1);
                }

                if (w == true) {
                    if (args[1].startsWith("--tracks=") || args[1].startsWith("-t ")) {
                        if (args[1].startsWith("--tracks=")) {
                            String x = args[1];
                            x = x.substring(9, x.length());
                            tracks = Integer.parseInt(x);
                        } else if (args[1].startsWith("-t ")) {
                            String x = args[1];
                            x = x.substring(3, x.length());
                            tracks = Integer.parseInt(x);
                        }} else {
                            System.out.println("Ivalid arguments entered. Exiting program.");
                            System.exit(-1);
                        }
                    } else {
                        if (args[1].startsWith("--workers=")) {
                            String x = args[1];
                            x = x.substring(10, x.length());
                            workers = Integer.parseInt(x);
                        } else if (args[1].startsWith("-w ")) {
                            String x = args[1];
                            x = x.substring(3, x.length());
                            workers = Integer.parseInt(x);
                        } else {

                            System.out.println("Ivalid arguments entered. Exiting program.");
                            System.exit(-1);
                        }
                    }
                }
                 else if (args.length == 1) {
                    boolean t = false;
                    if (args[0].startsWith("--workers=") || args[0].startsWith("-w ")) {
                        if (args[0].startsWith("--workers=")) {
                            String x = args[0];
                            x = x.substring(10, x.length());
                            workers = Integer.parseInt(x);
                        } else if (args[0].startsWith("-w ")) {
                            String x = args[0];
                            x = x.substring(3, x.length());
                            workers = Integer.parseInt(x);
                        }
                    } else if (args[0].startsWith("--tracks=") || args[0].startsWith("-t ")) {
                        t = true;
                        if (args[0].startsWith("--tracks=")) {
                            String x = args[0];
                            x = x.substring(9, x.length());
                            tracks = Integer.parseInt(x);
                        } else if (args[0].startsWith("-t ")) {
                            String x = args[0];
                            x = x.substring(3, x.length());
                            tracks = Integer.parseInt(x);
                        }
                    } else {
                        System.out.println("Ivalid arguments entered. Exiting program.");
                        System.exit(-1);
                    }

                    if (t == true) {
                        workers = Runtime.getRuntime().availableProcessors();
                    } else {
                        tracks = 4 * Runtime.getRuntime().availableProcessors();
                    }


                } else {
                    System.out.println("Ivalid arguments entered. Exiting program.");
                    System.exit(-1);
                }
            }

        if(1 > tracks) {
            System.out.println("Ivalid arguments entered. Exiting program.");
            System.exit(-1);
        }

        System.out.println("Image of fractal will appear shortly. Thank you.");
        FractalViewer.show(new MojProducer(complexes, workers, tracks));
    }

    public static class PosaoIzracuna implements Runnable {
        double reMin;
        double reMax;
        double imMin;
        double imMax;
        int width;
        int height;
        int yMin;
        int yMax;
        int m;
        short[] data;
        AtomicBoolean cancel;
        private Complex[] complexes;
        public static PosaoIzracuna NO_JOB = new PosaoIzracuna();

        private PosaoIzracuna() {

        }


        public PosaoIzracuna(double reMin, double reMax, double imMin,
                             double imMax, int width, int height, int yMin, int yMax,
                             int m, short[] data, AtomicBoolean cancel, Complex[] complexes) {
            super();
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.cancel = cancel;
            this.complexes = complexes;

        }

        @Override
        public void run() {

            int offset = this.yMin * this.width;

            ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(new Complex(1, 0), this.complexes);
            ComplexPolynomial poly = polynomial.toComplexPolynom();


            for (int y = yMin; y <= yMax; y++) {
                if (cancel.get()) break;

                for (int x = 0; x < width; x++) {

                    double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

                    Complex zn = new Complex(cre, cim);
                    int iter = 0;

                    double module;
                    do {
                        Complex numerator = poly.apply(zn);

                        Complex denominator = poly.derive().apply(zn);

                        Complex znold = new Complex(zn.getRe(), zn.getIm());
                        Complex fraction = numerator.divide(denominator);

                        zn = zn.sub(fraction);
                        module = znold.sub(zn).module();
                        iter++;
                    } while (module > 0.001 && iter < this.m);
                    int index = polynomial.indexOfClosestRootFor(zn, 0.002);
                    data[offset++] = (short) (index + 1);
                }

                //promijenit
            }

        }
    }

        public static class MojProducer implements IFractalProducer {

            private Complex[] complexes;
            private int brojTraka;
            private int brojRadnika;

            public MojProducer(Complex[] complexes, int brojTraka, int brojRadnika) throws IOException {

                this.complexes = complexes;
                this.brojRadnika = brojRadnika;
                this.brojTraka = brojTraka;
            }

            @Override
            public void produce(double reMin, double reMax, double imMin, double imMax,
                                int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
                if(brojTraka > width) {
                    brojTraka = width;
                }
                System.out.println("Zapocinjem izracun...");
                int m = 16 * 16 * 16;
                short[] data = new short[width * height];
                final int brojTraka = this.brojTraka;
                int brojYPoTraci = height / brojTraka;

                System.out.println("Broj radnika je " + String.valueOf(brojRadnika) + ", a broj poslova " + String.valueOf(brojTraka) + ".");

                ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(new Complex(1, 0), this.complexes);
                ComplexPolynomial poly = polynomial.toComplexPolynom();




                final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

                Thread[] radnici = new Thread[this.brojRadnika];

                for (int i = 0; i < radnici.length; i++) {
                    radnici[i] = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                PosaoIzracuna p = null;
                                try {
                                    p = queue.take();
                                    if (p == PosaoIzracuna.NO_JOB) break;
                                } catch (InterruptedException e) {
                                    continue;
                                }
                                p.run();
                            }
                        }
                    });
                }
                for (int i = 0; i < radnici.length; i++) {
                    radnici[i].start();
                }

                for (int i = 0; i < brojTraka; i++) {
                    int yMin = i * brojYPoTraci;
                    int yMax = (i + 1) * brojYPoTraci - 1;
                    if (i == brojTraka - 1) {
                        yMax = height - 1;
                    }
                    PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, this.complexes);
                    while (true) {
                        try {
                            queue.put(posao);
                            break;
                        } catch (InterruptedException e) {
                        }
                    }
                }
                for (int i = 0; i < radnici.length; i++) {
                    while (true) {
                        try {
                            queue.put(PosaoIzracuna.NO_JOB);
                            break;
                        } catch (InterruptedException e) {
                        }
                    }
                }

                for (int i = 0; i < radnici.length; i++) {
                    while (true) {
                        try {
                            radnici[i].join();
                            break;
                        } catch (InterruptedException e) {
                        }
                    }
                }

                System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
                observer.acceptResult(data, (short) (poly.order() + 1), requestNo);
            }
        }


    }

