package hr.fer.oprpp1.math;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.pow;

public class Complex {


    private double re;
    private double im;

    public static final Complex ZERO = new Complex(0,0);
    public static final Complex ONE = new Complex(1,0);
    public static final Complex ONE_NEG = new Complex(-1,0);
    public static final Complex IM = new Complex(0,1);
    public static final Complex IM_NEG = new Complex(0,-1);

    public Complex() {

        this.re = 0;
        this.im = 0;

    }
    public Complex(double re, double im) {

        this.re = re;
        this.im = im;

    }

    // returns module of complex number
    public double module() {

        return Math.sqrt(pow(re,2)+ pow(im,2));

    }

    // returns this*c
    public Complex multiply(Complex c) {
        double real = c.re * this.re - c.im * this.im;
        double imag = this.re * c.im + this.im * c.re;
        return new Complex(real, imag);
    }
    // returns this/c
    public Complex divide(Complex c) {
        double real = (this.re * c.re + this.im * c.im)/(pow(c.re,2) + pow(c.im,2));
        double im =  (this.im * c.re - this.re * c.im)/(pow(c.re,2) + pow(c.im,2));
        return new Complex(real,im);
    }
    // returns this+c
    public Complex add(Complex c) {
        double re = this.re + c.re;
        double im = this.im + c.im;
        return new Complex(re,im);
    }
    // returns this-c
    public Complex sub(Complex c) {
        double re = this.re - c.re;
        double im = this.im - c.im;
        return new Complex(re,im);
    }
    // returns -this
    public Complex negate() {
        return new Complex(this.re, -this.im);
    }
    // returns this^n, n is non-negative integer
    public Complex power(int n) {
        Complex compl = new Complex(this.re, this.im);
        if(n == 0){
           compl.re = 1;
           compl.im = 0;
        }else{

            for(int i = 0; i < n - 1; i++){
                compl = multiply(compl);
            }
        }

        return compl;
    }
    // returns n-th root of this, n is positive integer
    public List<Complex> root(int n) {

        double fi = (2*Math.PI) / n;
        double module_root = Math.pow(this.module(), (1/n));

        List<Complex> roots = new ArrayList<>();
        for(int k = 0; k < n - 1; k++){
            double re = module_root * cos((fi+2*k*Math.PI)/n);
            double im = module_root * Math.sin((fi+2*k*Math.PI)/n);
            Complex newCompl = new Complex(re,im);
            roots.add(newCompl);
        }

        return roots;
    }
    @Override
    public String toString() {

        String str;

        if(this.im > 0){
            str = "(" +  String.valueOf(this.re) + "+" + String.valueOf(this.im) + "i" + ")";
        }else if(this.im < 0){
            str = "(" + String.valueOf(this.re)  + String.valueOf(this.im) + "i" + ")";
        }else{
            str = "(" + String.valueOf(this.re) + ")";
        }

        return str;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }
}
