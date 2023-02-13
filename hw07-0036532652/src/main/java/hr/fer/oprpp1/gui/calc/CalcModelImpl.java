package hr.fer.oprpp1.gui.calc;

import hr.fer.oprpp1.gui.calc.model.CalcModel;
import hr.fer.oprpp1.gui.calc.model.CalcValueListener;
import hr.fer.oprpp1.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel
{

    private ArrayList<CalcValueListener> listenerList;
    private boolean isEditable; //is the model editable or not
    private boolean isPositive; //sign
    private String inputDigits; //string variable that holds input digits
    private double value; //numeric string variable
    private String frozenValue = null; // stores frozen String value, null initially
    private double activeOperand; //active operator
    private DoubleBinaryOperator pendingOperation; //operation to be executed
    private boolean isActiveOperandSet;


    public CalcModelImpl() {
        this.listenerList = new ArrayList<>();
        this.inputDigits = "";
        this.value = 0;
        this.isEditable = true;
        this.isPositive = true;
        this.frozenValue = "";
        this.isActiveOperandSet = false;
    }

    /**
     * Prijava promatrača koje treba obavijestiti kada se
     * promijeni vrijednost pohranjena u kalkulatoru.
     *
     * @param l promatrač; ne smije biti <code>null</code>
     * @throws NullPointerException ako je za <code>l</code> predana vrijednost <code>null</code>
     */
    @Override
    public void addCalcValueListener(CalcValueListener l) {


            this.listenerList.add(l);
    }

    /**
     * Odjava promatrača s popisa promatrača koje treba
     * obavijestiti kada se promijeni vrijednost
     * pohranjena u kalkulatoru.
     *
     * @param l promatrač; ne smije biti <code>null</code>
     * @throws NullPointerException ako je za <code>l</code> predana vrijednost <code>null</code>
     */
    @Override
    public void removeCalcValueListener(CalcValueListener l) {

        this.listenerList.remove(l);
    }

    /**
     * Method to notify all listeners subscribed to change of state
     */
    private void stateChanged(){
        for(CalcValueListener l : listenerList){
            l.valueChanged(this);
        }
    }
    /**
     * Vraća trenutnu vrijednost koja je pohranjena u kalkulatoru.
     *
     * @return vrijednost pohranjena u kalkulatoru
     */
    @Override
    public double getValue() {

        return (isPositive ? this.value : -this.value); //
    }

    /**
     * Upisuje decimalnu vrijednost u kalkulator. Vrijednost smije
     * biti i beskonačno odnosno NaN. Po upisu kalkulator
     * postaje needitabilan.
     *
     * @param value vrijednost koju treba upisati
     */
    @Override
    public void setValue(double value) {
        if(value < 0){
            this.isPositive = false;
        } else {
            this.isPositive = true;
        }
        if(value == 0){
            this.inputDigits = ""; //za clear
        }else{
            if(value < 0) value = -value;
            this.inputDigits = String.valueOf(value);
        }
        this.value = value;
        this.isEditable = false; //model stops being editable
        stateChanged();
    }

    /**
     * Vraća informaciju je li kalkulator editabilan (drugim riječima,
     * smije li korisnik pozivati metode {@link #swapSign()},
     * {@link #insertDecimalPoint()} te {@link #insertDigit(int)}).
     *
     * @return <code>true</code> ako je model editabilan, <code>false</code> inače
     */
    @Override
    public boolean isEditable() {
        return this.isEditable;
    }

    /**
     * Resetira trenutnu vrijednost na neunesenu i vraća kalkulator u
     * editabilno stanje.
     */
    @Override
    public void clear() {
        this.isEditable = true;
        this.inputDigits = "";
        this.value = 0;

        stateChanged();
    }

    /**
     * Obavlja sve što i {@link #clear()}, te dodatno uklanja aktivni
     * operand i zakazanu operaciju.
     */
    @Override
    public void clearAll() {
        this.clear();
        this.isActiveOperandSet = false;
        this.isPositive = true;
        this.setPendingBinaryOperation(null);
        stateChanged();
    }

    /**
     * Mijenja predznak unesenog broja.
     *
     * @throws CalculatorInputException ako kalkulator nije editabilan
     */
    @Override
    public void swapSign() throws CalculatorInputException {

            if(!this.isEditable) throw new CalculatorInputException("The model is not editable, cannot swap sign.");

            this.frozenValue = ""; //
            this.isPositive = !this.isPositive;



            stateChanged();

    }

    /**
     * Dodaje na kraj trenutnog broja decimalnu točku.
     *
     * @throws CalculatorInputException ako nije još unesena niti jedna znamenka broja,
     *                                  ako broj već sadrži decimalnu točku ili ako kalkulator nije editabilan
     */
    @Override
    public void insertDecimalPoint() throws CalculatorInputException {

            if(!this.isEditable || this.inputDigits.equals("")) throw new CalculatorInputException("The model is not editable, cannot insert decimal point.");
            this.frozenValue = ""; //*

            if(!this.inputDigits.contains(".")){
                 this.inputDigits += ".";
            }else{
                throw new CalculatorInputException("The decimal point already exists!");
            }

            stateChanged();
    }

    /**
     * U broj koji se trenutno upisuje na kraj dodaje poslanu znamenku.
     * Ako je trenutni broj "0", dodavanje još jedne nule se potiho
     * ignorira.
     *
     * @param digit znamenka koju treba dodati
     * @throws CalculatorInputException ako bi dodavanjem predane znamenke broj postao prevelik za konačan prikaz u tipu {@link Double}, ili ako kalkulator nije editabilan.
     * @throws IllegalArgumentException ako je <code>digit &lt; 0</code> ili <code>digit &gt; 9</code>
     */
    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
            this.frozenValue = ""; //*
            if(!this.isEditable) throw new CalculatorInputException("The model is not editable");

            try{
                String sufix = String.valueOf(digit);
                String newString = this.inputDigits + sufix;
                newString = newString.replaceAll("^0+([0-9])", "$1");
                double tst = Double.parseDouble(newString);
                if(Double.isInfinite(tst)){
                    throw new CalculatorInputException();
                }
                //if everythinh is okay, update variables
                this.value = tst;
                this.inputDigits = newString;
            }catch(IllegalArgumentException exc){
                throw new CalculatorInputException("New value cannot be parsed to double!");
            }

            stateChanged();
    }

    /**
     * Provjera je li upisan aktivni operand.
     *
     * @return <code>true</code> ako je aktivani operand upisan, <code>false</code> inače
     */
    @Override
    public boolean isActiveOperandSet() {
        return this.isActiveOperandSet;
    }

    /**
     * Dohvat aktivnog operanda.
     *
     * @return aktivni operand
     * @throws IllegalStateException ako aktivni operand nije postavljen
     */
    @Override
    public double getActiveOperand() throws IllegalStateException {
        if(isActiveOperandSet){
            return this.activeOperand;
        }else{
            throw new IllegalStateException("Active operand is not set!");
        }
    }

    /**
     * Metoda postavlja aktivni operand na predanu vrijednost.
     * Ako kalkulator već ima postavljen aktivni operand, predana
     * vrijednost ga nadjačava.
     *
     * @param activeOperand vrijednost koju treba pohraniti kao aktivni operand
     */
    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
        this.isActiveOperandSet = true;

    }

    /**
     * Uklanjanje zapisanog aktivnog operanda.
     */
    @Override
    public void clearActiveOperand() {
         this.isActiveOperandSet = false;
    }

    /**
     * Dohvat zakazane operacije.
     *
     * @return zakazanu operaciju, ili <code>null</code> ako nema zakazane operacije
     */
    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return this.pendingOperation;
    }

    /**
     * Postavljanje zakazane operacije. Ako zakazana operacija već
     * postoji, ovaj je poziv nadjačava predanom vrijednošću.
     *
     * @param op zakazana operacija koju treba postaviti; smije biti <code>null</code>
     */
    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
            this.pendingOperation = op;
    }

    /**
     * Vraća tekst koji treba prikazati na zaslonu kalkulatora.
     * Detaljnija specifikacija dana je u uputi za domaću zadaću.
     *
     * @return tekst za prikaz na zaslonu kalkulatora
     */
    @Override
    public String toString(){

            if(!this.frozenValue.equals("")){
                return this.frozenValue;
            }else{
                if(inputDigits.equals("")){
                    if(isPositive){
                        return "0";
                    }else{
                        return "-0";
                    }
                }else if(Double.isNaN(value)){
                    return "Nan";
                }else if(Double.isInfinite(value)){
                    return (this.isPositive ? "" : "-") + "Infinity";
                }else{
                    return (this.isPositive ? "" : "-") + this.inputDigits;
                }

            }
    }

    /**
     * Freezes current string value
     * @param value
     */
    void freezeValue(String value){
        this.frozenValue = value;
    }

    /**
     * Checks if frozen value exists
     * @return
     */
    boolean hasFrozenValue(){
        return (this.frozenValue == "");
    }


}
