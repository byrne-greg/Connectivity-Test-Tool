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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
	
	private String versionID = "1.2";
	
	private JFrame window;
	private JTextField enterAddressTF;
	private JTextArea debugOutputTA;
	private JCheckBox debugCB;
	private ArrayList<String> debugOutputsToAppend;
	private JButton startPingButton;
	private JButton stopPingButton;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JPanel resultPanel;
	private static Color defaultColor;
	private CTTLogicUnit cttLogicUnit;
	private CTTLogicUnitGuiThread cttGuiThread;
	
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
			window.setTitle("Connectivity Test Tool");
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
		
		JTextArea:
		{	// open JTextArea label
			
			debugOutputTA = new JTextArea();
			debugOutputTA.setEditable(false);
			debugOutputTA.setLineWrap(true);
			debugOutputTA.setWrapStyleWord(true); // wrap to words (not char)
			debugOutputTA.setBorder(new TitledBorder("Debug Output from Console") );
			
			Dimension dotaDim = new Dimension(475, 125);
			debugOutputTA.setMinimumSize(dotaDim);
			debugOutputTA.setPreferredSize(dotaDim);
			
			// enable ArrayList for debugOutputs
			debugOutputsToAppend = new ArrayList<>();
			
		}	// close JTextArea label
		
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
			
			controlPanel = new JPanel();
			controlPanel.add(textFieldPanel);
			controlPanel.add(buttonPanel);
			controlPanel.add(resultPanel);
			
			window.add(controlPanel, BorderLayout.CENTER);
			
			// assign defaultColor the default window color
			defaultColor = resultPanel.getBackground();
			
		}	// close JPanel label
		
		MenuBar:
		{	// open MenuBar label]
			
			// create menu
			// JMenuBar menuBar
			JMenuBar menuBar = new JMenuBar();
			// JMenu helpMenu
			JMenu helpMenu = new JMenu("Help");
			// JMenuItem aboutMI
			JMenuItem aboutMI = new JMenuItem("About");
			// JCheckBox debugCB
			debugCB = new JCheckBox("Show Debug Output");
			
			//	add to appropriate menu listings
			helpMenu.add(debugCB);
			helpMenu.add(aboutMI);
			menuBar.add(helpMenu);
			
			//	addActionListener for aboutMI
			aboutMI.addActionListener(new ActionListener()
			{	// open ACTIONLISTENER CLASS

				@Override
				public void actionPerformed(ActionEvent ae) 
				{	// open
					
					JOptionPane.showMessageDialog(null, "Created by Greg Byrne, October 2013\nEmail: byrne.greg@gmail.com\nVersion: " + versionID);
					
				}	// close	
				
			});		// close ACTIONLISTENER CLASS
			
			// addActionListener for debugCB
			debugCB.addActionListener(new ActionListener()
			{	// open ACTIONLISENER CLASS
				
				@Override
				public void actionPerformed(ActionEvent ae)
				{	// open
					
					// if debug check box is checked
					if(debugCB.isSelected())
					{	// open IF
						
						cttLogicUnit.setDebugEnabled(true);
						controlPanel.add(debugOutputTA);
						window.setSize(new Dimension(570, 260) );
					
					}	// close IF
					else
					{	// open ELSE
					
						cttLogicUnit.setDebugEnabled(false);
						controlPanel.remove(debugOutputTA);
						window.setSize(new Dimension(570, 120) );
						
					}	// close ELSE
					
				}	// close
				
			});	// close ACTIONLISTENER CLASS
			
			// set JMenuBar on window
			window.setJMenuBar(menuBar);
		
		}	// close MenuBar lebel
	
		JFrameEnd:
		{	// open JFrameEnd label
			
			Dimension windowDim = new Dimension(570, 120);
			window.setMinimumSize(windowDim);
			window.setPreferredSize(windowDim);
			window.setLocationRelativeTo(null);	// centre window
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.pack();
			window.setVisible(true);
			
		}	// close JFrameEnd label
		
	}	// close CONSTRUCTOR
	
//***** Methods
	
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
	 * 
	 * @param input
	 */
	public void appendToDebugOutputTA(String input)
	{	// open appendToDebugOutputTA()
		
		// clear text area
		debugOutputTA.setText("");
		
		// add new debug output line to array
		debugOutputsToAppend.add(input + "\n");
		
		// ensure array only contains 5 lines at a time (stop overloading of debugOutputTA)
		if(debugOutputsToAppend.size() > 5)
			debugOutputsToAppend.remove(0);
			
		// add each array element to text area 
		// performed instead of ArrayList.toString() so as to remove [] and ,
		for(String stringToAdd : debugOutputsToAppend)
			debugOutputTA.append(stringToAdd);

		
	}	// close appendToDebugOutputTA()
	
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
					
					// show debug info if enabled on cttLogicUnit
					if(cttLogicUnit.getDebugEnabled())
						appendToDebugOutputTA(cttLogicUnit.getDebugOutput());
				
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
				
				// clear debugOutputTA of text
				debugOutputTA.setText(" ");
				// clear debugOutputToAppend of contents
				debugOutputsToAppend.clear();
				
				// enable further pings
				startPingButton.setEnabled(true);
				enterAddressTF.setEnabled(true);
				
				// disable cancel
				stopPingButton.setEnabled(false);
				
		}	// close done()
		
	}	// close inner CTTLogicUnitGuiThread class

}	// close CLASS
