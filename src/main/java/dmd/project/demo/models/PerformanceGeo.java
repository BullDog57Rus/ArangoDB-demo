package dmd.project.demo.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PerformanceGeo {

    private String name;

    private String surname;

    private Double grade;

    private Double attendance;

    private Double distance;
}
