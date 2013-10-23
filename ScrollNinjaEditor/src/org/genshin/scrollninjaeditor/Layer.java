package org.genshin.scrollninjaeditor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Layer {
	private int layerNumber;
	private Label label;
	private Boolean active;
	private ArrayList<MapObject> mapObjects;

	public Layer(int num) {
		setLayerNumber(num);
		setLabel(num);
		active = false;
		mapObjects = new ArrayList<MapObject>();
	}

	public void setLayerNumber(int num) {
		this.layerNumber = num;
	}
	
	public int getLayerNumber() {
		return layerNumber;
	}
	
	public void setLabel(int num) {
		label = new Label("Layer : " + layerNumber, new Skin(Gdx.files.internal("data/uiskin.json")));
		label.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				active = !active;
				// TODO アクティブ状態で色変更
			}
		});
	}
	
	public Label getLabel() {
		return label;
	}
	
	public void setMapObject(MapObject obj) {
		mapObjects.add(obj);
	}
	
	public MapObject getMapObject(int index) {
		return mapObjects.get(index);
	}
	
	public ArrayList<MapObject> getMapObjects() {
		return mapObjects;
	}
	
	public void removeMapObject(MapObject obj) {
		mapObjects.remove(obj);
	}
	
	public void draw(SpriteBatch batch) {
		if (mapObjects.size() > 0) {
			for (MapObject obj : mapObjects) {
				obj.draw(batch);
			}
		}
	}
	
	public void next(int index) {
		if (mapObjects.size() > index + 1) {
			MapObject tmp = mapObjects.get(index + 1);
			mapObjects.set(index + 1, mapObjects.get(index));
			mapObjects.set(index, tmp);
		}
	}
	
	public void previous(int index) {
		if (index - 1 >= 0) {
			MapObject tmp = mapObjects.get(index - 1);
			mapObjects.set(index - 1, mapObjects.get(index));
			mapObjects.set(index, tmp);
		}
	}
}
