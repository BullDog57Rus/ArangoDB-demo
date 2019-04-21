package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Document("user")
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String name;

    private String surname;

    private Role role;

    private Short form;

    private Short group;
}
