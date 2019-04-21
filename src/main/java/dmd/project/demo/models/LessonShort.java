package dmd.project.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonShort {

    private String course;

    private String teacher;

    private String room;

    private Short number;
}
