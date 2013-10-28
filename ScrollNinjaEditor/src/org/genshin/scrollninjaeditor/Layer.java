package org.genshin.scrollninjaeditor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Layer {
	private int layerNumber;
	//private LayerManager layer;
	private TextButton label;
	private Boolean active;
	private ArrayList<MapObject> mapObjects;
	private int layerPlace;
	private boolean drawFlag = false;
	
	public static int FRONT = 0;
	public static int BACK = 1;

	public Layer(int num,int place) {
		setLayerNumber(num);
		setLabel(num);
		setPlace(place);
		active = false;
		drawFlag = true;
		mapObjects = new ArrayList<MapObject>();
		//layer = LayerManager.getInstance();
	}

	public void setPlace(int place)	{
		this.layerPlace = place;
	}
	public void setLayerNumber(int num) {
		this.layerNumber = num;
	}
	public void setDrawFlag(boolean flag) {
		this.drawFlag = flag;
	}
	public int getLayerNumber() {
		return layerNumber;
	}
	public int getLayerPlace() {
		return layerPlace;
	}
	
	public void setLabel(int num) {
		setLayerNumber(num);
		
		label = new TextButton("" , new Skin(Gdx.files.internal("data/uiskin.json")));
		label.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				active = !active;
				Gdx.app.log("", "aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			}
		});
	}
	
	public Label getLabel() {
		return label.getLabel();
	}
	
	public TextButton getButton() {
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
		if(drawFlag == true) {
			if (mapObjects.size() > 0) {
				for (MapObject obj : mapObjects) {
					obj.draw(batch);
				}
			}
		}
		drawLabel(batch);
	}
	
	public void drawLabel(SpriteBatch batch) {
		if(layerPlace == FRONT) {
			label.setText("Front" + layerNumber);
			label.setPosition(-Gdx.graphics.getWidth() / 2, 
								Gdx.graphics.getHeight() / 2 - 64 - 32 * (layerNumber ));
		}
		else {
			label.setText("Back" + layerNumber);
			label.setPosition(-Gdx.graphics.getWidth() / 2, 0.0f - 32.0f * (layerNumber ));
		}
		label.setWidth(64);
		label.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		label.draw(batch, 1.0f);
	}
	public void setLabelColor(float r,float g , float b ,float a) {
		label.setColor(r, g, b, a);
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
