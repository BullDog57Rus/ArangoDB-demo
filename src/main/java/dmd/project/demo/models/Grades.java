package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;

@Data
@Document("grades")
@NoArgsConstructor
public class Grades {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private User student;

    private Collection<Short> grades;

    private Course course;
}
