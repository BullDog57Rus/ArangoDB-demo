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
    private final PerformanceRepo performanceRepo;

    private static final short[] forms = new short[]{9, 10};
    private static final short[] groups = new short[]{1, 2, 3, 4, 5};
    private static final int formCount = forms.length;
    private static final int groupCount = groups.length;
    private static final int lessonCount = 7;


    @Autowired
    public DatabaseController(CourseRepo courseRepo,
                              RoomRepo roomRepo,
                              UserRepo userRepo,
                              GradesRepo gradesRepo,
                              LessonRepo lessonRepo,
                              TimetableRepo timetableRepo,
                              PerformanceRepo performanceRepo) {
        this.courseRepo = courseRepo;
        this.roomRepo = roomRepo;
        this.userRepo = userRepo;
        this.gradesRepo = gradesRepo;
        this.lessonRepo = lessonRepo;
        this.timetableRepo = timetableRepo;
        this.performanceRepo = performanceRepo;
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

        Collection<TimetableDay> timetable = new ArrayList<>();

        for (DayOfWeek weekDay : DayOfWeek.values()) {
            if (weekDay.getValue() > 5) {
                break;
            }

            Lesson[][][] lessons = new Lesson[formCount][groupCount][lessonCount];
            for (int i = 0; i < formCount; i++) {
                Short form = forms[i];
                for (int j = 0; j < groupCount; j++) {
                    Short group = groups[j];
                    List<Course> availableCourses = new ArrayList<>(courses);
                    TimetableDay timetableDay = new TimetableDay();
                    timetableDay.setWeekday(weekDay);
                    timetableDay.setForm(form);
                    timetableDay.setGroup(group);
                    timetableDay.setLessons(new ArrayList<>());
                    short n = 0;
                    while (n < lessonCount) {
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
    public void createTimetable() {
        Collection<TimetableDay> timetable = getTimetable();
        timetableRepo.saveAll(timetable);
    }

    @GetMapping("/getPerformance")
    public Collection<PerformanceGeo> getPerformance(@RequestParam String studentId,
                                                     @RequestParam long number) {
        Optional<Performance> bestPerformance = performanceRepo.findByStudent_Id(studentId);
        if (!bestPerformance.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with id " + studentId + ".");
        }

        return performanceRepo.findNearByPerformance(bestPerformance.get().getPerformance()[0],
                bestPerformance.get().getPerformance()[1],
                number);
    }

    @GetMapping("/getPerformanceWithDate")
    public Collection<PerformanceGeo> getPerformanceWithDate(@RequestParam String studentId,
                                                             @RequestParam String unit,
                                                             @RequestParam long number) {
        Optional<Performance> bestPerformance = performanceRepo.findByStudent_Id(studentId);
        if (!bestPerformance.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with id " + studentId + ".");
        }

        return performanceRepo.findNearByPerformanceWithDate(bestPerformance.get().getPerformance()[0],
                bestPerformance.get().getPerformance()[1],
                number,
                userRepo.count(),
                unit);
    }

    @PostMapping("/createPerformance")
    public void createPerformance() {
        Collection<Performance> performances = new ArrayList<>();
        Map<User, Long> studentsAttendance = userRepo.findByRole(Role.STUDENT).stream()
                .collect(Collectors.toMap(us -> us, us -> new Long(0)));
        long lessons = lessonRepo.count();
        for (Lesson l : lessonRepo.findAll()) {
            for (User stud : studentsAttendance.keySet()) {
                if (l.getStudents().contains(stud)) {
                    studentsAttendance.replace(stud, studentsAttendance.get(stud) + 1);
                }
            }
        }
        Map<User, ArrayList<Long>> studentsGrades = userRepo.findByRole(Role.STUDENT).stream()
                .collect(Collectors.toMap(us -> us, us -> new ArrayList<>(Arrays.asList(0L, 0L))));
        for (Grades grade :
                gradesRepo.findAll()) {
            if (studentsGrades.keySet().contains(grade.getStudent())) {
                Long gradeValue = studentsGrades.get(grade.getStudent()).get(0);
                Long gradeCount = studentsGrades.get(grade.getStudent()).get(1);
                Long newGrade = grade.getGrades().stream().mapToLong(Short::longValue).sum();
                studentsGrades.replace(grade.getStudent(), new ArrayList<>(Arrays.asList(gradeValue + newGrade, gradeCount + grade.getGrades().size())));
            }
        }
        for (User stud : studentsAttendance.keySet()) {
            Performance performance = new Performance();
            performance.setStudent(stud);
            performance.setPerformance(new double[]{(double) 100 * studentsGrades.get(stud).get(0) / studentsGrades.get(stud).get(1) / 5,
                    (double) 100 * studentsAttendance.get(stud) / ((double) lessons / (formCount * groupCount))});
            performances.add(performance);
        }
        performanceRepo.saveAll(performances);
    }

    @GetMapping("/getGrades/{studentId}")
    public StudentGrades getGradesOfStudentLastMonth(@PathVariable String studentId) {
        Optional<User> student = userRepo.findById(studentId);
        if (!student.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with id " + studentId + ".");
        }

        Collection<GradesShort> grades = gradesRepo.findByStudentLastMonth(studentId);

        return new StudentGrades(student.get(), grades);
    }
}
