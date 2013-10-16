package org.genshin.scrollninjaeditor;

import java.awt.List;
import java.util.ArrayList;

import org.genshin.scrollninjaeditor.factory.TextureFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;


public class MapObject {
	@JsonProperty("fileName")	private String fileName;
	@JsonProperty("labelName")	private String labelName;
	@JsonProperty("width")		private int width;
	@JsonProperty("height")		private int height;

	
	//@JsonProperty("name")  public <Name> listname;
	//@JsonProperty("name")  private String name;
	//@JsonProperty("first") private String first;
	//@JsonProperty("last")  private String last;
	//@JsonProperty("email") private String email;
	
	/*class Name
	{
		@JsonProperty("first") public String firstName;
		@JsonProperty("last")  public String lastName;
	}*/
	
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
	
	/*public String getfirstName() {
		return name.get(0);
	}
	public String getlastName() {
		return name.get(1);
	}
	
	public String getEmail() {
		return email;
	}*/
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