package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.Random;

public class Main {

    public static int[] generateArray(int length, int min, int max) {
        Random rand = new Random();
        int[] array = new int[length];
        for (int x = 0; x < length; x++) {
            array[x] = rand.nextInt(max - min) + min;
        }
        return array;
    }

    public static boolean myThreeSum(int[] array, int arrayLength) {
        Arrays.sort(array);
        int midIndex = 0;
        int posLow, posHigh, negLow, negHigh, highChecked, lowChecked;
        for (int x = 0; x < arrayLength; x++) {
            if (array[x] > 0) {
                midIndex = x;
                break;
            }
        }

        highChecked = arrayLength - 1;
        //Looking for a one negative number and two positive solution
        //Taking lowest negative first
        for (int neg = 0; neg < midIndex; neg++) {
            //Resetting position low value
            posLow = midIndex;
            posHigh = highChecked;
            //Looping down from highest element to find highest element smaller than current negative
            while(array[posHigh] + array[neg] > 0){
                posHigh--;
            }

            //Setting highChecked to highest positive element that is smaller than current negative for next round
            //of negative element
            highChecked = posHigh;

            //Finding matching elements by looping down and up through positive elements
            while(posHigh > posLow) {
                //If total equals 0, return
                if(array[posHigh] + array[posLow] + array[neg] == 0 && array[posHigh] != array[posLow]){
                    return true;
                //If total is greater than 0, decrement high positive index
                } else if(array[posHigh] + array[posLow] + array[neg] > 0){
                    posHigh--;
                //If total is less than 0, increment low positive index
                } else {
                    posLow++;
                }
            }
        }

        lowChecked = 0;

        //Doing same thing for one positive and two negative number solution
        for (int pos = midIndex; pos < arrayLength; pos++) {
            negLow = midIndex - 1;
            negHigh = lowChecked;
            //Looping "up" through negative elements to find largest negative element larger than current negative
            while(array[negHigh] + array[pos] < 0){
                negHigh++;
            }

            //Setting lowChecked to largest negative element that is smaller than the current positive for next round
            //of positive elements
            lowChecked = negHigh;

            //Finding matching elements by looping down and up through positive elements
            while(negHigh < negLow){
                //If total equals 0, return
                if(array[negHigh] + array[negLow] + array[pos] == 0 && array[negHigh] != array[negLow]){
                    return true;
                } else if(array[negHigh] + array[negLow] + array[pos] < 0) {
                    negHigh++;
                } else{
                    negLow--;
                }
            }
        }
        return false;
    }

