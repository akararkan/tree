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

    public Course() {
    }

    public Course(String name, int credit, String[] days, int startHour, int startMinute, int endHour, int endMinute) {
        this.name = name;
        this.credit = credit;
        this.days = Arrays.copyOf(days, days.length); // Create a defensive copy
        this.startTime = LocalTime.of(startHour, startMinute);
        this.endTime = LocalTime.of(endHour, endMinute);
    }

    public Course(String name, int credit, String[] days, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.credit = credit;
        this.days = Arrays.copyOf(days, days.length); // Create a defensive copy
        this.startTime = startTime;
        this.endTime = endTime;
    };

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
        this.days = days;
    }

    // Setters for start and end times
    public void setStartTime(int hour, int minute) {
        this.startTime = LocalTime.of(hour, minute);
    }

    public void setEndTime(int hour, int minute) {
        this.endTime = LocalTime.of(hour, minute);
    }

    // Getters for start and end times
    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
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
                '}';
    }
}
