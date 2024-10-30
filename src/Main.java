import com.sun.source.tree.Tree;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TreeNode<T> {
    private T data;
    private List<TreeNode<T>> children;

    public String treeName;

    public TreeNode() {
    }



    public TreeNode(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode<T> child) {
        children.add(child);
    }

    public void removeChild(TreeNode<T> child) {
        children.remove(child);
    }

    public void removeFirstChild() {
        children.removeFirst();
    }

    public void removeLastChild() {
        children.removeLast();
    }

    public void removeAllChildren(TreeNode<T> treeNode) {
        children.removeAll(treeNode.getChildren());
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void traverse() {
        System.out.println(data);
        int depth = 0;

        for (TreeNode<T> child : children) {
            child.traverse();
            depth++;
        }

//        System.out.println(depth);
    }

    public void traverseOnlyChildren(TreeNode<T> treeNode) {
        int depth = 0;
        for (TreeNode<T> node : treeNode.getChildren()) {
            System.out.println(node.getData());
            depth++;
        }
        System.out.println(depth);
    }

    public int treeNodeDepthCount() {
        int depth = 0;
        System.out.println(data);
        for (TreeNode<T> child : children) {
            child.traverse();
            depth++;
            int sum = 0;

            sum = sum + depth;
            System.out.println("SUM of children : " + sum);

        }
        System.out.println("depth of the node: " + depth);
//        System.out.println("Sum of Branches: "+ sum);
        return depth;
    }

    public int allTreeDepth() {
        int countChild = 0;
        for (TreeNode<T> node : children) {
            countChild++;
            countChild += node.allTreeDepth();

        }
//        System.out.println("the depth is :"+countChild);


        return countChild;

    }

    public boolean search(T key) {
        if (key.equals(this.data)) {
            return true;
        }
        return children.stream().anyMatch(child -> child.search(key));
    }

    public T get(int index) {
        return children.get(index).getData();
    }

    public int size() {
        return children.size();
    }

    static <T> int theDeepestTreeNode(List<TreeNode<T>> treeNodes) {
        int max = treeNodes.getFirst().allTreeDepth();
        for (TreeNode<T> treeNode : treeNodes) {
            if (max < treeNode.allTreeDepth()) {
                max = treeNode.allTreeDepth();
                if (max == treeNode.allTreeDepth()) {
                    System.out.println(treeNode.getData().toString() + " is the deepest tree or sub tree");
                }
                return max;
            }
        }
        System.out.println("the max is: " + max);

        return max;
    }

}

public class Main {
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Saturday", "Sunday"};

    public static void main(String[] args) {
//        // Creating a general tree
//        TreeNode<String> calculus1 = new TreeNode<>("Calculus 1");
//        TreeNode<String> node = new TreeNode<>();
//
//        TreeNode<String> calculus2 = new TreeNode<>("Calculus 2");
//        TreeNode<String> physics1 = new TreeNode<>("General Physics 1");
//        TreeNode<String> adCal = new TreeNode<>("Advanced Calculus");
//        TreeNode<String> differentialEquations = new TreeNode<>("Differential Equations");
//        TreeNode<String> statistics = new TreeNode<>("Statistics For Engineering");
//        TreeNode<String> discreteStructure = new TreeNode<>("Discrete Structure");
//
//        TreeNode<String> physics2 = new TreeNode<>("General Physics 2");
//        TreeNode<String> electronic = new TreeNode<>("Electronic Materials");
//        TreeNode<String> circuitAnalysis = new TreeNode<>("Circuit Analysis");
//
//        calculus1.addChild(calculus2);
//        calculus1.addChild(physics1);
//
//        calculus2.addChild(adCal);
//        calculus2.addChild(differentialEquations);
//        calculus2.addChild(statistics);
//        calculus2.addChild(discreteStructure);
//
//
//        physics1.addChild(physics2);
//        physics2.addChild(electronic);
//        electronic.addChild(circuitAnalysis);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Saturday", "Sunday"};

        LocalTime time1 = LocalTime.of(9, 30);
        LocalTime time2 = LocalTime.of(10, 0);
        LocalTime time3 = LocalTime.of(11, 30);
        LocalTime time4 = LocalTime.of(13, 0);
        LocalTime time5 = LocalTime.of(13, 30);
        LocalTime time6 = LocalTime.of(14, 0);
        LocalTime time7 = LocalTime.of(14, 30);
        LocalTime time8 = LocalTime.of(15, 0);

        Course cal1 = new Course("Calculus 1", 4, new String[]{days[0], days[5]}, 9, 30, 11, 0);
        Course phy1 = new Course("Physics 1", 3, new String[]{days[1], days[5]}, time1, time3);
        Course advancedCal = new Course("Advanced Calculus", 3, new String[]{days[2], days[4]}, 9, 30, 11, 0);
        Course st = new Course("Statistics", 3, new String[]{days[3]}, 11, 0, 12, 30); // Changed to have only one day
        Course ict = new Course("Information and Communication Technology", 4, new String[]{days[0], days[2]}, 10, 30, 12, 0);
        Course discrete = new Course("Discrete Structure", 3, new String[]{days[0], days[3]}, 9, 30, 11, 0);
        Course mi = new Course("MicroProcessor", 3, new String[]{days[2], days[3]}, 10, 0, 11, 30);
        Course def = new Course("Differential Equations", 4, new String[]{days[4], days[5]}, 11, 0, 12, 30);
        Course circuit = new Course("Circuit Analysis", 4, new String[]{days[1], days[5]}, 10, 30, 12, 0);
        Course electron = new Course("Electronic", 3, new String[]{days[0], days[3]}, 9, 30, 11, 0);

        Course prog = new Course("Programming Concepts & Algorithms", 4, new String[]{days[1], days[2]}, 13, 0, 14, 30);
        Course phy2 = new Course("Physics 2", 3, new String[]{days[2], days[4]}, 14, 0, 15, 30);
        Course logic = new Course("Computer Logic Design", 3, new String[]{days[3], days[5]}, 13, 30, 15, 0);
        Course o = new Course("Object Oriented Programming", 2, new String[]{days[0]}, 13, 0, 14, 0); // Changed to have only one day
        Course cal2 = new Course("Calculus 2", 4, new String[]{days[1], days[3]}, 14, 30, 16, 0);
        Course coa = new Course("COA", 3, new String[]{days[0], days[3]}, 13, 0, 14, 30);
        Course database = new Course("Fundamentals of Database Systems", 3, new String[]{days[4], days[2]}, 14, 0, 15, 30);
        Course ds = new Course("Data Structure", 4, new String[]{days[1], days[5]}, 13, 30, 15, 0);
        Course csd = new Course("Computer System Design", 3, new String[]{days[0], days[3]}, 13, 0, 14, 30);
        Course se = new Course("Software Engineering", 4, new String[]{days[2], days[4]}, 14, 30, 16, 0);
        Course nt1 = new Course("Computer Communication & Network", 4, new String[]{days[2], days[4]}, 14, 30, 16, 0);
        Course ops = new Course("Operating System", 4, new String[]{days[2], days[4]}, 14, 30, 16, 0);
        Course comsec = new Course("Computer Security", 4, new String[]{days[2], days[4]}, 14, 30, 16, 0);
        Course nt2 = new Course("Computer Networking Design & Analysis", 4, new String[]{days[2], days[4]}, 14, 30, 16, 0);
        Course imbs = new Course("Introduction to Embedded System", 4, new String[]{days[2], days[4]}, 14, 30, 16, 0);
        Course arti = new Course("Artificial Intelligent", 4, new String[]{days[2], days[4]}, 14, 30, 16, 0);

        List<Course> courseList = List.of(
                cal1, phy1, advancedCal, st, ict, discrete, mi, def, circuit, electron,
                prog, phy2, logic, o, cal2, coa, database, ds, csd, se,
                nt1, ops, comsec, nt2, imbs, arti
        );

//        seeAllPossibilities(courses);



        TreeNode<Course> computerEngineeringTree = new TreeNode<>(new Course());
        TreeNode<Course> calculus1 = new TreeNode<>(cal1);


        TreeNode<Course> calculus2 = new TreeNode<>(cal2);
        TreeNode<Course> physics1 = new TreeNode<>(phy1);
        TreeNode<Course> adCal = new TreeNode<>(advancedCal);
        TreeNode<Course> differentialEquations = new TreeNode<>(def);
        TreeNode<Course> statistics = new TreeNode<>(st);
        TreeNode<Course> discreteStructure = new TreeNode<>(discrete);
        TreeNode<Course> physics2 = new TreeNode<>(phy2);
        TreeNode<Course> electronic = new TreeNode<>(electron);
        TreeNode<Course> circuitAnalysis = new TreeNode<>(circuit);

        calculus1.addChild(calculus2);
        calculus1.addChild(physics1);

        calculus2.addChild(adCal);
        calculus2.addChild(differentialEquations);
        calculus2.addChild(statistics);
        calculus2.addChild(discreteStructure);


        physics1.addChild(physics2);
        physics2.addChild(electronic);
        electronic.addChild(circuitAnalysis);

        TreeNode<Course> info = new TreeNode<>(ict);
        TreeNode<Course> programing = new TreeNode<>(prog);
        TreeNode<Course> logicDesign = new TreeNode<>(logic);
        TreeNode<Course> oop = new TreeNode<>(o);
        TreeNode<Course> db = new TreeNode<>(database);
        TreeNode<Course> dataStructure = new TreeNode<>(ds);
        TreeNode<Course> comSysDesign = new TreeNode<>(csd);
        TreeNode<Course> software = new TreeNode<>(se);
        TreeNode<Course> coA = new TreeNode<>(coa);
        TreeNode<Course> micro = new TreeNode<>(mi);
        TreeNode<Course> net1 = new TreeNode<>(nt1);
        TreeNode<Course> net2 = new TreeNode<>(nt2);
        TreeNode<Course> os = new TreeNode<>(ops);
        TreeNode<Course> ai = new TreeNode<>(arti);
        TreeNode<Course> sec = new TreeNode<>(comsec);
        TreeNode<Course> embedded = new TreeNode<>(imbs);

        info.addChild(programing);
        info.addChild(logicDesign);
        programing.addChild(oop);
        programing.addChild(db);
        oop.addChild(dataStructure);
        dataStructure.addChild(comSysDesign);
        db.addChild(software);
        software.addChild(ai);
        logicDesign.addChild(coA);
        coA.addChild(micro);
        coA.addChild(net1);
        coA.addChild(os);
        coA.addChild(comSysDesign);
        micro.addChild(embedded);
        net1.addChild(net2);
        os.addChild(sec);





        computerEngineeringTree.addChild(calculus1);
        computerEngineeringTree.addChild(info);
        computerEngineeringTree.traverse();
        seeAllPossibilities(courseList);



    }


    private static void showPermutations(List<List<Course>> allLists, List<Course> currentList, Course currentCourse) {
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
            if (!hasTimeConflict(currentSet, course) && potentialCredit <= maxCredit ) {
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
            if (calculateTotalCredits(list) >=10){
                System.out.println("List: " + list);
                System.out.println("Total Credits: " + calculateTotalCredits(list));
                int uniqueDays = numOfDays(list);
                System.out.println("All Days of the week " + uniqueDays);
                System.out.println(calculateTimeRest(list));
                findTimeConflicts(list);
            }
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
                            return true; // Conflict found
                        }
                    }
                    if (course.getDayAndTime().contains(sharedDay) && newCourse.getDayAndTime().contains(sharedDay)) {
                        if (course.getStartTime().isBefore(newCourse.getEndTime()) && course.getEndTime().isAfter(newCourse.getStartTime())) {
                            return true; // Conflict found
                        }
                    }
                }
            }


        }
        return false; // No conflict found
    }


        public static int numOfDays(List<Course> courses){
        Set<String> daysOfWeek = new HashSet<>();
        for (Course course: courses){
            for (String day : course.getDays()){
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




    public static void findTimeConflicts(List<Course> courses) {
        IntStream.range(0, courses.size() - 1)
                .forEach(i ->
                        IntStream.range(i + 1, courses.size())
                                .forEach(j -> {
                                    Course course1 = courses.get(i);
                                    Course course2 = courses.get(j);

                                    List<String> sharedDays = Arrays.stream(course1.getDays())
                                            .filter(day -> Arrays.asList(course2.getDays()).contains(day))
                                            .toList();

                                    sharedDays.forEach(sharedDay -> {
                                        if (course1.getDayAndTime().contains(sharedDay) && course2.getDayAndTime().contains(sharedDay)) {
                                            if (course1.getEndTime().isAfter(course2.getStartTime()) && course1.getStartTime().isBefore(course2.getEndTime())) {
                                                System.out.println("Time conflict found between:");
                                                System.out.println(course1);
                                                System.out.println(course2);
                                                System.out.println();
                                            }
                                        }
                                    });
                                }));
    }

    



}
