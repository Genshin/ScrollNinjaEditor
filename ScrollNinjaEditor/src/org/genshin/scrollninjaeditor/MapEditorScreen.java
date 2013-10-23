package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MapEditorScreen implements Screen {
	private ScrollNinjaEditor   editor;
	private String				fileName;
	private Camera				camera;
	private SpriteBatch 		batch;									// バッチ用
	private Stage 				stage;									// ステージ用
	private Table 				table;									// テーブル用
	private ImageButton 		imageButton;							// イメージボタン
	private SpriteDrawable 		spriteDrawble = new SpriteDrawable();	// ここにスプライトを入れてテーブルに入れる
	private float 				mousePositionX = 0, 					// マウスポジションX
								mousePositionY = 0,						// マウスポジションY
								oldmousePositionX = 0,					// 前回マウスX座標
								oldmousePositionY = 0,					// 前回マウスY座標
								objectPositionX = 0,	 				// オブジェクトX座標
								objectPositionY = 0,					// オブジェクトY座標
								screenWidth = 0,
								screenHeight = 0;
	private MapObjectManager 	manager= new MapObjectManager();		// オブジェクトセレクト用
	private int 				loopCnt = 0;							// ループカウンタ用
	private int 				objectClickFlg = -1;					// オブジェクト用フラグ
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
		batch = new SpriteBatch();
		mouse = new Mouse();

		//===テクスチャ読み込み
		load = new Load(this.fileName);

		//====ボタン
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);				// インプットが可能なステージを選択(一つのみ以降は上書き)
		table = new Table();
		table.setFillParent(true);
		table.debug();
		manager = MapObjectManager.create();			// 生成
		
		//====スクロールペイン
		mapEditorStage = new MapEditorStage();
		mapEditorStage.createScrollPane(stage, manager, spriteDrawble, camera);
		

		//インポートボタン
		Import importButton = new Import(load.getSpriteDrawable(load.IMPORT));
		table.add(importButton).top().left().size(32,32);
		mapEditorStage.addButton(stage, table);
		
		//エクスポートボタン
		Export exportButton = new Export(load.getSpriteDrawable(load.EXPORT));
		table.add(exportButton).top().left().size(32,32);
		mapEditorStage.addButton(stage, table);
		
		// - メニューボタン  -
		imageButton = new ImageButton(load.getSpriteDrawable(load.MENU));
		imageButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y){
				if(!menuClickFlg)
				{
					mapEditorStage.addScrollPane(stage);
					table.getChildren().get(2).setX(screenWidth - imageButton.getWidth() - mapEditorStage.getWidth());
					menuClickFlg = true;
				}
				else
				{
					mapEditorStage.remove(stage);
					table.getChildren().get(2).setX(screenWidth - imageButton.getWidth());
					menuClickFlg = false;
				}
			}
		});
		table.add(imageButton).expand().right();
		mapEditorStage.addButton(stage, table);
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
		load.draw(batch);
		manager.drawFrontObjects(batch);			// オブジェボタン描画
		batch.end();
		stage.act(Gdx.graphics.getDeltaTime());		// ステージ描画
		stage.draw();
		Table.drawDebug(stage);
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