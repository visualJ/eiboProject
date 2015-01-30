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

import services.AudioCore;
import services.Preferences;
import services.SoundPackManager;


/** 
 * This Class create the upperPanel with the Settings and Help Buttons
 * 
 * **/
public class MenuPanel extends JPanel{
	
	//CONSTANTS for CardLayout
	private final String INFO = "INFO";
	private final String HELP = "HELP";
	private final String PREFERENCES = "PREFERENCES";
	private final String SOUNDPACK = "SOUNDPACK";
	
	private final String infomartion = "<html><b>Beschreibung unserer Dienste</b>"
		+ "<p><br />iBO verschafft Ihnen über seine sehr einfache Benutzungsoberfläche "
		+ "Zugang zu einer Vielzahl von Ressourcen einschließlich einer<br /> "
		+ "Soundbibliothek, die es ermöglicht Musik zu erstellen und aufzunehmen. "
		+ "Jede Menge nützlicher Aufnahme- und Bearbeitungsfunktionen machen "
		+ "iBO so benutzerfreundlich wie leistungsstark. "
		+ "Finde den richtigen Rhythmus mit einem Klick.</p> "
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ "</html>";
	
	//set booleans
	private boolean menuClosed = true; //is Menu closed
	
	//JPanels 
	private JPanel contentPanel; //main Panel with GridBagLayout
		//Menu Panels 
	private JPanel linestartPanel; 	// information Panel
	private JPanel lineendPanel; 	// Preferences Panel
	private JPanel panels; 			// menu content
		//panels Panels 
	private JPanel soundPackPanel;		// CardLayout: soundPack List
	private JPanel infoPanel;			// CardLayout information about the Program
	private JPanel helpPanel;			// CardLayout: help about the Programm
	private JPanel preferencesPanel;	// CardLayout: preferences of the programm
		//CardLayout Labels
	private JLabel infoLabel;
	private JLabel helpLabel;
	
	//Buttons
	private MenuOpenCloseButton collapseButton; // switch menu visibility 
		//Menu Buttons
	private MenuButton preferences; 	// open preferences Panel
	private MenuButton info;			// open info Panel 
	private MenuButton help;			// open help Panel
	private MenuButton soundPack;		// open soundPack Panel
	
	private JButton changeDirectory; 	// change record Folder preferences
		// Recording Button	
	private RecordingFolderButton recordFolder; // open the record Folder
	
	private GridBagConstraints constrain; // constrain for GridBagLayout
	
	private JFileChooser fileChooser;	// file Chooser for new record path
	

	
	private JScrollPane soundPackScrollPane;
	private SoundPackList soundPackList;
	
	private SoundPackManager soundPackManager;
	
	private CardLayout cardLayout;
	
	private UserInterface userInterface;
	private AudioCore audioCore;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8244586608783664164L;

