package Grammar;

import java.util.Iterator;
import java.util.List;

public class TreeNode {

    final String name;
    final List<TreeNode> children;
    static int counter = 0;
    public int rightSibling;
    public int parent;
    public final int index;

    public TreeNode(String name, List<TreeNode> children) {
        this.name = name;
        this.children = children;
        counter ++;
        rightSibling = 0;
        parent = 0;
        index = counter;
        for (var child : children) {
            child.parent = index;
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append("index name parent sibling");
        buffer.append("\n");
        for (Iterator<TreeNode> it = children.iterator(); ((Iterator<?>) it).hasNext();) {
            TreeNode next = it.next();
            buffer.append(next.index);
            buffer.append(" ");
            buffer.append(next.name);
            buffer.append(" ");
            buffer.append(next.parent);
            buffer.append(" ");
            buffer.append(next.rightSibling);
            buffer.append("\n");
        }
    }
}
