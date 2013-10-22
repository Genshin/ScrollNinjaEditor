package org.genshin.scrollninjaeditor;

import java.io.File;

import javax.swing.JFileChooser;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Export extends ImageButton {
	MapObjectManager manager;
	
	
	public Export(SpriteDrawable sd) {
		super(sd);
		
		manager = MapObjectManager.getInstance();
		
		this.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event ,float x,float y) {
				exportFile();
			}
		});
		
		setSize(32, 32);
	}
	
	private void exportFile() {
		File current = new File("./bin/data");
		JFileChooser fileChooser = new JFileChooser(current.getAbsoluteFile());
		int select = fileChooser.showSaveDialog(fileChooser);
		
		if(select == JFileChooser.APPROVE_OPTION) {
			JsonWrite write = new JsonWrite();
			for(MapObject obj:manager.getFrontObjects()) {
				write.addObject();
				write.putObject("name", obj.getFileName());
				write.putObject("label", obj.getLabelName());
				write.putObject("x", obj.getX());
				write.putObject("y", obj.getY());
			}
			write.writeData(fileChooser.getSelectedFile().toString() + ".json");
		}
	}
}