package dmd.project.demo.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import dmd.project.demo.models.Room;

import java.util.Collection;

public interface RoomRepo extends ArangoRepository<Room, String> {

    Iterable<Room> findAll();
}
