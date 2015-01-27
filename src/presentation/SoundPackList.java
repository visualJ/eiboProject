package presentation;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import repository.SoundPack;

public class SoundPackList extends JList<SoundPack> {

	private static final long serialVersionUID = 1L;
	private UserInterface userInterface;
	
	public SoundPackList(SoundPack[] soundPacks, UserInterface userInterface){
		super(soundPacks);
		
		this.userInterface = userInterface;
		
		addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(!arg0.getValueIsAdjusting()){
					System.out.println(getSelectedValuesList().get(0));
					SoundPackList.this.userInterface.setSoundPack(getSelectedValuesList().get(0));
				}
			}
		});
	}

}
