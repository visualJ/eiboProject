package presentation;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/** **/
public class MenuPanel extends JFrame {

	final JFrame frame = new JFrame();
	final int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	final int height =(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private JButton closeButton;
	private JButton minButton;
	public MenuPanel(){
		
		
		frame.setSize(width, height/2);
		frame.setUndecorated(true);
		frame.setBackground(new Color(100,100,100,100));
		frame.setAlwaysOnTop(true);
		frame.setLayout(null);
		
		closeButton = new JButton("close");
		closeButton.setBounds(width - 400, 20, 100, 50);
		
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
				
			}
		});
		
		minButton = new JButton("Fenster Minimieren");
		minButton.setBounds(50, 50, 150, 50);
		minButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "Bitte Programm über Alt + F4 beenden");
				frame.setVisible(false);
				
			}
		});
		
		
		
		frame.add(closeButton);
		frame.add(minButton);
	
		frame.setVisible(true);
		
		
	}
}
