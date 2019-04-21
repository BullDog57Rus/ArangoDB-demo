package dmd.project.demo.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Course;

import java.util.Collection;
import java.util.List;

public interface CourseRepo extends ArangoRepository<Course, String> {

    Collection<Course> findAllByNameContaining(String name);

    Iterable<Course> findAll();
}
