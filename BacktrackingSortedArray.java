public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    int last = -1; // last is a pointer to the last number in the array. starting from -1 because the first element is placed in arr[0]
    final int emptyValue = -998;

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
    }
    
    @Override
    public Integer get(int index){
    	if (index<=last)
    		return arr[index];
    	else return null;
    }

    @Override
    public Integer search(int x) {
    	if (last==-1)
    		return -1;
    	else 
    		return BinarySearch(x,minimum(),maximum());
    }
    
    public Integer BinarySearch(int x, int low, int high) { // regular binary search to provide O(log(n)) efficiency
    	int output = -1;
		int middle = (high+low)/2 ;
		if (high>= low) {
			if (x==arr[middle])
				output = middle;
			else if (x < arr[middle])
				output = BinarySearch(x, low, middle-1);
			else 
				output = BinarySearch(x, middle+1, high);		
		}
		return output;
    }

    @Override
    public void insert(Integer x) {
    	boolean found = false;
    	if (last<arr.length) { // making sure that the array has free spaces
    		for (int i=0; i<=last & !found; i=i+1) { // if x is smaller insert it, else move to the next known number and check    			
    			if (x<=arr[i]) {
    				last = last+1;
    				for (int j = last; j>=i; j=j-1) // move all the bigger elements to the right using their predecessors
    					if(predecessor(j)>=0)
    						arr[j]= get(predecessor(j));
    				arr[i]=x; // insert x in the right position
    				stack.push(new Command("INSERT", i, x)); // class created to operate insertion or deletion as objects
        			found = true;
    			}
    		}
    		if (!found) { // might be if the array is empty or the element x is bigger than every other element
    			last = last+1;
    			arr[last] = x;
    			stack.push(new Command("INSERT", last, x));
    		}
    	}
    }


    @Override
    public void delete(Integer index) {
    	if (index<=last) {
	    	stack.push(new Command("DELETE", index, arr[index])); // inserting the command to the stack
	    	for (int i=index; i<last; i=i+1){ // moving every bigger number one place the left
	    		arr[i]= get(this.successor(i));
	    	}
	    	arr[last]= emptyValue; // reseting
	    	last = last-1; // updating the field
    	}
    }
    
    @Override
    public Integer minimum() {
    	if(last == -1) // checks if the array is not empty
    		return -1;
    	else return 0; // because it's a sorted array
    }

    @Override
    public Integer maximum() {
    	if(last==-1) // // checks if the array is not empty
    		return -1;
    	else return last; // because it's a sorted array
    }

    @Override
    public Integer successor(Integer index) {
        if (index<last) // because last successor is undefined
			return index+1;
    	else return -1;
    }
    
    @Override
    public Integer predecessor(Integer index) {
    	if (index>0 & index<=last) // because 0 predecessor is undefined
    			return (index-1);
    	else return -1;
    }

    @Override
    public void backtrack() {
    	if(!stack.isEmpty()) {
    		Command com = (Command)stack.pop();
    		if (com.getCommandType().equals("INSERT")){
    			this.delete(com.getIndex());
    			stack.pop(); // because delete method insert a command to the stack
    		} else {
    			this.insert(com.getVal());
    			stack.pop(); // because insert method insert a command to the stack
    		}
    		System.out.println("backtracking performed");
    	}
    }

    @Override
    public void retrack() {
        // Do not implement anything here!!
    }

    @Override
    public void print() {
    	String output="";
        for(int i=0; i<=last; i=i+1){
                output = output + arr[i];
            if(i<last) // in order to prevent a space after the last number 
                output=output+" ";
        }
        System.out.print(output);
    }

}
