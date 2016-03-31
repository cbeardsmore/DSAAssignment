 /***************************************************************************
 *  FILE: Sorts.java                                                    
 *  AUTHOR: Connor Beardsmore - 15504319                                  
 *  UNIT: DSA120 Assignment S2- 2015                                                          
 *  PURPOSE: Implementation of The 5 Basic Sorting Algorithms
 *  LAST MOD: 05/10/15  
 *  REQUIRES: NONE                            
 ***************************************************************************/
package connorLib;

public class Sorts
{
    //bubbleSort
    //IMPORT: array (int[])
    //PURPOSE: Implements Bubble Sort Sorting Algorithm

    public static void bubbleSort(ISortable[] array)
    {
        int pass = 0;
        ISortable[] temp;
        boolean sorted;
        
        do
        {
            //We assume its sorted, find out during loops            
            sorted = true;
            //Up to -pass Optimization Applied. Don't check final element
            for ( int ii = 0; ii < ( array.length -1 - pass ); ii++ )
            {
                if ( array[ii+1].lessThan( array[ii] ) )
                {
                    //Swap 2 elements out of order
                    swap( array, ii, ii + 1 );

                    //Still need to continue sorting
                    sorted = false;
                } 
            }    
            //Each for loop is one pass of the array
            pass += 1;

        //Stop sorting when sorted, ie. No Swaps Occur Optimization
        } while ( !sorted );
    }
//----------------------------------------------------------------------------------------------------
    //selectionSort
    //IMPORT: array (int[])
    //PURPOSE: Implements Selection Sort Sorting Algorithm

    public static void selectionSort(ISortable[] array)
    {
        int minIndex;
        int temp;

        for ( int pass = 0; pass < array.length; pass++ )
        {
            minIndex = pass;
            //Ignore initial element, Already set to smallest
            for ( int jj = pass + 1; jj < array.length; jj++ )
            {
                //Update Newly Found Minimum Index
                if ( array[jj].lessThan( array[minIndex] ) )
                {
                    minIndex = jj;
                }    
            }   

            //Do the actual swap
            swap( array, minIndex, pass );
        }    
    }
//----------------------------------------------------------------------------------------------------
    //insertionSort
    //IMPORT: array (int[])
    //PURPOSE: Implements Insertion Sort Sorting Algorithm

    public static void insertionSort(ISortable[] array)
    {
        int ii = 0;
        ISortable temp;
        //Start Inserting at Element 1. 0 is already sorted
        for ( int pass = 1; pass < array.length; pass++ )
        {
            //Start from last item and go backwards
            ii = pass;
            temp = array[ii];

            //Insert into sub-array to left of pass. 
            //Use > To keep the sort stable
            while ( (ii > 0) && (temp.lessThan( array[ii - 1]) ) )
            {
                //Shuffle until correct location
                array[ii] = array[ii - 1];
                ii--;
            }    

            array[ii] = temp;
        }    
    }
//----------------------------------------------------------------------------------------------------
    //mergeSortRecurse
    //IMPORT: array (int[])
    //PURPOSE: Kicks off mergeSortRecurve from given array. kick-starts

    public static void mergeSort(ISortable[] array)
    {
        mergeSortRecurse( array, 0, array.length -1  );
    }
//----------------------------------------------------------------------------------------------------
    //mergeSortRecurse
    //IMPORT: array (int[]), leftIndex (int), rightIndex (int)
    //PURPOSE: Performs the merge sort recursive calls

    private static void mergeSortRecurse(ISortable[] array, int leftIndex, int rightIndex)
    {
        int midIndex;
        if ( leftIndex < rightIndex )
        {
            midIndex = ( leftIndex + rightIndex ) / 2;

            //Recursively: Sort Left and Right sides of sub-arrays
            mergeSortRecurse(array, leftIndex, midIndex);
            mergeSortRecurse(array, midIndex + 1, rightIndex);

            //Merge left and right sub-arrays
            merge(array, leftIndex, midIndex, rightIndex);
        }    
    }
//----------------------------------------------------------------------------------------------------
    //merge
    //IMPORT: array (int[]), leftIndex (int), midIndex (int) rightIndex (int)
    //PURPOSE: Merge sub-arrays back together

