 /***************************************************************************
 *	FILE: TaskFunctions.java										   
 *	AUTHOR: Connor Beardsmore - 15504319								  
 *	UNIT: DSA120 Assignment S2- 2015 													   
 *	PURPOSE: Handles and executes tasks on DC given a Taskline 
 *	LAST MOD: 26/10/15	
 *  REQUIRES: java.text.DateFormat, java.text.SimpleDateFormat
 *            java.util.Date, java.util.Iterator, connorLib			   
 ***************************************************************************/
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import connorLib.*;

public class TaskFunctions
{
	//ALGORITHM CONSTANTS
	private final static int MONTH_URGENCY = 6;
	private final static int PREFERENCES = 3;

	//SEARCH CONSTANTS
	private final static int  NOTE = 0;
	private final static int  DATE = 1;
	private final static int  PRODUCT = 2;
	private final static int  WHOLESALER = 3;			
//--------------------------------------------------------------------------
	//addTask
	//IMPORT: indexArray (Carton), dc (DistroCentre), cartLine (String), avoid (int)
	//PURPOSE: Places new Carton in most appropriate spot in DC

	public static void addTask(DistroCentre dc, CartonSearcher cs, String cartLine, int avoid)
	{
		Carton item = new Carton(cartLine);
		DateClass warranty = item.getWar();
		DateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
		DateClass curDate = new DateClass( dF.format( new Date() ) );
		int[] prefs = new int[PREFERENCES];

		//Key con note isn't already in the DC
		if ( dc.getCartonIndex( item.getNote() ) != null )
		{	
			throw new IllegalArgumentException("Carton already exists in DC" + dc.getCartonIndex( item.getNote() ));
		}	

		//Cannot add if DC is full
		if ( dc.isFull() )
		{
			System.out.println("FULL");
		}
		else 
		{
			//All Call the same function, order of PREFERENCE is different
			//Firstmost parameters are higher pref, so are checked first
			//Only in worst case (i.e. nearly full dc), does 3rd pref get checked
			if ( warranty.isInfinite() )
			{
				prefs[0] = dc.DEADEND;
				prefs[1] = dc.ROLLING;
				prefs[2] = dc.YARD;
				processAdd(dc, cs, item, prefs, avoid);
			}	
			else if ( warranty.withinMonths(curDate, MONTH_URGENCY) )
			{
				prefs[0] = dc.YARD;
				prefs[1] = dc.ROLLING;
				prefs[2] = dc.DEADEND;				
				processAdd(dc, cs, item, prefs, avoid);				
			}
			else 
			{
				prefs[0] = dc.ROLLING;
				prefs[1] = dc.YARD;
				prefs[2] = dc.DEADEND;				
				processAdd(dc, cs, item, prefs, avoid);				
			}	

			//User output given in specs
			if ( avoid == -1 )
			{	
				System.out.println( item.getDIndex() + ":" + item.getRIndex() );
			}
		}	
	}
//--------------------------------------------------------------------------
	//processAdd
	//IMPORT: dc (DistroCentre), item (Carton), prefs (int[]), avoid (int)
	//PURPOSE: Add's Cartons to first available slot in a room of 
	//		   matching preference. Will never add to stockroom with index
	//		   avoid. avoid of -1 if this field not relevant

	private static void processAdd(DistroCentre dc, CartonSearcher cs,
									Carton item, int[] prefs, int avoid)
	{
		boolean done = false;

		//Iterates over preferences from highest to lowest priority
		for ( int ii = 0; ii < PREFERENCES; ii++ )
		{
			int jj = 0;

			//Iterates over all stockrooms
			while ( ( jj < dc.getCount() ) && ( done == false ) )
			{
				//If stockroom matches current preference
				if ( ( dc.getType(jj) == prefs[ii] ) && ( jj != avoid ) )
				{
					IStockRoom room = dc.getStockRoom(jj);
					//If room isn't fully, add carton to it
					if ( !room.isFull() )
					{
						room.addCarton(item);
						item.setDIndex(jj);
						dc.setCartonIndex(item);
						cs.addSearchEnvironment(item);
						done = true;
						//break; (more efficient to exit loop ASAP after add)
					}	
				}	
				jj++;
			}	
		}	
	}
//--------------------------------------------------------------------------	
	//removeTask
	//IMPORT: dc (DistroCentre), product (String)
	//PURPOSE: Removes matching Carton from most appropriate spot in DC

