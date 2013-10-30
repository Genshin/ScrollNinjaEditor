package org.genshin.scrollninjaeditor;

import java.io.File;

import javax.swing.JFileChooser;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Export extends ImageButton {
	LayerManager manager;
	
	
	public Export(SpriteDrawable sd ) {
		super(sd);
		
		this.manager = LayerManager.getInstance();
		
		this.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event ,float x,float y) {
				
				exportFile();
			}
		});
		
		setSize(32, 32);
	}
	
	public void setlayer(LayerManager layer) {
		this.manager = layer;
	}
	
	private void exportFile() {
		File current = new File("./bin/data");
		JFileChooser fileChooser = new JFileChooser(current.getAbsoluteFile());
		int select = fileChooser.showSaveDialog(fileChooser);
		if(select == JFileChooser.APPROVE_OPTION) {
			JsonWrite write = new JsonWrite();
	
			//フロントレイヤ―情報書き出し
			for(int i = 0;i < manager.getFrontLayers().size();i++){
				for(MapObject obj:manager.getFrontLayer(i).getMapObjects()) {
					write.addObject();
					write.putObject("layer", manager.getFrontLayer(i).getLayerPlace());
					write.putObject("layerNo", i);
					write.putObject("name", obj.getFileName());
					write.putObject("label", obj.getLabelName());
					write.putObject("x", obj.getX());
					write.putObject("y", obj.getY());
					
				}
			}
			//バックレイヤ―情報書き出し
			for(int i = 0 ;i < manager.getBackLayers().size() ;i ++) {
				for(MapObject obj:manager.getBackLayer(i).getMapObjects()) {
					write.addObject();
					write.putObject("layer", manager.getBackLayer(i).getLayerPlace());
					write.putObject("layerNo", i);
					write.putObject("name", obj.getFileName());
					write.putObject("label", obj.getLabelName());
					write.putObject("x", obj.getX());
					write.putObject("y", obj.getY());
				}
			}
			write.writeData(fileChooser.getSelectedFile().toString() + ".json");
		}
	}
}