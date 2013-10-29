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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author Greg Byrne
 *
 */
public class CTTGui 
{	// open CLASS

//***** Instance Variables
	
	private JFrame window;
	private JTextField enterAddressTF;
	private JButton startPingButton;
	private JButton stopPingButton;
	private JLabel statusLabel;
	private JPanel resultPanel;
	private CTTLogicUnit cttLogicUnit;
	private CTTLogicUnitGuiThread cttGuiThread;
	private static Color defaultColor;

//***** Constructor
	
	@SuppressWarnings("unused")
	public CTTGui()
	{	// open no-arg CONSTRUCTOR
		
		// create instance of CTTLogicUnit
		cttLogicUnit = new CTTLogicUnit();
		
		SetUI:
		{	// open SetUI label
			
			// set the UI to default look and feel
			try 
			{	// open TRY
			
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			}	// close TRY 
			catch(Exception e) 
			{	// open CATCH
			
				System.out.println("EXCEPTION: Error setting native LAF: " + e);
		
			}	// close CATCH
		
		}	// close SetUI Label
		
		JFrameStart:
		{	// open JFrameStart label
		
			window = new JFrame();
			window.setTitle("Connectivity Test Tool by Greg Byrne (Oct 2013) - byrne.greg@gmail.com");
			window.setLayout(new BorderLayout() );
			
			Icon:
			{	// open Icon label
				
				try
				{	// open TRY
				
					// load icon from relative directory
					// ICON: "internet_e.png" used under freeware license
					// DESIGNER: jordanfc (jordanfc.deviantart.com)
					window.setIconImage(new ImageIcon(getClass().getResource("internet_e.png")).getImage());
					
				}	// close TRY
				catch(NullPointerException npe)
				{	// open CATCH
					
					System.out.println("EXCEPTION: Icon cannot be found: " + npe);
					
				}	// close CATCH				
			
			}	// close Icon label
			
		}	// close JFrameStart label
		
		
		JTextField:
		{	// open JTextField label
			
			enterAddressTF = new JTextField("www.google.com");
			enterAddressTF.setPreferredSize(new Dimension(150, 20));
			enterAddressTF.setEditable(true);
			enterAddressTF.isFocusable();
			
		}	// close JTextField label
		
		JButton:
		{	// open JButton label
			
			startPingButton = new JButton("Start");
			startPingButton.isFocusable();
			startPingButton.addActionListener(new ActionListener() 
			{	// open ActionListener

				@Override
				public void actionPerformed(ActionEvent ae) 
				{	// open actionPerformed()
					
					// amend CTTLogicUnit's hostAddress as to what is contained in enterAddressTF
					cttLogicUnit.setHostAddress(enterAddressTF.getText());
					
					// create instance of Swing Worker thread
					cttGuiThread = new CTTLogicUnitGuiThread();
					// start the Swing Worker thread
					cttGuiThread.execute();	
					
				}	// close actionPerformed()
				
			});	// close ActionListener
			
			stopPingButton = new JButton("Cancel");
			stopPingButton.isFocusable();
			stopPingButton.setEnabled(false);
			stopPingButton.addActionListener(new ActionListener() 
			{	// open ActionListener

				@Override
				public void actionPerformed(ActionEvent ae) 
				{	// open actionPerformed()
					
					// cancel SwingWorker thread
					cttGuiThread.cancel(true);
					// reset result label
					statusLabel.setText("----");
					
				}	// close actionPerformed()
				
			});	// close ActionListener

		}	// close JButton label
		
		JLabel:
		{	// open JLabel label
			
			statusLabel = new JLabel("---");
		
		}	// close JLabel label
		
		JPanel:
		{	// open JPanel label
					
			Dimension buttonPanelDim = new Dimension(160, 55);
			JPanel buttonPanel = new JPanel();
			buttonPanel.setBorder(new TitledBorder("Connection State Controls"));
			buttonPanel.setMinimumSize(buttonPanelDim);
			buttonPanel.setPreferredSize(buttonPanelDim);
			buttonPanel.add(startPingButton);
			buttonPanel.add(stopPingButton);
			
			Dimension textFieldPanelDim = new Dimension(180, 55);
			JPanel textFieldPanel = new JPanel();
			textFieldPanel.setBorder(new TitledBorder("Enter Connection Test Address"));
			textFieldPanel.setMinimumSize(textFieldPanelDim);
			textFieldPanel.setPreferredSize(textFieldPanelDim);
			textFieldPanel.add(enterAddressTF);
			
			Dimension resultPanelDim = new Dimension(185, 55);
			resultPanel = new JPanel();
			resultPanel.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), "Connection Status (Average in ms)"));
			resultPanel.setMinimumSize(resultPanelDim);
			resultPanel.setPreferredSize(resultPanelDim);
			resultPanel.add(statusLabel);
			
			JPanel controlPanel = new JPanel();
			controlPanel.add(textFieldPanel);
			controlPanel.add(buttonPanel);
			controlPanel.add(resultPanel);
			
			window.add(controlPanel, BorderLayout.CENTER);
			
			// assign defaultColor the default window color
			defaultColor = resultPanel.getBackground();
			
		}	// close JPanel label
	
		JFrameEnd:
		{	// open JFrameEnd label
			
			Dimension windowDim = new Dimension(570, 100);
			window.setMinimumSize(windowDim);
			window.setPreferredSize(windowDim);
			window.setLocationRelativeTo(null);	// centre window
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.pack();
			window.setVisible(true);
			
		}	// close JFrameEnd label
		
	}	// close CONSTRUCTOR
	
	/**
	 * 
	 * @param label
	 * @return color
	 */
	private Color getConnectionColor(JLabel label)
	{	// open getConnectionColor
		
		// default initialisation
		Color color = defaultColor;
		int testNumber = CTTLogicUnit.NO_RESPONSE;
		
		try
		{	// open TRY
			
			testNumber = Integer.parseInt(label.getText() );
			
		}	// close TRY
		catch(NumberFormatException nfe)
		{	// open CATCH
			
			System.out.println("EXCEPTION: Unexpected Characters encountered: " + nfe);
		
		}	// close CATCH
		
		// decision sieve of color assignment based on testNumber
		if(testNumber > 0)
			color = Color.GREEN;
		if(testNumber > 250)
			color = new Color(180, 255, 0); // green-yellow mix
		if(testNumber > 500)
			color = Color.YELLOW;
		if(testNumber > 1250)
			color = Color.ORANGE;
		if(testNumber > 2000)
			color = Color.RED;
									
		return color;
		
	}	// close getConnectionColor()
	
	/**
	 * @return details
	 */
	public String toString() 
	{	// open toString()
		
		String details = "Connectivity Test Tool GUI created by Greg Byrne (Oct 2013).\n" +
						 "CTT Logic Unit Address: " + cttLogicUnit.getHostAddress();
		
		return details;
		
	}	// close toString()
	
	/**
	 * 
	 * @author Greg
	 *
	 */
	class CTTLogicUnitGuiThread extends SwingWorker<Integer, Integer> 
	{	// open inner CTTLogicUnitGuiThread class

		/**
		 * @return Integer
		 */
		@Override
		protected Integer doInBackground() throws Exception 
		{	// open doInBackground()
			
			// disable further pings
			startPingButton.setEnabled(false);
			enterAddressTF.setEnabled(false);

			// enable cancel
			stopPingButton.setEnabled(true);
			
			// add initial label
			statusLabel.setText("Calculating...");
			
			// ping loop while not cancelled
			// if cancelled button is pressed, return rubbish value (zero)
			while(isCancelled() == false)
			{	// open WHILE
				
				// ping that!
				cttLogicUnit.ping();
				
				// if cancelled during a ping, do not show last result
				if(isCancelled() == false)
				{	// open IF
					
					// update the resultPanel with current average and associated connection color
					statusLabel.setText("" + cttLogicUnit.getAveragePingSpeedResult());
					resultPanel.setBackground(getConnectionColor(statusLabel) );
				
				}	// close IF
				
			}	// close WHILE
			
			// rubbish value
			return 0;
			
		}	// close doInBackground()

		/**
		 * 
		 */
		@Override
		protected void done() 
		{	// open done()
			
				// reset resultPanel color
				resultPanel.setBackground(defaultColor);
				
				// enable further pings
				startPingButton.setEnabled(true);
				enterAddressTF.setEnabled(true);
				
				// disable cancel
				stopPingButton.setEnabled(false);
				
		}	// close done()
		
	}	// close inner CTTLogicUnitGuiThread class

}	// close CLASS
