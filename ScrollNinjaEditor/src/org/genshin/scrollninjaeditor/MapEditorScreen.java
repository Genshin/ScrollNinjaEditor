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

	private OrthographicCamera 	camera;									// カメラ用
	private SpriteBatch 		batch;									// バッチ用
	private Texture				texture;								// 画像用
	private Sprite 				backSprite;								// 背景用
	private Sprite 				sprite;									// スプライト用
	private Stage 				stage;									// ステージ用
	private TextureRegion 		region;									// 画像の注視位置設定用
	private Table 				table;									// テーブル用
	private Table 				scroTable;								// スクロール用テーブル
	private ImageButton 		importButton;							// インポートボタン
	private ImageButton 		exportButton;							// エクスポートボタン
	private SpriteDrawable 		sd = new SpriteDrawable();				// ここにスプライトを入れてテーブルに入れる
	private Skin 				skin;									// スキン用
	private ScrollPane 			scro;									// スクロールペイン用
	private float 				mousePositionX = 0, 					// マウスX座標
								mousePositionY = 0,						// マウスY座標
								objectPositionX = 0,	 				// オブジェクトX座標
								objectPositionY = 0;					// オブジェクトY座標
	private MapObjectManager 	mapoOject= new MapObjectManager();		// オブジェクト用
	private ArrayList<Texture> 	array_tex = new ArrayList<Texture>();	// テクスチャ用配列
	private int 				loopCnt = 0;							// ループカウンタ用
	private int 				objectClickFlg = -1;					// オブジェクト用フラグ

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
		backSprite = new Sprite(region);
		backSprite.setSize(backSprite.getRegionWidth(),backSprite.getRegionHeight());		// サイズ設定
		backSprite.setOrigin(backSprite.getWidth()/2, backSprite.getHeight()/2);			// 原点設定(回転時に中心で回るように)
		backSprite.setPosition(-backSprite.getWidth()/2, -backSprite.getHeight()/2);		// 表示位置設定

		//====ボタン
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);				// インプットが可能なステージを選択(一つのみ以降は上書き)
		table = new Table();
		mapoOject = MapObjectManager.create();			// 生成
		
		// - 複数化 - 
		for (loopCnt = 0 ; loopCnt < mapoOject.getMapObjectList().size() ; loopCnt ++){
			mapoOject.getMapObjectList().get(loopCnt).getSp().setSize(64,64);
			sd = new SpriteDrawable();												// 上書きが必要
			sd.setSprite(mapoOject.getMapObjectList().get(loopCnt).getSp());		// 上書きではないので注意
			final ObjectButton objB = new ObjectButton(sd, mapoOject.getMapObjectList().get(loopCnt));
			
			objB.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event ,float x,float y){
					MapObject mapobj = new MapObject(objB.mapObject);										 // クリックされたオブジェクト情報を読み込み
					mapoOject.setMapObject(mapobj);															 // オブジェクトをセット
					mapoOject.getMapObjects().get(loopCnt).setPosition(camera.position.x,camera.position.y); // カメラに映るように描画
				}
			});
			table.add(objB);
		}

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));	// スキンファイルを読み込み
		scro = new ScrollPane(table,skin);							// スクロールに情報を読み込み
		scro.setFlickScroll(false);									// フリックの有無
		scro.setFadeScrollBars(true);								// ここでfalseなら常に。trueなら使用するとき。
		scro.setScrollingDisabled(false, false);					// 一番目は縦、二番目は横。これによりスクロールをするかしないか
		scroTable = new Table();
		scroTable.setFillParent(true);								// 表示位置を中心(true)に設定
		scroTable.setY(200);										// 入力した分表示位置をずらす
		scroTable.add(scro);
		stage.addActor(scroTable);

		// - インポート、エクスポートボタン - 
		for(loopCnt = 1 ; loopCnt < 3 ; loopCnt ++){	// array_tex.get(0)は最背面で使用しているためカウンタは1から
			table = new Table();
			table.setLayoutEnabled(false);				// この設定で任意の設定が可能
			table.setX((loopCnt - 1) * 150);
			region = new TextureRegion(array_tex.get(loopCnt % 3),0,0,array_tex.get(loopCnt % 3).getWidth(),array_tex.get(loopCnt % 3).getHeight());
			sprite = new Sprite(region);
			sd = new SpriteDrawable();
			sd.setSprite(sprite);
			if(loopCnt == 1){
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
			else if(loopCnt == 2){
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
			if (camera.position.x < backSprite.getX() + Gdx.graphics.getWidth()/2)
				camera.position.x = backSprite.getX() + Gdx.graphics.getWidth()/2;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)){
			camera.position.x += 50;
			if (camera.position.x > backSprite.getOriginX() - Gdx.graphics.getWidth()/2)
				camera.position.x = backSprite.getOriginX() - Gdx.graphics.getWidth()/2;
		}
		if (Gdx.input.isKeyPressed(Keys.UP)){
			camera.position.y += 30;
			if (camera.position.y > backSprite.getOriginY() - Gdx.graphics.getHeight()/2)
				camera.position.y = backSprite.getOriginY() - Gdx.graphics.getHeight()/2;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)){
			camera.position.y -= 30;
			if (camera.position.y < backSprite.getY() + Gdx.graphics.getHeight()/2)
				camera.position.y = backSprite.getY() + Gdx.graphics.getHeight()/2;
		}
		camera.update();

		//===オブジェクトクリック
		if(objectClickFlg == -1){
			for(loopCnt = 0 ; loopCnt < mapoOject.getMapObjects().size() ; loopCnt ++){
				mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x;
				mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y;
				if(mapoOject.getMapObjects().get(loopCnt).getSp().getBoundingRectangle().contains(mousePositionX,-mousePositionY)){
					if (Gdx.input.isButtonPressed(Buttons.LEFT)){
						objectClickFlg = loopCnt;
						break;
					}
					if (Gdx.input.isButtonPressed(Buttons.RIGHT)){
						mapoOject.getMapObjects().remove(loopCnt);
						break;
					}
				}
			}
		}

		else{
			if (Gdx.input.isButtonPressed(0)){
				mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x;
				mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y;
				if(mapoOject.getMapObjects().get(objectClickFlg).getSp().getBoundingRectangle().contains(mousePositionX,-mousePositionY)){
					objectPositionX = mousePositionX - mapoOject.getMapObjects().get(objectClickFlg).getSp().getWidth() / 2;
					objectPositionY = mousePositionY + mapoOject.getMapObjects().get(objectClickFlg).getSp().getHeight() / 2;
					mapoOject.getMapObjects().get(objectClickFlg).setPosition(objectPositionX, -objectPositionY);
				}
			}
			else
				objectClickFlg = -1;
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
		backSprite.draw(batch);						// 背景描画
		mapoOject.drawMapObjects(batch);			// オブジェボタン描画
		batch.end();
		stage.act(Gdx.graphics.getDeltaTime());		// ステージ描画
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