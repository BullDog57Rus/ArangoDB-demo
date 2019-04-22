package dmd.project.demo.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Lesson;

public interface LessonRepo extends ArangoRepository<Lesson, String> {

    long count();
}