    private static void merge(ISortable[] array, int leftIndex, int midIndex, int rightIndex)
    {
        ISortable[] tempArray = new ISortable[rightIndex - leftIndex + 1];
        //Index for front of left sub-array
        int ii = leftIndex;
        //Index for front of right sub-array
        int jj = midIndex + 1;
        //Index for next free element in tempArray
        int kk = 0;

        while ( (ii <= midIndex) && (jj <= rightIndex) )
        {
            //Take from left sub-array
            if ( ( array[ii].lessThan(array[jj]) ) || ( array[ii].equals(array[jj]) ) )
            {
                tempArray[kk] = array[ii];
                ii++;
            } 
            //Take from right sub-array
            else
            {
                tempArray[kk] = array[jj];
                jj++;
            }  
            kk++;   
        }    

        //Flush Remainder from left sub-array (midIndex Inclusively)
        for ( ii = ii; ii <= midIndex; ii++ )
        {
            tempArray[kk] = array[ii];
            kk++;
        }    
        //Flush Remainder from right sub-array (rightIndex Inclusively)
        for ( jj = jj; jj <= rightIndex; jj++ )
        {
            tempArray[kk] = array[jj];
            kk++;
        }

        //Copy sorted tempArray back to actual array
        for ( kk = leftIndex; kk <= rightIndex; kk++ )
        {
            //kk-leftIndex To align indexing to zero
            array[kk] = tempArray[kk - leftIndex];
        }    
    }
//----------------------------------------------------------------------------------------------------
    //quickSort
    //IMPORT: array (int[])
    //PURPOSE: Front end to kick off quick sort

    public static void quickSort(ISortable[] array)
    {
        quickSortRecurse( array, 0, array.length - 1 );
    }
//----------------------------------------------------------------------------------------------------    
    //quickSortRecurse
    //IMPORT: array (int[]), leftIndex (int), rightIndex (int)
    //PURPOSE: Performs quick sort recursive calls

    private static void quickSortRecurse(ISortable[] array, int leftIndex, int rightIndex)
    {
        int pivot, newPivot;

        //Check that array is bigger than size one
        if ( rightIndex > leftIndex )
        {
            //Pivot Selection Strategy - Use Middle For Simplicity 
            pivot = ( leftIndex + rightIndex ) / 2;
            newPivot = doPartitioning( array, leftIndex, rightIndex, pivot );

            //Recursively Sort Left Paritition, and Right Partition
            quickSortRecurse( array, leftIndex, newPivot - 1 );
            quickSortRecurse( array, newPivot + 1, rightIndex );
        }    
    }
//----------------------------------------------------------------------------------------------------
    //doPartitioning
    //IMPORT: array (int[]), leftIndex (int), rightIndex (int), pivot (int)
    //EXPORT: newPivot (int)
    //PURPOSE: Does The Actual Partioning based on the pivots. calcs new pivot.

    private static int doPartitioning(ISortable[] array, int leftIndex, int rightIndex, int pivot)
    {
        int curIndex, temp, newPivot;
        ISortable pivotVal;

        //Swap pivotVal with right most element
        pivotVal = array[pivot];
        array[pivot] = array[rightIndex];
        array[rightIndex] = pivotVal;

        //Find all values smaller than pivot, transfer to left side of array
        curIndex = leftIndex;

        for ( int ii = leftIndex; ii < rightIndex; ii++ )
        {
            //Find next value to go on left side
            if ( array[ii].lessThan(pivotVal) )
            {
                //Push value onto left side of pivot
                swap( array, ii, curIndex );
                curIndex++;  
            }    
        }   

        //Put pivot in its rightful place
        newPivot = curIndex;
        array[rightIndex] = array[newPivot];
        array[newPivot] = pivotVal;

        return newPivot;
    }
//----------------------------------------------------------------------------------------------------
    //swap
    //IMPORT: array (int[]), source, dest
    //PURPOSE: Swaps 2 items in an array, using temp variable
    //ASSERTION: order of source/dest unimportant, same either way

    private static void swap(ISortable[] array, int source, int dest)
    {
        ISortable temp = array[dest];
        array[dest] = array[source];
        array[source] = temp;
    }

}
