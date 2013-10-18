package org.genshin.scrollninjaeditor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MapEditorScreen implements Screen {
	ScrollNinjaEditor editor;
	String fileName;

	private OrthographicCamera 	camera;									// カメラ
	private SpriteBatch 		batch;									// バッチ
	private Texture				texture;								// 画像
	private Sprite 				backsprite;								// 背景用
	private Sprite 				subsprite;								// スプライト
	private Stage 				stage;									// ステージ
	private TextureRegion 		region;									// 画像の注視位置設定用
	private Table 				table;									// テーブル
	private Table 				scroTable;								// スクロール用テーブル
	private ImageButton 		importButton;							// インポートボタン
	private ImageButton 		exportButton;							// エクスポートボタン
	private SpriteDrawable 		sd = new SpriteDrawable();				// ここにスプライトを入れてテーブルに入れる
	private Skin 				skin;									// スキン
	private ScrollPane 			scro;									// スクロールペイン
	private float 				mousePositionX = 0, 					// マウスポジションX
								mousePositionY = 0,						// マウスポジションY
								spritePositionX = 0,	 				// スプライトポジションX
								spritePositionY = 0;					// スプライトポジションY
	private int 				i = 0;									// ループカウンタ
	private MapObjectManager 	mpobject= new MapObjectManager();		// オブジェクトセレクト用
	private ArrayList<Texture> 	array_tex = new ArrayList<Texture>();	// テクスチャ用配列
	private int f = -1;

	/**
	 * Constructor
	 * @param editor
	 * @param fileName		background file name
	 */
	public MapEditorScreen(ScrollNinjaEditor editor, String fileName) {
		this.editor = editor;
		this.fileName = fileName;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(w , h);
		batch = new SpriteBatch();

		//===テクスチャ読み込み
		texture = new Texture(this.fileName);							// 最背面
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		array_tex.add(texture);
		texture = new Texture(Gdx.files.internal("data/import.png"));	// インポート
		array_tex.add(texture);
		texture = new Texture(Gdx.files.internal("data/export.png"));	// エクスポート
		array_tex.add(texture);

		//====最背面(選択マップ)
		region = new TextureRegion(array_tex.get(0), 0, 0, array_tex.get(0).getWidth(),array_tex.get(0).getHeight());
		backsprite = new Sprite(region);
		backsprite.setSize(backsprite.getRegionWidth(),backsprite.getRegionHeight());
		backsprite.setOrigin(backsprite.getWidth()/2, backsprite.getHeight()/2);
		backsprite.setPosition(-backsprite.getWidth()/2, -backsprite.getHeight()/2);

		//====ボタン
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		mpobject = MapObjectManager.create();
		
		// - 複数化 - 
		for (i = 0 ; i < mpobject.getMapObjectList().size() ; i ++){
			mpobject.getMapObjectList().get(i).getSp().setSize(64,64);
			sd = new SpriteDrawable();															// 上書きが必要
			sd.setSprite(mpobject.getMapObjectList().get(i).getSp());							// 上書きではないので注意
			final ObjectButton objB = new ObjectButton(sd, mpobject.getMapObjectList().get(i));
			
			objB.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event ,float x,float y){
					MapObject mapobj = new MapObject(objB.mapObject);
					mpobject.setMapObject(mapobj);
					mpobject.getMapObjects().get(i).setPosition(camera.position.x,camera.position.y); // カメラに映るように描画
				}
			});
			table.add(objB);
		}

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		scro = new ScrollPane(table,skin);
		scro.setFlickScroll(false);					// フリックの有無
		scro.setFadeScrollBars(true);				// ここでfalseなら常に。trueなら使用するとき。
		scro.setScrollingDisabled(false, false);	// 一番目は縦、二番目は横。これによりスクロールをするかしないか
		scroTable = new Table();
		scroTable.setFillParent(true);
		scroTable.setY(200);
		scroTable.add(scro);
		stage.addActor(scroTable);

		// - インポート、エクスポートボタン - 
		for(i = 1 ; i < 3 ; i ++){
			table = new Table();
			table.setLayoutEnabled(false);
			table.setX((i-1) * 150);
			region = new TextureRegion(array_tex.get(i % 3),0,0,array_tex.get(i % 3).getWidth(),array_tex.get(i % 3).getHeight());
			subsprite = new Sprite(region);
			sd = new SpriteDrawable();
			sd.setSprite(subsprite);
			if(i == 1){
				importButton = new ImageButton(sd);
				importButton.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event ,float x,float y){
						fileImport();
					}
				});
				table.add(importButton);
				stage.addActor(table);
			}
			else if(i == 2){
				exportButton = new ImageButton(sd);
				exportButton.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event ,float x,float y){
						fileExport();
					}
				});
				table.add(exportButton);
				stage.addActor(table);
			}
		}
	}

	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
		//===カメラ移動
		if (Gdx.input.isKeyPressed(Keys.LEFT)){
			camera.position.x -= 50;
			if (camera.position.x < backsprite.getX() + Gdx.graphics.getWidth()/2)
				camera.position.x = backsprite.getX() + Gdx.graphics.getWidth()/2;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)){
			camera.position.x += 50;
			if (camera.position.x > backsprite.getOriginX() - Gdx.graphics.getWidth()/2)
				camera.position.x = backsprite.getOriginX() - Gdx.graphics.getWidth()/2;
		}
		if (Gdx.input.isKeyPressed(Keys.UP)){
			camera.position.y += 30;
			if (camera.position.y > backsprite.getOriginY() - Gdx.graphics.getHeight()/2)
				camera.position.y = backsprite.getOriginY() - Gdx.graphics.getHeight()/2;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)){
			camera.position.y -= 30;
			if (camera.position.y < backsprite.getY() + Gdx.graphics.getHeight()/2)
				camera.position.y = backsprite.getY() + Gdx.graphics.getHeight()/2;
		}
		camera.update();

		//===スプライトクリック
		if(f == -1){
			for(i = 0 ; i < mpobject.getMapObjects().size() ; i ++){
				mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x;
				mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y;
				if(mpobject.getMapObjects().get(i).getSp().getBoundingRectangle().contains(mousePositionX,-mousePositionY)){
					if (Gdx.input.isButtonPressed(Buttons.LEFT)){
						f = i;
						break;
					}
					if (Gdx.input.isButtonPressed(Buttons.RIGHT)){
						mpobject.getMapObjects().remove(i);
						break;
					}
				}
			}
		}

		else{
			if (Gdx.input.isButtonPressed(0)){
				mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x;
				mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y;
				if(mpobject.getMapObjects().get(f).getSp().getBoundingRectangle().contains(mousePositionX,-mousePositionY)){
					spritePositionX = mousePositionX - mpobject.getMapObjects().get(f).getSp().getWidth() / 2;
					spritePositionY = mousePositionY + mpobject.getMapObjects().get(f).getSp().getHeight() / 2;
					mpobject.getMapObjects().get(f).setPosition(spritePositionX, -spritePositionY);
				}
			}
			else
				f = -1;
		}
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
		backsprite.draw(batch);					// 背景描画
		mpobject.drawMapObjects(batch);			// オブジェボタン描画
		batch.end();
		stage.act(Gdx.graphics.getDeltaTime());	// ステージ描画
		stage.draw();
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
		texture.dispose();
	}
	
	public void fileImport(){
		Gdx.app.log("tag","インポート:" + importButton);
	}
	
	public void fileExport(){
		Gdx.app.log("tag","エクスポート:" + exportButton);
	}

}