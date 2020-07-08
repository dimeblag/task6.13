import java.util.Iterator;
import java.util.Stack;

public class AVLTree<T extends Comparable<T>> implements Iterable<T> {

    public class TreeNode<T extends Comparable<T>> {
        private T value;
        private TreeNode<T> left;
        private TreeNode<T> right;
        private int height;
        private boolean extendedBefore = false;

        TreeNode(T value, TreeNode<T> left, TreeNode<T> right, int height) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = height;
        }

        private TreeNode(T value) { this(value, null, null, 0); }

        public T getValue() { return value; }

        public TreeNode<T> getLeft() { return left; }

        public TreeNode<T> getRight() { return right; }

        public boolean isExtendedBefore() { return extendedBefore; }

        private void countHeight() {
            height = Math.max(left != null ? left.height : -1, right != null ? right.height : -1) + 1;
        }

        private int differenceBetweenLeftAndRight() {
            return (left != null ?  left.height : -1) - (right != null ? right.height : -1);
        }
    }

    private TreeNode<T> root = null;
    private int size = 0;

    public TreeNode<T> getRoot() { return root; }

    public int size() { return size; }

    public boolean add(T value) {
        root = add(root, value);
        return !root.extendedBefore;
    }

    private TreeNode<T> add(TreeNode<T> node, T value) {
        if (node == null) {
            size++;
            return new TreeNode<>(value);
        }
        node.extendedBefore = false;

        if (value.compareTo(node.value) == 0) {
            node.extendedBefore = true;
            return node;
        }
        else {
            if (value.compareTo(node.value) < 0)
                node.left = add(node.left, value);
            else
                node.right = add(node.right, value);
            node.countHeight();
            node = balance(node);
        }

        try {
            node.extendedBefore = node.left.extendedBefore || node.right.extendedBefore;
            node.left.extendedBefore = false;
            node.right.extendedBefore = false;
        } catch (NullPointerException ignored) {}
        return node;
    }

    private TreeNode<T> balance(TreeNode<T> node) {
        if (node.differenceBetweenLeftAndRight() < -1) {
            if (node.right != null && node.right.differenceBetweenLeftAndRight() > 0)
                node.right = rightRotate(node.right);
            node = leftRotate(node);
        } else if (node.differenceBetweenLeftAndRight() > 1) {
            if (node.left != null && node.left.differenceBetweenLeftAndRight() < 0)
                node.left = leftRotate(node.left);
            node = rightRotate(node);
        }
        return node;
    }

    private TreeNode<T> leftRotate(TreeNode<T> node) {
        TreeNode<T> right = node.right;
        node.right = right.left;
        right.left = node;
        node.countHeight();
        right.countHeight();
        return right;
    }

    private TreeNode<T> rightRotate(TreeNode<T> node) {
        TreeNode<T> left = node.left;
        node.left = left.right;
        left.right = node;
        node.countHeight();
        left.countHeight();
        return left;
    }

    public T getValue(T value) {
        TreeNode<T> node = getNode(root, value);
        return node != null ? node.value : null;
    }

    private TreeNode<T> getNode(TreeNode<T> node, T value) {
        if (node == null)
            return null;
        int compareResult = value.compareTo(node.value);
        if (compareResult == 0)
            return node;
        if (compareResult > 0)
            return node.right;
        return node.left;
    }

    public TreeNode<T> remove(T value) {
        root = remove(root, value);
        return root;
    }

    private TreeNode<T> remove(TreeNode<T> node, T value) {
        if (node == null)
            return null;
        int compareResult = value.compareTo(node.value);

        if (compareResult == 0) {
            if (node.left != null && node.right != null) {
                node.value = getMin(node.right).value;
                node.right = remove(node.right, node.value);
            } else {
                node = (node.left != null) ? node.left : node.right;
                size--;
            }
            node.extendedBefore = true;
        } else if (compareResult > 0)
            node.right = remove(node.right, value);
        else
            node.left = remove(node.left, value);

        node.extendedBefore = node.left.extendedBefore || node.right.extendedBefore;
        return balance(node);
    }

    private TreeNode<T> getMin(TreeNode<T> node) {
        return (node == null || node.left == null) ? node : getMin(node.left);
    }

    @Override
    public Iterator<T> iterator() { return treeVisitor().iterator(); }

    private Iterable<T> treeVisitor() {
        return () -> {
            Stack<TreeNode<T>> nodes = new Stack<>();
            TreeNode<T> node = root;
            while (node != null) {
                nodes.push(node);
                node = node.left;
            }

            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return !nodes.isEmpty();
                }

                @Override
                public T next() {
                    TreeNode<T> node = nodes.pop();
                    T result = node.value;
                    if (node.right != null) {
                        node = node.right;
                        while (node != null) {
                            nodes.push(node);
                            node = node.left;
                        }
                    }
                    return result;
                }
            };
        };
    }
}
