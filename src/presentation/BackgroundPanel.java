package presentation;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import repository.BeatListener;
import services.AudioCore;

/**
 * A backgroundpanel that can display a beat and a waveform.
 * It can also contain and display other components.
 * @author Benedikt Ringlein
 *
 */
public class BackgroundPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int NUMBER_OF_BARS = 100;
	private static final float HORIZON = 0.4f;
	private static final Color COLOR_FIRST_BEAT = new Color(240,20,170);
	private static final Color COLOR_HIGHLIGHT = new Color(170,20,240);

	private AudioCore audioCore;
	private Color beatColor = COLOR_FIRST_BEAT;
	private float[] bars = new float[NUMBER_OF_BARS];
	private int barsOffset = 0;
	private Thread barOffsetThread;
	private Runnable barOffsetAnimation = new Runnable(){
		@Override
		public void run() {
			while(!Thread.interrupted()){
				update(audioCore.getOutputLevel());
				for(int i=4;i>=0;i--){
					barsOffset = Math.round(i/4f*getWidth() / (float) bars.length);
					repaint();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
				barsOffset = 0;
			}
		}
	};
	private int pos = 0;
	private float beatIndicatorAlpha = 0;
	private int bpm = 60;
	private Thread beatIndicatorThread;
	private Runnable beatIndicatorAnimation = new Runnable(){
		@Override
		public void run() {
			// Slowly decrease the alpha value over time
			int framesPerBeat = 1500/bpm; // 25fps * 60bpm
			for(int i = framesPerBeat-1;i>=0;i--){
				beatIndicatorAlpha = 0.4f * i / (framesPerBeat-1);
				repaint();
				try {
					Thread.sleep(60000/(framesPerBeat)/bpm);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	};
	
	/**
	 * A background panel with different visualizers
	 */
	public BackgroundPanel(AudioCore audioCore){
		super();
		this.audioCore = audioCore;
		setOpaque(false);
		setBackground(Color.black);
		setForeground(COLOR_HIGHLIGHT);
		
		// Add a beat listener, so that the UI can react to beats
		audioCore.addBeatListener(new BeatListener() {
			@Override
			public void beat(int beatInBar) {
				showBeat(beatInBar);
			}
			@Override
			public void bpmChanged(int bpm) {
				setBpm(bpm);
			}
		});
		
		// Set the bpm right
		setBpm(audioCore.getBpm());
		
		// Start the bars waveform graph animation thing
		barOffsetThread = new Thread(barOffsetAnimation);
		barOffsetThread.start();
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
	}
	
	/**
	 * Visualize a beat.
	 * This method should be called once every beat at
	 * the set beats per minute.
	 */
	public void showBeat(int beatInBar){
		// Set beat color
		beatColor = (beatInBar==0)?COLOR_FIRST_BEAT:getForeground();
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
		graphics.setPaint(new GradientPaint(0, HORIZON*getHeight(), UserInterface.alphaColor(beatColor, beatIndicatorAlpha), 0, 0, getBackground()));
		paintBeatIndicator(graphics);
		
		// draw bars
		graphics.setColor(getForeground());
		for(int i = 0; i < bars.length; i++){
			paintBar(i, graphics, false);
		}
		
		// draw reflections
		graphics.setPaint(new GradientPaint(0, Math.round(HORIZON*getHeight()), UserInterface.alphaColor(getForeground(), 0.25f), 0, Math.round(HORIZON*getHeight()*1.5f), UserInterface.alphaColor(getForeground(), 0f)));
		for(int i = 0; i < bars.length; i++){
			paintBar(i, graphics, true);
		}
		
		// draw child elements
		super.paint(g);
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
		int x = (int) (Math.ceil((i*getWidth() / (float) bars.length))+barsOffset);
		int y = (int) (Math.floor(getHeight() * HORIZON));
		int width = (int) Math.ceil(getWidth() / (float) bars.length / 3);
		int height = (int) Math.floor(getHeight() * HORIZON * bars[(i + pos) % bars.length] ) * (reflection?1:-1);
		graphics.fillRect(x, y, width, height);
	}
	
	
}
