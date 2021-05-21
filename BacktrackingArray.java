public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    int lastInd = 0; // pointer for next available spot for adding
    int emptyVal=-111; //representing cells which is empty

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
    }

    @Override
    public Integer get(int index){
        if(index<=lastInd)
            return arr[index];
        else return null;
    }

    @Override
    public Integer search(int x) {
        Integer ans=-1;
        boolean found=false;
        for(int i=0; i<arr.length & !found; i++) { // search each element in arr if equals to x
            if (arr[i] == x){
                ans=i;
                found=true; // if found we can stop searching
            }
        }
        return ans;
    }

    @Override
    public void insert(Integer x) {
        if(lastInd <arr.length){ //if array is not full
            stack.push(new Command("INSERT", lastInd, x)); // Class created for operating INSERT or DELETE command as object
            arr[lastInd]=x; // adding value and pointing to the next available spot
            lastInd++; //updating the pointer for future adding or deleting
        }

    }

    @Override
    public void delete(Integer index) {
        if (lastInd>index) { // removing a value which is not last meaning- replacing its value with the last value in array
            stack.push(new Command("DELETE", index, arr[index])); // Class created for operating INSERT or DELETE command as object
            arr[index] = arr[lastInd-1];
            arr[lastInd-1]=emptyVal;
            lastInd = lastInd -1; //updating the pointer for future adding or deleting
        }
        else if (lastInd==index) {  //removing the last object is simply changing its value to empty
            stack.push(new Command("DELETE", index, arr[index])); // Class created for operating INSERT or DELETE command as object
            arr[lastInd - 1] = emptyVal;
            lastInd = lastInd -1; //updating the pointer for future adding or deleting
        }
    }
    @Override
    public Integer minimum() {
        if(lastInd==0) // if the array has no elements
            return -1;
        else{
            int min=arr[0]; // setting the minimum as the first cells' value
            Integer ans=0;
            for(int i=1; i<lastInd; i++){ // check if there is something smaller in the array
                if(arr[i]<min) {
                    min = arr[i];
                    ans=i;
                }

            }
            return ans; // return the smallest value's index in array
        }
    }

    @Override
    public Integer maximum() {
    	Integer ans;
    	if(lastInd==0) // if the array has no elements
            ans = -1;
        else{
        	int max = arr[0]; // set maximum as the first element
        	ans = 0;
            for(int i=0; i<lastInd; i++){ // check for bigger elements in the array
                if(arr[i]>max){
                    max=arr[i];
                    ans=i;
                }
            }
        }
        return ans; //returns the biggest value's index in array
    }

    @Override
    public Integer successor(Integer index) {
        int left= arr[index], right= arr[maximum()]; // making boundaries to find the index of value which is bigger than a[index] but is the smallest of them
        Integer ans=maximum();
        for(int i=0; i< lastInd; i++){
            int curr=arr[i];
            if(curr>left & curr<right) {//reshaping boundaries from right
                right=curr;
                ans=i;
            }
        }
        if(arr[ans]<=arr[index])
            return -1;
        return ans;
    }

    @Override
    public Integer predecessor(Integer index) {
        int left= arr[minimum()], right= arr[index]; // making boundaries to find index of the value which is smaller than a[index] but is the biggest of them
        Integer ans=minimum();
        for(int i=0; i< lastInd; i++){
            int curr=arr[i];
            if(curr>left & curr<right) { //reshaping boundaries from left
                left = curr;
                ans=i;
            }
        }
        if(arr[ans]>=arr[index])
            return -1;
        return ans;
    }
    @Override
    public void backtrack() {
        if(!stack.isEmpty()){
            Command com = (Command)stack.pop(); // class created for operating INSERT or DELETE command as object
            if(com.getCommandType().equals("INSERT")){
                int i = com.getIndex(); // receiving cell index which we entered the value
                arr[i] = emptyVal;     // undo the insert
                lastInd = lastInd - 1; // return pointer to the last place to add
            } else {
                int i = com.getIndex();
                int val = com.getVal(); // value which should go back to its previous cell
                arr[lastInd] = arr[i]; // return the value to last index
                lastInd = lastInd + 1; // return pointer
                arr[i] = val;         // return the previous value back to the place it was
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
        for(int i=0; i<lastInd; i++){
                output=output+arr[i];
            if(i<arr.length-1)
                output=output+" ";
        }
        System.out.print(output);
    }
}
