package dmd.project.demo.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Course;

public interface CourseRepo extends ArangoRepository<Course, String> {

    Iterable<Course> findAll();
}
