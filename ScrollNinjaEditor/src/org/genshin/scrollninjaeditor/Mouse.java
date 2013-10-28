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

	public Mouse() {
		camera = Camera.getInstance();
		layerManager = LayerManager.getInstance();
	}
	
	public void update(Camera camera ,LayerManager layer) {
		this.camera = camera;
		this.layerManager = layer;

		setOldMousePosition();
		getMousePosition();
		input();
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
		}
		
		//レイヤ―削除
		if(Gdx.input.isKeyPressed(Keys.B) && oldPressKey != Keys.B) {
			oldPressKey = Keys.B;
			inputFlag = true;
			if(layerManager.getSelectPlace() == Layer.FRONT) {
				if(layerManager.getFrontLayers().size() > 1) {
					layerManager.removeFront(layerManager.getSelectLayer());
					Gdx.app.log("delete","" + layerManager.getSelectLayer());
					if(layerManager.getSelectLayer() != 0)
						layerManager.setLayer(Layer.FRONT, layerManager.getSelectLayer() - 1);
					else
						layerManager.setLayer(Layer.FRONT, 0);
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
						layerManager.setLayer(Layer.BACK, layerManager.getSelectLayer() );
				}
				else if(layerManager.getBackLayers().size() == 1) {
					layerManager.addBack(layerManager.getSelectLayer() + 1);
					layerManager.removeBack(layerManager.getSelectLayer());
					layerManager.setLayer(Layer.BACK, 0);
				}
			}
		}
		
		//レイヤ―入れ替え(仮)
		if(Gdx.input.isKeyPressed(Keys.A)) {
			if(layerManager.getFrontLayers().size() > 1)
				layerManager.changeLayerFrontToFront(0, 1);
		}
		
		//レイヤ―選択描画(仮)
		if(Gdx.input.isKeyPressed(Keys.D)) {
			layerManager.selectDraw(Layer.FRONT, 1);
		}		
		if(Gdx.input.isKeyPressed(Keys.F)) {
			layerManager.allDraw();
		}
		
		if(!inputFlag && !Gdx.input.isKeyPressed(oldPressKey))
			oldPressKey = 0;
	}

	private void getMousePosition() {
		mousePositionX = ((Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x) * camera.zoom;
		mousePositionY = ((Gdx.input.getY() - Gdx.graphics.getHeight() / 2) - camera.position.y) * camera.zoom;
	}

	private void setOldMousePosition() {
		oldmousePositionX = mousePositionX;
		oldmousePositionY = mousePositionY;
	}
}