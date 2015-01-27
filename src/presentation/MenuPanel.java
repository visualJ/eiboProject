package presentation;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
	private MenuOpenCloseButton einklappen;
	
	private MenuButton preferences;
	private MenuButton info;
	private MenuButton help;
	private MenuButton recordFolder;
	
	private JPanel lineendPanel;
	private JPanel linestartPanel;
	private JPanel listPanel;
	
	private UserInterface userInterface;
	
	private JScrollPane soundPackScrollPane;
	private SoundPackList soundPackList;
	
	private SoundPackManager soundPackManager;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8244586608783664164L;

	public MenuPanel(SoundPackManager soundPackManager, UserInterface userInterface){
		
		this.soundPackManager = soundPackManager;
		this.userInterface = userInterface;
		
		//set genereal informations
	

		setLayout(new FlowLayout()); //set Layout of outer Panel
		setOpaque(true);
		setBackground(new Color(255,255,255,0)); // set Panelbackground to invisible
		
		
		content = new JPanel(); // define content Panel
	
		content.setOpaque(true);
		content.setBackground(UserInterface.alphaColor(Color.BLACK, 0.5f));
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
		info = new MenuButton("i");
		help = new MenuButton("h");
		
		
		info.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("i wurde gedrückt");
			}
		});
		
		linestartPanel.setSize(new Dimension(150,50));
		linestartPanel.setOpaque(false);
	
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
		einklappen = new MenuOpenCloseButton("einklappen");
		
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 1;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;

		einklappen.setText("einklappn");
		einklappen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchMenu();
			}
		});
		content.add(einklappen,constrain);
		

		lineendPanel = new JPanel();
		preferences = new MenuButton("p");
		recordFolder = new MenuButton("rF");
		
		
		recordFolder.setText("rF");
		recordFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File("./"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		preferences.setText("p");
		lineendPanel.setOpaque(false);
		
		lineendPanel.add(preferences);
		lineendPanel.add(recordFolder);
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 2;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		content.add(lineendPanel,constrain);
		
		listPanel = new JPanel();
		soundPackList = new SoundPackList(soundPackManager.getSoundpacksInDirectory("./"), this.userInterface);
		
		soundPackScrollPane = new JScrollPane(soundPackList);
		soundPackScrollPane.setPreferredSize(new Dimension(200,120));
		soundPackScrollPane.setBorder(null);
		soundPackScrollPane.getViewport().setOpaque(false);
		soundPackScrollPane.setOpaque(false);
		
		listPanel.setOpaque(false);
		listPanel.add(soundPackScrollPane);
		
		constrain.gridheight = 1;
		constrain.gridwidth = 3;
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
		if(menuOpen){
			listPanel.setVisible(false);
		}else{
			listPanel.setVisible(true);
		}
		menuOpen = !menuOpen;
	}
	
	
}
