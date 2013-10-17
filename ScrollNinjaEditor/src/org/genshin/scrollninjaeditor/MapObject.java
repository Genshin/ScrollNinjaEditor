package org.genshin.scrollninjaeditor;

import org.genshin.scrollninjaeditor.factory.TextureFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.fasterxml.jackson.annotation.JsonProperty;


public class MapObject{
	@JsonProperty("fileName")	private String fileName;
	@JsonProperty("labelName")	private String labelName;
	@JsonProperty("width")		private int width;
	@JsonProperty("height")		private int height;
	private Sprite sprite;

	public MapObject() {}

	/**
	 * sprite set
	 */
	public void setSprite() {
		Texture texture = TextureFactory.getInstance().get("data/objects/" + fileName);
		TextureRegion region = new TextureRegion(texture, 0, 0, width, height);
		this.sprite = new Sprite(region);
		this.sprite.setSize(this.sprite.getRegionWidth(),this.sprite.getRegionHeight());
		this.sprite.setOrigin(this.sprite.getWidth()/2,this.sprite.getHeight()/2);
		this.sprite.setPosition(-this.sprite.getWidth()/2,-this.sprite.getHeight()/2);
	}
	
	/**
	 * set sprite position
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		sprite.setPosition(x, y);
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