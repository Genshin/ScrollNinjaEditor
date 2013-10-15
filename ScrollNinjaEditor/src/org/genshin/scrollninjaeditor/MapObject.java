package org.genshin.scrollninjaeditor;

import org.genshin.scrollninjaeditor.factory.TextureFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class MapObject {
	@JsonProperty("fileName")	private String fileName;
	@JsonProperty("labelName")	private String labelName;
	@JsonProperty("width")		private int width;
	@JsonProperty("height")		private int height;
	private Sprite sprite;
	
	public MapObject() {
	}
	
	public MapObject(String s) {
		Gdx.app.log("t", "aaa");
		Texture texture = TextureFactory.getInstance().get("data/objects/" + fileName);
		TextureRegion region = new TextureRegion(texture, 0, 0, width, height);
		this.sprite = new Sprite(region);
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getLabelName() {
		return labelName;
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