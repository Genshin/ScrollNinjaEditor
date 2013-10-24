package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MapEditorScreen implements Screen {
	private ScrollNinjaEditor   editor;
	private String				fileName;
	
	private Camera				camera;
	private SpriteBatch 		batch;									// バッチ用
	private Table 				table;									// テーブル用
	private ImageButton 		imageButton;							// イメージボタン
	private float				screenWidth = 0,
								screenHeight = 0;
	private MapObjectManager 	manager= new MapObjectManager();		// オブジェクトセレクト用
	private boolean				cameraMove = false;
	private boolean 			menuClickFlg = false;					// メニュー用フラグ
	private MapEditorStage 		mapEditorStage;
	private Load				load;
	private Mouse				mouse;

	/**
	 * Constructor
	 * @param editor
	 * @param fileName		background file name
	 */
	public MapEditorScreen(ScrollNinjaEditor editor, String fileName) {
		this.editor = editor;
		this.fileName = fileName;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		camera = new Camera(screenWidth, screenHeight);
		//camera = Camera.getInstance();
		batch = new SpriteBatch();
		mouse = new Mouse();

		//===テクスチャ読み込み
		load = new Load(this.fileName);

		//====ボタン
		mapEditorStage = new MapEditorStage();
		Gdx.input.setInputProcessor(mapEditorStage);
		manager = MapObjectManager.create();			// 生成
		table = new Table();
		table.setFillParent(true);
		table.debug();
		
		//====スクロールペイン
		mapEditorStage.createScrollPane( manager, camera);

		//インポートボタン
		Import importButton = new Import(load.getSpriteDrawable(load.IMPORT));
		table.add(importButton).top().left().size(32,32);
		mapEditorStage.addButton(table);
		
		//エクスポートボタン
		Export exportButton = new Export(load.getSpriteDrawable(load.EXPORT));
		table.add(exportButton).top().left().size(32,32);
		mapEditorStage.addButton(table);
		
		// - メニューボタン  -
		imageButton = new ImageButton(load.getSpriteDrawable(load.MENU));
		imageButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y){
				if(!menuClickFlg)
				{
					mapEditorStage.addScrollPane();
					table.getChildren().get(2).setX(screenWidth - imageButton.getWidth() - mapEditorStage.getPaneWidth());
					menuClickFlg = true;
				}
				else
				{
					mapEditorStage.remove();
					table.getChildren().get(2).setX(screenWidth - imageButton.getWidth());
					menuClickFlg = false;
				}
			}
		});
		table.add(imageButton).expand().right();
		mapEditorStage.addButton(table);
		
	}

	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
		//===カメラ処理
		cameraMove = camera.update(2.0f);
		
		//===オブジェクトクリック
        if(!cameraMove)
           	mouse.update();
        
	}

	/**
	 * Draw process
	 * @param delta		delta time
	 */
	public void draw(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		manager.drawBackObject(batch);
		load.draw(batch);
		manager.drawFrontObjects(batch);			// オブジェボタン描画
		
		batch.end();
		//stage.act(Gdx.graphics.getDeltaTime());		// ステージ描画
	//	stage.draw();
		mapEditorStage.act(Gdx.graphics.getDeltaTime());
		mapEditorStage.draw();
		Table.drawDebug(mapEditorStage);
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		load.dispose();
	}
	



}