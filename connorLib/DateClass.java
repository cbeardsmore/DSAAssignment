 /***************************************************************************
 *  FILE: DateClass.java                                                   
 *  AUTHOR: Connor Beardsmore - 15504319                                  
 *  UNIT: DSA120 Assignment S2- 2015 	                                                       
 *  PURPOSE: Container class to represent a date as 3 seperate integer values
 *  LAST MOD: 19/10/15  
 *  REQUIRES: NONE                      
 ***************************************************************************/
package connorLib;
import java.lang.*;

public class DateClass implements Comparable<DateClass>
{
	//CLASSFIELDS
	private static final int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 
														  30, 31, 30, 31 };
	//Once we set values, the date does not change													  
	private final int year; // 0000 to 9999 valid
	private final int month; // 0 to 12 valid
	private final int day;  // 0 to DAYS[month] valid													  
//--------------------------------------------------------------------------
	//ALTERNATE Constructor
	//IMPORT: inYear (int), inMonth (int), inDay (int)

	public DateClass(int inYear, int inMonth, int inDay)
	{
		//Validation utilizing DAYS constant array
		if ( inMonth > (DAYS.length - 1) )
		{	
			throw new IllegalArgumentException("Invalid Date (Month)");
		}		
		if ( !isValid( inYear, inMonth, inDay ) )
		{	
			throw new IllegalArgumentException("Invalid Date");
		}
		year = inYear;
		month = inMonth;
		day = inDay;
	}
//--------------------------------------------------------------------------
	//ALTERNATE Constructor
	//IMPORT: date (String)
	//PURPOSE: Construct date from string of format YYYY-MM-DD

	public DateClass(String date)
	{
		String[] fields = date.split("-");
		if ( fields.length != 3 )
		{	
		    throw new IllegalArgumentException("Invalid Date (Too Long)");
		}

		try 
		{
			year = Integer.parseInt( fields[0] );
			month = Integer.parseInt( fields[1] );
			day = Integer.parseInt( fields[2] );
		}
		catch (NumberFormatException e)
		{
			throw new NumberFormatException("Date Can Only Contain Numbers");
		}


		if ( month > (DAYS.length - 1) )
		{	
			throw new IllegalArgumentException("Invalid Date (Month)");
		}
		if ( !isValid( year, month, day ) )
		{	
			throw new IllegalArgumentException("Invalid Date");				
		}	
	}
//--------------------------------------------------------------------------
	//ACCESSOR getYear
	//EXPORT: year (int)

	public int getYear()
	{
		return year;
	}
//--------------------------------------------------------------------------
	//ACCESSOR getMonth
	//EXPORT: month (int)
	
	public int getMonth()
	{
		return month;
	}
//--------------------------------------------------------------------------
	//ACCESSOR getDay
	//EXPORT: day (int)
	
	public int getDay()
	{
		return day;
	}
//--------------------------------------------------------------------------
	//ACCESSOR isValid
	//IMPORT: inYear (int), inMonth (int), inDay (int)
	//EXPORT: valid (boolean)

	private static boolean isValid(int inYear, int inMonth, int inDay)  
	{
		boolean valid = true;

		if ( (inMonth < 1) || (inMonth > 12) )
		{	
			valid = false;
		}
		if ( (inDay < 1) || (inDay > DAYS[inMonth]) )
		{
			valid = false;
		}	
		if ( (inMonth == 2) && (inDay == 29) && (!isLeapYear(inYear)) )
		{
			valid = false;
		}
		if ( (inYear < 1) || (inYear > 9999) )
		{
			valid = false;
		}
		//Accounts for default date values of 0000-00-00 format
		if ( (inYear == 0) && (inMonth == 0) && (inDay == 0) )
		{
			valid = true;
		}	

		return valid;
	}
//--------------------------------------------------------------------------
	//withinMonths
	//IMPORT: inDate (DateClass), urgency (int)
	//EXPORT: isWithin (boolean)
	//PURPOSE: Checks if inDate is within 'urgency' months of this date
	//		   Returns TRUE if it is, FALSE if not

	public boolean withinMonths(DateClass inDate, int urgency)
	{
		boolean isWithin = false;

		if ( year == inDate.getYear() )
		{
			if ( ( month - inDate.getMonth() ) < urgency )
			{
				isWithin = true;
			}	
		}	
		return isWithin;
	}
//--------------------------------------------------------------------------	
	//compareTo 
	//IMPORT: inDate (DateClass)
	//PURPOSE: Compares 2 dates for use in sorting. 0 = dates equals
	//		   -ve = this.date is less than. +ve = this.date is more than
	//         OVERRIDES compareTo in Comparable

	public int compareTo(DateClass inDate)
	{
		int comparison = 0;

		if ( isInfinite() )
		{
			comparison = +1;
		}	
		else if ( year < inDate.getYear() )
		{
			comparison = -1;
		}	
		else if ( year > inDate.getYear() )
		{
			comparison = +1;
		}
		else if ( month < inDate.getMonth() )
		{
			comparison = -1;
		}
		else if ( month > inDate.getMonth() )
		{
			comparison = +1;
		}
		else if ( day < inDate.getDay() )
		{
			comparison = -1;
		}
		else if ( day > inDate.getDay() )
		{
			comparison = +1;
		}
		return comparison;
	}
//--------------------------------------------------------------------------	
	//ACCESSOR isLeapYear
	//IMPORT: inYear (int)
	//EXPORT: leapYear (boolean)

	private static boolean isLeapYear(int inYear)
	{
		return ( (inYear % 100 == 0) && (inYear % 4 == 0) 
									 || (inYear % 400 == 0) );
	}
//--------------------------------------------------------------------------
	//ACCESSOR isInfinite
	//EXPORT: infinite (boolean)
	//PURPOSE: Check if date is unspecified/infinite/default. i.e. 0000-00-00

	public boolean isInfinite()
	{	
		return ( (year == 0) && (month == 0) && (day == 0) );
	}
//--------------------------------------------------------------------------	
	//ACCESSOR toString
	//EXPORT: statestring (String)
	//PURPOSE: Exports string in format YYYY-MM-DD

	public String toString()
	{
		return String.format("%04d-%02d-%02d", year, month, day);
	}
//--------------------------------------------------------------------------	
}
