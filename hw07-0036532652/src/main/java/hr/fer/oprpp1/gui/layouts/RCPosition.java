package hr.fer.oprpp1.gui.layouts;

import java.util.Objects;

/**
 * Represents row - column position of the component
 */

public class RCPosition {

    private int row;
    private int column;

    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * factory method
     * @param text
     * @return a RCposition object
     */
    public static RCPosition parse(String text) {
        String [] arr = text.split(",");


        try{

            int arg1 = Integer.parseInt(arr[0]);
            int arg2 = Integer.parseInt(arr[1]);
            return new RCPosition(arg1, arg2);

        }catch(Exception e){

            throw new IllegalArgumentException("Illegal argument when parsing.");

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RCPosition)) return false;
        RCPosition that = (RCPosition) o;
        return getRow() == that.getRow() && getColumn() == that.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }
}
