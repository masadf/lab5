package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Limit {
    private Double max;
    private Double min;

    @Override
    public String toString() {
        return "max = " + max + "\n" +
                "min = " + min;
    }
}
