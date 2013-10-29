package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ObjectButton extends ImageButton {
	 MapObject mapObject;
	 
	 public ObjectButton(SpriteDrawable sprite, MapObject mapObject) {
		 super(sprite);
		 this.mapObject = mapObject;
	 }
	 
	 public MapObject getMapObject() {
		 return mapObject;
	 }
}