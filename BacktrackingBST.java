public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    BacktrackingBST.Node root = null;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
    }

    public Node getRoot() {
        return root;
    }

    public Node search(int x) {
        if (root == null)
            return null;
        return root.search(x); //calling for private method which returns the proper Node
    }

    public void insert(BacktrackingBST.Node z) {
        stack.push(new CommandTree("INSERT",z,z.key,z.value,z.key,z.value));
        redoStack.clear(); // due the obligations
        Node curr = root;
        Node currParent = null;
        while (curr != null) {
            currParent = curr;
            if (z.key < curr.key)
                curr = curr.left;
            else
                curr = curr.right;
        }
        z.parent = currParent;
        if (currParent == null)
            this.root = z;
        else if (z.key < currParent.key)
            currParent.left = z;
        else
            currParent.right = z;
    }

    public void delete(Node x) {
        if (root != null) {
            root = root.delete(stack,x); // calling private method from class node which returns new root- pointing to tree with deleted node
        	if (root != null) // the new root which is the node that was deleted - that means that if the deletion wasn't successful the redoStack doesn't clear
        		redoStack.clear(); // due the obligations
        }
    }
    public Node minimum() {
        Node curr = root;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }

    public Node maximum() {
        Node curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr;
    }

    public Node successor(Node x) {
        if (x.right != null) {
            return x.right.minimum(); // method from class Node
        } else {
            Node curr = x;
            Node currParent = x.parent;
            while (currParent != null && currParent.right == curr) {
                curr = currParent;
                currParent = currParent.parent;
            }
            return currParent;
        }
    }

    public Node predecessor(Node x) {
        if (x.left != null) {
            return x.left.maximum(); // method from class Node
        } else {
            Node curr = x;
            Node currParent = x.parent;
            while (currParent != null && currParent.left == curr) {
                curr = currParent;
                currParent = currParent.parent;
            }

            return currParent;
        }
    }

    @Override
    public void backtrack() {
        if (!stack.isEmpty()) {
            CommandTree com = (CommandTree) stack.pop(); // using new object which includes type of command : insert , delete_leaf, delete_son,switch_val and its Node values.
            if (com.getCommandType().equals("INSERT")) { // we only insert new leaf so we can simply remove the added leaf
                Node curr=com.getNode();
                if(curr.parent.key>curr.key)
                    curr.parent.left=null;
                else
                    curr.parent.right=null;

            }else if (com.getCommandType().equals("DELETE_LEAF")) { //we need to return deleted leaf
                Node leaf = com.getNode();
                Node parent = leaf.parent;
                if(parent == null){
                    this.root = leaf;
                } else {
                    if(parent.key > leaf.key)
                        parent.left = leaf;
                    else
                        parent.right = leaf;
                }

            }else if (com.getCommandType().equals("DELETE_SON")) { // we need to return deleted node which had a single son
                Node current = com.getNode();
                Node parent = current.parent;
                // update my parent to point to me (current)
                if(parent == null){
                    this.root = current;
                } else {
                    if(parent.key > current.key)
                        parent.left = current;
                    else
                        parent.right = current;
                }
                // update my son to point to me (current)
                if(current.left != null)
                    current.left.parent = current;
                else
                    current.right.parent = current;

            } else if (com.getCommandType().equals("SWITCH_VAL")) { // case when the deleted node has 2 sons
                // in this case we need to backtrack another command, so we'll pop twice
                Node toSwitch = com.getNode();
                toSwitch.key = com.getOldKey();
                toSwitch.value = com.getOldVal();
                backtrack(); // enters to stack the base case ( deleting the successor)
                
            }
            
            redoStack.push(com); // update retrack stack
        }
    }

    @Override
    public void retrack() {
    	if (!redoStack.isEmpty()) {
           CommandTree com = (CommandTree) redoStack.pop();
           if (com.getCommandType().equals("INSERT")) { // means that we have to insert what was deleted
            	Node curr=com.getNode();
                if(curr.parent.key>curr.key)
                    curr.parent.left=curr;
                else
                    curr.parent.right=curr;
  
            } else if (com.getCommandType().equals("DELETE_LEAF")) { // means that the node which we need to delete has no sons
            	Node leaf = com.getNode();
                Node parent = leaf.parent;
                if(parent == null){
                    this.root = null;
                } else {
                	if (parent.key > leaf.key) // means that this is a left son
                		parent.left = null;
                	else
                		parent.right = null;
                }

            } else if (com.getCommandType().equals("DELETE_SON")) { // means that the node which we need to delete has one son
            	Node curr = com.getNode();
            	Node son; Node parent = curr.parent;
                if (curr.right == null) // finding whether the son is left or right son
                	son = curr.left;
                else
                	son = curr.right;
                if (curr.parent == null) { // in case current is the root
                	son.parent	= null;
                	this.root = son;
                } else {
	                son.parent = parent;
	                if (parent.key>curr.key) // finding whether curr is left or right son
	                	parent.left = son;
	                else 
	                	parent.right = son;
                }

            } else { // means that the node which we need to delete has 2 sons
            	Node curr = com.getNode();
            	curr.key = com.newKey;
                curr.value = com.newVal;
                retrack(); // activating the deletion of the son
            }        
           
          	stack.push(com); // for back and forth
    	}
    }

    public void printPreOrder() {
        if (root != null) {
            root.printPreOrder();
        }
    }

    @Override
    public void print() {
        printPreOrder();
    }


    public static class Node {
        //These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public BacktrackingBST.Node left;
        public BacktrackingBST.Node right;

        private BacktrackingBST.Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Node search(int toFind) {
            if (this.key == toFind) //either we found the the item or we reached the end of tree
                return this;
            if (toFind < this.key) {
                if (this.left != null)
                	return this.left.search(toFind);
                else return null;
            } else {
            	if (this.right != null)
            		return this.right.search(toFind);
            	else
            		return null;
            }
        }

        public Node minimum() {
            Node curr = this;
            while (curr.left != null) {
                curr = curr.left;
            }
            return curr;
        }

        public Node maximum() {
            Node curr = this;
            while (curr.right != null) {
                curr = curr.right;
            }
            return curr;
        }

        private Node delete(Stack stack, Node toRemove) {
            if (key > toRemove.key) {
                if (left != null)
                    left = left.delete(stack, toRemove);
            } else if (key < toRemove.key) {
                if (right != null)
                    right = right.delete(stack, toRemove);
            } else { //need to remove the data in this node
                if (left == null && right == null) { // 0 children
                    CommandTree commandTree = new CommandTree("DELETE_LEAF", toRemove, toRemove.key, toRemove.value, toRemove.key, toRemove.value);
                    stack.push(commandTree);
                    return null; // the new node for the parent
                }
                else if (left == null | right == null) { // 1 children
                    CommandTree commandTree = new CommandTree("DELETE_SON", toRemove, toRemove.key, toRemove.value, toRemove.key, toRemove.value);
                    stack.push(commandTree);
                    if (left == null) {
                        right.parent = parent;
                        return right; // parent.right = right - update the parent
                    } else {
                        left.parent = parent;
                        return left;
                    }
                }
                else { // this node has two children
                    Node successor = right.minimum();
                    CommandTree commandTree = new CommandTree("SWITCH_VAL", this, this.key, this.value, successor.key, successor.value);
                    key = successor.key;
                    value = successor.value;
                    right = right.delete(stack, successor);
                    stack.push(commandTree); // the switch_val command will be the upper command in the stack
                }
            }
            return this;
        }

        public void printPreOrder() {
            if (this != null) {
                System.out.print(key);
                if (left != null) {
                    System.out.print(" ");
                    left.printPreOrder();
                }
                if (right != null) {
                    System.out.print(" ");
                    right.printPreOrder();
                }
            }
        }
    }
    

    public static class CommandTree {
        private String commandType; // INSERT, DELETE_LEAF, DELETE_SON, SWITCH_VAL
        private Node node; // the node to work with
        // These fields are for backtracking 'SWITCH_VAL'
        private int oldKey;
        private Object oldVal;
        // These fields are for retracking 'SWITCH_VAL'
        private int newKey;
        private Object newVal;

        public CommandTree(String command, Node node, int oldKey, Object oldVal, int newKey, Object newVal) {
            this.commandType = command;
            this.node = node;
            this.oldKey = oldKey;
            this.oldVal = oldVal;
            this.newKey = newKey;
            this.newVal = newVal;
        }

        public String getCommandType() {
            return commandType;
        }

        public Node getNode() {
            return node;
        }

        public int getOldKey() {
            return oldKey;
        }

        public Object getOldVal() {
            return oldVal;
        }

        public int getNewKey() {
            return newKey;
        }

        public Object getNewVal() {
            return newVal;
        }
    }

}
