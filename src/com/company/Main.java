package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Array;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main {

    //Function to generate array of random integers
    public static int[] generateArray(int length, int min, int max) {
        Random rand = new Random();
        int[] array = new int[length];
        for (int x = 0; x < length; x++) {
            array[x] = rand.nextInt(max - min) + min;
        }
        return array;
    }


    //Recursive merge sort function
    //Also removes duplicate elements
    public static int[] mergeSort(int[] arrayToSort, int low, int high) {
        //Calculating "mid" value to split array
        int mid = (int) Math.ceil((high - low) / 2) + low;
        int lowLength = 0;
        int highLength = 0;
        int totalLength = 0;
        int[] sortedArray = new int[1];
        int duplicate;
        //If there's only 2 elements in the array
        if (high - low == 2) {
            //Put them in the appropriate order
            //Case if the elements are the same, return only one
            if (arrayToSort[low] == arrayToSort[low + 1]) {
                sortedArray = new int[1];
                sortedArray[0] = arrayToSort[low];
            } else if (arrayToSort[low] > arrayToSort[low + 1]) {
                sortedArray = new int[2];
                sortedArray[0] = arrayToSort[high - 1];
                sortedArray[1] = arrayToSort[low];
            } else {
                sortedArray = new int[2];
                sortedArray[0] = arrayToSort[low];
                sortedArray[1] = arrayToSort[high - 1];
            }
            //If there's only one element, simply return it
        } else if (high - low == 1) {
            sortedArray[0] = arrayToSort[low];
            //If there's more than two elements
        } else if (high - low > 2) {
            //Recursively sort each half
            int[] lowArray = mergeSort(arrayToSort, low, mid);
            int[] highArray = mergeSort(arrayToSort, mid, high);
            int lowIndex = 0;
            int highIndex = 0;

            //Get new lengths in case they "shrunk" because of duplicate elements
            lowLength = lowArray.length;
            highLength = highArray.length;
            totalLength = lowLength + highLength;
            sortedArray = new int[totalLength];

            //And then merge
            for (int x = 0; x < totalLength; x++) {
                //If low array has been looped through up to mid
                if (lowIndex >= lowLength && highIndex < highLength) {
                    //Then loop through high array, adding elements to final list
                    sortedArray[x] = highArray[highIndex];
                    highIndex++;
                    //If high array has been looped through up to high
                } else if (highIndex >= highLength && lowIndex < lowLength) {
                    //Then loop through low array, adding elements to final list
                    sortedArray[x] = lowArray[lowIndex];
                    lowIndex++;
                    //Otherwise, if lowArray value is less than highArray value
                    //place low array value in sorted array
                } else if (lowArray[lowIndex] < highArray[highIndex]) {
                    sortedArray[x] = lowArray[lowIndex];
                    lowIndex++;
                    //If the elements are the same, put the lowIndex element in the array
                    //And loop up through both arrays until we find an element that is not a duplicate
                    //This ensures that we remove numbers that have two or more duplicates
                } else if (lowArray[lowIndex] == highArray[highIndex]) {
                    sortedArray[x] = lowArray[lowIndex];
                    duplicate = lowArray[lowIndex];
                    lowIndex++;
                    while (lowIndex < lowLength && lowArray[lowIndex] == duplicate) {
                        lowIndex++;
                        totalLength--;
                    }
                    while (highIndex < highLength && highArray[highIndex] == duplicate) {
                        highIndex++;
                        totalLength--;
                    }
                    //Or place high value in sorted array
                } else {
                    sortedArray[x] = highArray[highIndex];
                    highIndex++;
                }
            }
        }
        //Creating new output array if sorted array ended up with less elements because of removing duplicate
        //while merging
        if (totalLength != lowLength + highLength) {
            int[] outputArray = new int[totalLength];
            for (int x = 0; x < totalLength; x++) {
                outputArray[x] = sortedArray[x];
            }
            return outputArray;
        }
        return sortedArray;
    }

    ///My three sum algorithm
    //Returns an array of triplets that sum to 0
    public static ArrayList<Integer[]> myThreeSum(int[] arrayToSort, int arrayLength) {
        int[] array = mergeSort(arrayToSort, 0, arrayLength);
        int newLength = array.length;
        int midIndex = 0;
        int posLow, posHigh, negLow, negHigh, highChecked, lowChecked;
        ArrayList<Integer[]> tripletList = new ArrayList<Integer[]>();

        //Finding element at which array switches from negative to positive elements
        //0 is counted as a negative element
        for (int x = 0; x < newLength; x++) {
            if (array[x] > 0) {
                midIndex = x;
                break;
            }
        }

        highChecked = newLength - 1;
        //Looking for a one negative number and two positive solution
        //Taking largest negative first
        for (int neg = 0; neg < midIndex; neg++) {
            //Resetting positive low and high value
            posLow = midIndex;
            posHigh = highChecked;
            //Looping down from highest element to find highest element smaller than absolute value of current negative
            while (posHigh > midIndex + 1 && array[posHigh] + array[neg] > 0) {
                posHigh--;
            }

            //Setting highChecked to highest positive element that is smaller than current negative for next round
            //of negative elements
            highChecked = posHigh;

            //Finding matching elements by looping down and up through positive elements
            while (posHigh > posLow) {
                //If total equals 0, return
                if (array[posHigh] + array[posLow] + array[neg] == 0) {
                    tripletList.add(new Integer[]{array[neg], array[posLow], array[posHigh]});
                    posHigh--;
                    posLow++;
                    //If total is greater than 0, decrement high positive index
                } else if (array[posHigh] + array[posLow] + array[neg] > 0) {
                    posHigh--;
                    //If total is less than 0, increment low positive index
                } else {
                    posLow++;
                }
            }
        }

        lowChecked = 0;

        //Doing same thing for one positive and two negative number solution
        for (int pos = newLength - 1; pos > midIndex - 1; pos--) {
            negLow = midIndex - 1;
            negHigh = lowChecked;
            //Looping "up" through negative elements to find largest negative element whose absolute value is smaller
            //than value of current positive
            while (negHigh < midIndex - 2 && array[negHigh] + array[pos] < 0) {
                negHigh++;
            }

            //Setting lowChecked to largest negative element that is smaller than the current positive for next round
            //of positive elements
            lowChecked = negHigh;

            //Finding matching elements by looping down and up through positive elements
            while (negHigh < negLow) {
                //If total equals 0, return
                if (array[negHigh] + array[negLow] + array[pos] == 0) {
                    tripletList.add(new Integer[]{array[negHigh], array[negLow], array[pos]});
                    negHigh++;
                    negLow--;
                    //If total is less than 0, increment "high" negative index
                    //Because we "need" less negative
                } else if (array[negHigh] + array[negLow] + array[pos] < 0) {
                    negHigh++;
                    //If total is greater than 0, decrement "low" negative index
                } else {
                    negLow--;
                }
            }
        }
        return tripletList;
    }

    //Brute force three sum algorithm
    //Returns list of triplets whose sum equals 0
    public static ArrayList<Integer[]> bruteThreeSum(int[] arrayToSort, int arrayLength) {
        //Removing duplicate values
        int[] array = mergeSort(arrayToSort, 0, arrayLength);
        int newLength = array.length;
        ArrayList<Integer[]> tripletList = new ArrayList<>();

        //Iterating through each element 0..length-2
        for (int i = 0; i < newLength - 2; i++) {
            //Iterating through each element i+1..length-1
            for (int j = i + 1; j < newLength - 1; j++) {
                //Iterating through each element j+1..length-1
                for (int k = j + 1; k < newLength; k++) {
                    //Checking if it equals 0
                    if ((array[i] + array[j] + array[k]) == 0) {
                        tripletList.add(new Integer[]{array[i], array[j], array[k]});
                    }
                }
            }
        }
        return tripletList;
    }

    //Binary search - splits array into sub-arrays and searches those arrays for the searchValue
    public static int binarySearch(int[] list, int searchValue) {
        int low = 0;
        int mid;
        int high = list.length - 1;

        while (low <= high) {
            mid = low + (high - low) / 2;
            if (searchValue == list[mid]) {
                return mid;
            } else if (searchValue > list[mid]) {
                low = mid + 1;
            } else if (searchValue < list[mid]) {
                high = mid - 1;
            }
        }
        return -1;
    }

    //Heavily based on book example on page 190
    //Used my own binary search (above)
    //Returns a list of triplets that sum to 0
    public static ArrayList<Integer[]> fasterThreeSum(int[] arrayToSort, int arrayLength) {
        int[] array = mergeSort(arrayToSort, 0, arrayLength);
        int newLength = array.length;
        int searchResult;
        ArrayList<Integer[]> tripletList = new ArrayList<>();

        //Working up the list, comparing two elements and searching to find the third
        for (int x = 0; x < newLength; x++) {
            for (int y = x + 1; y < newLength; y++) {
                searchResult = binarySearch(array, -(array[x] + array[y]));
                if (searchResult != -1 && searchResult > y) {
                    tripletList.add(new Integer[]{array[x], array[y], array[searchResult]});
                }
            }
        }
        return tripletList;
    }

    //Fastest three sum
    public static ArrayList<Integer[]> fastestThreeSum(int[] arrayToSort, int arrayLength) {
        int[] array = mergeSort(arrayToSort, 0, arrayLength);
        int newLength = array.length;
        int low, high;
        ArrayList<Integer[]> tripletList = new ArrayList<>();

        //Iterating up through list, selecting elements
        for (int x = 0; x < newLength - 2; x++) {
            low = x + 1;
            high = newLength - 1;
            //Working down from up and up from low simultaneously
            //To converge on a result that sums to 0
            while (low < high) {
                //If elements sum to 0
                if (array[x] + array[low] + array[high] == 0) {
                    tripletList.add(new Integer[]{array[x], array[low], array[high]});
                    low++;
                    high--;
                    //If sum is less than 0, increment low
                } else if (array[x] + array[low] + array[high] < 0) {
                    low++;
                    //If sum is greater than 0, decrement high
                } else {
                    high--;
                }
            }
        }
        return tripletList;
    }

    //Function to retrieve CPU time
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    //Tests all three sum functions
    public static boolean verificationTests() {
        int[] testArray;
        ArrayList<Integer[]> bruteResult, myResult, fasterResult, fastestResult;
        for (int N = 4; N < 10000; N = N * 2) {
            System.out.printf("Testing for array of length %d\n", N);
            //Generates arrays of triplets for same list
            testArray = generateArray(N, -(N * 100), N * 100);
            bruteResult = bruteThreeSum(testArray, N);
            myResult = myThreeSum(testArray, N);
            fasterResult = fasterThreeSum(testArray, N);
            fastestResult = fastestThreeSum(testArray, N);
            //Printing array and lists of triplets for smaller lists for visual verification
            if (N < 100) {
                Arrays.sort(testArray);
                for (int x = 0; x < N; x++) {
                    System.out.printf("%d ", testArray[x]);
                }

                System.out.printf("\n");
                if (bruteResult.size() != 0) {
                    System.out.printf("Brute force triplets: ");
                    for (int x = 0; x < bruteResult.size(); x++) {
                        for (int y = 0; y < 3; y++) {
                            System.out.printf("%d ", bruteResult.get(x)[y]);
                        }
                        System.out.print("\t");
                    }
                    System.out.println();
                }

                if (fasterResult.size() != 0) {
                    System.out.printf("Faster triplets: ");
                    for (int x = 0; x < fasterResult.size(); x++) {
                        for (int y = 0; y < 3; y++) {
                            System.out.printf("%d ", fasterResult.get(x)[y]);
                        }
                        System.out.print("\t");
                    }
                    System.out.println();
                }

                if (fastestResult.size() != 0) {
                    System.out.printf("Fastest triplets: ");
                    for (int x = 0; x < fastestResult.size(); x++) {
                        for (int y = 0; y < 3; y++) {
                            System.out.printf("%d ", fastestResult.get(x)[y]);
                        }
                        System.out.print("\t");
                    }
                    System.out.println();
                }

                if (myResult.size() != 0) {
                    System.out.printf("My triplets: ");
                    for (int x = 0; x < myResult.size(); x++) {
                        for (int y = 0; y < 3; y++) {
                            System.out.printf("%d ", myResult.get(x)[y]);
                        }
                        System.out.print("\t");
                    }
                    System.out.println();
                }
            }
            //Comparing length of triplet lists from all algorithms
            if (bruteResult.size() == fasterResult.size() && fasterResult.size() == myResult.size() && fasterResult.size() == fastestResult.size()) {
                System.out.printf("Results match - all returned %d triplets - " +
                        "tests passed!\n", bruteResult.size());
                bruteResult.removeAll(bruteResult);
                myResult.removeAll(myResult);
                fasterResult.removeAll(fasterResult);
                fastestResult.removeAll(fastestResult);
            } else {
                System.out.printf("Three sum results don't match for array of size %d\n ", N);
                return false;
            }
        }
        return true;
    }

    //Calculates log base 2 of N
    public static float logBaseTwo(int N) {
        float result = ((float) Math.log(N) / (float) Math.log(2));
        return result;
    }

    //This functions runs tests on all algorithms
    public static void runTimeTests() {
        int[] testArray = new int[1];
        long beforeTime, afterTime, totalTime;
        long brutePrevTime = 1;
        long myPrevTime = 1;
        long fasterPrevTime = 1;
        long fastestPrevTime = 1;
        int range;

        //Printing first row of table
        System.out.printf("%31s %47s %47s %47s\n", "Brute 3sum", "Faster 3sum", "Fastest 3sum", "My 3sum");
        System.out.printf("%15s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s\n",
                "N", "Time", "Doubling", "Expected", "Time", "Doubling", "Expected", "Time", "Doubling", "Expected",
                "Time", "Doubling", "Expected");
        System.out.printf("%47s %15s %31s %15s %31s %15s %31s %15s\n", "Ratios", "Doubling", "Ratios", "Doubling",
                "Ratios", "Doubling", "Ratios", "Doubling");
        System.out.printf("%63s %47s %47s %47s\n", "Ratios", "Ratios", "Ratios", "Ratios");
        for (int N = 4; N < 100000; N = N * 2) {
            System.out.printf("%16s", N);
            //Checking to make sure range for arrays is within integer constraints
            if (N * 10 > Integer.MAX_VALUE) {
                range = Integer.MAX_VALUE;
            } else {
                range = N * 100;
            }

            //I chose to run brute force less often, because it took so much time
            if (N < 10000) {
                //Running brute force algorithm
                testArray = generateArray(N, -(range), range);
                beforeTime = getCpuTime();
                bruteThreeSum(testArray, N);
                afterTime = getCpuTime();
                totalTime = afterTime - beforeTime;
                System.out.printf("%15d ", totalTime);
                if (N != 4) {
                    System.out.printf("%15.3f %15d ", (float) totalTime / brutePrevTime, (int) (Math.pow(N, 3) / (Math.pow((N / 2), 3))));
                } else {
                    System.out.printf("%15s %15s ", "na", "na");
                }
                brutePrevTime = totalTime;
            } else {
                System.out.printf("%15s %15s %15s ", "na", "na", "na");
            }

            //Running faster3sum algorithm
            testArray = generateArray(N, -(range), range);
            beforeTime = getCpuTime();
            fasterThreeSum(testArray, N);
            afterTime = getCpuTime();
            totalTime = afterTime - beforeTime;
            System.out.printf("%16d", totalTime);
            if (N != 4) {
                System.out.printf("%15.3f %15.3f ", (float) totalTime / fasterPrevTime,
                        (float) ((logBaseTwo(N) * Math.pow(N, 2)) / (logBaseTwo(N / 2) * Math.pow((N / 2), 2))));
            } else {
                System.out.printf("%15s %15s ", "na", "na");
            }
            fasterPrevTime = totalTime;

            //Running fastest3sum algorithm
            testArray = generateArray(N, -(range), range);
            beforeTime = getCpuTime();
            fastestThreeSum(testArray, N);
            afterTime = getCpuTime();
            totalTime = afterTime - beforeTime;
            System.out.printf("%16d", totalTime);
            if (N != 4) {
                System.out.printf("%15.3f %15d ", (float) totalTime / fastestPrevTime, (int) (Math.pow(N, 2) / (Math.pow((N / 2), 2))));
            } else {
                System.out.printf("%15s %15s ", "na", "na");
            }
            fastestPrevTime = totalTime;

            //Running my3sum algorithm
            testArray = generateArray(N, -(range), range);
            beforeTime = getCpuTime();
            myThreeSum(testArray, N);
            afterTime = getCpuTime();
            totalTime = afterTime - beforeTime;
            System.out.printf("%16d", totalTime);
            if (N != 4) {
                System.out.printf("%15.3f %15d ", (float) totalTime / myPrevTime, (int) (Math.pow(N, 2) / (Math.pow((N / 2), 2))));
            } else {
                System.out.printf("%15s %15s ", "na", "na");
            }
            myPrevTime = totalTime;

            System.out.printf("\n");
        }
    }

    public static void main(String[] args) {
        if (verificationTests()) {
            System.out.printf("Tests passed! \n");
            runTimeTests();
        } else {
            System.out.printf("Tests failed! \n");
        }
    }
}
