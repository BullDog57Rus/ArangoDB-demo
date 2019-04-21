package dmd.project.demo.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.TimetableDay;

public interface TimetableRepo extends ArangoRepository<TimetableDay, String> {
}
