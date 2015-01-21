package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


/** 
 * This Class create the upperPanel 
 * 
 * **/
public class MenuPanel extends JPanel{

	private JPanel content;
	private GridBagConstraints constrain;
	private JButton einklappen;
	
	private JButton preferences;
	private JButton info;
	private JButton help;
	private JPanel compactPanel;
	
	private JButton recordFolder;
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8244586608783664164L;

	public MenuPanel(){
	
		content = new JPanel();

		setLayout(new FlowLayout());
		setOpaque(true);
		setBackground(new Color(255,255,255,0));
		
		
		content.setPreferredSize(new Dimension(600,200));
		content.setOpaque(true);
		content.setBackground(new Color(255,0,0));
		
		content.setLayout(new GridBagLayout());
		constrain = new GridBagConstraints();
		
		einklappen = new JButton();
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 0;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		einklappen.setText("einklappn");
		einklappen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				closeMenu();
				System.out.println("hallo ich wurde geklickt");
			}
		});
		content.add(einklappen,constrain);
		
		compactPanel = new JPanel();
		preferences = new JButton();
		info = new JButton();
		help = new JButton();
		
		preferences.setText("p");
		preferences.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame();
				frame.setSize(new Dimension(500,500));
				frame.setTitle("this would be the preference Frame");
				
				frame.setVisible(true);
				
			}
		});
		info.setText("i");
		info.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame();
				frame.setSize(new Dimension(500,500));
				frame.setTitle("this would be the info Frame");
				frame.setVisible(true);
				
			}
		});
		help.setText("h");
		help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame();
				frame.setSize(new Dimension(500,500));
				frame.setTitle("this would be the help Frame");
				frame.setVisible(true);
				
			}
		});
		compactPanel.add(preferences);
		compactPanel.add(info);
		compactPanel.add(help);
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 1;
		constrain.gridy = 0;
		constrain.fill = GridBagConstraints.BOTH;
		
		content.add(compactPanel,constrain);
		
		recordFolder = new JButton();
		recordFolder.setText("open Record");
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 1;
		constrain.gridy = 1;
		constrain.fill = GridBagConstraints.BOTH;
		
		content.add(recordFolder,constrain);
		
		


  
		
		content.setVisible(true);
		
		
	
	
		

		add(content);
		setVisible(true);
		
	}
	
	public void closeMenu()
	{
		this.setSize(1000, 50);
	}
	
	
}
