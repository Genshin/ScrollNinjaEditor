package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MapEditorStage {
	private ScrollPaneStage scrolloPaneStage;
	
	public MapEditorStage(){
	}
	
	public void createScrollPane(Stage stage,final MapObjectManager manager,SpriteDrawable spriteDrawble,final OrthographicCamera camera){
		scrolloPaneStage = new ScrollPaneStage();
		scrolloPaneStage.create(stage,manager, spriteDrawble, camera);
	}
	
	/**
	 * AddScrollPane process
	 */
	public void addScrollPane(Stage stage){
		scrolloPaneStage.add(stage);
	}
	
	/**
	 * AddButton process
	 */
	public void addButton(Stage stage ,Table table){
		stage.addActor(table);
	}
	
	/**
	 * Remove process
	 */
	public void remove(Stage stage){
		scrolloPaneStage.remove(stage);
	}

	/**
	 * getScrollPaneWidth
	 * @return scrollPane.getWidth()
	 */
	public float getWidth(){
		return scrolloPaneStage.getScrollPaneWidth();
	}

}