    public static boolean bruteThreeSum(int[] array, int arrayLength) {
        for (int i = 0; i < arrayLength - 2; i++) {
            for (int j = i + 1; j < arrayLength - 1; j++) {
                for (int k = j + 1; k < arrayLength; k++) {
                    if ((array[i] + array[j] + array[k]) == 0 && array[i] != array[j] && array[j] != array[k] && array[i] != array[k]) {
                      //  System.out.printf("Match found at indices %d, %d and %d\n", i, j, k);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Binary search - splits array into sub-arrays
    public static int BinarySearch(int[] list, int searchValue){
        int low = 0;
        int mid;
        int high = list.length-1;

        while (low <= high) {
            mid = low + (high - low)/2;
            if (searchValue == list[mid]) {
                return mid;
            } else if (searchValue > list[mid]) {
                low = mid+1;
            } else if (searchValue < list[mid]){
                high = mid-1;
            }
        }
        return -1;
    }

    //Heavily based on book example on page 190
    //Used my own binary search (above)
    public static boolean fasterThreeSum(int[] array, int arrayLength) {
        Arrays.sort(array);
        //Working up the list, comparing two elements and searching to find the third
        for(int x = 0; x < arrayLength; x++){
            for(int y = x+1; y < arrayLength; y++){
                if(BinarySearch(array, -(array[x]+array[y])) != -1){
                    return true;
                }
            }
        }
        return false;
    }

    //Function to retrieve CPU time
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    public static boolean verificationTests() {
        int[] testArray;
        boolean bruteResult, myResult, fasterResult;
        for(int N = 4; N < 10000; N=N*2 ){
            System.out.printf("Testing for array of length %d\n", N);
            testArray = generateArray(N, -(N*2), N*2);
                bruteResult = bruteThreeSum(testArray, N);
                myResult = myThreeSum(testArray, N);
                fasterResult = fasterThreeSum(testArray, N);
                if (bruteResult != myResult || myResult != fasterResult){
                    System.out.printf("Three sum results don't match for array of size %d\n ", N);
                    return false;
                }
                System.out.printf("Results match - all returned %b - tests passed!\n", fasterResult);
            if(N < 100){
                for(int x = 0; x < N; x++){
                    System.out.printf("%d ", testArray[x]);
                }
                System.out.printf("\n");
            }
        }
        return true;
    }

    //Calculates log base 2 of N
    public static float logBaseTwo(int N){
        float result = ((float) Math.log(N)/ (float) Math.log(2));
        return result;
    }

    public static void runTimeTests() {
        int[] testArray = new int[1];
        long beforeTime, afterTime, totalTime;
        long brutePrevTime = 1;
        long myPrevTime = 1;
        long fasterPrevTime = 1;

        System.out.printf("%31s %47s %47s\n", "Brute 3sum", "Faster 3sum", "Fastest 3sum");
        System.out.printf("%15s %15s %15s %15s %15s %15s %15s %15s %15s %15s\n",
                "N", "Time", "Doubling", "Expected", "Time", "Doubling", "Expected", "Time", "Doubling", "Expected");
        System.out.printf("%47s %15s %31s %15s %31s %15s\n", "Ratios", "Doubling", "Ratios", "Doubling", "Ratios", "Doubling");
        System.out.printf("%63s %47s %47s\n", "Ratios", "Ratios", "Ratios");
        for (int N = 4; N < 1000; N = N * 2) {
            System.out.printf("%16s", N);
            testArray = generateArray(N, -(N * 2), N * 2);

            beforeTime = getCpuTime();
            bruteThreeSum(testArray, N);
            afterTime = getCpuTime();
            totalTime = afterTime - beforeTime;
            System.out.printf("%15d ", totalTime);
            if(N!= 4){
                System.out.printf("%15.3f %15d ", (float) totalTime/brutePrevTime, (int)(Math.pow(N, 3)/(Math.pow((N/2), 3))));
            } else {
                System.out.printf("%15s %15s ", "na", "na");
            }
            brutePrevTime = totalTime;

            testArray = generateArray(N, -(N * 2), N * 2);
            beforeTime = getCpuTime();
            fasterThreeSum(testArray, N);
            afterTime = getCpuTime();
            totalTime = afterTime - beforeTime;
            System.out.printf("%16d", totalTime);
            if(N!= 4){
                System.out.printf("%15.3f %15.3f ", (float) totalTime/fasterPrevTime,
                        (float)((logBaseTwo(N)*Math.pow(N, 2))/(logBaseTwo(N/2)*Math.pow((N/2), 2))));
            } else {
                System.out.printf("%15s %15s ", "na", "na");
            }
            fasterPrevTime = totalTime;

            testArray = generateArray(N, -(N * 2), N * 2);
            beforeTime = getCpuTime();
            myThreeSum(testArray, N);
            afterTime = getCpuTime();
            totalTime = afterTime - beforeTime;
            System.out.printf("%16d", totalTime);
            if(N!= 4){
                System.out.printf("%15.3f %15d ", (float) totalTime/myPrevTime, (int)(Math.pow(N, 2)/(Math.pow((N/2), 2))));
            } else {
                System.out.printf("%15s %15s ", "na", "na");
            }
            myPrevTime = totalTime;

            System.out.printf("\n");
        }
    }

    public static void main(String[] args) {
        // write your code here
        if(verificationTests()){
            System.out.printf("Tests passed!");
        }
        runTimeTests();
    }
}
