package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

public class Mouse {
	private Camera 			camera;
	private int				selectFlag = -1;
	private float			mousePositionX;
	private float			mousePositionY;
	private float			oldmousePositionX;
	private float			oldmousePositionY;
	private float			objectPositonX;
	private float			objectPositonY;
	private int				oldPressKey;
	private LayerManager	layerManager;
	private MapEditorStage mapEditorStage;
	private boolean			inputFlag = false;

	public Mouse() {
		camera			= Camera.getInstance();
		layerManager	= LayerManager.getInstance();
		mapEditorStage = MapEditorStage.getInstance();
	}
	
	public void update(Camera camera ,LayerManager layer,MapEditorStage mapEditorStage) {
		this.camera			= camera;
		this.layerManager	= layer;
		this.mapEditorStage = mapEditorStage;

		MouseOver();
		setOldMousePosition();
		getMousePosition();
		input();
		layer.checkClick();
		if(selectFlag == -1)
			hitCheck();
		else
			drag();
	}

	private void hitCheck() {
        for(int i = 0 ; i <layerManager.getLayer(layerManager.getSelectLayer()).getMapObjects().size();i++){
            if(layerManager.getLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY)) {
                if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
                    objectPositonX = layerManager.getLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().getX();
                    objectPositonY = -layerManager.getLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().getY();
                    selectFlag = i;
                }
                if(Gdx.input.isButtonPressed(Buttons.RIGHT)) {
                    layerManager.getLayer(layerManager.getSelectLayer()).getMapObjects().remove(i);
                    break;
                }
            }
        }
	}

	private void drag() {
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {

			objectPositonX += (mousePositionX - oldmousePositionX);
			objectPositonY += (mousePositionY - oldmousePositionY);
			
			layerManager.getLayer(layerManager.getSelectLayer()).getMapObject(selectFlag).setPosition(objectPositonX, -objectPositonY);
			int flag = selectFlag;
			
			//オブジェクト順序入れ替え(仮)
			if(Gdx.input.isKeyPressed(Keys.A)) {	
				layerManager.getLayer(layerManager.getSelectLayer()).next(selectFlag);
				if(selectFlag + 1 < layerManager.getLayer(layerManager.getSelectLayer()).getMapObjects().size())
				selectFlag = flag + 1;
			}
			if(Gdx.input.isKeyPressed(Keys.S)) {
				layerManager.getLayer(layerManager.getSelectLayer()).previous(selectFlag);
				if(selectFlag > 0)
				selectFlag = flag - 1;
			}
		}
		else
			selectFlag = -1;
	}
		
	private void input() {
		inputFlag = false;
		//レイヤ―移動
		//次のレイヤ―へ
		if(Gdx.input.isKeyPressed(Keys.Z) && oldPressKey != Keys.Z) {
			oldPressKey = Keys.Z;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT) {
				if(layerManager.getFrontLayers().size() > layerManager.getSelectLayer() + 1)
				layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayer() + 1);
			}
			else {
				if(layerManager.getBackLayers().size() > layerManager.getSelectLayer() + 1)
					layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayer() + 1);
			}
		}
		//ひとつ前のレイヤ―へ
		if(Gdx.input.isKeyPressed(Keys.X) && oldPressKey != Keys.X) {
			oldPressKey = Keys.X;
			inputFlag = true;
			if(!(layerManager.getSelectLayer() - 1 < 0) && layerManager.getLayer(layerManager.getSelectLayer() - 1) != null )
				layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayer() - 1);
		}
		
		//レイヤ―前後切替
		if(Gdx.input.isKeyPressed(Keys.C) && oldPressKey != Keys.C) {
			oldPressKey = Keys.C;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT)
				layerManager.setLayer(Layer.BACK, 0);
			else
				layerManager.setLayer(Layer.FRONT, 0);
		}
		
		//レイヤ―追加
		if(Gdx.input.isKeyPressed(Keys.V) && oldPressKey != Keys.V) {
			oldPressKey = Keys.V;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT) {
				layerManager.addFront(layerManager.getFrontLayers().size());
				layerManager.setLayer(Layer.FRONT,layerManager.getFrontLayers().size() - 1);
			}
			else {
				layerManager.addBack(layerManager.getBackLayers().size());
				layerManager.setLayer(Layer.BACK,layerManager.getBackLayers().size() - 1);
			}
			mapEditorStage.updateLayer();
		}
		
		//レイヤ―削除
		if(Gdx.input.isKeyPressed(Keys.B) && oldPressKey != Keys.B) {
			oldPressKey = Keys.B;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT) {
				if(layerManager.getFrontLayers().size() > 1) {
					layerManager.removeFront(layerManager.getSelectLayer());
					Gdx.app.log("delete","" + layerManager.getSelectLayer());
					if(layerManager.getSelectLayer() != 0){
						layerManager.setLayer(Layer.FRONT, layerManager.getSelectLayer() - 1);
					}
					else{
						layerManager.setLayer(Layer.FRONT, 0);
					}
				}
				else if(layerManager.getFrontLayers().size() == 1) {
					layerManager.addFront(layerManager.getSelectLayer() + 1);
					layerManager.removeFront(layerManager.getSelectLayer());
					layerManager.setLayer(Layer.FRONT, 0);
				}
			}
			
			else {
				if(layerManager.getBackLayers().size() > 1) {
					layerManager.removeBack(layerManager.getSelectLayer());
					if(layerManager.getSelectLayer() != 0)
						layerManager.setLayer(Layer.BACK, layerManager.getSelectLayer() - 1);
					else
						layerManager.setLayer(Layer.BACK, 0);
				}
				else if(layerManager.getBackLayers().size() == 1) {
					layerManager.addBack(layerManager.getSelectLayer() + 1);
					layerManager.removeBack(layerManager.getSelectLayer());
					layerManager.setLayer(Layer.BACK, 0);
				}
			}
			mapEditorStage.updateLayer();
		}
		
		//レイヤ―入れ替え(仮)
		if(Gdx.input.isKeyPressed(Keys.A)) {
			if(layerManager.getFrontLayers().size() > 1)
				layerManager.changeLayerFrontToFront(0, 1);
		}
		
		//レイヤ―選択描画
		if(Gdx.input.isKeyPressed(Keys.D)) {
			layerManager.selectDraw(layerManager.getSelectPlace(),layerManager.getSelectLayer());
		}		
		if(Gdx.input.isKeyPressed(Keys.F)) {
			layerManager.allDraw();
		}
		
		if(!inputFlag && !Gdx.input.isKeyPressed(oldPressKey))
			oldPressKey = 0;
	}
	
	private void MouseOver(){
		frontOver();
		backOver();
	}
	
	private void frontOver(){
		for(int i = layerManager.getFrontLayers().size() -1 ; i >= 0 ; i --){
			for (int j = layerManager.getFrontLayer(i).getMapObjects().size() - 1 ; j >= 0 ; j --){
				layerManager.getFrontLayer(i).getMapObject(j).getSp().setColor(1, 1, 1, 1);
			}
		}
		if(layerManager.getSelectPlace() == Layer.FRONT ){
			for(int i = layerManager.getFrontLayer(layerManager.getSelectLayer()).getMapObjects().size() - 1 ;i >=0 ;i-- ) {
				if(layerManager.getFrontLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY)){
						layerManager.getFrontLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().setColor(0.0f, 1.0f, 0.0f, 0.5f);
				}
				else
					layerManager.getFrontLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().setColor(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
	}
	
	private void backOver(){
		for(int i = layerManager.getBackLayers().size() -1 ; i >= 0 ; i --){
			for (int j = layerManager.getBackLayer(i).getMapObjects().size() - 1 ; j >= 0 ; j --){
				layerManager.getBackLayer(i).getMapObject(j).getSp().setColor(1, 1, 1, 1);
			}
		}
		if(layerManager.getSelectPlace() == Layer.BACK){
			for(int i = layerManager.getBackLayer(layerManager.getSelectLayer()).getMapObjects().size() - 1 ;i >=0 ;i-- ) {
				if(layerManager.getBackLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY)){
					layerManager.getBackLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().setColor(0.0f, 1.0f, 0.0f, 0.5f);
				}
				else
					layerManager.getBackLayer(layerManager.getSelectLayer()).getMapObject(i).getSp().setColor(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
	}

	private void getMousePosition() {
		mousePositionX = ((Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x /2) * camera.zoom;
		mousePositionY = ((Gdx.input.getY() - Gdx.graphics.getHeight() / 2) - camera.position.y/2) * camera.zoom;
	}

	private void setOldMousePosition() {
		oldmousePositionX = mousePositionX;
		oldmousePositionY = mousePositionY;
	}
}