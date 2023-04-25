import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.knowm.xchart.style.lines.SeriesLines.SOLID;

public class ChartBuilder {
    private final List<Double> x = new ArrayList<>();
    private final List<Double> y = new ArrayList<>();
    private final XYChart chart;

    public ChartBuilder(String X, String Y, String Title) {
        chart = new XYChartBuilder().theme(Styler.ChartTheme.Matlab)
                .title(Title).xAxisTitle(X).yAxisTitle(Y)
                .build();

        chart.setCustomXAxisTickLabelsFormatter((x) -> String.format("%.2f", x));
        chart.setCustomYAxisTickLabelsFormatter((y) -> String.format("%.2f", y));
        chart.getStyler().setSeriesLines(new BasicStroke[]{SOLID});
    }

    public void addChart(String name, double a, double b, double h) {
        x.clear();
        y.clear();
        x.add(a);
        x.add(b);
        y.add(h);
        y.add(h);
        chart.getStyler().setMarkerSize(0);
        chart.addSeries(name, x, y).setLineWidth(2);
    }

    public void PolygonalChart(double a, double h) {
        x.add(a);
        y.add(h);
        chart.getStyler().setMarkerSize(10);
    }

    public void addHistogram(String name, double a, double b, double h) {
        x.clear();
        y.clear();
        x.add(a);
        x.add(b);
        y.add(h);
        y.add(h);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.StepArea);
        chart.getStyler().setMarkerSize(0);
        chart.addSeries(name, x, y).setLineWidth(1);
    }

    public void plotPolygon(String name) {
        chart.addSeries(name, x, y).setLineWidth(2);
        try {
            BitmapEncoder.saveBitmap(chart, name, BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void plot(String name) {
        try {
            BitmapEncoder.saveBitmap(chart, name, BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
