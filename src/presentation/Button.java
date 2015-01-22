package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ButtonUI;



public class Button extends JButton {

	private Image icon;

	public Button(String label){
		setText(label);
		setSize(100,100);
		setOpaque(false);
		

		
		setUI(new ButtonUI() {
			
			protected void paintText(Graphics g, AbstractButton b,
					Rectangle textRect, String text){
				
				g.setColor(b.getForeground());
				g.drawString(b.getText(), 5, 15);
			}
			
			
			public void paint(Graphics g1, JComponent c){
				Graphics2D g = (Graphics2D)g1;
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setPaint(new GradientPaint(0, 0, c.getBackground(), c.getWidth(), c.getHeight(), Color.black));
				
				AbstractButton b = (AbstractButton)c;
				
				
				if(b.getModel().isEnabled()){
					
					// Draw the enabled state of the button
					g.fillRoundRect(0, 0, b.getWidth(), b.getHeight(),10,10);
					
					// Paint a nice reflection effect
					g.setPaint(new GradientPaint(0, 0, UserInterface.alphaColor(Color.white, 0.4f), 0, c.getHeight()/2, UserInterface.alphaColor(Color.white, 0.05f)));
					g.fillRoundRect(0, 0, c.getWidth(), c.getHeight()/2,10,10);
					
				}
				
				// Paint the other elements on the button
				paintText(g, b, getBounds(), getText());
				paintIcon(g, c, getBounds());
						
			}
			
			protected void paintIcon(Graphics g, JComponent c,
					Rectangle iconRect){
				
				if(icon != null)
				{
					int iconWidth = icon.getWidth(Button.this);
					int iconHeight = icon.getHeight(Button.this);
					if(iconWidth/iconHeight != getWidth()/getHeight()){
						if(iconWidth - getWidth() < iconHeight - getHeight()){
							// Scale icon to fit vertically, keep aspect ratio
							g.drawImage(icon, getWidth()/2-getHeight()/2, 0, (int) (getHeight()*((float)iconWidth/iconHeight)), getHeight(), c);
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
		
		setVisible(true);
	}
	
	
	public void setImage(Image icon){
		this.icon = icon; 
	}
	
	public void setPreferredSize(int x, int y){
		setPreferredSize(new Dimension(x,y));
	}
	
	public void setPreferredSize(Dimension d){
		setPreferredSize(d);
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
