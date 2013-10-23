package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CopyOfMapEditorStage extends Stage{
	private ScrollPaneStage scrolloPaneStage;
	
	public CopyOfMapEditorStage(){
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
		//scrolloPaneStage.add();
		addActor(scrolloPaneStage.getScrollTable());
	}
	
	/**
	 * AddButton process
	 */
	public void addButton(Table table){
		//stage.addActor(table);
		addActor(table);
	}
	
	/**
	 * Remove process
	 */
	public void remove(){
		//scrolloPaneStage.remove();
		getRoot().removeActor(scrolloPaneStage.getScrollTable());
	}

	/**
	 * getScrollPaneWidth
	 * @return scrollPane.getWidth()
	 */
	public float getWidth(){
		return scrolloPaneStage.getScrollPaneWidth();
	}
}
