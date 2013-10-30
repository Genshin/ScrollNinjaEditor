package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

public class Mouse {
	private Camera 			 camera;
	private int				 selectFlag = -1;

	private float			 mousePositionX;
	private float			 mousePositionY;
	private float			 oldmousePositionX;
	private float			 oldmousePositionY;
	private float			 objectPositonX;
	private float			 objectPositonY;
	private int				 oldPressKey;
	private LayerManager	 layerManager;
	private boolean			 inputFlag = false;
	private MapEditorStage	mapEditorStage;
	
	public class UseKeys {
		public static final int OBJECT_UP    		 = Keys.W;
		public static final int OBJECT_DOWN  		 = Keys.Q;
		public static final int NEXT_LAYER			 = Keys.X;
		public static final int PREV_LAYER			 = Keys.Z;
		public static final int CHANGE_LAYER 		 = Keys.C;
		public static final int ADD_LAYER  			 = Keys.V;
		public static final int REMOVE_LAYER		 = Keys.B;
		public static final int LAYER_UP			 = Keys.S;
		public static final int LAYER_DOWN			 = Keys.A;
		public static final int SELECT_LAYER_DRAW	 = Keys.D;
		public static final int ALL_LAYER_DRAW	 	 = Keys.F;
	}

	public Mouse() {
		camera			= Camera.getInstance();
		layerManager	= LayerManager.getInstance();
		mapEditorStage = MapEditorStage.getInstance();
	}
	
	public void update(Camera camera ,LayerManager layer,MapEditorStage mapEditorStage) {
		this.camera			= camera;
		this.layerManager	= layer;
		this.mapEditorStage = mapEditorStage;

		
		setOldMousePosition();
		getMousePosition();
		layer.checkClick();
		if(selectFlag == -1){
			input();
			hitCheck();
			MouseOver();
		}
		else
			drag();
	}

