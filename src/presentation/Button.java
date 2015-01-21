package presentation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

public class Button extends JButton {

	private Image iconImage;
	

	public Button(Image iconImage){
		this.iconImage = iconImage;
		
		setUI(new BasicButtonUI() {
		
			public void paint(Graphics g1,JComponent c)
			{
				Graphics2D g2d = (Graphics2D)g1;
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2d.setPaint(new GradientPaint(0, 0, c.getBackground(), c.getWidth(), c.getHeight(), Color.black));
			}
			
		
	
	});
	}
	
	
	
}
