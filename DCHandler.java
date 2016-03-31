 /***************************************************************************
 *	FILE: DCHandler											   
 *	AUTHOR: Connor Beardsmore - 15504319								  
 *	UNIT: DSA120 Assignment S2- 2015 													   
 *	PURPOSE: Handles DC Tasks
 *	LAST MOD: 19/10/15	
 *  REQUIRES: java.util.Iterator, java.io.IOException, connorLib					   
 ***************************************************************************/
import java.io.IOException;
import java.util.Iterator;
import connorLib.*;

 public class DCHandler
{
	//createDC
	//IMPORT: filename (String)
	//EXPORT: dc (DistroCentre)
	//PURPOSE: Creates and populates a new DC, based on date in 'filename'

	public static DistroCentre createDC( String filename )
									throws IOException
	{
		DSALinkedList roomList = new DSALinkedList();	
		DSALinkedList itemList = new DSALinkedList();

		//Reads file into two linked lists
		FileIO.readDCFile( filename, roomList, itemList );
		//Constructs DC now number rooms is known
		DistroCentre dc = new DistroCentre( roomList.getLength() );
		//Populate Index Array with items, and DC with new empty rooms
		populateArray( dc, itemList );
		populateDC( dc, roomList );

		//Ensure top and bottom Cartons match up 
		if ( itemList.getLength() != dc.totalItems() )
		{
			throw new IllegalArgumentException("mistmatch item/s in DC file");
		}

		return dc;
	}
//--------------------------------------------------------------------------
	//populateArray
	//IMPORT: dc (DistroCentre), itemList (DSALinkedList)
	//PURPOSE: Creates and populates DC's index array, based on itemList

	private static void populateArray( DistroCentre dc,
										DSALinkedList itemList )
	{
		String newLine;
		Carton newItem;
		Iterator iter = itemList.iterator();

		//Iterates over itemList, creating Cartons and adding to array
		while ( iter.hasNext() )
		{
			newLine = (String)iter.next();
			newItem = new Carton( newLine );
			dc.setCartonIndex(newItem);
		}	
	}
//--------------------------------------------------------------------------
	//createDC
	//IMPORT: dc (DistroCentre), roomList (DSALinkedList)
	//PURPOSE: Creates and populates a new DC, based on roomList

	private static void populateDC(DistroCentre dc, DSALinkedList roomList)
	{
		String newLine, roomType;
		Iterator iter = roomList.iterator();
		int items, total, spaces, ii = 0;

		while ( iter.hasNext() )
		{
			//Tokenize line
			newLine = (String)iter.next();	
			newLine = newLine.trim();		
			String[] tokens = newLine.split(":", -1);

			//Create Room based on first token, i.e. D/R/Y
			roomType = tokens[0];
			createRoom(roomType, tokens.length - 1, dc);

			//Populate the new room with Cartons from the indexArray in DC
			IStockRoom room = dc.getStockRoom(ii);
			populateRoom(room, dc, tokens, ii);
			ii++;
		}
	}
//--------------------------------------------------------------------------
	//createRoom
	//IMPORT: type (String), length (int), dc (DistroCentre)
	//PURPOSE: Creates and populates a new DC, based on date in 'filename'

	private static void createRoom(String type, int length, DistroCentre dc)
	{
		//Creates Room depending on type declared in the start of line
		if ( type.equals("D") )
		{
			dc.createDeadEnd(length);
		}	
		else if ( type.equals("R") )
		{
			dc.createRolling(length);				
		}	
	    else if ( type.equals("Y") )
		{
			dc.createYard(length);				
		}
		else 
		{
			String errMessage = ("Invalid room type in DC Geometry");
			throw new IllegalArgumentException( errMessage );
		}	
	}
//--------------------------------------------------------------------------
	//populateRoom
	//IMPORT: room (IStockRoom), dc (DistroCentre, tokens (String), dIndex (int)
	//PURPOSE: Populates a stockroom with a new carton

	private static void populateRoom(IStockRoom room, DistroCentre dc,
										 String[] tokens, int dIndex)
	{
		for ( int ii = 1; ii < tokens.length; ii++ )
		{
			String nextCart = tokens[ii];
			if ( !nextCart.equals("") )
			{	
				int newNote;
				//Catch possible erros in the room description section
				try 
				{
					newNote = Integer.parseInt(nextCart);
				}
				catch (NumberFormatException e)
				{
					throw new NumberFormatException("Con Note Must Be Numbers Only");
				}

				Carton newCarton = dc.getCartonIndex(newNote);

				if (newCarton == null)
				{	
					String errMessage = ("Carton doesn't exist in DC file");
					throw new IllegalArgumentException( errMessage );
				}	
				//If it's a yard, add to specific index, to allow empty slots
				if ( room instanceof Yard )
				{	
					((Yard)room).addCarton(newCarton, ii - 1);
				}
				//If not a yard, we place in whatever we are allowed to 
				else 
				{
					room.addCarton(newCarton);
				}
				newCarton.setDIndex(dIndex);	
			} 
		}
	}		
//--------------------------------------------------------------------------		
}
