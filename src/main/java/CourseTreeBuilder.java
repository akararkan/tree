import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseTreeBuilder {

    public static TreeNode<Course> buildCourseTree(List<Course> courses) {
        Map<String, TreeNode<Course>> courseNodeMap = new HashMap<>();
        TreeNode<Course> root = new TreeNode<>(new Course("Root", 0, new String[]{}, 0, 0, 0, 0)); // Placeholder root

        // Step 1: Create TreeNode for each course and store in a map
        for (Course course : courses) {
            courseNodeMap.put(course.getName(), new TreeNode<>(course));
        }

        // Step 2: Build tree structure based on prerequisites and unlocks
        for (Course course : courses) {
            TreeNode<Course> courseNode = courseNodeMap.get(course.getName());

            if (course.getPrerequisites().isEmpty()) {
                // Add courses without prerequisites as children of the root
                root.addChild(courseNode);
            }

            // Add unlocked courses as children based on the "unlocks" field
            for (String unlockName : course.getUnlocks()) {
                TreeNode<Course> unlockNode = courseNodeMap.get(unlockName);
                if (unlockNode != null) {
                    courseNode.addChild(unlockNode);
                }
            }
        }

        return root;
    }
}
