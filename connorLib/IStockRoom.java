 /***************************************************************************
 *  FILE: IStockRoom.java                                                    
 *  AUTHOR: Connor Beardsmore - 15504319                                  
 *  UNIT: DSA120 Assignment S2- 2015 	                                                       
 *  PURPOSE: StockRoom interface, implemented by D/R/Y stockrooms
 *  LAST MOD: 19/10/15  
 *  REQUIRES: NONE                        
 ***************************************************************************/
package connorLib;

public interface IStockRoom
{
    //BASIC ADD/REMOVE FUNCTIONALITY
    void addCarton(Carton inCart);
    Carton removeCarton();

    //STATUS INDICATORS
    int getCount();	
    int getCapacity();
    boolean isFull();
    boolean isEmpty();

    //IO FUNCTIONS
    String toString();
    String contentString();
}
//---------------------------------------------------------------------------