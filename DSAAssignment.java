 /***************************************************************************
 *	FILE: DSAAssignment.java												   
 *	AUTHOR: Connor Beardsmore - 15504319								  
 *	UNIT: DSA120 Assignment S2- 2015 														   
 *	PURPOSE: Main Program for Hardly Normal Assignment
 *	LAST MOD: 24/10/15	
 *  REQUIRES: java.io.IOException, connorLib.Carton/DistroCentre				   
 ***************************************************************************/
import java.io.IOException;
import connorLib.Carton;
import connorLib.DistroCentre;

public class DSAAssignment
{
	public static void main(String[] args)
	{
		if ( args.length != 2 )
		{
			System.err.println("ERROR: Invalid command line parameters");
			System.out.println("Usage: java DSAAssignment <DC file> <Task file>");
		}	
		else
		{	
			String dcFile = args[0];
			String taskFile = args[1];
			
			try
			{			
				DistroCentre dc = DCHandler.createDC( dcFile );
				TaskHandler.performTasks( dc, taskFile, dcFile );
			}
			//ALL EXCEPTION HANDLING DONE HERE
			catch (IOException e)
			{
				System.err.println("FILE ERROR: " + e.getMessage() );
			}
			catch (IllegalStateException e)
			{
				System.err.println("STATE ERROR: " + e.getMessage() );
			}
			catch (IllegalArgumentException e)
			{
				System.err.println("ARGUMENT ERROR: " + e.getMessage() );
			}
			//CATCHING AS A FAILSAFE ONLY
			catch (Exception e)
			{
				System.err.println("UNEXPECTED ERROR: " + e.toString() );
			}
		}
	}
}
 //--------------------------------------------------------------------------