 /***************************************************************************
 *	FILE: DistroCentre.java												   
 *	AUTHOR: Connor Beardsmore - 15504319								  
 *	UNIT: DSA120 Assignment S2- 2015 														   
 *	PURPOSE: Container Class For a DC and it's stock rooms
 *	LAST MOD: 23/10/15	
 *  REQUIRES: NONE					   
 ***************************************************************************/
package connorLib;

public class DistroCentre
{
	//TYPEREF CONSTANTS
	//MAKES IT EASY TO CHECK WHAT TYPE A STOCKROOM IS
	public static final int DEADEND = 0;
	public static final int ROLLING = 1;
	public static final int YARD = 2;

	//CLASSFIELDS
	private IStockRoom[] stockRooms;
	private Carton[] indexArray;
	private int[] typeRef;
	private int count;

//---------------------------------------------------------------------------
	//ALTERNATE Constructor
	//IMPORT: numRooms (int)

	public DistroCentre(int numRooms)
	{
		stockRooms = new IStockRoom[numRooms];
		//Since array is 0-indexed, we increment before creation.
		//indexArray[0] will always be empty
		indexArray = new Carton[Carton.MAX_CON_NOTE + 1];
		typeRef = new int[numRooms];
		count = 0;
	}
//---------------------------------------------------------------------------
	//ACCESSOR getCount
	//EXPORT: count (int)

	public int getCount()
	{
		return count;
	}
//---------------------------------------------------------------------------
	//ACCESSOR copyIndexArray
	//EXPORT: Copy of index array (Carton)
	//PURPOSE: Returns a copy of the index array, if modification is required

	public Carton[] copyIndexArray()
	{
		return indexArray.clone();
	}	
//---------------------------------------------------------------------------
	//ACCESSOR getCartonIndex
	//IMPORT: index (int)
	//PURPOSE: O(1) access to and Carton given its index, will return null
	//	       if no Carton of that index exists

	public Carton getCartonIndex(int index)
	{
		return indexArray[index];
	}
//---------------------------------------------------------------------------
	//ACCESSOR setCartonIndex
	//IMPORT: inCart (Carton)
	//PURPOSE: O(1) access to place a Carton into the indexArray

	public void setCartonIndex(Carton inCart)
	{
		indexArray[ inCart.getNote() ] = inCart;
	}	
//---------------------------------------------------------------------------
	public void nullCartonIndex(int index)
	{
		indexArray[index] = null;
	}
//---------------------------------------------------------------------------	
	//ACCESSOR getStockRoom
	//IMPORT: index (int)
	//EXPORT: stockRoom (IStockRoom)

	public IStockRoom getStockRoom(int index)
	{
		return stockRooms[index];
	}	
//---------------------------------------------------------------------------
	//ACCESSOR getType
	//IMPORT: index (int)
	//EXPORT: typeRef (int)
	//PURPOSE: Get type of a specific stockroom in DC, via type reference array

	public int getType(int index)
	{
		return typeRef[index];
	}	
//---------------------------------------------------------------------------
	//ACCESSOR totalItems
	//EXPORT: totalItems (int)
	//PURPOSE: Calculate total items across entire DC

	public int totalItems()
	{
		int totalItems = 0;
		for (int ii = 0; ii < count; ii++)
		{
			totalItems += stockRooms[ii].getCount();
		}	
		return totalItems;
	}	
//---------------------------------------------------------------------------
	//ACCESSOR totalCapacity
	//EXPORT: totalCapacity (int)
	//PURPOSE: Calculate total capacity across entire DC

	public int totalCapacity()
	{
		int totalCapacity = 0;
		for (int ii = 0; ii < count; ii++)
		{
			totalCapacity += stockRooms[ii].getCapacity();
		}	
		return totalCapacity;
	}
//---------------------------------------------------------------------------	
	//ACCESSOR isFull
	//EXPORT: full (boolean)
	//PURPOSE: Returns true if DC has no free slots, false otherwise

	public boolean isFull()
	{
		boolean full = true;
		for (int ii = 0; ii < count; ii++)
		{
			//Break or return would be more effiecient here
			full = ( full && stockRooms[ii].isFull() );
		}
		return full;
	}
//---------------------------------------------------------------------------	
	//createDeadEnd
	//IMPORT: maxCap (int)
	//PURPOSE: Create a new DeadEnd stockroom in the DC based on maxCap

	public void createDeadEnd(int maxCap)
	{
		stockRooms[count] = new DeadEnd(maxCap);
		typeRef[count] = DEADEND;
		count++;
	}
//---------------------------------------------------------------------------
	//createRolling
	//IMPORT: maxCap (int)
	//PURPOSE: Create a new Rolling stockroom in the DC based on maxCap

	public void createRolling(int maxCap)
	{
		stockRooms[count] = new Rolling(maxCap);
		typeRef[count] = ROLLING;		
		count++;
	}
//---------------------------------------------------------------------------
	//createYard
	//IMPORT: maxCap (int)
	//PURPOSE: Create a new Yard stockroom in the DC based on maxCap

	public void createYard(int maxCap)
	{
		stockRooms[count] = new Yard(maxCap);
		typeRef[count] = YARD;		
		count++;
	}		
//---------------------------------------------------------------------------
	//createOutString
	//EXPORT: outputString (String)
	//PURPOSE: Create String representation of entire DC

	public String createOutString()
	{
		String outputString = "";

		for (int ii = 0; ii < count; ii ++)
		{
			outputString += stockRooms[ii].toString() + "\n";
		}	
		//Formatting to split DC section and Carton section
		outputString += "\n%\n\n";

		for (int ii = 0; ii < count; ii ++)
		{
			outputString += stockRooms[ii].contentString();
		}
		
		return ( outputString + "\n" );			
	}
//---------------------------------------------------------------------------
}