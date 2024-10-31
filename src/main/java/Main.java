import lombok.Getter;

import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {

    public static void main(String[] args) {
        // Load courses from JSON
        List<Course> courseList = JsonLoader.loadCourses("/home/akar/Music/tree/src/main/java/courses.json");
        assert courseList != null;
        seeAllPossibilities(courseList);
    }



    static void showPermutations(List<List<Course>> allLists, List<Course> currentList, Course currentCourse) {
        // Create a copy of the current list
        List<Course> tempList = new ArrayList<>(currentList);

        // Remove the current course if it already exists in the list
        tempList.removeIf(course -> course.equals(currentCourse));

        // Add the current course to the list
        tempList.add(currentCourse);

        // Convert the list to a set to remove duplicates
        Set<Course> uniqueCourses = new HashSet<>(tempList);

        // Convert the set back to a list and add it to allLists
        allLists.add(new ArrayList<>(uniqueCourses));
    }

    private static int calculateTotalCredits(List<Course> courses) {
        int totalCredits = 0;
        for (Course course : courses) {
            totalCredits += course.getCredit();
        }
        return totalCredits;
    }

    public static void seeAllPossibilities(List<Course> courses) {
        int maxCredit = 18;
        // List to store all possible lists
        List<List<Course>> allLists = new ArrayList<>();
        List<Course> currentSet = new ArrayList<>();
        int currentCredit = 0;

        for (Course course : courses) {
            int potentialCredit = currentCredit + course.getCredit();

            // Check for time conflicts with existing courses in the current list
            if (hasTimeConflict(currentSet, course) && potentialCredit <= maxCredit) {
                currentSet.add(course);
                currentCredit += course.getCredit(); // Update currentCredit
            } else {
                // Add the current set to allLists if it's not empty
                if (!currentSet.isEmpty()) {
                    allLists.add(new ArrayList<>(currentSet));
                }
                // Start a new set with the current course
                currentSet.clear();
                currentSet.add(course);
                currentCredit = course.getCredit(); // Reset currentCredit
            }

            // Show permutations for each course
            showPermutations(allLists, new ArrayList<>(currentSet), course);
//            System.out.println(endTimeBeforeStartTime(currentSet , course));
        }

        // Add the last set to allLists if it's not empty
        if (!currentSet.isEmpty()) {
            allLists.add(new ArrayList<>(currentSet));
        }

        // Print all possible lists and their total credits
        for (List<Course> list : allLists) {
            if (calculateTotalCredits(list) >= 10) {
                System.out.println("List: " + list);
                System.out.println("Total Credits: " + calculateTotalCredits(list));
                int uniqueDays = numOfDays(list);
                System.out.println("All Days of the week: " + uniqueDays);
                System.out.println(calculateTimeRest(list));
                System.out.println("Conflicts: " + findTimeConflicts(list));

                // Count the number of unique unlocked courses
                Set<String> unlockedCourses = new HashSet<>();
                for (Course course : list) {
                    unlockedCourses.add(String.valueOf(course.getUnlocks()));
                }

                // Print the number of unique unlocked courses
                System.out.println("Number Of Unlock Courses: " + unlockedCourses.size());
            }
        }

        List<Pair<List<Course>, Integer>> scoredLists = chooseBestCourseList(allLists);

        // Print all lists from best to worst
        System.out.println("\nCourse Lists from Best to Worst:");
        for (Pair<List<Course>, Integer> scoredList : scoredLists) {
            System.out.println("List: " + scoredList.getKey());
            System.out.println("Score: " + scoredList.getValue());
        }

    }

    public static boolean hasTimeConflict(List<Course> courses, Course newCourse) {
        for (Course course : courses) {
            // Find shared days between courses
            List<String> sharedDays = new ArrayList<>();
            for (String day : newCourse.getDays()) {
                if (Arrays.asList(course.getDays()).contains(day)) {
                    sharedDays.add(day);
                }
            }
            // Check for time overlap only on shared days
            for (String sharedDay : sharedDays) {
                if (course.getDayAndTime().contains(sharedDay) && newCourse.getDayAndTime().contains(sharedDay)) {
                    // Check for time overlap only if start and end times are different
                    if (!course.getStartTime().equals(newCourse.getStartTime()) || !course.getEndTime().equals(newCourse.getEndTime())) {
                        if (course.getStartTime().isBefore(newCourse.getEndTime()) && course.getEndTime().isAfter(newCourse.getStartTime())) {
                            return false; // Conflict found
                        }
                    }
                    if (course.getDayAndTime().contains(sharedDay) && newCourse.getDayAndTime().contains(sharedDay)) {
                        if (course.getStartTime().isBefore(newCourse.getEndTime()) && course.getEndTime().isAfter(newCourse.getStartTime())) {
                            return false; // Conflict found
                        }
                    }
                }
            }


        }
        return true; // No conflict found
    }

    public static int numOfDays(List<Course> courses) {
        Set<String> daysOfWeek = new HashSet<>();
        for (Course course : courses) {
            for (String day : course.getDays()) {
                daysOfWeek.add(day);
            }
        }

        return daysOfWeek.size();
    }

    public static String calculateTimeRest(List<Course> courses) {
        return courses.stream()
                .flatMap(firstCourse -> courses.stream()
                        .filter(secondCourse -> !firstCourse.equals(secondCourse))
                        .flatMap(secondCourse ->
                                Arrays.stream(firstCourse.getDays())
                                        .flatMap(day1 ->
                                                Arrays.stream(secondCourse.getDays())
                                                        .filter(day1::equals)
                                                        .map(day -> {
                                                            if (firstCourse.getDayAndTime().contains(day) && secondCourse.getDayAndTime().contains(day)) {
                                                                int timeRest = Math.abs((int) Duration.between(firstCourse.getEndTime(), secondCourse.getStartTime()).toMinutes());
                                                                return new AbstractMap.SimpleEntry<>(timeRest, day);
                                                            } else {
                                                                return null;
                                                            }
                                                        })
                                                        .filter(entry -> entry != null)
                                        )
                        )
                )
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.minBy(Comparator.comparingInt(Map.Entry::getKey))
                ))
                .entrySet().stream()
                .map(entry -> "Day: " + entry.getKey() + ", Time Rest: " +
                        entry.getValue().map(Map.Entry::getKey).orElse(0) + " minutes")
                .collect(Collectors.joining("\n"));
    }

    public static List<List<Course>> findTimeConflicts(List<Course> courses) {
        List<List<Course>> conflicts = new ArrayList<>(); // List to store conflicting pairs

        IntStream.range(0, courses.size() - 1)
                .forEach(i -> IntStream.range(i + 1, courses.size())
                        .forEach(j -> {
                            Course course1 = courses.get(i);
                            Course course2 = courses.get(j);

                            List<String> sharedDays = Arrays.stream(course1.getDays())
                                    .filter(day -> Arrays.asList(course2.getDays()).contains(day))
                                    .toList();

                            sharedDays.forEach(sharedDay -> {
                                if (course1.getDayAndTime().contains(sharedDay) && course2.getDayAndTime().contains(sharedDay)) {
                                    if (course1.getEndTime().isAfter(course2.getStartTime()) && course1.getStartTime().isBefore(course2.getEndTime())) {
                                        // Add the conflicting courses as a pair to the conflicts list
                                        conflicts.add(List.of(course1, course2));
                                    }
                                }
                            });
                        }));

        return conflicts;
    }



    public static List<Pair<List<Course>, Integer>> chooseBestCourseList(List<List<Course>> allLists) {
        List<Pair<List<Course>, Integer>> scoredLists = new ArrayList<>();

        for (List<Course> courseList : allLists) {
            int score = 0;

            // 1. Count unlocked courses with weighted values
            int unlockScore = courseList.stream()
                    .mapToInt(course -> {
                        int unlockValue = course.getUnlocks().size() * 3; // Assume each unlock is worth 3 points
                        return unlockValue;
                    })
                    .sum();

            // Count prerequisites separately
            int prerequisiteScore = courseList.stream()
                    .mapToInt(course -> course.getPrerequisites().size() * 2) // Assume each prerequisite is worth 2 points
                    .sum();

            score += (unlockScore + prerequisiteScore); // Combine unlock and prerequisite scores

            // 2. Calculate total credits with tiered values
            int totalCredits = calculateTotalCredits(courseList);
            for (Course course : courseList) {
                if (course.getCredit() == 4) {
                    score += 10; // 4-credit course
                } else if (course.getCredit() == 3) {
                    score += 7; // 3-credit course
                } else if (course.getCredit() == 2) {
                    score += 4; // 2-credit course
                }
            }

            // 3. Count unique days with penalties for weekends
            int uniqueDays = numOfDays(courseList);
            score += (7 - uniqueDays) * 3; // Weight for fewer days
            if (courseList.stream().anyMatch(course -> Arrays.asList(course.getDays()).contains("Saturday") || Arrays.asList(course.getDays()).contains("Sunday"))) {
                score -= 5; // Penalty for weekend classes
            }

            // 4. Calculate total rest time and assign scores
            String timeRest = calculateTimeRest(courseList);
            int totalRestTime = Arrays.stream(timeRest.split("\n"))
                    .mapToInt(line -> {
                        String[] parts = line.split(", ");
                        if (parts.length > 1) {
                            try {
                                int restMinutes = Integer.parseInt(parts[1].split(" ")[0]); // Get time rest from the string
                                // Score for preferred rest time
                                if (restMinutes >= 30 && restMinutes <= 60) {
                                    return restMinutes + 5; // Additional points for preferred range
                                } else if (restMinutes < 30) {
                                    return -2; // Penalty for less than 30 minutes
                                } else {
                                    return 0; // Neutral score for rest time over 60 minutes
                                }
                            } catch (NumberFormatException e) {
                                return 0; // If parsing fails, return 0
                            }
                        }
                        return 0; // Return 0 if the line doesn't have enough parts
                    })
                    .sum();
            score += totalRestTime; // Weight for total rest time

            // 5. Consecutive Days Check
            int consecutiveDaysBonus = calculateConsecutiveDaysBonus(courseList);
            score += consecutiveDaysBonus; // Add bonus for consecutive days

            // 6. Interaction Score: If there are many unlocks and high total credits, add bonus
            if (unlockScore > 0 && totalCredits > 12) {
                score += 10; // Bonus for high unlocks with high credits
            }

            scoredLists.add(new Pair<>(courseList, score));
        }

        // Sort the lists by score in descending order
        scoredLists.sort((a, b) -> b.getValue() - a.getValue());

        return scoredLists; // Return the sorted lists with scores
    }

    // Method to calculate bonus for consecutive days
    private static int calculateConsecutiveDaysBonus(List<Course> courseList) {
        Set<String> daysSet = new HashSet<>();
        for (Course course : courseList) {
            daysSet.addAll(Arrays.asList(course.getDays()));
        }

        // Assume a week has these days
        List<String> weekDays = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        int bonus = 0;
        for (int i = 0; i < weekDays.size() - 1; i++) {
            if (daysSet.contains(weekDays.get(i)) && daysSet.contains(weekDays.get(i + 1))) {
                bonus += 5; // Add bonus for each consecutive day
            }
        }

        return bonus;
    }

    // Helper class to store pairs of lists and their scores
    static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }




}