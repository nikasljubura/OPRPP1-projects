package hr.fer.oprpp1.gui.charts;

import java.util.List;

public class BarChart {
    private List<XYValue> list;
    private String xDescription;
    private String yDescription;
    private int minY;
    private int maxY;
    private int space;

    public BarChart(List<XYValue> list, String xDescription, String yDescription, int minY, int maxY, int space) {

        if(minY < 0) throw new IllegalArgumentException("minimum Y cannot be negative");
        if(maxY <= minY) throw new IllegalArgumentException("maximum Y has to be strictly greater that minimum Y");

        this.list = list;
        this.xDescription = xDescription;
        this.yDescription = yDescription;
        this.minY = minY;
        this.maxY = maxY;
        this.space = space;

        for(XYValue value: list){
            if(value.getY() < minY) throw new IllegalArgumentException("Y values cannot be smaller than yMin");
        }


    }

    public List<XYValue> getList() {
        return list;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getSpace() {
        return space;
    }

    public String getxDescription() {
        return xDescription;
    }

    public String getyDescription() {
        return yDescription;
    }
}
