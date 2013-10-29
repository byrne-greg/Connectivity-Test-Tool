/**
 * <h1>Connectivity Test Tool</h1>
 * 
 * <p>
 * Created by Greg Byrne, October 2013
 * Email: byrne.greg@gmail.com
 * Copyright remains reserved
 * <br/>
 * Package Description:
 * A tool that tests connection status to a user-input IP or web address via Windows console pings and returns an average
 * - provides a colour-coded status of connection aswell as the average of five previous pings
 * - continually tests connection until cancelled
 * <br/>
 * Package Content:
 * - CTTGui 
 * - CTTLogicUnit
 * - CTTStarter (executable)
 * <br/>
 * "internet_e.png" icon used under Freeware license. Created by jordanfc @ jordanfc.deviantart.com
 * <p>
 */

package ctt.gregbyrne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 
 * @author Greg Byrne
 *
 */
public class CTTLogicUnit 
{	// open CLASS
	
//***** Instance Variables	

	private String hostAddress;
	private ArrayList<Integer> pingAverages;
	protected static final int NO_RESPONSE = 9999;

//***** Constructors
	/**
	 * 
	 */
	public CTTLogicUnit() 
	{	// open no-arg CONSTRUCTOR
		
		hostAddress = "www.google.com";	
		pingAverages = new ArrayList<>();
		
	}	// close no-arg CONSTRUCTOR
	
	/**
	 * @param hostAddress
	 */
	public CTTLogicUnit(String destination)
	{	// open CONSTRUCTOR
		
		this();
		hostAddress = destination;
		
	}	// close CONSTRUCTOR
	
//***** Methods	
		
	/**
	 *  
	 */
	public void ping()
	{	// open ping()
		
		try
		{	// open TRY
			
			// assign hostAddress to variable command
			String command = getHostAddress();
			
			// enter "ping xxxx" onto command-line where xxx is selected IP address
			ProcessBuilder pb = new ProcessBuilder("ping ", getHostAddress() );
			Process process = pb.start();
		
			// create a BufferedReader that will read the command line result of pinging
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			while ((command = stdInput.readLine()) != null)
			{	// open WHILE
				
				//TODO DEBUG ONLY - will print ping execution to command line
				//System.out.println(command);
				
				// filter for command line that contains "Average =" and perform array logic
				if(command.contains("Average ="))
				{	// open IF
					
					addToArray(parseForAveragePingSpeed(command));
					break;
				
				}	// close IF
				else
				// filter for complete ping failure (e.g. four request timeouts or a disconnection)			
				if(command.contains("Lost = 4"))
					// add NO_RESPONSE to pingAverages
					addToArray(NO_RESPONSE);
				
				else
				// filter for unreachable IP
				if(command.contains("could not find host"))
					// add NO_RESPONSE to pingAverages
					addToArray(NO_RESPONSE);
				
			}	// end WHILE
			
		}	// close TRY
		catch(IOException ioe)
		{	// open CATCH
			
			ioe.printStackTrace();
			System.exit(0);
			
		}	// close CATCH
		  
	}	// close ping()
	
	/**
	 * @param pingOutput
	 */
	private int parseForAveragePingSpeed(String pingOutput)
	{	// open parseForAveragePingSpeed()
		
		// filter pingOutput string for all numbers contained and add to a String array
		String[] numbersOnly = pingOutput.split("[^\\d]+");
		
		// parse third number (which is the Average ms round trip of ping) and return
		return Integer.parseInt(numbersOnly[2]);
		 
	}	// close parseForAveragePingSpeed()
	
	/**
	 * @param number
	 */
	private void addToArray(int number)
	{	// open addToArray()
		
		// add number to last element
		pingAverages.add(number);
		
		// if ping averages greater than 3, remove first element
		if(pingAverages.size() > 3)
			pingAverages.remove(0);
		
	}	// close addToArray()
	
	/**
	 * @param arrList
	 * @return result
	 */
	private int calculateAveragePingSpeed(ArrayList<Integer> arrList)
	{	// open calculateAveragePingSpeed()
		
		int sum = 0;
		
		// add all values from arrList to variable sum
		for(int i = 0; i < arrList.size(); i++)
			sum += arrList.get(i);
		
		// calculate average dividing sum by number of values in arrList
		int result = sum/arrList.size();
		
		return result;
		
	}	// close calculateAveragePingSpeed()
	
	/**
	 * @return result
	 */
	public int getAveragePingSpeedResult()
	{	// open getAveragePingSpeedResult()
	
		// create local ArrayList with contents of array pingAverages
		ArrayList<Integer> pingAverages = getPingAverages();
		// calculate average ping speed from local ArrayList and assign to variable
		int result = calculateAveragePingSpeed(pingAverages);
		
		return result;
		
	}	// close getAveragePingSpeedResult()
	
	/**
	 * @return pingAverages
	 */
	public ArrayList<Integer> getPingAverages() 
	{	// open getPingAverages()
		
		/* if there is a complete ping failure on first attempt or a new address is unreachable
		 * then when called, return with at least a single value of NO_RESPONSE
		 */
		if(pingAverages.isEmpty() )
			pingAverages.add(NO_RESPONSE);
		
		return pingAverages;
		
	}	// close getPingAverages()
	/**
	 * @param newHostAddress
	 */
	public void setHostAddress(String newHostAddress) 
	{	// open getPingAverages()
		
		this.hostAddress = newHostAddress;
		
		// clear PingAverages array, removing previous results from previous destinations
		pingAverages.clear();
		
	}	// close getPingAverages()
	
	/**
	 * @return hostAddress
	 */
	public String getHostAddress() 
	{	// open getPingAverages()
		
		return hostAddress;
		
	}	// close getPingAverages()
	
	/**
	 * @return details
	 */
	public String toString() 
	{	// open toString()
		
		String details = "CTT Logic Unit for address: " + hostAddress;
		
		return details;
		
	}	// close toString()
	
	/**
	 * @return boolean
	 */
	public boolean equals(Object obj)
	{	// open equals()
		
		// test if passed object is an instance of CTTLogicUnit 
		if(obj instanceof CTTLogicUnit)
		{	// open IF
			
			// if true - cast Object obj to an instance of CTTLogicUnit
			CTTLogicUnit cttluObj = (CTTLogicUnit) obj;
			
			// if test object contains a host address equal to the caller object, then return true (is equal)
			if(cttluObj.hostAddress.equals(this.hostAddress) )
				return true;
			else
				return false;
		
		}	// close IF
		else
		return false;
		
	}	// close equals()
		
}	// close CLASS

