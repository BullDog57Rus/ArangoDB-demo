package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Collection;

@Data
@Document("course")
@NoArgsConstructor
public class Course {

    @Id
    private String id;

    private String name;

    private Collection<Short> forms;

    private User teacher;
}