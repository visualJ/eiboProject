package presentation;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int NUMBER_OF_BARS = 100;
	private static final float HORIZON = 0.4f;

	private float[] bars = new float[NUMBER_OF_BARS];
	private int pos = 0;
	private float beatIndicatorAlpha = 0;
	private int bpm = 120;
	private Thread beatIndicatorThread;
	private Runnable beatIndicatorAnimation = new Runnable(){
		@Override
		public void run() {
			// Slowly decrease the alpha value over time
			for(int i = 9;i>=0;i--){
				beatIndicatorAlpha = 0.4f * i / 9;
				repaint();
				try {
					Thread.sleep(6000/bpm);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			repaint();
		}
	};
	
	/**
	 * A background panel with different visualizers
	 */
	public BackgroundPanel(){
		super();
		setOpaque(false);
		setBackground(Color.black);
		setForeground(new Color(170,20,240));
	}

	public int getBpm() {
		return bpm;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
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
	
	/**
	 * Visualize a beat.
	 * This method should be called once every beat at
	 * the set beats per minute.
	 */
	public void beat(){
		// Start or restart the animation
		if(beatIndicatorThread!=null) beatIndicatorThread.interrupt();
		beatIndicatorThread = new Thread(beatIndicatorAnimation);
		beatIndicatorThread.start(); 
	}
	
	@Override
	public void paint(Graphics g) {
		
		// Prepare graphics context
		Graphics2D graphics = (Graphics2D)g;
		graphics.setColor(getBackground());
		graphics.fillRect(0, 0, getWidth(), getHeight());
		
		// draw beat indicator
		graphics.setPaint(new GradientPaint(0, HORIZON*getHeight(), alphaColor(getForeground(), beatIndicatorAlpha), 0, 0, getBackground()));
		paintBeatIndicator(graphics);
		
		// draw bars
		graphics.setColor(getForeground());
		for(int i = 0; i < bars.length; i++){
			paintBar(i, graphics, false);
		}
		
		// draw reflections
		graphics.setPaint(new GradientPaint(0, Math.round(HORIZON*getHeight()), alphaColor(getForeground(), 0.25f), 0, Math.round(HORIZON*getHeight()*1.5f), alphaColor(getForeground(), 0f)));
		for(int i = 0; i < bars.length; i++){
			paintBar(i, graphics, true);
		}
		
		// draw child elements
		super.paint(g);
	}
	
	/**
	 * Creates a new Color with an alpha value
	 * @param color The color to use
	 * @param alpha The alpha value the new color should have
	 * @return A new color with the specified alpha value
	 */
	private Color alphaColor(Color color, float alpha){
		return new Color(color.getRed(),color.getGreen(),color.getBlue(),Math.round(255*alpha));
	}
	
	/**
	 * Paints a subtle pulsing beat indicator
	 * @param graphics The graphics object to paint on
	 */
	private void paintBeatIndicator(Graphics2D graphics){
		graphics.fillRect(0,0, getWidth(), (int) Math.floor(HORIZON*getHeight()));
	}
	
	/**
	 * Paints a bar or the reflection of a bar
	 * @param i The bar's index
	 * @param graphics The graphics object to paint on
	 * @param reflection If true, the bar is drawn as a reflection
	 */
	private void paintBar(int i, Graphics2D graphics, boolean reflection){
		int x = (int) Math.ceil((i*getWidth() / (float) bars.length));
		int y = (int) (Math.floor(getHeight() * HORIZON));
		int width = (int) Math.ceil(getWidth() / (float) bars.length / 3);
		int height = (int) Math.floor(getHeight() * HORIZON * bars[(i + pos) % bars.length] ) * (reflection?1:-1);
		graphics.fillRect(x, y, width, height);
	}
	
	
}
