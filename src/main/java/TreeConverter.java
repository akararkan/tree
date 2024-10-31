import javax.swing.tree.DefaultMutableTreeNode;

public class TreeConverter {
    public static DefaultMutableTreeNode convertToSwingNode(TreeNode<Course> node) {
        DefaultMutableTreeNode swingNode = new DefaultMutableTreeNode(node.getData().getName());
        for (TreeNode<Course> child : node.getChildren()) {
            swingNode.add(convertToSwingNode(child)); // Recursively convert each child
        }
        return swingNode;
    }
}