	public static void removeTask(DistroCentre dc, CartonSearcher cs, String product)
	{
		boolean done = false;
		String searchLine = "::" + product + ":";
		Carton[] matchArray = searchTask(dc, cs, searchLine);
		DSALinkedList dateList = new DSALinkedList();
		int steps, maxShuffles = 0;
		int freeSlots = dc.totalCapacity() - dc.totalItems();

		for ( int ii = 0; ii < matchArray.length; ii++ )
		{
			if ( matchArray[ii].getWar().isInfinite() )
			{
				dateList.insertLast( matchArray[ii] );
			}	
			else
			{
				dateList.insertSorted( matchArray[ii] );
			}

		}


		do 
		{
			Iterator iter = dateList.iterator();
			maxShuffles += 5;
			if ( maxShuffles > freeSlots )
			{
				maxShuffles = freeSlots;
			}	

			while ( (iter.hasNext()) && ( done == false) )
			{
				Carton item = (Carton)iter.next();
				steps = calcSteps(dc, item);

				if ( steps < maxShuffles )
				{
					printSearchResults(item);					
					if ( steps == 0 )
					{
						IStockRoom room = dc.getStockRoom( item.getDIndex() );
						if ( room instanceof Yard )
						{
							((Yard)room).removeCarton( item.getRIndex() );
						}	
						else
						{
							room.removeCarton();
						}	
					}	
					else 
					{
						shuffleCartons(dc, cs, item);
					}
					dc.nullCartonIndex( item.getNote() );				
					cs.removeSearchEnvironment(item);									
					done = true;
				}
			
			}	

		} while ( ( maxShuffles < freeSlots ) && ( done == false ) );

		//Couldnt remove so it must be stuck
		if ( !( dateList.isEmpty() ) && (done == false) )
		{
			System.out.println("STUCK");
		}	
	}
//--------------------------------------------------------------------------
	//calcSteps
	//IMPORT: dc (DistroCentre), item (Carton)
	//EXPORT: steps (int)
	//PURPOSE: Calculates the number of steps to remove an item from a stockroom

	private static int calcSteps(DistroCentre dc, Carton item)
	{
		int steps = 0;
		IStockRoom itemRoom = dc.getStockRoom( item.getDIndex() );
		if ( itemRoom instanceof DeadEnd )
		{
			steps = itemRoom.getCount() - item.getRIndex() - 1;
		}
		else if ( itemRoom instanceof Rolling )
		{
			steps = item.getRIndex();
		}	
		return steps;
	}
//--------------------------------------------------------------------------
	//shuffleCartons
	//IMPORT: dc (DistroCentre), item (Carton)
	//PURPOSE: Shuffles cartons in the DC to allow allow to the required item 

	private static void shuffleCartons(DistroCentre dc, CartonSearcher cs, Carton item)
	{
		String cartLine = null;
		IStockRoom room = dc.getStockRoom( item.getDIndex() );
		Carton removedItem = room.removeCarton();

		while ( removedItem != item )
		{
			dc.nullCartonIndex( removedItem.getNote() );
			cs.removeSearchEnvironment(removedItem);									
			cartLine = removedItem.toString();
			addTask( dc, cs, cartLine, item.getDIndex() );
			removedItem = room.removeCarton();			
		}	
	}
//--------------------------------------------------------------------------	
	//searchTask
	//IMPORT: dc (DistroCentre), cartLine (String)
	//PURPOSE: Finds all instances of matching Carton in DC