	public MenuPanel(SoundPackManager soundPackManager, UserInterface userInterface, AudioCore audioCore){
		
		this.soundPackManager = soundPackManager;
		this.userInterface = userInterface;
		this.audioCore = audioCore;
		
		//set genereal informations
		setLayout(new FlowLayout()); //set Layout of outer Panel
		setOpaque(true);
		setBackground(new Color(255,255,255,0)); // set Panelbackground to invisible
		
		
		contentPanel = new JPanel(); // define content Panel
	
		contentPanel.setOpaque(true);
		contentPanel.setBackground(UserInterface.alphaColor(Color.BLACK, 0.5f));
		contentPanel.setSize(new Dimension(1000,600));
		contentPanel.setLayout(new GridBagLayout()); // Gridbacklayout for inner Panel
		constrain = new GridBagConstraints(); // add constrain for Layout 
		
		
	// linestartPanel = info and help button
		linestartPanel = new JPanel();
		linestartPanel.setSize(new Dimension(150,50));
		linestartPanel.setOpaque(false);
		
		//info Button
		info = new MenuButton("");						// inizial the info Menu Button						
		info.setBigIcon(UserInterface.infoIcon);// set Icon from the class UserInterface
		info.setToolTipText("Infos über iBo");
		info.addActionListener(new ActionListener() {	// switch the CardLayout to info
			@Override
			public void actionPerformed(ActionEvent e) {
				if(menuClosed)
				{
					switchMenu();
				}
				cardLayout.show(panels, INFO);
				System.out.println("open Menu -- info");
			}
		});
		
		// help Button
		help = new MenuButton("");
		help.setBigIcon(UserInterface.helpIcon);
		help.setToolTipText("Hilfe zum Programm");
		help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(menuClosed)
				{
					switchMenu();
				}
				cardLayout.show(panels, HELP);
				System.out.println("open Menu -- Help");
				
			}
		});
		// linestart Panel add
		linestartPanel.add(info);
		linestartPanel.add(help);
		
		// set Constrain to set the linestartPanel to the 1 point of the GridBagConstrain
		constrain.gridheight = 1;
		constrain.gridwidth = 1;
		constrain.gridx = 0;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		contentPanel.add(linestartPanel,constrain); // finally add
		//
		
		
		// collapse or show menue	
		collapseButton = new MenuOpenCloseButton("");
		collapseButton.setBigIcon(UserInterface.arrowDonwIcon);
		collapseButton.setToolTipText("Menü ein - oder ausklappen");
		collapseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchMenu();
			}
		});

		// set collapse Button to GridBagLayout
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 1;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;

		contentPanel.add(collapseButton,constrain);
		//
		
		lineendPanel = new JPanel();
		lineendPanel.setOpaque(false);
			//prefernces Menu Buttone defination
		preferences = new MenuButton("");
		preferences.setBigIcon(UserInterface.settingsIcon);
		preferences.setToolTipText("Einstellungen ändern");
		preferences.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(menuClosed){
					switchMenu();
				}
				cardLayout.show(panels, PREFERENCES);
				System.out.println("open Menu -- preferences");
				
			}
		});
			//record Folder defination
		recordFolder = new RecordingFolderButton("", this.audioCore);
		recordFolder.setBigIcon(UserInterface.folderIcon);
		recordFolder.setToolTipText("Aufnahmeordner öffnen");
		recordFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File(Preferences.getInstance().getRecordFolder()));
					System.out.println("open Menu -- open record Path");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
			// soundPack 
		soundPack  = new MenuButton("");
		soundPack.setToolTipText("Soundpack wählen");
		soundPack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(menuClosed){
					switchMenu();
				}
				cardLayout.show(panels, SOUNDPACK);
				System.out.println("open Menu -- SoundPack");
				
			}
		});
			// add Menu Buttons to lineend Panel
		lineendPanel.add(preferences);
		lineendPanel.add(soundPack);
		lineendPanel.add(recordFolder);
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 2;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		contentPanel.add(lineendPanel,constrain);
		
	
		soundPackPanel = new JPanel();
		soundPackPanel.setOpaque(false);
		soundPackList = new SoundPackList(this.soundPackManager.getSoundpacksInDirectory("./"), this.userInterface);
		
		soundPackScrollPane = new JScrollPane(soundPackList);
		soundPackScrollPane.setPreferredSize(new Dimension(200,120));
		soundPackScrollPane.setBorder(null);
		soundPackScrollPane.getViewport().setOpaque(false);
		soundPackScrollPane.setOpaque(false);
		
		soundPackPanel.add(soundPackScrollPane);
		
		
		//Card Layout items
		cardLayout = new CardLayout();
		
		panels = new JPanel();
		panels.setLayout(cardLayout);
		panels.setOpaque(false);
		panels.setBackground(new Color(0,0,0,0.5f));
		panels.setVisible(false);
		
		
		
		infoPanel = new JPanel();
		infoPanel.setMaximumSize(new Dimension(500,500));
		infoLabel = new JLabel(infomartion);
		
		
		helpPanel = new JPanel();
		helpLabel = new JLabel("<html><b>ejfsdakljfdskfs<br />dakjfsjadkljklsdf</b></html>");
		
		preferencesPanel = new JPanel();
		preferencesPanel.setBackground(UserInterface.alphaColor(Color.BLACK, 0.3f));
		
		fileChooser = new JFileChooser();
		
		changeDirectory = new JButton("Aufnahmeort ändern");
		changeDirectory.setFocusable(false);
		changeDirectory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setCurrentDirectory(new File(Preferences.getInstance().getRecordFolder()));
				int choice = fileChooser.showOpenDialog(null);
				
				if(choice == JFileChooser.APPROVE_OPTION){
					Preferences.getInstance().setRecordFolder(fileChooser.getSelectedFile().toString());
				}
			}
		});
		
		infoPanel.add(infoLabel);
		helpPanel.add(helpLabel);
		preferencesPanel.add(changeDirectory);
		
		panels.add(soundPackPanel,SOUNDPACK);
		panels.add(infoPanel,INFO);
		panels.add(helpPanel, HELP);
		panels.add(preferencesPanel,PREFERENCES);
		

		constrain.gridheight = 1;
		constrain.gridwidth = 3;
		constrain.gridx = 0;
		constrain.gridy = 2;
		constrain.fill = GridBagConstraints.BOTH;
		
		contentPanel.add(panels,constrain);
		contentPanel.setVisible(true);

		add(contentPanel);
		setVisible(true);
		
	}
	
	/**
	 * 
	 */
	
	public void switchMenu()
	{
		if(!menuClosed){
			panels.setVisible(false);
			collapseButton.setBigIcon(UserInterface.arrowDonwIcon);
		}else{
			panels.setVisible(true);
			collapseButton.setBigIcon(UserInterface.arrowUpIcon);
		}
		menuClosed = !menuClosed;
	}
	
	
}
