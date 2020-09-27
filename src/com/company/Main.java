package com.company;

import java.util.Arrays;

public class Main {

    public static boolean myThreeSum(int[] array, int arrayLength){
        Arrays.sort(array);
        int midIndex = 0;
        int posLow, negLow, highChecked, lowChecked;
        for(int x = 0; x < arrayLength; x++){
            if(array[x] > 0){
                midIndex = x;
                break;
            }
        }

        for(int neg = 0; neg < midIndex; neg++){
            posLow = midIndex;
            for(int posHigh = arrayLength-1; posHigh > posLow; posHigh--){
                    while (posLow < posHigh && (array[neg] + array[posHigh] + array[posLow] < 0)) {
                        posLow++;
                    }
                    if ((array[neg] + array[posHigh] + array[posLow] == 0) && array[posHigh] != array[posHigh]) {
                        System.out.printf("Match found at indices %d, %d and %d\n", posHigh, posLow, neg);
                        return true;
                    }
            }
        }


        for(int pos = midIndex; pos < arrayLength; pos++){
            negLow = midIndex-1;
            for(int negHigh = 0; negHigh < negLow; negHigh++){
                while(negLow > negHigh && (array[pos] + array[negHigh] + array[negLow]) > 0){
                    negLow--;
                }
                if ((array[pos] + array[negHigh] + array[negLow] == 0) && array[negHigh] != array[negLow]) {
                    System.out.printf("Match found at indices %d, %d and %d\n", negHigh, negLow, pos);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean bruteThreeSum(int[] array, int arrayLength){
        for(int i=0; i<arrayLength-2; i++){
            for(int j = i+1; j<arrayLength-1; j++){
                for(int k = j+1; k<arrayLength; k++){
                    if((array[i]+array[j]+array[k]) == 0 && array[i] != array[j] && array[j] != array[k] && array[i] != array[k]){
                        System.out.printf("Match found at indices %d, %d and %d\n", i, j, k);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
	// write your code here
        int[] array = {-19, -14, -7, -3, 4, 6, 1, 19};
        if(bruteThreeSum(array, 8)){
            System.out.printf("Match found!\n");
        }
    }
}
