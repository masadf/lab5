package dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MathStat {
    private Double expectedValue;
    private Double dispersion;
    private Double deviation;
    private List<Double> dataWithoutRepetition;
    private List<Long> count;
    private List<Double> probability;
}
