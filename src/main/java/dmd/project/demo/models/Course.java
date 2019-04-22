package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Document("course")
@NoArgsConstructor
public class Course {

    private String name;

    private Set<Short> forms;

    private User teacher;
}
