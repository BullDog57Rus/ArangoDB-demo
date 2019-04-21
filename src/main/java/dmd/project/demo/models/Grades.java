package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Collection;

@Data
@Document("grades")
@NoArgsConstructor
public class Grades {

    @Id
    private String id;

    private LocalDate date;

    private User student;

    private Collection<Short> grades;

    private Course course;
}
