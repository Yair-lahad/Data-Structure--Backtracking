public class Warmup {
    public static int backtrackingSearch(int[] arr, int x, int fd, int bk, Stack myStack) {
        int aInd = 0;
        while(aInd<arr.length-1) { // running on array to search for x
            // forward steps
            for (int i = 0; i < fd & aInd<arr.length; i++) { // if we found x or we reached the end of array, we stop
                myStack.push(arr[aInd]);
                if (arr[aInd] == x) { //checking if x is in array cell, if so the search is over
                    return aInd;
                }
                else if(aInd == arr.length-1){ // Reached the end of the array
                    return -1;
                }
                aInd = aInd + 1;
            }
            // backward steps
            for (int j = 0; j < bk & aInd>=0; j++) { // if we found x or we reached the end of array, no need to backward
                myStack.pop();
                aInd = aInd - 1;
            }
        }
        return -1;
    }

    /**
     * A consistent binary search for int x in an int array (using stack).
     *
     * @return the index of the element.
     */
    public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
		return cBSrec(arr, x, 0, arr.length-1, myStack);
    }
    
    /**
     * A recursive function which checks each "step" for "inConsistent".
     * Search for x in array arr, using indexes high and low, using myStack stack.
     *
     * @return the index of the element.
     */
    public static int cBSrec(int[] arr, int x, int low, int high, Stack myStack) {
    	int output = -1;
    	int stepsback = isConsistent(arr); // use the function to find how much steps are needed to go back.
    	if (stepsback != 0 & !myStack.isEmpty()) { // checks if isConsistent and if the stack is not empty (for example at the first recursive call)
			int lastlow = low; // just because java wants that to be initialized
			int lasthigh = high;
			while (stepsback>0 & !myStack.isEmpty()) { // used to determined how many pops are needed
				lastlow = (int)myStack.pop();
				lasthigh = (int)myStack.pop();
				stepsback = stepsback-1;
			}
    		return cBSrec(arr, x, lastlow, lasthigh, myStack); // send the old values back
    	}
    	int middle = (high+low)/2 ;
		myStack.push(high);
		myStack.push(low);
		if (high>= low) {
			if (x==arr[middle])
				output = middle;
			else if (x < arr[middle]) {
				output = cBSrec(arr, x, low, middle-1, myStack);
			}else {
				output = cBSrec(arr, x, middle+1, high, myStack);		
			}
		}
		return output;
    }
    
    private static int isConsistent(int[] arr) {
        double res = Math.random() * 100 - 75;
        if (res > 0)
            return (int)Math.round(res / 10);
        else
        	return 0;
    }

}
