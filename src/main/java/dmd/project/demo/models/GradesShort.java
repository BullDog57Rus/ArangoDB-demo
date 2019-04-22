package dmd.project.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradesShort {

    private LocalDate date;

    private String course;

    private Collection<Short> grades;
}
