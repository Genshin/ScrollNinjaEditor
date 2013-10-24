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
		if(!checkFront())
			checkBack();
	}
	
	private boolean checkFront() {
		for(int i = 0; i < manager.getFrontObjects().size();i++) {
			getMousePosition();
			if(manager.getFrontObject(i).getSp().getBoundingRectangle().contains(mousePositionX , -mousePositionY)) {
				if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
					Gdx.app.log("mouseX", "" + mousePositionX);
					Gdx.app.log("mouseY", "" + -mousePositionY);
					Gdx.app.log("spriteX", "" + manager.getFrontObject(i).getSp().getX());
					Gdx.app.log("spriteY", "" + manager.getFrontObject(i).getSp().getY());
					Gdx.app.log("rectX", "" + manager.getFrontObject(i).getSp().getBoundingRectangle().getX());
					Gdx.app.log("rectY", "" + manager.getFrontObject(i).getSp().getBoundingRectangle().getY());
					Gdx.app.log("rectSizeX", "" + manager.getFrontObject(i).getSp().getBoundingRectangle().getWidth());
					Gdx.app.log("rectSizeY", "" + manager.getFrontObject(i).getSp().getBoundingRectangle().getHeight());
					Gdx.app.log("CameraX", "" + camera.position.x);
					Gdx.app.log("CameraY", "" + camera.position.y);
	
					
					
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
		for(int i = 0; i < manager.getBackObjects().size();i++) {
			getMousePosition();
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
			setOldMousePosition();
			getMousePosition();
			objectPositonX += mousePositionX - oldmousePositionX;
			objectPositonY += mousePositionY - oldmousePositionY;
			if(selectLayer == FRONT)
			{
				manager.getFrontObject(selectFlag).setPosition(objectPositonX, -objectPositonY);
				
				if(Gdx.input.isKeyPressed(Keys.A))
				{
					manager.setBackObject(manager.getFrontObject(selectFlag));
					manager.removefrontObject(manager.getFrontObject(selectFlag));
					selectLayer = BACK;
					selectFlag = -1;
				}
			}
			else if(selectLayer == BACK)
			{
				manager.getBackObject(selectFlag).setPosition(objectPositonX, -objectPositonY);
				if(Gdx.input.isKeyPressed(Keys.S))
				{
					
					manager.setFrontObject(manager.getBackObject(selectFlag));
					manager.removeBackObject(manager.getBackObject(selectFlag));
					selectLayer = FRONT;
					selectFlag = -1;
				}
			}
			
		}
		else
		{
			selectLayer = -1;
			selectFlag = -1;
		}
	}
	
	private void getMousePosition() {
		mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.getX() * camera.zoom;
		mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() / 2) - camera.getY() * camera.zoom;
	}
	
	private void setOldMousePosition() {
		oldmousePositionX = mousePositionX;
		oldmousePositionY = mousePositionY;
	}
	
}