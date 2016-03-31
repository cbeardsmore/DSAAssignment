 /***************************************************************************
 *	FILE: TaskHandler.java											   
 *	AUTHOR: Connor Beardsmore - 15504319								  
 *	UNIT: DSA120 Assignment S2- 2015 													   
 *	PURPOSE: Handles and executes tasks on DC given a Taskfile 
 *	LAST MOD: 25/10/15	
 *  REQUIRES: java.util.Iterator, java.io, connorLib			   
 ***************************************************************************/
import java.io.*;
import java.util.Iterator;
import connorLib.*;

public class TaskHandler
{
	//CLASS CONSTANTS
	private static final String ADD = "A";
	private static final String REMOVE = "R";
	private static final String SEARCH = "S";		
//--------------------------------------------------------------------------
	//performTasks 
	//IMPORT: dc (DistroCentre), taskFile + dcFile (String)
	//PURPOSE: Execute tasks given in taskFile, call methods to handle

	public static void performTasks( DistroCentre dc, String taskFile, String dcFile)
										throws IOException
	{
		DSALinkedList taskList = new DSALinkedList();
		//Convert taskFile into Linked List of task Strings
		FileIO.readTaskFile(taskFile, taskList);
		//In Case any task is search OR remove, set up CartonSearcher
		CartonSearcher cs = new CartonSearcher( dc );	
		
		//Perform task on every line in the taskList
		String newLine;
		Iterator iter = taskList.iterator();
		while ( iter.hasNext() )
		{
			newLine = (String)iter.next();
			//Send individual task off seperately
			delegateTask(dc, cs, newLine, dcFile);
		}	
	}
//--------------------------------------------------------------------------
	//delegateTask
	//IMPORT: indexArray (Carton), dc (DistroCentre), taskFile + dcFile (String)
	//PURPOSE: determine what task type is, call correct method to handle

	private static void delegateTask(DistroCentre dc, CartonSearcher cs,
											String newLine, String dcFile)
												throws IOException
	{
		String[] taskID = newLine.split(":", 2);

		//Task Dependent on first string split before semicolon
		//Writeoutput only occurs for Add and Remove, not for Search
		//Writes after every appropriate task, enforces Atomicity of task
		if ( taskID[0].equals(ADD) )
		{
			TaskFunctions.addTask( dc, cs, taskID[1], -1 );
			FileIO.writeOutput( dc, dcFile );
		}	
		else if ( taskID[0].equals(REMOVE) )
		{
			TaskFunctions.removeTask( dc, cs, taskID[1] );
			FileIO.writeOutput( dc, dcFile );
		}	
		else if ( taskID[0].equals(SEARCH) )
		{
			Carton[] matches = TaskFunctions.searchTask( dc, cs, taskID[1] );
			TaskFunctions.printArray( matches );	
		}	
 		else 
 		{
 			throw new IllegalArgumentException("Task file format invalid");
 		}
	}		
//--------------------------------------------------------------------------	
}