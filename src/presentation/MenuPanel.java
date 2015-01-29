package presentation;

import java.awt.CardLayout;
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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import services.SoundPackManager;


/** 
 * This Class create the upperPanel 
 * 
 * **/
public class MenuPanel extends JPanel{
	
	private final String INFO = "INFO";
	private final String HELP = "HELP";
	private final String PREFERENCES = "PREFERENCES";
	private final String SOUNDPACK = "SPOUNDPACK";
	
	
	private boolean menuOpen = true;
	private JPanel content;
	private GridBagConstraints constrain;
	private MenuOpenCloseButton einklappen;
	
	private MenuButton preferences;
	private MenuButton info;
	private MenuButton help;
	private MenuButton recordFolder;
	private MenuButton soundPack;
	
	private JButton changeDirectory;
	
	private JPanel lineendPanel;
	private JPanel linestartPanel;
	private JPanel listPanel;
	private JPanel panels;
	
	private JPanel infoPanel;
	private JPanel helpPanel;
	private JPanel preferencesPanel;
	private JPanel soundpackPanel;
	
	
	private JLabel infoLabel;
	private JLabel helpLabel;
	private JLabel preferencesLabel;
	
	private JFileChooser fileChooser;
	
	private UserInterface userInterface;
	
	private JScrollPane soundPackScrollPane;
	private SoundPackList soundPackList;
	
	private SoundPackManager soundPackManager;
	
	private CardLayout cardLayout;
	

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
		info = new MenuButton("");
		help = new MenuButton("");
		
		info.setBigIcon(UserInterface.infoIcon);
		help.setBigIcon(UserInterface.helpIcon);
		
		info.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(menuOpen)
				{
					switchMenu();
				}
				cardLayout.show(panels, INFO);
				System.out.println("open Menu -- info");
			}
		});
		
		help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(menuOpen)
				{
					switchMenu();
				}
				cardLayout.show(panels, HELP);
				System.out.println("open Menu -- Help");
				
			}
		});
		
		linestartPanel.setSize(new Dimension(150,50));
		linestartPanel.setOpaque(false);
	
		help.setText("");
		
		
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
		einklappen = new MenuOpenCloseButton("");
		
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 1;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;

		einklappen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchMenu();
			}
		});
		
		einklappen.setBigIcon(UserInterface.arrowDonwIcon);
		content.add(einklappen,constrain);
		

		lineendPanel = new JPanel();
		preferences = new MenuButton("");
		recordFolder = new MenuButton("");
		soundPack  = new MenuButton("");
		
		
		soundPack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(menuOpen){
					switchMenu();
				}
				cardLayout.show(panels, SOUNDPACK);
				System.out.println("open Menu -- SoundPack");
				
			}
		});
		
		
		
		preferences.setBigIcon(UserInterface.settingsIcon);
		
		preferences.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(menuOpen){
					switchMenu();
				}
				cardLayout.show(panels, PREFERENCES);
				System.out.println("open Menu -- preferences");
				
			}
		});
		recordFolder.setBigIcon(UserInterface.folderIcon);
		recordFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File("./"));
					System.out.println("open Menu -- open record Path");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		lineendPanel.setOpaque(false);
		
		lineendPanel.add(preferences);
		lineendPanel.add(soundPack);
		lineendPanel.add(recordFolder);
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 2;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		content.add(lineendPanel,constrain);
		
		listPanel = new JPanel();
		soundPackList = new SoundPackList(this.soundPackManager.getSoundpacksInDirectory("./"), this.userInterface);
		
		soundPackScrollPane = new JScrollPane(soundPackList);
		soundPackScrollPane.setPreferredSize(new Dimension(200,120));
		soundPackScrollPane.setBorder(null);
		soundPackScrollPane.getViewport().setOpaque(false);
		soundPackScrollPane.setOpaque(false);
		
		listPanel.setOpaque(false);
		listPanel.add(soundPackScrollPane);
		
		//Card Layout
		panels = new JPanel();
		cardLayout = new CardLayout();
		infoPanel = new JPanel();
		helpPanel = new JPanel();
		preferencesPanel = new JPanel();
		fileChooser = new JFileChooser();
		changeDirectory = new JButton("change Directory");
	
		panels.setLayout(cardLayout);
		
		
		infoLabel = new JLabel("lalala");
		infoPanel.add(infoLabel);
		
		helpLabel = new JLabel("Hilfe");
		helpPanel.add(helpLabel);
		
		preferencesLabel = new JLabel("preferences");
		preferencesPanel.add(preferencesLabel);
		preferencesPanel.add(changeDirectory);
		
		changeDirectory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int choice = fileChooser.showOpenDialog(null);

				
				
			}
		});
		
		panels.add(listPanel,SOUNDPACK);
		panels.add(infoPanel,INFO);
		panels.add(helpPanel, HELP);
		panels.add(preferencesPanel,PREFERENCES);
		
		panels.setBackground(new Color(0,0,0));
		panels.setVisible(false);
		
		constrain.gridheight = 1;
		constrain.gridwidth = 3;
		constrain.gridx = 0;
		constrain.gridy = 2;
		constrain.fill = GridBagConstraints.BOTH;
		
		
		content.add(panels,constrain);
		
		content.setVisible(true);

		add(content);
		setVisible(true);
		
	}
	
	public void switchMenu()
	{
		if(!menuOpen){
			panels.setVisible(false);
			einklappen.setBigIcon(UserInterface.arrowDonwIcon);
		}else{
			panels.setVisible(true);
			einklappen.setBigIcon(UserInterface.arrowUpIcon);
		}
		menuOpen = !menuOpen;
	}
	
	
}
