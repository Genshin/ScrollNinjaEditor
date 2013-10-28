package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapEditorStage extends Stage{
	private ScrollPaneStage scrolloPaneStage;
	private Table			table;
	private Import			importButton;
	private Export			exportButton;
	private MenuButton		menuButton;
	private float z = 1.0f;
	
	/**
	 * Create
	 * @param fileName
	 * @param load
	 */
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
	
	/**
	 * create
	 * @param screenWidth
	 * @param screenHeight
	 * @param manager
	 * @param camera
	 */
	public void create(float screenWidth ,float screenHeight,final MapObjectManager manager,Camera camera){
		createScrollPane(manager,camera);
		menuButton.create(table, screenWidth, this);
		addButton(screenWidth, screenHeight);
	}
	
	public float update(){
		// スクロール
		addListener(new InputListener(){
		@Override
		public boolean handle (Event e) {
		if (!(e instanceof InputEvent)) return false;
			InputEvent event = (InputEvent)e;
			if (event.getType() == InputEvent.Type.scrolled) {
				z += 0.1f * event.getScrollAmount();
				if(z < -1)
					z = -1;
				Gdx.app.log("tes", "scrol" + event.getScrollAmount());
			}
			return true;
		}
		});
		return z / 10;
	}
	
	/**
	 * addButton
	 * @param screenWidth
	 * @param screenHeight
	 */
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
	
	/**
	 * createScrollPane
	 * @param manager
	 * @param camera
	 */
	public void createScrollPane(final MapObjectManager manager,Camera camera){
		scrolloPaneStage.create(manager, camera);
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
