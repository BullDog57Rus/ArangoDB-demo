package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Collection;

@Data
@Document("lesson")
@NoArgsConstructor
public class Lesson {

    @Id
    private String id;

    private Course course;

    private User teacher;

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
