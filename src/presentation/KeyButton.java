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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import repository.KeyMapping;
import repository.SampleListener;
import repository.SoundSample;
import services.ActivationModeBehavior;
import services.AudioCore;

/**
 * A button that can display a key label, an activation mode icon and a sample icon.
 * You just have to give it a key mapping, and it does all the work, like playing samples when clicked.
 * @author Benedikt Ringlein
 *
 */
public class KeyButton extends JButton {

	private static final long serialVersionUID = 1L;
	private static Color VIOLETT = new Color(170,20,240);
	private static Color ORANGE = new Color(240,170,20);
	private static Color GREEN = new Color(170,240,20);
	
	private KeyMapping keyMapping;
	private ImageIcon keyIcon;
	private Image activationModeIcon;
	private ActivationModeBehavior activationModeBehavior;
	private SampleState sampleState = SampleState.NORMAL;
	private AudioCore audioCore;
	private SampleListener sampleListener;
	
	public KeyButton(String keyLabel, AudioCore audioCore){
		this.activationModeBehavior = ActivationModeBehavior.getInstance();
		this.audioCore = audioCore;
		setText(keyLabel);
		setPreferredSize(new Dimension(60, 60));
		setBorderPainted(false);
		setOpaque(false);
		
		setUI(new BasicButtonUI() {
			@Override
			protected void paintText(Graphics g, AbstractButton b,
					Rectangle textRect, String text) {
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
					switch (sampleState) {
					case NORMAL:
						g.setPaint(new GradientPaint(0, 0, VIOLETT, c.getWidth(), c.getHeight(), Color.black));
						break;
					case PLAYING:
						g.setPaint(new GradientPaint(0, 0, GREEN, c.getWidth(), c.getHeight(), Color.black));
						break;
					case WAITING:
						g.setPaint(new GradientPaint(0, 0, ORANGE, c.getWidth(), c.getHeight(), Color.black));
						break;
					default:
						break;
					}
					g.fillRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
					if(button.getModel().isPressed()){
						g.setColor(UserInterface.alphaColor(Color.black, 0.5f));
						g.setStroke(new BasicStroke(2));
						g.fillRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
						g.drawRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
						g.translate(getWidth()/2, getHeight()/2);
						g.scale(0.95, 0.95);
						g.translate(-getWidth()/2, -getHeight()/2);
					}else if(button.getModel().isRollover()){
						g.setColor(UserInterface.alphaColor(Color.black, 0.2f));
						g.fillRoundRect(0, 0, button.getWidth(), button.getHeight(),10,10);
					}
				}else{
					g.setColor(UserInterface.alphaColor(Color.darkGray, 0.2f));
					g.fillRect(0, 0, button.getWidth(), button.getHeight());
				}
				paintText(g, button, getBounds(), getText());
				paintIcon(g, c, getBounds());
				paintActivationModeIcon(g, c);
			}

			@Override
			protected void paintIcon(Graphics g, JComponent c,
					Rectangle iconRect) {
				if(keyIcon != null){
						g.drawImage(keyIcon.getImage(), 0, 0, getWidth(), getHeight(), c);
				}
			}
			
			protected void paintActivationModeIcon(Graphics g, JComponent c){
				if(activationModeIcon!=null){
					g.drawImage(activationModeIcon, c.getWidth()-20, 3, 15, 15, c);
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(keyMapping != null){
					activationModeBehavior.trigger(keyMapping.getActivationMode(), keyMapping.getSoundSample());
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(keyMapping != null){
					activationModeBehavior.untrigger(keyMapping.getActivationMode(), keyMapping.getSoundSample());
				}
			}
		});
	}

	public KeyMapping getKeyMapping() {
		return keyMapping;
	}

	public void setKeyMapping(KeyMapping keyMapping) {
		this.keyMapping = keyMapping;
		if(sampleListener != null){
			// Remove the sample listener from the audio core
			audioCore.removeSampleListener(sampleListener);
		}
		if(keyMapping != null){
			keyIcon = new ImageIcon(keyMapping.getImageFile());
			activationModeIcon = UserInterface.getActivationModeImage(keyMapping.getActivationMode());
			
			// Set a new samplelistener in the audiocore
			sampleListener = new SampleListener() {
				
				@Override
				public void stoppedSample() {
					sampleState = SampleState.NORMAL;
				}
				
				@Override
				public void sheduledSample() {
					sampleState = SampleState.WAITING;
				}
				
				@Override
				public void playedSample() {
					sampleState = SampleState.PLAYING;
				}
				
				@Override
				public SoundSample getSample() {
					return KeyButton.this.keyMapping.getSoundSample();
				}
			};
			audioCore.addSampleListener(sampleListener);
		}else{
			activationModeIcon = null;
		}
	}
	
	private enum SampleState{
		NORMAL, WAITING, PLAYING
	}
	
}
