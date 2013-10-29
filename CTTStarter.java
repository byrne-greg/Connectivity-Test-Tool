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

/**
 * 
 * @author Greg Byrne
 *
 */
public class CTTStarter 
{	// open CLASS

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{	// open MAIN

		new CTTGui();

	}	// close MAIN

}	// close CLASS
