package dmd.project.demo.repositories;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Performance;
import dmd.project.demo.models.PerformanceGeo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PerformanceRepo extends ArangoRepository<Performance, String> {

    Iterable<Performance> findAll(Sort sort);

    @Query("FOR per IN NEAR(performance, @grade, @attendance, @number, 'distance') " +
            "RETURN {" +
            "name: per.student.name, " +
            "surname: per.student.surname, " +
            "grade: per.performance[0], " +
            "attendance: per.performance[1], " +
            "distance: per.distance / 1000" +
            "}")
    Collection<PerformanceGeo> findNearByPerformance(@Param("grade") double grade,
                                                     @Param("attendance") double attendance,
                                                     @Param("number") long number);
}
