package dmd.project.demo.repositories;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Grades;
import dmd.project.demo.models.GradesShort;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface GradesRepo extends ArangoRepository<Grades, String> {

    @Query("FOR g IN grades " +
            "FILTER g.student._key == @student_id" +
            "    AND " +
            "DATE_TIMESTAMP(g.date) > DATE_TIMESTAMP(DATE_SUBTRACT(DATE_NOW(), 1, 'm')) " +
            "SORT g.date ASC " +
            "RETURN {" +
            "   date: g.date," +
            "   course: g.course.name," +
            "   grades: g.grades" +
            "}")
    Collection<GradesShort> findByStudentLastMonth(@Param("student_id") String student_id);
}
