package dmd.project.demo.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Role;
import dmd.project.demo.models.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepo extends ArangoRepository<User, String> {

    Collection<User> findByRole(Role role);

    Optional<User> findById(String id);
}
