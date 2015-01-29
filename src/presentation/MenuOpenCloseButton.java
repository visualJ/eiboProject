package presentation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

public class MenuOpenCloseButton extends MenuButton{

	private static final long serialVersionUID = 1L;
	private Image icon;
	
	public MenuOpenCloseButton(String keyLabel) {
		super(keyLabel);
		setPreferredSize(new Dimension(200,30));
		// TODO Auto-generated constructor stub
		
		setUI(new BasicButtonUI() {
			
			@Override
			protected void paintText(Graphics g, AbstractButton b,
					Rectangle textRect, String text) {
				g.setColor(b.getForeground());
				g.drawString(b.getText(), 5, 15);
			}
			
			@Override
			public void paint(Graphics g1, JComponent c) {
				
				// Prepare graphics for painting the button
				Graphics2D g = (Graphics2D)g1;
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setPaint(new GradientPaint(0, 0, c.getBackground(), c.getWidth(), c.getHeight(), Color.black));
				
				// Get the button
				AbstractButton button =  (AbstractButton)c;
				
				if(button.getModel().isEnabled()){
					
					// Draw the enabled state of the button
					g.fillRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
					
					// Paint a nice reflection effect
					g.setPaint(new GradientPaint(0, 0, UserInterface.alphaColor(Color.white, 0.4f), 0, c.getHeight()/2, UserInterface.alphaColor(Color.white, 0.05f)));
					g.fillRoundRect(0, 0, c.getWidth(), c.getHeight()/2,10,10);
					
					
					if(button.getModel().isPressed()){
						
						// Draw pressed state
						g.setColor(UserInterface.alphaColor(Color.black, 0.5f));
						g.setStroke(new BasicStroke(2));
						g.fillRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
						g.drawRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
						g.translate(getWidth()/2, getHeight()/2);
						g.scale(0.95, 0.95);
						g.translate(-getWidth()/2, -getHeight()/2);
						
					}else if(button.getModel().isRollover()){
						
						// Draw mouse over state
						g.setColor(UserInterface.alphaColor(Color.black, 0.2f));
						g.fillRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
					}
				}else{
					// Draw the disabled state of the button
					g.setColor(UserInterface.alphaColor(Color.darkGray, 0.2f));
					g.fillRect(0, 0, button.getWidth(), button.getHeight());
				}
				
				// Paint the other elements on the button
				paintText(g, button, getBounds(), getText());
				paintIcon(g, c, getBounds());
				
				
			}
	
			@Override
			protected void paintIcon(Graphics g, JComponent c,
					Rectangle iconRect) {
				if(icon != null){
					int iconWidth = icon.getWidth(MenuOpenCloseButton.this);
					int iconHeight = icon.getHeight(MenuOpenCloseButton.this);
					if(iconWidth/iconHeight != getWidth()/getHeight()){
						if(iconWidth - getWidth() < iconHeight - getHeight()){
							// Scale icon to fit vertically, keep aspect ratio
							g.drawImage(icon, 50, 0, 100, getHeight(), c);
						}else{
							// Scale icon to fit horizontally, keep aspect ratio
							g.drawImage(icon, 0, getHeight()/2-getWidth()/2, getWidth(), (int) (getWidth()*((float)iconWidth/iconHeight)), c);
						}
					}else{
						// Scale Icon to full button size
						g.drawImage(icon, 0, 0, getWidth(), getHeight(), c);
					}
				}
			}
			
			
		});
	}
		
		

	
	public Image getBigIcon() {
		return icon;
	}

	/**
	 * Sets the big icon that is displayed on the button
	 * @param bigIcon The icon to show
	 */
	public void setBigIcon(Image bigIcon) {
		this.icon = bigIcon;
	}

	}

	/**
	 * 
	 */

	
	

