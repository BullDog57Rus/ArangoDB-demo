package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.GeoIndexed;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document("performance")
@EqualsAndHashCode(callSuper = true)
public class Performance extends PerformanceGeo {

    private User student;

    @GeoIndexed
    private double[] performance;
}
