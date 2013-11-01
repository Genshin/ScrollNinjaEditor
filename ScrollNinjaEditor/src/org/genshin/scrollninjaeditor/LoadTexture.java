package org.genshin.scrollninjaeditor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class LoadTexture {
	private ArrayList<Texture>		  array_Texs      		 = new ArrayList<Texture>();
	private ArrayList<TextureRegion>  array_TexRegions		 = new ArrayList<TextureRegion>();
	private ArrayList<Sprite>		  array_Sprites  		 = new ArrayList<Sprite>();

	public static final int	BACKGROUND = 0;
	public static final int	IMPORT     = 1;
	public static final int	EXPORT	   = 2;
	public static final int	MENU	   = 3;
	public static final int	FIELD	   = 4;
	public static final int	TEX_NUM	   = 5;

	/**
	 * 
	 * @param path
	 */
	public LoadTexture(String path) {
		array_Texs.add(new Texture(path));
		array_Texs.get(BACKGROUND).setFilter(TextureFilter.Linear, TextureFilter.Linear);
		array_Texs.add(new Texture(Gdx.files.internal("data/arrow-down.png")));
		array_Texs.add(new Texture(Gdx.files.internal("data/arrow-up.png")));
		array_Texs.add(new Texture(Gdx.files.internal("data/menu.png")));
		
		for(int i = 0; i < array_Texs.size() ; i++) {
			array_TexRegions.add(new TextureRegion(array_Texs.get(i),0,0,array_Texs.get(i).getWidth(),array_Texs.get(i).getHeight()));
			array_Sprites.add(new Sprite(array_TexRegions.get(i)));
		}
		
		//背景画像設定
		Sprite sprite = array_Sprites.get(BACKGROUND);
		sprite.setSize(sprite.getRegionWidth(), sprite.getRegionHeight());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight() / 2);
		sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
		array_Sprites.set(BACKGROUND, sprite);
	}
	
	/**
	 * 
	 * @param index
	 * @return array_Sprites.get(index)
	 */
	public Sprite getSprite(int index) {
		return array_Sprites.get(index);
	}
	
	/**
	 * 
	 * @param index
	 * @return sd
	 */
	public SpriteDrawable getSpriteDrawable(int index) {
		SpriteDrawable sd = new SpriteDrawable(array_Sprites.get(index));
		return sd;
	}
	
	/**
	 * 
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		array_Sprites.get(BACKGROUND).draw(batch);
	}
	
	public void dispose() {
		for(Texture tex : array_Texs) {
			tex.dispose();
		}
	}
}