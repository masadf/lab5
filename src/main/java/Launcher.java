import dto.Limit;
import dto.MathStat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Launcher {
    private static String CONFIG_PATH = "config.properties";
    private static MathUtils mathUtils = new MathUtils();

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Launcher.class.getResourceAsStream(CONFIG_PATH));

        List<Double> variables = Arrays.stream(properties.getProperty("inputData")
                        .split(" "))
                .map(Double::parseDouble)
                .sorted()
                .toList();
        Limit limit = mathUtils.calculateLimit(variables);
        MathStat mathStat = mathUtils.calculateMathStat(variables);

        System.out.println("Вариационный ряд:");
        System.out.println(variables);

        System.out.println("Минимум и максимум:");
        System.out.println(limit);

        System.out.println("Размах:");
        System.out.println(mathUtils.calculateScope(limit));

        System.out.println("Математическое ожидание:");
        System.out.println(mathStat.getExpectedValue());

        System.out.println("Дисперсия:");
        System.out.println(mathStat.getDispersion());

        System.out.println("Среднеквадратичное отклонение:");
        System.out.println(mathStat.getDeviation());

        printEmpiricFunc(mathStat);

        drawEmpiricFunction(mathStat);
        drawFrequencyPolygon(variables);
        drawHistogram(variables);
    }

    private static void printEmpiricFunc(MathStat mathStat) {
        double h = mathStat.getProbability().get(0);

        System.out.println("Функция:");
        System.out.printf("\t\t\tx\t<=\t%.2f\t->\t%.2f\n", mathStat.getDataWithoutRepetition().get(0), 0.0);
        for (int i = 0; i < mathStat.getDataWithoutRepetition().size() - 1; i++) {
            System.out.printf("%.2f\t<\tx\t<=\t%.2f\t->\t%.2f\n", mathStat.getDataWithoutRepetition().get(i), mathStat.getDataWithoutRepetition().get(i + 1), h);
            h += mathStat.getProbability().get(i + 1);
        }
        System.out.printf("%.2f\t<\tx\t\t\t\t->\t%.2f\n", mathStat.getDataWithoutRepetition().get(mathStat.getDataWithoutRepetition().size() - 1), h);
    }

    private static void drawEmpiricFunction(MathStat mathStat) {
        ChartBuilder drawChart = new ChartBuilder("x", "f(X)", "Эмпирическая функция");

        double h = mathStat.getProbability().get(0);
        drawChart.addChart("x <= " + mathStat.getDataWithoutRepetition().get(0), mathStat.getDataWithoutRepetition().get(0) - 0.5, mathStat.getDataWithoutRepetition().get(0), 0);
        for (int i = 0; i < mathStat.getDataWithoutRepetition().size() - 1; i++) {
            drawChart.addChart(mathStat.getDataWithoutRepetition().get(i) + " < x <= " + mathStat.getDataWithoutRepetition().get(i + 1), mathStat.getDataWithoutRepetition().get(i), mathStat.getDataWithoutRepetition().get(i + 1), h);
            h += mathStat.getProbability().get(i + 1);
        }
        drawChart.addChart(mathStat.getDataWithoutRepetition().get(mathStat.getDataWithoutRepetition().size() - 1) + " < x", mathStat.getDataWithoutRepetition().get(mathStat.getDataWithoutRepetition().size() - 1), mathStat.getDataWithoutRepetition().get(mathStat.getDataWithoutRepetition().size() - 1) + 1, h);
        drawChart.plot("EmpiricFunc");
    }

    private static void drawFrequencyPolygon(List<Double> variables) {
        ChartBuilder frequencyPolygon = new ChartBuilder("x", "p_i", "Полигон частот");
        double h = calculateH(variables);

        double x_start = variables.get(0) - h / 2;
        for (int i = 0; i < calculateM(variables); i++) {
            int count = 0;
            for (double value : variables)
                if (value >= x_start && value < (x_start + h))
                    count++;

            frequencyPolygon.PolygonalChart(x_start + h / 2, (double) count / (double) variables.size());
            x_start += h;
        }
        frequencyPolygon.plotPolygon("FrequencyPolygon");
    }

    private static void drawHistogram(List<Double> variables) {
        ChartBuilder Histogram = new ChartBuilder("x", "p_i / h", "Гистограмма частот");
        double h = calculateH(variables);
        double x_start = variables.get(0) - h / 2;

        for (int i = 0; i < calculateM(variables); i++) {
            int count = 0;
            for (double value : variables)
                if (value >= x_start && value < (x_start + h)) {
                    count++;
                }

            Histogram.addHistogram(x_start + " : " + (x_start + h), x_start, x_start + h,
                    ((double) count / (double) variables.size()) / h);
            x_start += h;
        }
        Histogram.plot("Histogram");
    }

    private static Double calculateH(List<Double> variables) {
        return (variables.get(variables.size() - 1) - variables.get(0)) / (1 + ((Math.log(variables.size()) / Math.log(2))));
    }

    private static int calculateM(List<Double> variables) {
        return (int) Math.ceil(1 + (Math.log(variables.size()) / Math.log(2)));
    }
}
