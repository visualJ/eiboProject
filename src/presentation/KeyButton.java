package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import repository.ActivationModeBehavior;
import repository.KeyMapping;
import services.AudioCore;

public class KeyButton extends JButton {

	private static final long serialVersionUID = 1L;
	private KeyMapping keyMapping;
	private ImageIcon keyIcon;
	private AudioCore audioCore;
	private ActivationModeBehavior AMBehavior;
	
	public KeyButton(String keyLabel, AudioCore audioCore){
		this.audioCore = audioCore;
		this.AMBehavior = ActivationModeBehavior.getInstance(audioCore);
		setText(keyLabel);
		setPreferredSize(new Dimension(60, 60));
		setBorderPainted(false);
		
		setUI(new BasicButtonUI() {
			@Override
			protected void paintText(Graphics g, AbstractButton b,
					Rectangle textRect, String text) {
				super.paintText(g, b, textRect, null);
				g.setColor(Color.white);
				g.drawString(b.getText(), 5, 15);
			}
			
			@Override
			public void paint(Graphics g, JComponent c) {
				AbstractButton button =  (AbstractButton)c;
				if(button.getModel().isPressed()){
					g.setColor(Color.green);
				}else if(button.getModel().isRollover()){
					g.setColor(new Color(0,100,0));
				}else{
					g.setColor(Color.black);
				}
				g.fillRect(0, 0, button.getWidth(), button.getHeight());
				paintText(g, button, getBounds(), getText());
				paintIcon(g, c, getBounds());
			}

			@Override
			protected void paintIcon(Graphics g, JComponent c,
					Rectangle iconRect) {
				if(keyIcon != null){
					g.drawImage(keyIcon.getImage(), 0, 0, getWidth(), getHeight(), c);
				}
				
			}
			
			@Override
			protected void paintButtonPressed(Graphics g, AbstractButton b) {
				g.setColor(Color.green);
				g.fillRect(0, 0, b.getWidth(), b.getHeight());
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				AMBehavior.trigger(keyMapping.getActivationMode(), keyMapping.getSoundSample());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				AMBehavior.untrigger(keyMapping.getActivationMode(), keyMapping.getSoundSample());
			}
		});
	}

	public KeyMapping getKeyMapping() {
		return keyMapping;
	}

	public void setKeyMapping(KeyMapping keyMapping) {
		this.keyMapping = keyMapping;
		keyIcon = new ImageIcon(keyMapping.getImageFile());
	}
	
}