	private void hitCheck() {
        for(int i = 0 ; i <layerManager.getLayer(layerManager.getSelectLayerNum()).getMapObjects().size();i++){
            if(layerManager.getLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY)) {
                if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
                    objectPositonX = layerManager.getLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().getX();
                    objectPositonY = -layerManager.getLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().getY();
                    selectFlag = i;
                }
                if(Gdx.input.isButtonPressed(Buttons.RIGHT)) {
                    layerManager.getLayer(layerManager.getSelectLayerNum()).getMapObjects().remove(i);
                    break;
                }
            }
        }
	}

	private void drag() {
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {

			objectPositonX += (mousePositionX - oldmousePositionX);
			objectPositonY += (mousePositionY - oldmousePositionY);
			
			layerManager.getLayer(layerManager.getSelectLayerNum()).getMapObject(selectFlag).setPosition(objectPositonX, -objectPositonY);
			int flag = selectFlag;
			
			//オブジェクト順序入れ替え(仮)
			if(Gdx.input.isKeyPressed(UseKeys.OBJECT_UP) && oldPressKey != UseKeys.OBJECT_UP) {
				oldPressKey = UseKeys.OBJECT_UP;
				inputFlag = true;
				layerManager.getLayer(layerManager.getSelectLayerNum()).next(selectFlag);
				if(selectFlag + 1 < layerManager.getLayer(layerManager.getSelectLayerNum()).getMapObjects().size())
				selectFlag = flag + 1;
			}
			if(Gdx.input.isKeyPressed(UseKeys.OBJECT_DOWN) && oldPressKey != UseKeys.OBJECT_DOWN) {
				oldPressKey = UseKeys.OBJECT_DOWN;
				inputFlag = true;
				layerManager.getLayer(layerManager.getSelectLayerNum()).previous(selectFlag);
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
		if(Gdx.input.isKeyPressed(UseKeys.NEXT_LAYER) && oldPressKey != UseKeys.NEXT_LAYER) {
			oldPressKey = UseKeys.NEXT_LAYER;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT) {
				if(layerManager.getFrontLayers().size() > layerManager.getSelectLayerNum() + 1)
				layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayerNum() + 1);
			}
			else {
				if(layerManager.getBackLayers().size() > layerManager.getSelectLayerNum() + 1)
					layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayerNum() + 1);
			}
		}
		//ひとつ前のレイヤ―へ
		if(Gdx.input.isKeyPressed(UseKeys.PREV_LAYER) && oldPressKey != UseKeys.PREV_LAYER) {
			oldPressKey = UseKeys.PREV_LAYER;
			inputFlag = true;
			if(!(layerManager.getSelectLayerNum() - 1 < 0) && layerManager.getLayer(layerManager.getSelectLayerNum() - 1) != null )
				layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayerNum() - 1);
		}
		
		//レイヤ―前後切替
		if(Gdx.input.isKeyPressed(UseKeys.CHANGE_LAYER) && oldPressKey != UseKeys.CHANGE_LAYER) {
			oldPressKey = UseKeys.CHANGE_LAYER;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT)
				layerManager.setLayer(Layer.BACK, 0);
			else
				layerManager.setLayer(Layer.FRONT, 0);
		}
		
		//レイヤ―追加
		if(Gdx.input.isKeyPressed(UseKeys.ADD_LAYER) && oldPressKey != UseKeys.ADD_LAYER) {
			oldPressKey = UseKeys.ADD_LAYER;
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
		if(Gdx.input.isKeyPressed(UseKeys.REMOVE_LAYER) && oldPressKey != UseKeys.REMOVE_LAYER) {
			oldPressKey = UseKeys.REMOVE_LAYER;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT) {
				if(layerManager.getFrontLayers().size() > 1) {
					layerManager.removeFront(layerManager.getSelectLayerNum());
					Gdx.app.log("delete","" + layerManager.getSelectLayerNum());
					if(layerManager.getSelectLayerNum() != 0){
						layerManager.setLayer(Layer.FRONT, layerManager.getSelectLayerNum() - 1);
					}
					else{
						layerManager.setLayer(Layer.FRONT, 0);
					}
				}
				else if(layerManager.getFrontLayers().size() == 1) {
					layerManager.addFront(layerManager.getSelectLayerNum() + 1);
					layerManager.removeFront(layerManager.getSelectLayerNum());
					layerManager.setLayer(Layer.FRONT, 0);
				}
			}
			
			else {
				if(layerManager.getBackLayers().size() > 1) {
					layerManager.removeBack(layerManager.getSelectLayerNum());
					if(layerManager.getSelectLayerNum() != 0)
						layerManager.setLayer(Layer.BACK, layerManager.getSelectLayerNum() - 1);
					else
						layerManager.setLayer(Layer.BACK, 0);
				}
				else if(layerManager.getBackLayers().size() == 1) {
					layerManager.addBack(layerManager.getSelectLayerNum() + 1);
					layerManager.removeBack(layerManager.getSelectLayerNum());
					layerManager.setLayer(Layer.BACK, 0);
				}
			}
			mapEditorStage.updateLayer();
		}
		
		//レイヤ―入れ替え
		if(Gdx.input.isKeyPressed(UseKeys.LAYER_DOWN) && oldPressKey != UseKeys.LAYER_DOWN) {
			oldPressKey = UseKeys.LAYER_DOWN;
			inputFlag = true;
			if(layerManager.getSelectLayerNum() - 1 >= 0) {
				if(layerManager.getSelectPlace() == Layer.FRONT) 
					layerManager.changeLayerFrontToFront(layerManager.getSelectLayerNum(), layerManager.getSelectLayerNum() - 1);
				else if(layerManager.getSelectPlace() == Layer.BACK)
					layerManager.changeLayerBackToBack(layerManager.getSelectLayerNum(), layerManager.getSelectLayerNum() - 1);
				
				layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayerNum() - 1);	
				mapEditorStage.updateLayer();
			}		
		}
		if(Gdx.input.isKeyPressed(UseKeys.LAYER_UP) && oldPressKey != UseKeys.LAYER_UP) {
			oldPressKey = UseKeys.LAYER_UP;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT) {
				if(layerManager.getFrontLayers().size() > layerManager.getSelectLayerNum() + 1) {
					layerManager.changeLayerFrontToFront(layerManager.getSelectLayerNum(), layerManager.getSelectLayerNum() + 1);
					layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayerNum() + 1);	
				}

			}
			else if(layerManager.getSelectPlace() == Layer.BACK) {
				if(layerManager.getBackLayers().size() > layerManager.getSelectLayerNum() + 1) {
					layerManager.changeLayerBackToBack(layerManager.getSelectLayerNum(), layerManager.getSelectLayerNum() + 1);
					layerManager.setLayer(layerManager.getSelectPlace(), layerManager.getSelectLayerNum() + 1);	
				}
			}
			 mapEditorStage.updateLayer();
		}
		
		//レイヤ―選択描画
		if(Gdx.input.isKeyPressed(UseKeys.SELECT_LAYER_DRAW)) {
			layerManager.selectDraw(layerManager.getSelectPlace(),layerManager.getSelectLayerNum());
		}		
		if(Gdx.input.isKeyPressed(UseKeys.ALL_LAYER_DRAW)) {
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
			for(int i = layerManager.getFrontLayer(layerManager.getSelectLayerNum()).getMapObjects().size() - 1 ;i >=0 ;i-- ) {
				if(layerManager.getFrontLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY)){
						layerManager.getFrontLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().setColor(0.0f, 1.0f, 0.0f, 0.5f);
						break;
				}
				else
					layerManager.getFrontLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().setColor(1.0f, 1.0f, 1.0f, 1.0f);
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
			for(int i = layerManager.getBackLayer(layerManager.getSelectLayerNum()).getMapObjects().size() - 1 ;i >=0 ;i-- ) {
				if(layerManager.getBackLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY)){
					layerManager.getBackLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().setColor(0.0f, 1.0f, 0.0f, 0.5f);
					break;
				}
				else
					layerManager.getBackLayer(layerManager.getSelectLayerNum()).getMapObject(i).getSp().setColor(1.0f, 1.0f, 1.0f, 1.0f);
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