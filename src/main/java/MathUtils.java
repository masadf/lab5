import dto.Limit;
import dto.MathStat;

import java.util.List;

public class MathUtils {

    public Limit calculateLimit(List<Double> data) {
        return Limit.builder()
                .max(data.stream().max(Double::compareTo).orElseThrow())
                .min(data.stream().min(Double::compareTo).orElseThrow())
                .build();
    }

    public Double calculateScope(Limit limit) {
        return limit.getMax() - limit.getMin();
    }

    public MathStat calculateMathStat(List<Double> data) {
        List<Double> dataWithoutRepetition = data.stream().distinct().toList();
        int size = data.size();

        List<Long> count = dataWithoutRepetition.stream().map((el) -> data.stream().filter(it -> it.equals(el)).count()).toList();
        List<Double> probability = count.stream().map(el -> (double) el / size).toList();
        double expectedValue = 0D;
        double dispersion = 0D;

        for (int itemIndex = 0; itemIndex < dataWithoutRepetition.size(); itemIndex++) {
            expectedValue += dataWithoutRepetition.get(itemIndex) * probability.get(itemIndex);
        }

        for (int itemIndex = 0; itemIndex < dataWithoutRepetition.size(); itemIndex++) {
            dispersion += Math.pow((dataWithoutRepetition.get(itemIndex) - expectedValue), 2) * count.get(itemIndex);
        }

        dispersion *= 1.0 / size;

        return MathStat.builder()
                .dataWithoutRepetition(dataWithoutRepetition)
                .expectedValue(expectedValue)
                .dispersion(dispersion)
                .deviation(Math.sqrt(dataWithoutRepetition.size() * dispersion / (dataWithoutRepetition.size() - 1)))
                .count(count)
                .probability(probability)
                .build();
    }

}
