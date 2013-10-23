package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapEditorStage extends Stage{
	private ScrollPaneStage scrolloPaneStage;
	
	public MapEditorStage(){
		super();
	}
	
	public void createScrollPane(final MapObjectManager manager,final OrthographicCamera camera){
		scrolloPaneStage = new ScrollPaneStage();
		scrolloPaneStage.create(manager, camera);
	}
	
	/**
	 * AddScrollPane process
	 */
	public void addScrollPane(){
		addActor(scrolloPaneStage.getScrollTable());
	}
	
	/**
	 * AddButton process
	 */
	public void addButton(Table table){
		addActor(table);
	}
	
	/**
	 * Remove process
	 */
	public void remove(){
		getRoot().removeActor(scrolloPaneStage.getScrollTable());
	}

	/**
	 * getScrollPaneWidth
	 * @return scrollPane.getWidth()
	 */
	public float getPaneWidth(){
		return scrolloPaneStage.getScrollPaneWidth();
	}
}
