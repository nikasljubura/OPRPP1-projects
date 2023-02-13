package hr.fer.oprpp1.gui.charts;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Arrays;
import java.util.List;

public class BarChartDemo extends JFrame {

    public BarChartDemo(String filepath, BarChart model){

        initGUI(filepath, model);

    }
    public void initGUI(String filepath, BarChart model){
        this.setLayout(new BorderLayout());
        this.setSize(500, 500);
        this.setTitle(filepath);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.add(new JLabel(filepath,SwingConstants.CENTER), BorderLayout.PAGE_START);
        this.add(new BarChartComponent(model), BorderLayout.CENTER);
    }

    public static void main(String[] args) {

        if(args.length != 1){
            System.out.println("Program takes one argument");
            System.exit(-1);
        }
        Path filepath = Paths.get(args[0]);

        List<String> fileContents = null;

        try {
            fileContents = Files.readAllLines(filepath);
        } catch (IOException e) {
            System.out.println("Error reading file");
            System.exit(-1);
        }

        if(fileContents.size() < 6){
            System.out.println("Program takes 6 arguments!");
            System.exit(-1);
        }

        String descX = fileContents.get(0);
        String descY = fileContents.get(1);
        int minY = Integer.parseInt(fileContents.get(3));
        int maxY = Integer.parseInt(fileContents.get(4));
        int unitSize = Integer.parseInt(fileContents.get(5));

        String xyline = fileContents.get(2);
        String[] arr = xyline.split(" ");
        List<XYValue> xyValues = new ArrayList<>();

        for(String value: arr){
            String[] xy = value.split(",");
            XYValue newValue = new XYValue(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
            xyValues.add(newValue);
        }

        BarChart chart = new BarChart(xyValues, descX, descY, minY, maxY, unitSize);

       /* BarChart model = new BarChart(
                Arrays.asList(
                        new XYValue(1,8), new XYValue(2,20), new XYValue(3,22),
                        new XYValue(4,10), new XYValue(5,4)
                ),
                "Number of people in the car",
                "Frequency",
                0, // y-os kreće od 0
                22, // y-os ide do 22
                2
        );*/

        SwingUtilities.invokeLater(() -> {
            new BarChartDemo(args[0], chart).setVisible(true);
        });
    }
}
