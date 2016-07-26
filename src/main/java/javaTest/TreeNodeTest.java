package javaTest;

/**
 * Created by antong on 16/7/26.
 */
public class TreeNodeTest {
    public TreeNode firstNode;

    /**
     * 如果没有root节点，返回空，
     * 如果有第一个节点，递归查找如果该节点大于当前节点，从右面继续查找，如果小于，则从左面，直到返回相等的节点
     *
     * @param data
     * @return
     */
    public TreeNode getNode(int data) {
        if (this.firstNode == null) {
            return null;
        }
        TreeNode current = firstNode;
        while (current != null) {
            if (current.data > data) {
                current = current.left;
            } else if (current.data < data) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    /**
     * 如果没有root节点则插入
     * 如果有递归查找，如果该节点大于当前节点，从右面继续查找，如果小于，则从左面，直到得到空节点，将他空节点的父节点的空节点替换为插入节点
     *
     * @param data
     * @return
     */
    public TreeNode insert(int data) {
        TreeNode node = new TreeNode(data);
        TreeNode parent = null;
        if (firstNode == null) {
            this.firstNode = node;
            return firstNode;
        }
        TreeNode current = this.firstNode;
        while (true) {
            parent = current;
            if (data > current.data) {
                current = current.right;
                if (current == null) {
                    parent.right = node;
                    return node;
                }
            } else {
                current = current.left;
                if (current == null) {
                    current.left = node;
                    return node;
                }
            }
        }
    }

    /**
     * 分为三种，一没有任何子节点，直接删除
     * 二，只有一边有子节点，将父节点对应的子节点，指向子节点存在的节点
     * 三，两边都有子节点，将父节点指向的子节点数据替换为子节点右边的最小节点的数据，并将最小节点删除
     * @param data
     */
    public void delete(int data) {
        TreeNode current = firstNode;
        TreeNode parent = null;
        while (current != null && current.data != data) {
            parent = current;
            if (current.data > data) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (current.left == null && current.right == null) {
            if (parent.left.data == data) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }

        if ((current.left == null && current.right != null)) {
            if (parent.left.data == data) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
        }

        if ((current.right == null && current.left != null)) {
            if (parent.left.data == data) {
                parent.left = current.left;
            } else {
                parent.right = current.left;
            }
        }
        if (current.right != null && current.left != null) {
            TreeNode node = getMinTreeNode(current);
            current.data = node.data;
        }
    }

    public TreeNode getMinTreeNode(TreeNode node) {
        TreeNode parent = node;
        node = parent.right;
        TreeNode smallNode = null;
        while (node != null) {
            smallNode = node;
            node = node.left;
        }
        smallNode.left = null;
        return node;
    }

    class TreeNode {
        public int data;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int data) {
            this.data = data;
        }
    }
}
