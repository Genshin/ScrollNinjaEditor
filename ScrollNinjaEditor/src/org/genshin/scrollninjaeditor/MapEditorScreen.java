package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapEditorScreen implements Screen {
	private ScrollNinjaEditor   editor;
	private String				fileName;
	
	private Camera				camera;
	private SpriteBatch 		batch;									// バッチ用
	private float				screenWidth = 0,
								screenHeight = 0;
	private MapObjectManager 	manager = new MapObjectManager();
	private boolean				cameraMove = false;
	private MapEditorStage 		mapEditorStage;
	private Load				load;
	private Mouse				mouse;
	private float				zoom = 1.0f;

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
		//====ステージ
		
		mapEditorStage = new MapEditorStage(this.fileName,load);
		Gdx.input.setInputProcessor(mapEditorStage);
		manager = MapObjectManager.create();
		
		//===クリエイト
		mapEditorStage.create(screenWidth,screenHeight,manager,Camera.getInstance());
	}

	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
		zoom = mapEditorStage.update() / 100;
		Gdx.app.log("tag", "" + zoom);
		if(zoom < 1){
			zoom = 1;
		}
		//===カメラ処理
		cameraMove = camera.update(zoom);
		
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