package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Document("room")
@NoArgsConstructor
public class Room {

    @Id
    private String id;

    private String room;
}
