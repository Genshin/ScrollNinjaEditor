package org.genshin.scrollninjaeditor;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LayerManager {
	private static LayerManager instance = new LayerManager();
	private ArrayList<Layer> frontLayers = new ArrayList<Layer>();
	private ArrayList<Layer> backLayers = new ArrayList<Layer>();
	private int	selectLayer = 0;
	private int	selectPlace = Layer.FRONT;
	private int	oldSelectLayer;
	
	public LayerManager() {
		addFront(0);
		addBack(0);
		setLayer(0,Layer.FRONT);
	}
	
	public static LayerManager getInstance() {
		return instance;
	}
		
	public void checkClick(){
		for(int i = 0; i < frontLayers.size() ; i++) {
			if(frontLayers.get(i).getClickFlag()) {
				setLayer(Layer.FRONT, i);
				frontLayers.get(i).setClickFlag(false);
				break;
			}
		}
		for(int i = 0 ; i < backLayers.size() ; i ++) {
			if(backLayers.get(i).getClickFlag()) {
				setLayer(backLayers.get(i).getLayerPlace(), i);
				backLayers.get(i).setClickFlag(false);
				break;
			}
		}
	}
	
	public void setLayer(int place,int number) {
	
		setLabelColor(this.selectPlace, this.selectLayer, false);
		this.selectPlace = place;
		oldSelectLayer = this.selectLayer;
		this.selectLayer = number;
		setLabelColor(this.selectPlace, this.selectLayer, true);
	}
	
	private void setLabelColor(int place ,int number ,boolean flag) {
		if(!flag) {
			if(place == Layer.FRONT && number < this.frontLayers.size())
				this.frontLayers.get(number).getLabel().setColor(1.0f, 1.0f, 1.0f, 1.0f);
			else if(place == Layer.BACK && number < this.backLayers.size())
				this.backLayers.get(number).getLabel().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
		else {
			if(place == Layer.FRONT)
				this.frontLayers.get(number).getLabel().setColor(1.0f, 0.0f, 0.0f, 1.0f);
			else if(place == Layer.BACK)
				this.backLayers.get(number).getLabel().setColor(1.0f, 0.0f, 0.0f, 1.0f);
		}
	}
	public int getSelectPlace() {
		return this.selectPlace;
	}
	
	public int getSelectLayerNum() {
		return this.selectLayer;
	}
	
	public int getOldlayer(){
		return this.oldSelectLayer;
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
	public Layer getSelectLayer() {
		if(this.selectPlace == Layer.FRONT)
			return getFrontLayer(this.selectLayer);
		else if(this.selectPlace == Layer.BACK)
			return getBackLayer(this.selectLayer);
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
	
	//描画レイヤ―の選択
	public void selectDraw(int layerPlace,int layerNo) {
		for(Layer lay:frontLayers) 
			lay.setDrawFlag(false);
		for(Layer lay:backLayers) 
			lay.setDrawFlag(false);
		
		if(layerPlace == Layer.FRONT)
			this.getFrontLayer(layerNo).setDrawFlag(true);
		else if(layerPlace == Layer.BACK)
			this.getBackLayer(layerNo).setDrawFlag(true);
		
	}
	public void allDraw(){
		for(Layer lay:frontLayers) 
			lay.setDrawFlag(true);
		for(Layer lay:backLayers) 
			lay.setDrawFlag(true);
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