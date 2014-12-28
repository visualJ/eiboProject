package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import repository.ActivationModeBehavior;
import repository.KeyMapping;

public class KeyButton extends JButton {

	private static final long serialVersionUID = 1L;
	private KeyMapping keyMapping;
	private ImageIcon keyIcon;
	private Image AMIcon;
	private ActivationModeBehavior AMBehavior;
	
	public KeyButton(String keyLabel){
		this.AMBehavior = ActivationModeBehavior.getInstance();
		setText(keyLabel);
		setPreferredSize(new Dimension(60, 60));
		setBorderPainted(false);
		setOpaque(false);
		
		setUI(new BasicButtonUI() {
			@Override
			protected void paintText(Graphics g, AbstractButton b,
					Rectangle textRect, String text) {
				super.paintText(g, b, textRect, null);
				g.setColor(Color.white);
				g.drawString(b.getText(), 5, 15);
			}
			
			@Override
			public void paint(Graphics g1, JComponent c) {
				Graphics2D g = (Graphics2D)g1;
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				AbstractButton button =  (AbstractButton)c;
				if(keyMapping != null){
					if(button.getModel().isPressed()){
						g.setPaint(new GradientPaint(0, 0, new Color(120,85,10), c.getWidth(), c.getHeight(), Color.black));
					}else if(button.getModel().isRollover()){
						g.setPaint(new GradientPaint(0, 0, new Color(240,170,20), c.getWidth(), c.getHeight(), Color.black));
					}else{
						g.setPaint(new GradientPaint(0, 0, new Color(170,20,240), c.getWidth(), c.getHeight(), Color.black));
					}
					g.fillRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
				}else{
					g.setColor(UserInterface.alphaColor(Color.darkGray, 0.2f));
					g.fillRect(0, 0, button.getWidth(), button.getHeight());
				}
				paintText(g, button, getBounds(), getText());
				paintIcon(g, c, getBounds());
				if(AMIcon!=null){
					g.drawImage(AMIcon, c.getWidth()-20, 3, 15, 15, c);
				}
			}

			@Override
			protected void paintIcon(Graphics g, JComponent c,
					Rectangle iconRect) {
				if(keyIcon != null){
					AbstractButton button =  (AbstractButton)c;
					if(button.getModel().isPressed()){
						g.drawImage(keyIcon.getImage(), 2, 2, getWidth()-4, getHeight()-4, c);
					}else{
						g.drawImage(keyIcon.getImage(), 0, 0, getWidth(), getHeight(), c);
					}
				}
				
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(keyMapping != null){
					AMBehavior.trigger(keyMapping.getActivationMode(), keyMapping.getSoundSample());
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(keyMapping != null){
					AMBehavior.untrigger(keyMapping.getActivationMode(), keyMapping.getSoundSample());
				}
			}
		});
	}

	public KeyMapping getKeyMapping() {
		return keyMapping;
	}

	public void setKeyMapping(KeyMapping keyMapping) {
		this.keyMapping = keyMapping;
		if(keyMapping != null){
			keyIcon = new ImageIcon(keyMapping.getImageFile());
			AMIcon = UserInterface.getActivationModeImage(keyMapping.getActivationMode());
		}else{
			AMIcon = null;
		}
	}
	
}