	public static Carton[] searchTask(DistroCentre dc, CartonSearcher cs, String cartLine)
	{
		String[] tokens = cartLine.split(":", -1);
		int searchParams = getParamNum(tokens);
		DSALinkedList matches = null;
		Carton[] matchArray = null;

		//Semi validate line before checking
		if ( tokens.length != 4 )
		{
			throw new IllegalArgumentException("Invalid Search Task");
		}	

		//If con note is in search, O(1) straight into index array
		if ( !(tokens[NOTE].equals("")) )
		{
			Carton item = dc.getCartonIndex( Integer.parseInt(tokens[0]));
			if ( item != null )
			{	
				matchArray = new Carton[1];
				matchArray[0] = item;
			}
			else
			{
				matchArray = new Carton[0];
			}	
		}

		//If param is 1, we search appropriate data structure, no cross
		//	referencing is required to confirm matches.
		else if ( searchParams == 1 )
		{
			if ( !(tokens[PRODUCT].equals("")) )
			{
				matches = cs.searchProd( tokens[PRODUCT] );
				matchArray = listToArray(matches);	
			}
			else if ( !(tokens[WHOLESALER].equals("")) )
			{
				matches = cs.searchWhole( tokens[WHOLESALER] );
				matchArray = listToArray(matches);				
			}
			else 
			{
				matches = cs.searchDate( tokens[DATE] );
				matchArray = listToArray(matches);									
			}		
		}
		else if ( searchParams == 2 )
		{
			if ( (!(tokens[PRODUCT].equals(""))) && (!(tokens[WHOLESALER].equals(""))) )
			{
				matches = cs.searchProd( tokens[PRODUCT] );
				DSALinkedList crossRefed = new DSALinkedList();
				Iterator iter = matches.iterator();
				while ( iter.hasNext() )
				{
					Carton item = (Carton)iter.next();
					if ( item.getWhole().equals(tokens[WHOLESALER]) )
					{
						crossRefed.insertFirst( item );
					}	
				}
				matchArray = listToArray(crossRefed);					
			}

			if ( (!(tokens[PRODUCT].equals(""))) && (!(tokens[DATE].equals(""))) )
			{
				matches = cs.searchProd( tokens[PRODUCT] );
				DateClass maxDate = new DateClass( tokens[DATE] );				
				DSALinkedList crossRefed = new DSALinkedList();
				Iterator iter = matches.iterator();
				while ( iter.hasNext() )
				{
					Carton item = (Carton)iter.next();
					if ( item.getWar().compareTo(maxDate) <= 0 )
					{
						crossRefed.insertFirst( item );
					}	
				}
				matchArray = listToArray(crossRefed);					
			}

			if ( (!(tokens[WHOLESALER].equals(""))) && (!(tokens[DATE].equals(""))) )
			{
				matches = cs.searchWhole( tokens[WHOLESALER] );
				DateClass maxDate = new DateClass( tokens[DATE] );				
				DSALinkedList crossRefed = new DSALinkedList();
				Iterator iter = matches.iterator();
				while ( iter.hasNext() )
				{
					Carton item = (Carton)iter.next();
					if ( item.getWar().compareTo(maxDate) <= 0 )
					{
						crossRefed.insertFirst( item );
					}	
				}
				matchArray = listToArray(crossRefed);					
			}
		}	
		else 
		{
			matches = cs.searchProd( tokens[PRODUCT] );
			DateClass maxDate = new DateClass( tokens[DATE] );
			DSALinkedList crossRefed = new DSALinkedList();
			Iterator iter = matches.iterator();
			while ( iter.hasNext() )
			{
				Carton item = (Carton)iter.next();
				if ( ( item.getWhole().equals(tokens[WHOLESALER]) ) 
						&& ( item.getWar().compareTo(maxDate) <= 0) )
				{
					crossRefed.insertFirst( item );
				}	
			}	

			matchArray = listToArray(crossRefed);				
		}	
		return matchArray;		  	
	}
//--------------------------------------------------------------------------
	private static Carton[] listToArray(DSALinkedList matches)
	{
		Carton[] matchArray = new Carton[matches.getLength()];
		Iterator iter = matches.iterator();
		for (int ii = 0; ii < matchArray.length; ii++)
		{
			matchArray[ii] = (Carton)iter.next();
		}	
		return matchArray;
	}
//--------------------------------------------------------------------------	
	public static void printArray(Carton[] matches)
	{
		Sorts.quickSort( matches );

		for (int jj = 0; jj < matches.length; jj++)
		{
			printSearchResults(matches[jj]);
		}		
	}
//--------------------------------------------------------------------------	
	private static void printSearchResults(Carton match)
	{
		System.out.print( match.getDIndex() + ":" + match.getRIndex() );
		System.out.print( ":" + match.toString() + "\n" );
	}
//--------------------------------------------------------------------------
	private static int getParamNum(String[] tokens)
	{	
		int searchParams = 0;
		for ( int ii = 0; ii < tokens.length; ii++ )
		{
			if ( !(tokens[ii].equals("")) )
			{
				searchParams++;
			}	
		}	
		return searchParams;
//--------------------------------------------------------------------------			
	}	
}