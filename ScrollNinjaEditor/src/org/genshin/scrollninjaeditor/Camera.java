package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sun.corba.se.impl.ior.OldPOAObjectKeyTemplate;

public class Camera extends OrthographicCamera {
	
	private static Camera instance = new Camera();
	private MapObjectManager manager;
	private boolean moveFlag          = false;
	private float   oldmousePositionX = 0.0f;
	private float   oldmousePositionY = 0.0f;
	private float   mousePositionX = 0.0f;
	private float   mousePositionY = 0.0f;
	
	public Camera() {
		
	}
	
	public Camera(float width , float height) {
		super(width , height);
		manager = MapObjectManager.getInstance();
	}
	
	public static Camera getInstance() {
		return instance;
	}
	
	public void move() {
			
		oldmousePositionX = mousePositionX;
		oldmousePositionY = mousePositionY;
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			
			mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + this.position.x;
			mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() / 2) - this.position.y;
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT))	{
				moveFlag = true;
				
				this.translate(-(mousePositionX - oldmousePositionX) / 2, (mousePositionY - oldmousePositionY) / 2);
				
				Gdx.app.log("tag","" + this.getX());
				Gdx.app.log("tag","" + this.getY());
			}
			
		}
		else {
			moveFlag = false;
		}
		
	}
	
	public void zoom(float zoomSize) {
		if((Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && Gdx.input.isKeyPressed(Keys.NUM_0)) {
			this.zoom = 2;
		}
		else{
			this.zoom = zoomSize;
		}
	}
	
	public boolean update(float zoomSize)
	{
		move();
		zoom(zoomSize);
		this.update();
		return moveFlag;
	}
	
	
	public float getX() {
		return this.position.x;
	}
	
	public float getY() {
		return this.position.y;
	}
}