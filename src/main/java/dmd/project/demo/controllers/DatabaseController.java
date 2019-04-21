package dmd.project.demo.controllers;

import dmd.project.demo.models.*;
import dmd.project.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/")
public class DatabaseController {

    private final CourseRepo courseRepo;
    private final RoomRepo roomRepo;
    private final UserRepo userRepo;
    private final GradesRepo gradesRepo;
    private final LessonRepo lessonRepo;
    private final TimetableRepo timetableRepo;

    @Autowired
    public DatabaseController(CourseRepo courseRepo,
                              RoomRepo roomRepo,
                              UserRepo userRepo,
                              GradesRepo gradesRepo,
                              LessonRepo lessonRepo,
                              TimetableRepo timetableRepo) {
        this.courseRepo = courseRepo;
        this.roomRepo = roomRepo;
        this.userRepo = userRepo;
        this.gradesRepo = gradesRepo;
        this.lessonRepo = lessonRepo;
        this.timetableRepo = timetableRepo;
    }

    @PostMapping("/addStudent")
    public void addStudent(@RequestBody User user) {
        user.setRole(Role.STUDENT);
        userRepo.save(user);
    }

    @GetMapping("/getTimetable")
    public Collection<TimetableDay> getTimetable() {
        List<Course> courses = StreamSupport.stream(courseRepo.findAll().spliterator(), false).collect(Collectors.toList());
        List<Room> rooms = StreamSupport.stream(roomRepo.findAll().spliterator(), false).collect(Collectors.toList());
        if (rooms.isEmpty() || courses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No courses or rooms available.");
        }

        short[] forms = new short[]{9, 10};
        short[] groups = new short[]{1, 2, 3};
        Collection<TimetableDay> timetable = new ArrayList<>();

        for (DayOfWeek weekDay : DayOfWeek.values()) {
            if (weekDay.getValue() > 5) {
                break;
            }

            Lesson[][][] lessons = new Lesson[2][3][7];
            for (int i = 0; i < forms.length; i++) {
                Short form = forms[i];
                for (int j = 0; j < groups.length; j++) {
                    Short group = groups[j];
                    List<Course> availableCourses = new ArrayList<>(courses);
                    TimetableDay timetableDay = new TimetableDay();
                    timetableDay.setWeekday(weekDay);
                    timetableDay.setForm(form);
                    timetableDay.setGroup(group);
                    timetableDay.setLessons(new ArrayList<>());
                    short n = 0;
                    while (n < 7) {
                        short number = n;
                        Collections.shuffle(rooms);
                        Collections.shuffle(availableCourses);
                        Room room = rooms.get(0);
                        Course course = availableCourses.stream().filter(c -> c.getForms().contains(form)).findFirst().orElseThrow(IllegalArgumentException::new);
                        if (Arrays.stream(lessons)
                                .anyMatch(l ->
                                        Arrays.stream(l)
                                                .anyMatch(lesson -> lesson[number] != null &&
                                                        (lesson[number].getCourse().equals(course) || lesson[number].getRoom().equals(room))))) {
                            continue;
                        }
                        availableCourses.remove(course);
                        lessons[i][j][number] = new Lesson(course, room);
                        LessonShort lesson = new LessonShort();
                        lesson.setCourse(course.getName());
                        lesson.setNumber(number);
                        lesson.setRoom(room.getRoom());
                        lesson.setTeacher(course.getTeacher().getName() + " " + course.getTeacher().getSurname());
                        timetableDay.getLessons().add(lesson);
                        n++;
                    }
                    timetable.add(timetableDay);
                }
            }
        }
        return timetable;
    }

    @PostMapping("/createTimetable")
    public void addTimetable() {
        Collection<TimetableDay> timetable = getTimetable();
        timetableRepo.saveAll(timetable);
    }
}
