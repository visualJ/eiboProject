package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import repository.SoundPack;

public class SoundPackList extends JList<SoundPack> {

	private static final long serialVersionUID = 1L;
	private UserInterface userInterface;
	private final Color VIOLETT = new Color(170,20,240);
	
	public SoundPackList(SoundPack[] soundPacks, UserInterface userInterface){
		super(soundPacks);
		
		this.userInterface = userInterface;
		setOpaque(false);
		setCellRenderer(new SoundPackCellRenderer());
		setSelectedIndex(0);
		
		addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(!arg0.getValueIsAdjusting()){
					SoundPackList.this.userInterface.setSoundPack(getSelectedValuesList().get(0));
				}
			}
		});
	}
	
	private class SoundPackCellRenderer extends JLabel implements ListCellRenderer<SoundPack>{

		private static final long serialVersionUID = 1L;

		public SoundPackCellRenderer(){
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, SoundPack sp,
				int index, boolean selected, boolean focus) {
			if(selected){
				setForeground(VIOLETT);
				setBackground(UserInterface.alphaColor(Color.white, 0.05f));
			}else{
				setForeground(Color.white);
				setBackground(UserInterface.alphaColor(Color.white, index%2==0?0.1f:0.12f));
			}
			setIcon(new ImageIcon(UserInterface.programmIconBig.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			try {
				setIcon(new ImageIcon(new ImageIcon(sp.getImageFile()).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			} catch (Exception e) {
				// Just in cas a file is missing, or
				// an error occurrs during loading of the image
				e.printStackTrace();
			}
			setToolTipText(String.format("%s (%s) - %d BPM - %d Samples",sp.getPackName(), sp.getCreatorName(), sp.getBpm(), sp.getKeyMappings().size()));
			setText(sp.getPackName());
			return this;
		}
		
	}

}
