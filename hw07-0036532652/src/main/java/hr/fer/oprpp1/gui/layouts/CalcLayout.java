package hr.fer.oprpp1.gui.layouts;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

public class CalcLayout implements LayoutManager2 {

    private int row_col_space; //num of spaces between rows and cols in pixels
    private final int rows = 5; //numbers of rows and cols are unchangeble
    private final int cols = 7;
    private Map<RCPosition, Component> componentMap;


    public CalcLayout(int row_col_space){
        this.row_col_space = row_col_space;
        this.componentMap = new HashMap<>();
    }

    public CalcLayout(){
        this.row_col_space = 0;
        this.componentMap = new HashMap<>();
    }

    public int getRow_col_space() {
        return row_col_space;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     * @param comp        the component to be added
     * @param constraints where/how the component is added to the layout.
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {


        if(constraints instanceof RCPosition){
            RCPosition pos = (RCPosition) constraints;
            if (componentMap.containsKey(pos)) {
                throw new CalcLayoutException("Component with given constraint already exists!");
            }else{
                check_constraint(pos);
                componentMap.put(pos, comp);
            }

        }else if(constraints instanceof String){
            String x = (String) constraints;
            RCPosition constraint = RCPosition.parse(x);
            if (componentMap.containsKey(constraint)) {
                throw new CalcLayoutException("Component with given constraint already exists!");
            }else{
                check_constraint(constraint);
                componentMap.put(constraint, comp);
            }
        }else{
            throw new IllegalArgumentException("Component constraint should be either RCPosition or String!");
        }

    }

    /**
     * Checks if given position is valid and obeys all of the constraints
     * @param position
     */
    private void check_constraint(RCPosition position){

        if(position.getRow() < 1 || position.getRow() > 5){
            throw new CalcLayoutException("Position not allowed!");
        }

        if(position.getColumn() < 1 || position.getColumn() > 7){
            throw new CalcLayoutException("Position not allowed!");
        }

        if(position.getRow() == 1 && (position.getColumn() > 1 && position.getColumn() < 6)){
            throw new CalcLayoutException("Position not allowed!");
        }
    }

    /**
     * Calculates the maximum size dimensions for the specified container,
     * given the components it contains.
     *
     * @param target the target container
     * @return the maximum size of the container
     * @see Component#getMaximumSize
     * @see LayoutManager
     */
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return custom_layout_size(target, Component::getMaximumSize);
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the target container
     * @return the x-axis alignment preference
     */
    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the target container
     * @return the y-axis alignment preference
     */
    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     *
     * @param target the target container
     */
    @Override
    public void invalidateLayout(Container target) {

    }

    /**
     * If the layout manager uses a per-component string,
     * adds the component {@code comp} to the layout,
     * associating it
     * with the string specified by {@code name}.
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Method call not allowed!");
    }

    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        componentMap.values().remove(comp);
    }

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the container to be laid out
     * @return the preferred dimension for the container
     * @see #minimumLayoutSize
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return custom_layout_size(parent, Component::getPreferredSize);
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the component to be laid out
     * @return the minimum dimension for the container
     * @see #preferredLayoutSize
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return custom_layout_size(parent, Component::getMinimumSize);
    }

    /**
     * Lays out the specified container.
     *
     * @param parent the container to be laid out
     */
    @Override
    public void layoutContainer(Container parent) {

        for(Map.Entry<RCPosition, Component> kvPair: componentMap.entrySet()){

            kvPair.getValue().setBounds(getPosition(kvPair.getKey(), parent));

        }
    }


    /**
     * Method to calculate actual layout heights based on current available layout size
     * @param parent component
     * @return array of heights
     */
    private int[] getLayoutHeights(Container parent) {

        int heights[] = new int[this.rows];
        Insets ins = parent.getInsets();

        int parent_height = parent.getHeight() - ins.top - ins.bottom - (this.rows - 1) * this.row_col_space;
        int height = parent_height / this.rows;
        int space_left = parent_height % this.rows;

        for(int i = 0; i  < heights.length; i++){
            heights[i] = height;
        }

        if(space_left > 0){
            int x = this.rows / space_left;
            for(int i = 0; i  < heights.length; i = i + x){
                heights[i] ++;
                space_left--;
                if(space_left == 0) break;
            }
        }

        return heights;

    }


    /**
     * Method to calculate actual layout widths based on current available layout size
     * @param parent component
     * @return array of heights
     */
    private int[] getLayoutWidths(Container parent) {

        int widths[] = new int[this.cols];
        Insets ins = parent.getInsets();

        int parent_width = parent.getWidth() - ins.left - ins.right - (this.cols - 1) * this.row_col_space;
        int width = parent_width / this.cols;
        int space_left = parent_width % this.cols;

        for(int i = 0; i  < widths.length; i++){
            widths[i] = width;
        }

        if(space_left > 0){
            int x = this.rows / space_left;
            for(int i = 0; i  < widths.length; i = i + x){
                widths[i] ++;
                space_left--;
                if(space_left == 0) break;
            }
        }

        return widths;
    }


    private Rectangle getPosition(RCPosition key, Container parent) {

        int[] widths = getLayoutWidths(parent);
        int[] heights = getLayoutHeights(parent);
        Insets insets = parent.getInsets();

        int left_inset = insets.left;
        int top_inset = insets.top;

        if(key.getRow() == 1 && key.getColumn() == 1){
            Point p1 = new Point(top_inset, left_inset); //starting point

            int w = 4 * row_col_space;
            for(int i = 0; i < 5; i++){
                w += widths[i];
            }

            return new Rectangle(p1.x, p1.y, w, heights[0]);
        }

        for(int i = 1; i < key.getColumn(); i++){
            left_inset += widths[i] + row_col_space; //x
        }

        for(int i = 1; i < key.getRow(); i++){
            top_inset += heights[i] + row_col_space; //y
        }

        Point point = new Point(left_inset, top_inset);

        //position
        return new Rectangle(point.x, point.y, widths[key.getColumn() - 1], heights[key.getRow()-1]);

    }



    /**
     * Method created to avoid duplicating code
     * Used for calculating preffered, minimum and maximum size of custom layout
     * @param parent
     * @param function
     * @return dimension of custom layout depending on the function used
     */
    private Dimension custom_layout_size(Container parent, Function<Component, Dimension> function){

        int h = componentMap.values().stream()
                .map(function)
                .filter(dimension -> Objects.nonNull(dimension)) // remove components who don't have the attribute defined
                .mapToInt(dimension -> dimension.height)
                .max()
                .orElse(50); //default height



        int w = componentMap.entrySet().stream()
                .filter(entry -> function.apply(entry.getValue()) != null) //filtering without mapping
                .mapToInt(entry -> {
                    int width;
                    Dimension dim = function.apply(entry.getValue());
                    if(entry.getKey().getColumn() == 1 && entry.getKey().getRow() == 1){
                        // (1,1) -> 5 components and 4 spaces
                        width = (dim.width - 4*this.row_col_space) / 5;
                    }else{
                        width = dim.width;
                    }

                    return width;
                }).max()
                .orElse(50); //default width



        h = h * rows + row_col_space * (rows-1); //adding spacing
        w = w * cols +  row_col_space * (cols - 1);

        if(parent.getInsets() != null){
            Insets insets = parent.getInsets();
            h += insets.top + insets.bottom;
            w += insets.left + insets.right;
        }

        return new Dimension(w,h);

    }

}
