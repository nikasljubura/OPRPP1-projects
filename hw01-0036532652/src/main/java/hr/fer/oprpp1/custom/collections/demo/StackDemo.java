package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * demonstates usage of stack
 */
public class StackDemo {

    /**
     *
     * @param args expression which should be evaluated using stack, in postfix notation
     * args.length is always 1, and args[0] is the whole expression
     */
    public static void main(String[] args) {

        String[] arguments = args[0].split("\\s+");

        /*String arg1 = arguments[0]; //first operand
        String arg2 = arguments[1]; //second operand
        String operator = arguments[2]; //operator to be applied*/

        ObjectStack stack = new ObjectStack();

        for (String arg: arguments) {
            try{
               int argument =  Integer.parseInt(arg);
               stack.push(argument);
            }catch(Exception e){

                String operation = arg;
                int arg1 = (Integer) stack.pop();
                int arg2 = (Integer) stack.pop();

                if(operation.equals("/")){
                    if(arg1 == 0){
                        System.out.println("Cannot divide by zero!");
                        return;
                    }else{
                        stack.push(Integer.valueOf(arg2 / arg1));
                    }

                }else if(operation.equals("+")){
                    stack.push(Integer.valueOf(arg2 + arg1));
                }else if(operation.equals("-")){
                    stack.push(Integer.valueOf(arg2 - arg1));
                }else if(operation.equals("*")){
                    stack.push(Integer.valueOf(arg2 * arg1));
                }else if(operation.equals("%")){
                    stack.push(Integer.valueOf(arg2 % arg1));
                }else{
                    System.out.println("Unsupported operation!");
                    return;
                }
            }

        }

        if(stack.size() != 1){
            System.out.println("Error");
        }else{
            System.out.println(stack.pop().toString()); //result
        }


    }
}
