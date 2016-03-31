 /***************************************************************************
 *	FILE: DeadEnd.java												   
 *	AUTHOR: Connor Beardsmore - 15504319								  
 *	UNIT: DSA120 Assignment S2- 2015 													   
 *	PURPOSE: Dead-End StockRoom for use in the DC ( stack )
 *	LAST MOD: 19/10/15	
 *  REQUIRES: NONE	  							   
 ***************************************************************************/
package connorLib;

public class DeadEnd implements IStockRoom
{
	//CLASS FIELDS
	private Carton[] stack;
	private int count;	

	//CLASS CONSTANTS
	private static final int MAX_VALID_CAP = 10000;
	private static final int MIN_VALID_CAP = 1;
	private static final String DEADEND = "D";
//--------------------------------------------------------------------------
	//ALTERNATE Constructor
	//IMPORT: maxCapacity (int)
	//ASSERTION: stack allocated 'maxCapacity' elements. Count to default 0

	public DeadEnd(int maxCapacity)
	{
		//maxCapacity must be a value between 2 and 10,000
		if ( (maxCapacity < MIN_VALID_CAP) || (maxCapacity > MAX_VALID_CAP) )
		{	
			throw new IllegalArgumentException("DeadEnd Capacity Not Valid");
		}	
		stack = new Carton[maxCapacity];
		count = 0;
	}
//--------------------------------------------------------------------------
	//ACCESSOR getCount
	//EXPORT: count (int)

	public int getCount()
	{
		return count;
	}
//--------------------------------------------------------------------------
	//ACCESSOR getCapacity
	//EXPORT: array length (int)

	public int getCapacity()
	{
		return stack.length;
	}
//--------------------------------------------------------------------------	
	//ACCESSOR isEmpty
	//EXPORT: empty (boolean)

	public boolean isEmpty()
	{
		return ( count == 0 );
	}
//--------------------------------------------------------------------------
	//ACCESSOR isFull
	//EXPORT: full (boolean)

	public boolean isFull()
	{
		//Length stored in stack itself
		return ( count == stack.length );
	}
//--------------------------------------------------------------------------
	//MUTATOR addCarton
	//IMPORT: inCart (Carton)
	//PURPOSE: Add new value to the top of the stack

	public void addCarton(Carton inCart)
	{	
		//Can't add anymore values if stack is full. Must remove first
		if ( isFull() )
		{	
			throw new IllegalStateException("DeadEnd Is Full. Cannot Add");
		}	
		//Add to stack, increment counter
		stack[count] = inCart;
		stack[count].setRIndex(count);
		count++;	
	}
//--------------------------------------------------------------------------
	//MUTATOR removeCarton
	//EXPORT: outCart (Carton)
	//PURPOSE: Remove top value from the stack

	public Carton removeCarton()
	{	
		Carton outCart = top();
		stack[count - 1] = null;
		//Indexes set back to default state. Doesn't exist in DC
		outCart.setDIndex(-1);
		outCart.setRIndex(-1);
		count--;
		return outCart;
	}
//--------------------------------------------------------------------------
	//ACCESSOR top
	//IMPORT: value (Carton)
	//PURPOSE: View top value on the stack. Not removed

	public Carton top()
	{
		if ( isEmpty() )
		{	
			throw new IllegalStateException("DeadEnd Is Empty. No Top");
		}	
		//Top Element
		return stack[count-1];			
	}
//--------------------------------------------------------------------------
	//ACCESSOR toString
	//EXPORT: stateString (String)
	//PURPOSE: Prints out room Carton's in  DC Geometry file format

	public String toString()
	{
		String stateString = DEADEND;
		for (int i = 0; i < stack.length; i++)
		{	
			//Accounts for empty slots via ":" print outside 
			stateString += ":";
			if ( stack[i] != null )
			{	
				stateString += stack[i].getNote();
			}			
		}
		return stateString;
	}	
//--------------------------------------------------------------------------
	//ACCESSOR contentString
	//EXPORT: stateString (String)
	//PURPOSE: Output All Carton Contents in Stack As a String

	public String contentString()
	{
		String stateString = "";
		for ( int ii = 0; ii < count; ii++ )
		{
			stateString += stack[ii].toString() + "\n";
		}	
		return stateString;
	}
//--------------------------------------------------------------------------		
}