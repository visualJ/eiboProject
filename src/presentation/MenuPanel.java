package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;


  
public class MenuPanel extends JPanel{

	private JPanel content;
	private GridBagConstraints constrain;
	

	
	  
	 
	private static final long serialVersionUID = -8244586608783664164L;

	public MenuPanel(){
	
		content = new JPanel();
		
	
		JButton button = new JButton();
		JButton button2 = new JButton(); 
		
		setLayout(new FlowLayout());
		setOpaque(true);
		setBackground(new Color(100,100,100));
		
		content.setLayout(new GridBagLayout());
		constrain = new GridBagConstraints();
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 1;
		constrain.gridy = 1;
		constrain.fill = GridBagConstraints.BOTH;
		content.add(button,constrain);
		
		constrain.gridwidth = 1;
		constrain.gridheight = 1;
		constrain.gridx = 2;
		constrain.gridy = 1;
		constrain.fill = GridBagConstraints.BOTH;
		button.setText("Hallo");
		button.setBackground(new Color(255,0,0));
		button.setVisible(true);
		
		constrain.fill = 3;
		button2.setText("Tschuess");
		button2.setBackground(new Color(0,255,0));
		button2.setVisible(true);
	
		content.add(button2,constrain);
		content.setPreferredSize(new Dimension(600,200));
		content.setOpaque(true);
		content.setBackground(new Color(255,0,0));
		content.setVisible(true);
		
		
		
	
	
		

		add(content);
		setVisible(true);
		
	}
	
	public void setVisible(boolean visible)
	{
		content.setVisible(visible);
	}
	
	
}
