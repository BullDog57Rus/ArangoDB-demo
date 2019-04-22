package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;

@Data
@Document("lesson")
@NoArgsConstructor
public class Lesson {

    private Course course;

    private User teacher;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Room room;

    private Collection<User> students;

    private Short number;

    public Lesson(Course course,
                  Room room) {
        this.course = course;
        this.room = room;
    }
}
