package hr.fer.oprpp1.gui.calc;

import hr.fer.oprpp1.gui.calc.model.CalcModel;
import hr.fer.oprpp1.gui.layouts.CalcLayout;
import hr.fer.oprpp1.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/**
 * basic calculator GUI with basic operations
 */
public class Calculator extends JFrame {

    private CalcModel model;
    private Color BG_COLOR = Color.YELLOW;
    private Color BUTTON_BG_COLOR = Color.GRAY;
    private Stack<Double> stack;


    public Calculator(CalcModel model){
        this.model = model;
        this.stack = new Stack<>();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initGUI();
        pack();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new Calculator(new CalcModelImpl()).setVisible(true);
        });
    }

    private void initGUI() {
        Container container = getContentPane();
        container.setLayout(new CalcLayout(2));

        this.setAllDigitsButtons(container);
        this.setDisplayLabel(container);
        this.setBinaryOperationButtons(container);
        this.setSignButton(container);
        this.setDecimalPointButton(container);
        this.setEqualsButton(container);
        this.addClearButton(container);
        this.addResetButton(container);
        this.addPushButton(container);
        this.addPopButton(container);
        this.setInvertibleButtons(container);

    }

    private void setInvertibleButtons(Container container) {


        JCheckBox invCheckbox = new JCheckBox("Inv");
        invCheckbox.setBackground(BUTTON_BG_COLOR);
        invCheckbox.setBorder(BorderFactory.createLineBorder(Color.black));
        invCheckbox.setFont(invCheckbox.getFont().deriveFont(30f));
        container.add(invCheckbox, new RCPosition(5, 7));

        Map<String, MyButton> map = new LinkedHashMap<>();

        map.put("1/x", new MyButton("1/x", "1/x",
                () -> model.setValue(1 / model.getValue()),
                () -> model.setValue(1 / model.getValue()), BUTTON_BG_COLOR));

        map.put("log", new MyButton("log", "10^x",
                () -> model.setValue(Math.log10(model.getValue())),
                () -> model.setValue(pow(10, model.getValue())), BUTTON_BG_COLOR));

        map.put("ln", new MyButton("ln", "e^x",
                () -> model.setValue(Math.log(model.getValue())),
                () -> model.setValue(exp(model.getValue())), BUTTON_BG_COLOR));

        map.put("x^n", new MyButton("x^n", "x^(1/n)",
                () -> applyOperation((x, y) -> Math.pow(x, y)),
                () -> applyOperation((x, y) -> Math.pow(x, 1.0 / y)), BUTTON_BG_COLOR));

        map.put("sin", new MyButton("sin", "arcsin",
                () -> model.setValue(Math.sin(model.getValue())),
                () -> model.setValue(Math.asin(model.getValue())), BUTTON_BG_COLOR));

        map.put("cos", new MyButton("cos", "arccos",
                () -> model.setValue(Math.cos(model.getValue())),
                () -> model.setValue(Math.acos(model.getValue())), BUTTON_BG_COLOR));

        map.put("tan", new MyButton("tan", "arctan",
                () -> model.setValue(Math.tan(model.getValue())),
                () -> model.setValue(Math.atan(model.getValue())), BUTTON_BG_COLOR));

        map.put("ctg", new MyButton("ctg", "arcctg",
                () -> model.setValue(1 / Math.tan(model.getValue())),
                () -> model.setValue(Math.atan(1 / model.getValue())), BUTTON_BG_COLOR));


        int i = 0;
        for(Map.Entry<String, MyButton> entry: map.entrySet()){
            RCPosition pos = new RCPosition((i%4) + 2, (i/4) + 1); //position formula
            MyButton button = entry.getValue();

            button.setBorder(BorderFactory.createLineBorder(Color.black));
            button.setFont(button.getFont().deriveFont(30f));

            container.add(button, pos);
            invCheckbox.addItemListener(button); //subscribe buttons to change

            i++;
        }

    }

    /**
     * Adds pop button for stack use
     * @param container
     */
    private void addPopButton(Container container) {

        JButton button = new JButton("pop");
        button.setBackground(BUTTON_BG_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.black));
        button.setFont(button.getFont().deriveFont(30f));

        button.addActionListener(event -> {
            if(!stack.isEmpty()){
                this.model.setValue(stack.pop());
            }


        });

        container.add(button, new RCPosition(4,7));

    }


    /**
     * Adds push button for stack use
     * @param container
     */
    private void addPushButton(Container container) {

        JButton button = new JButton("push");
        button.setBackground(BUTTON_BG_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.black));
        button.setFont(button.getFont().deriveFont(30f));

        button.addActionListener(event -> {
            double value = this.model.getValue();
            stack.push(value);
            this.model.clear();
        });

        container.add(button, new RCPosition(3,7));
    }

    /**
     * adds button to reset calculator
     * @param container
     */
    private void addResetButton(Container container) {
        JButton button = new JButton("res");
        button.setBackground(BUTTON_BG_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.black));
        button.setFont(button.getFont().deriveFont(30f));

        button.addActionListener(event -> {
            this.model.clearAll();
        });

        container.add(button, new RCPosition(2,7));
    }

    /**
     * adds button to reset calculator
     * @param container
     */
    private void addClearButton(Container container) {
        JButton button = new JButton("clr");
        button.setBackground(BUTTON_BG_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.black));
        button.setFont(button.getFont().deriveFont(30f));

        button.addActionListener(event -> {
            this.model.clear();
        });

        container.add(button, new RCPosition(1,7));

    }

    private void setEqualsButton(Container container) {
        JButton button = new JButton("=");
        button.setBackground(BUTTON_BG_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.black));
        button.setFont(button.getFont().deriveFont(30f));
        RCPosition pos = new RCPosition(1,6);
        container.add(button, pos);

        button.addActionListener(event -> {
            double value = this.model.getValue();
            if(this.model.isActiveOperandSet()){
                double activeOperand = this.model.getActiveOperand();
                double v = model.getValue();
                value = this.model.getPendingBinaryOperation().applyAsDouble(activeOperand, v);
            }

            this.model.clearAll();
            this.model.setValue(value);

        });
    }

    /**
     * adds binary operation buttons (/, *, -, +)
     * @param container
     */
    private void setBinaryOperationButtons(Container container) {
        Map<String, DoubleBinaryOperator> operatorMap = new HashMap<>();
        operatorMap.put("/", (x,y) -> x/y);
        operatorMap.put("*", (x,y) -> x*y);
        operatorMap.put("-", (x,y) -> x-y);
        operatorMap.put("+", (x,y) -> x+y);

        int i = 0;
        for(Map.Entry<String, DoubleBinaryOperator> entry: operatorMap.entrySet()){
            JButton button = new JButton(entry.getKey());
            button.setBackground(BUTTON_BG_COLOR);
            button.setBorder(BorderFactory.createLineBorder(Color.black));
            button.setFont(button.getFont().deriveFont(30f));
            RCPosition pos = new RCPosition(i + 2, 6);
            DoubleBinaryOperator operator = entry.getValue();
            container.add(button, pos);
            button.addActionListener(event -> {
                applyOperation(operator);
            });
            i++;

        }

    }

    /**
     * perfors operation when button is pressed
     * @param operator
     */
    private void applyOperation(DoubleBinaryOperator operator) {
        if(!this.model.isActiveOperandSet()){
            this.model.setActiveOperand(this.model.getValue());
        }else{
            //we perform the pending operation and set its result to be active operand
            double res = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(),
                    model.getValue());
            this.model.setActiveOperand(res);
        }

        //set new value to be the active operation and pressed operation as pending
        this.model.setValue(this.model.getActiveOperand());
        this.model.setPendingBinaryOperation(operator);

    }

    /**
     * sets button "." to add decimal point
     * @param container
     */
    private void setDecimalPointButton(Container container) {
        JButton button = new JButton(".");
        button.setBackground(BUTTON_BG_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.black));
        button.setFont(button.getFont().deriveFont(30f));
        RCPosition pos = new RCPosition(5,5);
        container.add(button, pos);
        button.addActionListener(event -> {
            if(!this.model.isEditable()){
                model.clear();
            }

            model.insertDecimalPoint();
        });
    }

    /**
     * sets button +/- to swap sign
     * @param container
     */
    private void setSignButton(Container container) {
        JButton button = new JButton("+/-");
        button.setBackground(BUTTON_BG_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.black));
        button.setFont(button.getFont().deriveFont(30f));
        RCPosition pos = new RCPosition(5,4);
        container.add(button, pos);
        button.addActionListener(event -> {
            if(!this.model.isEditable()){
                model.clear();
            }

            model.swapSign();
        });
    }


    /**
     * sets digits 1-9
     * @param container
     */
    private void setAllDigitsButtons(Container container) {

        for(int i = 0; i < 10; i++){

            int row;
            int col;

            int a = (i - 1) % 3;
            int b = (i - 1) / 3;

            //formula for row and column
            row = 4 - b;
            col = 3 + a;

            if(i == 0){ //0 position = (5,3)
                row = 5;
                col = 3;

            }

            JButton button = new JButton(String.valueOf(i));
            RCPosition pos = new RCPosition(row, col);
            container.add(button, pos);

            int d = i;

            button.addActionListener(event -> {
                if(!this.model.isEditable()){ //operation was pressed
                    this.model.clear();
                }

                this.model.insertDigit(d);
            });

            button.setBorder(BorderFactory.createLineBorder(Color.black));
            button.setFont(button.getFont().deriveFont(30f));
            button.setBackground(BUTTON_BG_COLOR);

        }
    }

    /**
     * sets output display
     * @param container
     */
    private void setDisplayLabel(Container container) {

        JLabel displayLablel = new JLabel(this.model.toString());
        this.model.addCalcValueListener(x -> displayLablel.setText(this.model.toString()));
        displayLablel.setFont(displayLablel.getFont().deriveFont(30f));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.add(displayLablel, BorderLayout.LINE_END);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        RCPosition displayPos = new RCPosition(1,1);
        container.add(panel, displayPos);

    }


}
