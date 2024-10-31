import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Course {
    private String name;
    private int credit;
    private String[] days;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> prerequisites; // List of prerequisite course names
    private List<String> unlocks; // List of course names that this course unlocks

    public Course() {
    }

    // Constructor with prerequisites and unlocks for JSON compatibility
    public Course(String name, int credit, String[] days, int startHour, int startMinute, int endHour, int endMinute, List<String> prerequisites, List<String> unlocks) {
        this.name = name;
        this.credit = credit;
        this.days = Arrays.copyOf(days, days.length); // Create a defensive copy
        this.startTime = LocalTime.of(startHour, startMinute);
        this.endTime = LocalTime.of(endHour, endMinute);
        this.prerequisites = prerequisites;
        this.unlocks = unlocks;
    }

    // Constructor without prerequisites/unlocks for simpler cases
    public Course(String name, int credit, String[] days, int startHour, int startMinute, int endHour, int endMinute) {
        this(name, credit, days, startHour, startMinute, endHour, endMinute, List.of(), List.of());
    }

    // Another constructor with LocalTime for flexibility
    public Course(String name, int credit, String[] days, LocalTime startTime, LocalTime endTime, List<String> prerequisites, List<String> unlocks) {
        this.name = name;
        this.credit = credit;
        this.days = Arrays.copyOf(days, days.length); // Create a defensive copy
        this.startTime = startTime;
        this.endTime = endTime;
        this.prerequisites = prerequisites;
        this.unlocks = unlocks;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = Arrays.copyOf(days, days.length);
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<String> getUnlocks() {
        return unlocks;
    }

    public void setUnlocks(List<String> unlocks) {
        this.unlocks = unlocks;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(int hour, int minute) {
        this.startTime = LocalTime.of(hour, minute);
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(int hour, int minute) {
        this.endTime = LocalTime.of(hour, minute);
    }

    // Get formatted day and time information
    public String getDayAndTime() {
        return Arrays.toString(days) + " " + startTime + "-" + endTime;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", credit=" + credit +
                ", days=" + Arrays.toString(days) +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", prerequisites=" + prerequisites +
                ", unlocks=" + unlocks +
                '}';
    }
}
