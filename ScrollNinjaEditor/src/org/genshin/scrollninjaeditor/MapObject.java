package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MapObject {
	@JsonProperty("fileName")	private String fileName;
	@JsonProperty("labelName")	private String labelName;
	@JsonProperty("width")		private int width;
	@JsonProperty("height")		private int height;
	private Sprite sprite;

	public MapObject() {}
	
	/**
	 * Constructor
	 * @param obj	mapObject
	 */
	public MapObject(MapObject obj) {
		fileName = obj.fileName;
		labelName = obj.labelName;
		width = obj.width;
		height = obj.height;
		setSprite();
	}

	/**
	 * Constructor
	 * @param sprite
	 */
	public MapObject(Sprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * sprite set
	 */
	public void setSprite() {
		Texture texture = new Texture(Gdx.files.internal("data/objects/" + fileName));
		TextureRegion region = new TextureRegion(texture, 0, 0, width, height);
		sprite = new Sprite(region);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(-sprite.getWidth() / 2 , -sprite.getHeight() / 2);
	}
	
	/**
	 * set sprite position
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		this.sprite.setPosition(x, y);
	}

	/**
	 * @return	fileName
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * @return	labelName
	 */
	public String getLabelName() {
		return labelName;
	}

	/**
	 * @return x position
	 */
	public float getX() {
		return sprite.getX();
	}
	
	/**
	 * @return y position
	 */
	public float getY() {
		return sprite.getY();
	}
	
	/**
	 * @return	sprite
	 */
	public Sprite getSp() {
		return sprite;
	}
	
	/**
	 * draw process
	 * @param batch		SpriteBatch
	 */
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	/**
	 * dispose process
	 */
	public void dispose() {
		sprite = null;
	}
}