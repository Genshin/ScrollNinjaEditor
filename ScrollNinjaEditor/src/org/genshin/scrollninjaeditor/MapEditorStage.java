package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapEditorStage extends Stage{
	private ScrollPaneStage scrolloPaneStage;
	private Table			table;
	private Import			importButton;
	private Export			exportButton;
	private MenuButton		menuButton;
	private Camera			camera2;
	private LayerManager    layermanager;
	private Label			scale;
	
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
		
		scale = new Label((100 * z) + "%",new Skin(Gdx.files.internal("data/uiskin.json")));
		importButton = new Import(load.getSpriteDrawable(Load.IMPORT));
		exportButton = new Export(load.getSpriteDrawable(Load.EXPORT));
		menuButton = new MenuButton(load.getSpriteDrawable(Load.MENU));
	}
	
	/**
	 * create
	 * @param screenWidth
	 * @param screenHeight
	 * @param manager
	 * @param camera
	 */
	public void create(final float screenWidth ,float screenHeight,final MapObjectManager manager,Camera camera,LayerManager layer){
		layermanager = layer;
		importButton.setlayer(layermanager);
		exportButton.setlayer(layermanager);
		createScrollPane(manager,camera);
		menuButton.create(table, screenWidth, this);
		addButton(screenWidth, screenHeight);
		camera2 = camera;
		scale.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		// スクロール
		addListener(new InputListener(){
		@Override
		public boolean handle (Event e) {
		if (!(e instanceof InputEvent)) return false;
			InputEvent event = (InputEvent)e;
			if (event.getType() == InputEvent.Type.scrolled) {
				if(z < 1.0f) {
					z += 0.1f * event.getScrollAmount();
					if(z < 0.1f)
						z = 0.1f;
					scale.setText(Math.round(100 + (100 - 100 * z)) + "%");
				}
				else if(z < 2.0f){
					z +=0.2f * event.getScrollAmount();
					scale.setText(Math.round(100 - ((z - 1.0f) / 2 * 100)  ) + "%");
				}
				else if(z <= 6.0f) {
					z +=1.0f * event.getScrollAmount();
					if(z > 6.0f)
						z = 6.0f;
					scale.setText(Math.round(70 - 10 * z) + "%");
				}				
				
				Gdx.app.log("tes", "scrol" + event.getScrollAmount());
				Gdx.app.log("tes", "scrol" + z);
			}
			return true;
		}
		});
	}
	
	public float getZoom(float texWidth,float texHeight ){
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		if((Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && Gdx.input.isKeyPressed(Keys.NUM_0)){
			if(texWidth / screenWidth < texHeight / screenHeight)
				return texHeight / screenHeight;
			else
				return texWidth / screenWidth;
		}
		return z ;
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
		
		table.add(scale).top().left().size(64,32);
		addActor(table);
		
		// メニュー
		table.add(menuButton).expand().right().top();
		addActor(table);
	}
	
	/**
	 * createScrollPane
	 * @param manager
	 * @param camera
	 */
	public void createScrollPane(final MapObjectManager manager,Camera camera){
		scrolloPaneStage.menuCreate(manager, camera,layermanager);
		scrolloPaneStage.row();
		scrolloPaneStage.layerFrontCreate();
		addActor(scrolloPaneStage.getTable());
		scrolloPaneStage.layerBackCreate();
		addActor(scrolloPaneStage.getTable());
		removeButton();
	}
	
	/**
	 * AddScrollPane process
	 */
	public void addScrollPane(){
		addActor(scrolloPaneStage.getTable());
	}

	/**
	 * Remove process
	 */
	public void removeButton(){
		getRoot().removeActor(scrolloPaneStage.getTable());
	}

	/**
	 * getScrollPaneWidth
	 * @return scrollPane.getWidth()
	 */
	public float getPaneWidth(){
		return scrolloPaneStage.getScrollPaneWidth();
	}
	
	public void updateLayer(){
		scrolloPaneStage.addFront();
		scrolloPaneStage.addBack();
	}
}
