package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;


public class Mouse {

	private MapObjectManager manager;
	private Camera 			 camera;
	private int				 selectFlag = -1;
	private int				 selectLayer = -1;
	private float			 mousePositionX;
	private float			 mousePositionY;
	private float			 oldmousePositionX;
	private float			 oldmousePositionY;
	private float			 objectPositonX;
	private float			 objectPositonY;
	private int				 FRONT = 0;
	private int				 BACK  = 1;
	private int				 oldPressKey;

	public Mouse() {
		manager = MapObjectManager.getInstance();
	}

	public void update(Camera camera) {
		this.camera = camera;

		MouseOver();

		setOldMousePosition();
		getMousePosition();
		if(selectFlag == -1)
			hitCheck();
		else
			drag();
	}


	private void hitCheck() {
		if(!checkFront())
			checkBack();
	}

	private boolean checkFront() {
		for(int i =  manager.getFrontObjects().size() - 1; i >= 0; i--) {
			
			if(manager.getFrontObject(i).getSp().getBoundingRectangle().contains(mousePositionX , -mousePositionY)) {
				if(Gdx.input.isButtonPressed(Buttons.LEFT)) {

	
					objectPositonX = manager.getFrontObject(i).getSp().getX();
					objectPositonY = -manager.getFrontObject(i).getSp().getY();
					selectFlag = i;
					selectLayer = FRONT;
					return true;

				}
				if(Gdx.input.isButtonPressed(Buttons.RIGHT)) {
					manager.getFrontObjects().remove(i);
					break;
				}
			}
		}
		return false;
	}

	private void checkBack() {
		for(int i = manager.getBackObjects().size() - 1; i >= 0;i--) {

			if(manager.getBackObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY)) {
				if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
					objectPositonX = manager.getBackObject(i).getSp().getX();
					objectPositonY = -manager.getBackObject(i).getSp().getY();
					selectFlag = i;
					selectLayer = BACK;
					break;	
				}
				if(Gdx.input.isButtonPressed(Buttons.RIGHT)) {
					manager.getBackObjects().remove(i);
					break;
				}
			}
		}
	}

	private void drag() {
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {

			objectPositonX += (mousePositionX - oldmousePositionX);
			objectPositonY += (mousePositionY - oldmousePositionY);
			if(selectLayer == FRONT){
				manager.getFrontObject(selectFlag).setPosition(objectPositonX, -objectPositonY);

				if(Gdx.input.isKeyPressed(Keys.A) && oldPressKey != Keys.A){
					oldPressKey = Keys.A;
					manager.setBackObject(manager.getFrontObject(selectFlag));
					manager.removefrontObject(manager.getFrontObject(selectFlag));
					selectLayer = BACK;
					selectFlag = manager.getBackObjects().size() - 1;
				}
			}
			else if(selectLayer == BACK){
				manager.getBackObject(selectFlag).setPosition(objectPositonX, -objectPositonY);
				if(Gdx.input.isKeyPressed(Keys.S) && oldPressKey != Keys.S){
					oldPressKey = Keys.S;
					manager.setFrontObject(manager.getBackObject(selectFlag));
					manager.removeBackObject(manager.getBackObject(selectFlag));
					selectLayer = FRONT;
					selectFlag = manager.getFrontObjects().size() - 1;
				}
			}
		}
		else{
			oldPressKey = -1;
			selectLayer = -1;
			selectFlag = -1;
		}
	}

	private void MouseOver() {
		BackOver(FrontOver());
	}

	private boolean FrontOver() {
		boolean check = false;
		for(int i = manager.getFrontObjects().size() - 1 ;i >=0 ;i-- ) {
			if(manager.getFrontObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY) && !check) {
				manager.getFrontObject(i).getSp().setColor(0.0f, 1.0f, 0.0f, 0.5f);
				check = true;
			}
			else
				manager.getFrontObject(i).getSp().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}

		return check;
	}
	private void BackOver(boolean check) {
		for(int i = manager.getBackObjects().size() - 1 ;i >=0 ;i-- ) {
			if(manager.getBackObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY) && !check) {
				manager.getBackObject(i).getSp().setColor(0.0f, 1.0f, 0.0f, 0.5f);

			}
			else
				manager.getBackObject(i).getSp().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

	private void getMousePosition() {
		mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x * camera.zoom;
		mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() / 2) - camera.position.y * camera.zoom;
	}

	private void setOldMousePosition() {
		oldmousePositionX = mousePositionX;
		oldmousePositionY = mousePositionY;
	}
}