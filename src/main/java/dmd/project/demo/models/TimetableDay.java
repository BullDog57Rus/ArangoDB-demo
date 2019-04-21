package dmd.project.demo.models;

import com.arangodb.springframework.annotation.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.Collection;

@Data
@Document("timetable")
@NoArgsConstructor
@AllArgsConstructor
public class TimetableDay {

    private Short form;

    private Short group;

    private DayOfWeek weekday;

    private Collection<LessonShort> lessons;
}
