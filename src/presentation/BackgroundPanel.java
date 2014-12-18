package presentation;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int NUMBER_OF_BARS = 100;
	private static final float HORIZON = 0.3f;

	private float[] bars = new float[NUMBER_OF_BARS];
	private GradientPaint gradient;
	private int pos = 0;
	
	public BackgroundPanel(){
		super();
		setOpaque(false);
		setBackground(Color.black);
		setForeground(Color.blue);
		gradient = new GradientPaint(0, 0, Color.blue, 0, 1000, Color.black);
	}
	
	/**
	 * Updates the Graph with a new value
	 * @param value New value to append to the right
	 */
	public void update(float value){
		
		// Shift the graph
		pos = (pos+1)%bars.length; 
		
		// Set the new value on the right end
		bars[(pos+bars.length-1)%bars.length] = value; 
		
		// repaint the panel
		repaint(); 
	}
	
	@Override
	public void paint(Graphics g) {
		
		// Prepare graphics context
		Graphics2D graphics = (Graphics2D)g;
		graphics.setColor(getBackground());
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.setColor(getForeground());
		
		// draw bars
		for(int i = 0; i < bars.length; i++){
			paintBar(i, graphics, false);
		}
		
		// draw reflections
		graphics.setPaint(gradient);
		for(int i = 0; i < bars.length; i++){
			paintBar(i, graphics, true);
		}
		
		// draw child elements
		super.paint(g);
	}
	
	/**
	 * Paints a bar or the reflection of a bar
	 * @param i The bar's index
	 * @param graphics THe graphics object to paint on
	 * @param reflection If true, the bar is drawn as a reflection
	 */
	private void paintBar(int i, Graphics2D graphics, boolean reflection){
		graphics.fillRect(
				/*X*/ Math.round(i * (getWidth() / (float) bars.length)),
				/*Y*/ Math.round(getHeight() * HORIZON - ((reflection) ? 0 : getHeight() * HORIZON * bars[(i + pos) % bars.length] )), 
				/*W*/ Math.round(getWidth() / bars.length / 2), 
				/*H*/ (int) Math.ceil(getHeight() * HORIZON * bars[(i + pos) % bars.length] ));
	}
	
	
}
