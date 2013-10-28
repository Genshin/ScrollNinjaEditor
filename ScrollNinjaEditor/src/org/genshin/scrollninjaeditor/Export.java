package org.genshin.scrollninjaeditor;

import java.io.File;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
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
			for(Layer layer:manager.getFrontLayers()) {
				for(MapObject obj:layer.getMapObjects()) {
					write.addObject();
					write.putObject("layer", layer.getLayerPlace());
					write.putObject("layerNo", layer.getLayerNumber());
					write.putObject("name", obj.getFileName());
					write.putObject("label", obj.getLabelName());
					write.putObject("x", obj.getX());
					write.putObject("y", obj.getY());
					
				}
			}
			//バックレイヤ―情報書き出し
			for(Layer layer:manager.getBackLayers()) {
				for(MapObject obj:layer.getMapObjects()) {
					write.addObject();
					write.putObject("layer", layer.getLayerPlace());
					write.putObject("layerNo", layer.getLayerNumber());
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