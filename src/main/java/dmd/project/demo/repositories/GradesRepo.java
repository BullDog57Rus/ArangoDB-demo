package dmd.project.demo.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Grades;

public interface GradesRepo extends ArangoRepository<Grades, String> {
}
