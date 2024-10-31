import java.util.ArrayList;
import java.util.List;

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
