package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapEditorStage extends Stage{
	private ScrollPaneStage scrolloPaneStage;
	private Table			table;
	private Import			importButton;
	private Export			exportButton;
	private MenuButton		menuButton; 
	private LayerManager    layermanager;
	
	public MapEditorStage(String fileName, Load load){
		super();
		scrolloPaneStage = new ScrollPaneStage();
		table = new Table();
		table.setFillParent(true);
		table.debug();
		
		importButton = new Import(load.getSpriteDrawable(load.IMPORT));
		
		exportButton = new Export(load.getSpriteDrawable(load.EXPORT));
		
		menuButton = new MenuButton(load.getSpriteDrawable(load.MENU));
	}
	
	public void create(float screenWidth ,float screenHeight,final MapObjectManager manager,Camera camera,LayerManager layer){
		layermanager = layer;
		createScrollPane(manager,camera);
		menuButton.create(table, screenWidth, this);
		addButton(screenWidth, screenHeight);
		
	}
	
	public void addButton(final float screenWidth ,final float screenHeight){
		
		// インポート
		table.add(importButton).top().left().size(32,32);
		addActor(table);
		
		// エクスポート
		table.add(exportButton).top().left().size(32,32);
		addActor(table);
		
		// メニュー
		table.add(menuButton).expand().right();
		addActor(table);
	}
	
	public void createScrollPane(final MapObjectManager manager,Camera camera){
		scrolloPaneStage.create(manager, camera,layermanager);
	}
	
	/**
	 * AddScrollPane process
	 */
	public void addScrollPane(){
		addActor(scrolloPaneStage.getScrollTable());
	}
	
	/**
	 * Remove process
	 */
	public void removeButton(){
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
