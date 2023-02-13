package hr.fer.oprpp1.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * represents barchart component
 */
public class BarChartComponent extends JComponent {


    private BarChart chart;

    private static int TOP_BOTTOM_MARGIGN = 10; //space for top and bottom
    private static int RIGHT_LEFT_MARGIN = 20; //space for right and left
    private static int DESCRIPTION_MARGIN = 10; //space between x/y descriptions and x/y labels
    private static int MARGIN = 10;
    private static Color TEXT_COLOR = Color.black; //label color
    private static Color BARCHART_COLOR =  Color.orange; //color of bar


    public BarChartComponent(BarChart barChart){
        this.chart = barChart;
    }


    @Override
    protected void paintComponent(Graphics g) {

        int yLabel_width = Math.max(g.getFontMetrics().stringWidth(chart.getMinY() + ""),
                g.getFontMetrics().stringWidth(chart.getMaxY() + ""));
                //najveca sirina broja
        //x pocetka grafa
        int xStart = RIGHT_LEFT_MARGIN + g.getFontMetrics().getHeight() + DESCRIPTION_MARGIN + yLabel_width;
        //y pocetka grafa
        int yStart = this.getHeight() - TOP_BOTTOM_MARGIGN - g.getFontMetrics().getHeight() - DESCRIPTION_MARGIN;

        //broj y labela
        int numOfLabels = (int) Math.ceil((this.chart.getMaxY() - this.chart.getMinY() * 1.0) / this.chart.getSpace());
        //visina jedinice
        int unitHeight = (yStart - TOP_BOTTOM_MARGIGN) / numOfLabels;
        //sirina jedinice
        int unitWidth = (this.getWidth() - xStart - RIGHT_LEFT_MARGIN) / this.chart.getList().size();

        //add labels

        //y labels

        for(int y = 0; y <= numOfLabels; y++){
            String num = String.valueOf(y*this.chart.getSpace() + this.chart.getMinY());
            int numWidth = g.getFontMetrics().stringWidth(num);

            g.drawString(num, xStart - numWidth - MARGIN, yStart - y * unitHeight); // ----

        }

        g.drawLine(xStart, yStart + MARGIN / 2, xStart, yStart - numOfLabels * unitHeight - MARGIN/2); //Y axis


        //x labels
        int x = 0;
        int fontHeight = g.getFontMetrics().getHeight(); //font height

        for(XYValue value: this.chart.getList()) {
            g.drawString(String.valueOf(value.getX()), (int) (xStart + (x + 0.5) * unitWidth), yStart + fontHeight);
            x++;
        }

        //add charts

        int z = 0;
        for(XYValue value: this.chart.getList()){
            int w = unitWidth;
            int x_cord = xStart + z * w;
            int h = ((value.getY() - this.chart.getMinY()) / this.chart.getSpace()) * unitHeight;
            int y_cord = yStart - h;


            g.setColor(BARCHART_COLOR);
            g.fillRect(x_cord, y_cord, w, h);
            g.setColor(Color.black); //border
            g.drawRect(x_cord, y_cord, w, h);
            g.setColor(TEXT_COLOR);

            z++;
        }



        g.drawLine(xStart + z * unitWidth, yStart, xStart + z *  unitWidth, yStart + MARGIN/2); //X axis
        g.drawLine(xStart, yStart, xStart + z * unitWidth + MARGIN/2, yStart);

        //add descriptions


        // x

        String xDescription = this.chart.getxDescription();
        int xDescriptionWidth = g.getFontMetrics().stringWidth(xDescription);

        g.drawString(xDescription, (xStart + this.getWidth()) / 2 - xDescriptionWidth / 2,
                yStart + DESCRIPTION_MARGIN + fontHeight);



        //y

        Graphics2D newGraphics = (Graphics2D) g;
        AffineTransform defaultAt = newGraphics.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);

        newGraphics.setTransform(at);

        String description = chart.getyDescription();
        int heightOfDescription = newGraphics.getFontMetrics().stringWidth(description);

        newGraphics.drawString(description, -(yStart / 2 + heightOfDescription / 2), RIGHT_LEFT_MARGIN);

        newGraphics.setTransform(defaultAt);
    }
}
