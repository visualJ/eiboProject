package presentation;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
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

	private final Color BLACK = new Color(0,0,0);
	
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
	

	private SoundPackManager manager;
	private JList<SoundPack> liste;
	
	private SoundPackManager spm;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8244586608783664164L;

	public MenuPanel(){
		
		spm = new SoundPackManager();
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
		info = new MenuButton("i");
		help = new MenuButton("h");
		
		
		info.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("i wurde gedrückt");
				
			}
		});
		
		linestartPanel.setSize(new Dimension(150,50));
		linestartPanel.setBackground(BLACK);
	
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
				// TODO Auto-generated method stub
				
				switchMenu();
				System.out.println("menu open is " + menuOpen);
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
				// TODO Auto-generated method stub
				try {
					Desktop.getDesktop().open(new File("./"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		preferences.setText("p");
		lineendPanel.setBackground(BLACK);
		
		lineendPanel.add(preferences);
		lineendPanel.add(recordFolder);
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 2;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		content.add(lineendPanel,constrain);
		
		
		
		listPanel = new JPanel();
		liste = new JList(getSoundpackNames("./"));
		
		liste.setPreferredSize(new Dimension(200,60));
		
		listPanel.setBackground(BLACK);
		listPanel.add(liste);
		
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
	
		menuOpen = !menuOpen;
	}
	
	private String[] getSoundpackNames(String path){
		SoundPack[] soundpacks;
		
		
		soundpacks = spm.getSoundpacksInDirectory(path);
		String[] soundpacknames = new String[soundpacks.length];
		for(int i = 0; i < soundpacks.length; i++)
		{
			soundpacknames[i] = soundpacks[i].getPackName();
		}
		
		return soundpacknames;
	}
	
	private SoundPack getSoundpack(String name,String path)
	{
		SoundPack[] soundpacks;
		soundpacks = spm.getSoundpacksInDirectory(path);
		for(int i = 0; i < soundpacks.length; i++)
		{
			if(soundpacks[i].getPackName().equals(name)){
				return soundpacks[i];
			}
		}
		System.out.println("exeption handling ... sounpack not found exeption");
		return null;
	}
	
	
}
