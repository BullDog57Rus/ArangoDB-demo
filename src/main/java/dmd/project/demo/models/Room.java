package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document("room")
@NoArgsConstructor
public class Room {

    private String room;
}
