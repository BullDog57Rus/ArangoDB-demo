package dmd.project.demo.repositories;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Performance;
import dmd.project.demo.models.PerformanceGeo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface PerformanceRepo extends ArangoRepository<Performance, String> {

    Iterable<Performance> findAll(Sort sort);

    Optional<Performance> findByStudent_Id(String student_id);

    @Query("FOR per IN NEAR(performance, @grade, @attendance, @number, 'distance') " +
            "RETURN {" +
            "name: per.student.name, " +
            "surname: per.student.surname, " +
            "grade: per.performance[0], " +
            "attendance: per.performance[1], " +
            "distance: per.distance / 1000, " +
            "birthday: per.student.birthday" +
            "}")
    Collection<PerformanceGeo> findNearByPerformance(@Param("grade") double grade,
                                                     @Param("attendance") double attendance,
                                                     @Param("number") long number);

    @Query("LET current = (FOR cur IN NEAR(performance, @grade, @attendance, 1) RETURN cur)[0] " +
            "FOR per IN NEAR(performance, @grade, @attendance, @usersCount, 'distance') " +
            "FILTER DATE_COMPARE(per.student.birthday, current.student.birthday, @unit) " +
            "LIMIT @number " +
            "RETURN {" +
            "name: per.student.name, " +
            "surname: per.student.surname, " +
            "grade: per.performance[0], " +
            "attendance: per.performance[1], " +
            "distance: per.distance / 1000, " +
            "birthday: per.student.birthday" +
            "}")
    Collection<PerformanceGeo> findNearByPerformanceWithDate(@Param("grade") double grade,
                                                             @Param("attendance") double attendance,
                                                             @Param("number") long number,
                                                             @Param("usersCount") long usersCount,
                                                             @Param("unit") String unit);
}
