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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * A button that can display a key label, a big icon and a small icon.
 * It can get Runnables to run when pressed or released. Those
 * are also run when the according action happens on the actual keyboard.
 * @author Benedikt Ringlein
 *
 */
public class KeyButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	private Image bigIcon;
	private Image smallIcon;
	private int keyCode;
	private Runnable onTrigger;
	private Runnable onUntrigger;
	
	public KeyButton(String keyLabel, int keyCode){
		this.keyCode = keyCode;
		
		setText(keyLabel);
		setPreferredSize(new Dimension(60, 60));
		setBorderPainted(false);
		setOpaque(false);
		setFocusable(false);
		setForeground(Color.white);
		setBackground(Color.gray);
		
		// Initialize Triggers
		setOnTrigger(null);
		setOnUntrigger(null);
		
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
					g.setPaint(new GradientPaint(0, 0, UserInterface.alphaColor(Color.white, 0.3f), 0, c.getHeight(), UserInterface.alphaColor(Color.white, 0.0f)));
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
				paintSmallIcon(g, c);
				
			}
	
			@Override
			protected void paintIcon(Graphics g, JComponent c,
					Rectangle iconRect) {
				if(bigIcon != null){
					g.drawImage(bigIcon, 0, 0, getWidth(), getHeight(), c);
				}
			}
			
			protected void paintSmallIcon(Graphics g, JComponent c){
				if(smallIcon!=null){
					g.drawImage(smallIcon, c.getWidth()-20, 3, 15, 15, c);
				}
			}
		});
		
		// Add a mouse listener to the button
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(getModel().isEnabled() && KeyButton.this.onTrigger != null){
					KeyButton.this.onTrigger.run();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(getModel().isEnabled() && KeyButton.this.onUntrigger != null){
					KeyButton.this.onUntrigger.run();
				}
			}
		});
	}
	
	public Runnable getOnTrigger() {
		return onTrigger;
	}

	/**
	 * Sets the Runnable to run, when the button is triggered
	 * @param onTrigger The Runnable for the triggering event
	 */
	public void setOnTrigger(Runnable onTrigger) {
		this.onTrigger = onTrigger;
		assignKeyboardKeyTrigger();
	}

	public Runnable getOnUntrigger() {
		return onUntrigger;
	}

	/**
	 * Sets the Runnable to run, when the button is untriggered
	 * @param onTrigger The Runnable for the untriggering event
	 */
	public void setOnUntrigger(Runnable onUntrigger) {
		this.onUntrigger = onUntrigger;
		assignKeyboardKeyUntrigger();
	}

	public Image getBigIcon() {
		return bigIcon;
	}

	/**
	 * Sets the big incon that is displayed on the button
	 * @param bigIcon The icon to show
	 */
	public void setBigIcon(Image bigIcon) {
		this.bigIcon = bigIcon;
	}

	public Image getSmallIcon() {
		return smallIcon;
	}

	/**
	 * Sets the small icon that is displayed in the top right corner of the button
	 * @param smallIcon The icon to show
	 */
	public void setSmallIcon(Image smallIcon) {
		this.smallIcon = smallIcon;
	}

	public int getKeyCode() {
		return keyCode;
	}
	
	/**
	 * Assigns the corredponding Keyboard key to this Button
	 */
	private void assignKeyboardKeyTrigger(){
		
		// Create Actions for pressing a key
		final Action pressed = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onTrigger.run();
				getActionMap().remove(keyCode+"p"); // dont retrigger, when held down
			}
		};
		
		// Put the action in the action map
		getActionMap().remove(keyCode+"p");
		getActionMap().put(keyCode+"p", pressed);
		
		// register key strokes for the actions
		getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke(keyCode, 0, false));
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyCode, 0, false), keyCode+"p");
	}
	
	/**
	 * Assigns the corredponding Keyboard key to this Button
	 */
	private void assignKeyboardKeyUntrigger(){
		
		// Create Action for pressing a key
				final Action pressed = new AbstractAction() {
					private static final long serialVersionUID = 1L;
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(onTrigger != null){
							onTrigger.run();
						}
						getActionMap().remove(keyCode+"p"); // dont retrigger, when held down
					}
				};
		
		// Create Action for realeasing a key
		final Action released = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(onUntrigger != null){
					onUntrigger.run();
				}
				getActionMap().put(keyCode+"p", pressed); // register again, when released
			}
		};
		
		// Put the action in the action map
		getActionMap().remove(keyCode+"r");
		getActionMap().put(keyCode+"r", released);
		
		// register key strokes for the actions
		getInputMap(WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke(keyCode, 0, true));
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyCode, 0, true), keyCode+"r");
	}
	
}
