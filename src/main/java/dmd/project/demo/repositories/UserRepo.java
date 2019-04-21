package dmd.project.demo.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.User;

public interface UserRepo extends ArangoRepository<User, String> {
}
