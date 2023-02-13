package hr.fer.oprpp1.math;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ComplexRootedPolynomial {

    private Complex z0;
    private Complex[] complexroots;

    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        this.z0 = constant;
        complexroots = new Complex[roots.length];
        int i = 0;
        for(Complex root: roots){
            complexroots[i++] = root;
        }
    }

    // computes polynomial value at given point z
    public Complex apply(Complex z) {
        Complex res = z0;
        for(int i = 0; i < complexroots.length; i++){
            res = res.multiply(z.sub(complexroots[i]));
        }
        return res;
    }

    // converts this representation to ComplexPolynomial type
    public ComplexPolynomial toComplexPolynom() {

        Map<Integer, Complex> map = new TreeMap<>();

        map.put(0, z0); //pocetak mnozenja

        for(int i = 0; i < this.complexroots.length; i++){
            List<Complex> newComplexList1 = new LinkedList<>();     // tu spremamo rezultate mnozenja sa Z
            List<Complex> newComplexList2 = new LinkedList<>();     // mnozenje s rootovima

            List<Integer> complexPotention1 = new LinkedList<>();
            List<Integer> complexPotention2 = new LinkedList<>();

            Complex root = this.complexroots[i];
            Map<Integer, Complex> updatedMap = new TreeMap<>();

            for(Integer key: map.keySet()){
                newComplexList1.add(map.get(key));                  // stara vrijednost puta Z
                complexPotention1.add(key + 1);                     // potenciju povecamo za 1

                Complex negated = root.multiply(new Complex(-1, 0)); // root u -root
                newComplexList2.add(negated.multiply(map.get(key)));// mnozimo staru vrijednost s -root
                complexPotention2.add(key);                         // potencija ostaje ista

            }

            // kopiranje vrijednosti
            int pos = 0;
            for(Complex complex : newComplexList1) {
                if(!updatedMap.containsKey(complexPotention1.get(pos))) {
                    updatedMap.put(complexPotention1.get(pos), complex);
                } else {
                    updatedMap.put(complexPotention1.get(pos),
                            complex.add(updatedMap.get(complexPotention1.get(pos))));
                }
                pos++;
            }
            pos = 0;
            for(Complex complex : newComplexList2) {
                if(!updatedMap.containsKey(complexPotention2.get(pos))) {
                    updatedMap.put(complexPotention2.get(pos), complex);
                } else {
                    updatedMap.put(complexPotention2.get(pos),
                            complex.add(updatedMap.get(complexPotention2.get(pos))));
                }
                pos++;
            }

            for(int j = 0; j < map.size() + 1; j++) {
                if(updatedMap.containsKey(j)) {
                    map.put(j, updatedMap.get(j));
                }
            }
        }


        Complex[] arr = new Complex[map.size()];

        for(Integer key: map.keySet()){
            arr[key] = map.get(key);
        }

        return new ComplexPolynomial(arr);



    }


    @Override
    public String toString() {

        String str = "";
        str += z0.toString();
        for(int i = 0; i < this.complexroots.length; i++){
            str+= "*" + "(z-" + this.complexroots[i].toString() + ")";
        }

        return str;

    }
    // finds index of closest root for given complex number z that is within
    // treshold; if there is no such root, returns -1
    // first root has index 0, second index 1, etc
    public int indexOfClosestRootFor(Complex z, double treshold) {

        Map<Double, Integer> map = new TreeMap<>();

        for(int i = 0; i < this.complexroots.length; i++){
            double distance = Math.sqrt(Math.pow(z.getRe() - this.complexroots[i].getRe(),2) +
                    Math.pow(z.getIm() - this.complexroots[i].getIm(),2));

            if(distance <= treshold && !map.containsKey(distance)){
                map.put(distance, i);
            }
        }

        if(map.isEmpty()){
            return -1;
        }else{
            for(double key : map.keySet()) {
                if(key <= treshold) {
                    return map.get(key);
                }
            }
        }

        return -1;
    }
}
