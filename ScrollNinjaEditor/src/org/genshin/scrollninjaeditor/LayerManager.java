package org.genshin.scrollninjaeditor;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LayerManager {
	private static LayerManager instance = new LayerManager();
	private ArrayList<Layer> frontLayers = new ArrayList<Layer>();
	private ArrayList<Layer> backLayers = new ArrayList<Layer>();
	private int selectLayer = 0;
	private int selectPlace = Layer.FRONT;

	
	public LayerManager() {
		addFront(0);
		addBack(0);
		setLayer(0,Layer.FRONT);
	}
	
	public static LayerManager getInstance() {
		return instance;
	}
	
	
	public void setLayer(int place,int number) {
		this.selectPlace = place;
		this.selectLayer = number;
	}
	
	public int getSelectPlace() {
		return this.selectPlace;
	}
	
	public int getSelectLayer() {
		return this.selectLayer;
	}
	
	
	//レイヤ―取得
	public Layer getLayer(int index) {
		if(this.selectPlace == Layer.FRONT)
			return getFrontLayer(index);
		else if(this.selectPlace == Layer.BACK)
			return getBackLayer(index);
		else
			return null;
	}
	public Layer getFrontLayer(int index) {
		if(index < this.frontLayers.size())
			return frontLayers.get(index);
		else
			return null;
	}
	public Layer getBackLayer(int index) {
		if(index < this.backLayers.size())
			return backLayers.get(index);
		else
			return null;
	}
	
	
	//レイヤ―配列取得
	public ArrayList<Layer> getFrontLayers() {
		return frontLayers;
	}
	public ArrayList<Layer> getBackLayers() {
		return backLayers;
	}
	
	
	//レイヤ―追加
	public void addFront(int i) {
		frontLayers.add(new Layer(i,Layer.FRONT));
	}
	public void addBack(int i) {
		backLayers.add(new Layer(i,Layer.BACK));
	}
	
	//レイヤ―削除
	public void removeFront(int index) {
		if(frontLayers.get(index) != null) {	
			frontLayers.remove(index);
			for(int i = 0 ; i + index < frontLayers.size() ; i++ ) {
				frontLayers.get(index + i).setLabel(index + i);
				frontLayers.get(index + i).setLayerNumber(index + i);
				
			}
		}
	}
	public void removeBack(int index) {
		if(backLayers.get(index) != null) {
			backLayers.remove(index);
			for(int i = 0 ; i + index < backLayers.size() ; i++ ) {
				backLayers.get(index + i).setLabel(index + i);
				backLayers.get(index + i).setLayerNumber(index + i);
			}
		}
			
	}
	
	//レイヤ―描画
	public void drawFrontLayers(SpriteBatch batch) {
		for(Layer lay:frontLayers) {
				lay.draw(batch);
		}
	}
	public void drawBackLayers(SpriteBatch batch) {
		for(Layer lay:backLayers) {
				lay.draw(batch);
		}
	}
	
	//レイヤ―交換
	public void changeLayerFrontToFront(int change,int target) {
		Layer stack = frontLayers.get(change);
		frontLayers.set(change,frontLayers.get(target));
		frontLayers.set(target,stack);
	}
	public void changeLayerBackToBack(int change,int target) {
		Layer stack = backLayers.get(change);
		backLayers.set(change,backLayers.get(target));
		backLayers.set(target,stack);
	}
	
	//レイヤ―移動
	public void moveLayerFrontToBack(int index) {
		backLayers.add(frontLayers.get(index));
		frontLayers.remove(index);
	}
	public void moveLayerBackToFront(int index) {
		frontLayers.add(backLayers.get(index));
		backLayers.remove(index);
	}
	

}