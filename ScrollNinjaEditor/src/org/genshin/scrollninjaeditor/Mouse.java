package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;


public class Mouse {
	
	private MapObjectManager manager;
	private Camera 			 camera;
	private int				 selectFlag = -1;
	private float			 mousePositionX;
	private float			 mousePositionY;
	private float			 oldmousePositionX;
	private float			 oldmousePositionY;
	private float			 objectPositonX;
	private float			 objectPositonY;

	
	public Mouse() {
		manager = MapObjectManager.getInstance();
		camera = Camera.getInstance();
	}
	
	public void update() {
		if(selectFlag == -1)
			hitCheck();
		else
			drag();
	}
	
	
	private void hitCheck() {
		for(int i = 0; i < manager.getFrontObjects().size();i++) {
			getMousePosition();
			if(manager.getFrontObject(i).getSp().getBoundingRectangle().contains(mousePositionX, -mousePositionY)) {
				if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
					objectPositonX = manager.getFrontObject(i).getSp().getX();
					objectPositonY = -manager.getFrontObject(i).getSp().getY();
					selectFlag = i;
					break;	
				}
				if(Gdx.input.isButtonPressed(Buttons.RIGHT)) {
					manager.getFrontObjects().remove(i);
					break;
				}
				
			}
		}
		
	}
	
	private void drag() {
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			setOldMousePosition();
			getMousePosition();
			objectPositonX += mousePositionX - oldmousePositionX;
			objectPositonY += mousePositionY - oldmousePositionY;
			manager.getFrontObject(selectFlag).setPosition(objectPositonX, -objectPositonY);
					
		}
		else
			selectFlag = -1;
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