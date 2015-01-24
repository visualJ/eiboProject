package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import repository.SoundPack;
import services.SoundPackManager;


/** 
 * This Class create the upperPanel 
 * 
 * **/
public class MenuPanel extends JPanel{

	
	private boolean menuOpen = true;
	private JPanel content;
	private GridBagConstraints constrain;
	private JButton einklappen;
	
	private JButton preferences;
	private MenuButton info;
	private JButton help;
	private JButton recordFolder;
	
	private JPanel lineendPanel;
	private JPanel linestartPanel;
	private JPanel listPanel;
	
	
	private JButton button; 

	private SoundPackManager manager;
	private JList<SoundPack> liste;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8244586608783664164L;

	public MenuPanel(){
		
		
		//set genereal informations
	

		setLayout(new FlowLayout()); //set Layout of outer Panel
		setOpaque(true);
		setBackground(new Color(255,255,255,0)); // set Panelbackground to invisible
		
		
		content = new JPanel(); // define content Panel
	
		content.setOpaque(true);
		content.setBackground(new Color(255,0,0,0)); //set Panelbackground to invisible
		content.setSize(new Dimension(1000,600));
		content.setLayout(new GridBagLayout()); // Gridbacklayout for inner Panel
		constrain = new GridBagConstraints(); // add constrain for Layout 
		
		/*
		 * Genereller aufbau solle wie folgt aussehen 
		 * Panel	einklappen	Panel
		 * Listbutton	Liste mit Soundpacks
		 * 
		 * (mit Tabs hilfe / info / einstellungen)
		 * */
		
		
	// linestartPanel = info and help button
		linestartPanel = new JPanel();
		info = new MenuButton();
		help = new JButton();
		
		linestartPanel.setSize(new Dimension(150,50));
		try {
			info.setImage(ImageIO.read(MenuPanel.class.getResource("res/LOOP.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		help.setText("h");
		
		linestartPanel.add(info);
		linestartPanel.add(help);
		
		constrain.gridheight = 1;
		constrain.gridwidth = 1;
		constrain.gridx = 0;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		content.add(linestartPanel,constrain);
		
	//
	// einklappen button mit der einblendfunktion	
		einklappen = new JButton();
		
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 1;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;

		einklappen.setText("einklappn");
		einklappen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				switchMenu();
				System.out.println("menu open is " + menuOpen);
			}
		});
		content.add(einklappen,constrain);
		

		lineendPanel = new JPanel();
		preferences = new JButton();
		recordFolder = new JButton();
		
		
		recordFolder.setText("oR");
		preferences.setText("p");
		
		
		lineendPanel.add(preferences);
		lineendPanel.add(recordFolder);
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 2;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		content.add(lineendPanel,constrain);
		
		listPanel = new JPanel();
		button = new JButton("pref");
		
		
		listPanel.add(button);
		
		constrain.gridheight = 1;
		constrain.gridwidth = 1;
		constrain.gridx = 0;
		constrain.gridy = 2;
		constrain.fill = GridBagConstraints.BOTH;
		
		
		content.add(listPanel,constrain);
		
		content.setVisible(true);
		
		
	
	
		

		add(content);
		setVisible(true);
		
	}
	
	public void switchMenu()
	{
	
		menuOpen = !menuOpen;
	}
	
	
}
