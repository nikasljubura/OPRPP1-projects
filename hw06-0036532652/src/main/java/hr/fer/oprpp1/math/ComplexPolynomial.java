package hr.fer.oprpp1.math;

import java.util.Map;
import java.util.TreeMap;

public class ComplexPolynomial {
    // ...

    private Complex[] complexfactors;

// constructor
    public ComplexPolynomial(Complex ...factors) {
        complexfactors = new Complex[factors.length];
        for(int i = 0; i < factors.length; i++){
            this.complexfactors[i] = factors[i];
        }
    }
    // returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
    public short order() {
        return (short)(complexfactors.length - 1);
    }


    // computes a new polynomial this*p
    public ComplexPolynomial multiply(ComplexPolynomial p) {

        Map<Integer, Complex> map = new TreeMap<>();

        for(int i = 0; i < this.complexfactors.length; i++){
            for(int j = 0; i < p.complexfactors.length; j++){
                Complex res = this.complexfactors[i].multiply(p.complexfactors[i]);
                if(map.containsKey(i+j)){
                    map.put(i+j,map.get(i+1).add(res));
                }else{
                    map.put(i+j, res);
                }
            }
        }
        Complex[] arr = new Complex[map.size()];
        int i = 0;
        for(Complex complexnum: map.values()){
            arr[i++] = complexnum;
        }

        return new ComplexPolynomial(arr);

    }


    // computes first derivative of this polynomial; for example, for
    // (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
    public ComplexPolynomial derive() {

        Complex[] newArr = new Complex[this.complexfactors.length - 1];

        for(int i = 1; i <= newArr.length; i++){
            newArr[i-1] = this.complexfactors[i].multiply(new Complex(i,0));
        }

        return new ComplexPolynomial(newArr);

    }
    // computes polynomial value at given point z
    public Complex apply(Complex z) {

        Complex res = complexfactors[0]; //0,0
        for(int i = 1; i < this.complexfactors.length; i++){
            res = res.add(this.complexfactors[i].multiply(z.power(i)));
        }
        return res;
    }
    @Override
    public String toString() {

        String str = "";
        for(int i = this.complexfactors.length - 1; i > 0; i--){
            str += "(" + this.complexfactors[i].toString() + ")" + "z" + "^" + i + "+";
        }
        str += this.complexfactors[0];

        return str;

    }
}
