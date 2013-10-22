package org.genshin.scrollninjaeditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
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
import com.sun.corba.se.impl.ior.OldPOAObjectKeyTemplate;

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
	private ImageButton 		imageButton;							// イメージボタン
	private SpriteDrawable 		sd = new SpriteDrawable();				// ここにスプライトを入れてテーブルに入れる
	private Skin 				skin;									// スキン
	private ScrollPane 			scro;									// スクロールペイン
	private float 				mousePositionX = 0, 					// マウスポジションX
								mousePositionY = 0,						// マウスポジションY
								oldmousePositionX = 0,					// 前回マウスX座標
								oldmousePositionY = 0,					// 前回マウスY座標
								objectPositionX = 0,	 				// オブジェクトX座標
								objectPositionY = 0,					// オブジェクトY座標
								spritePositionX = 0,	 				// スプライトポジションX
								spritePositionY = 0,					// スプライトポジションY
								menuPositionX = 0,						// メニュー座標X
								menuPositionY = 0,						// メニュー座標Y
								w = 0,
								h = 0;
	private int 				i = 0;									// ループカウンタ
	private MapObjectManager 	manager= new MapObjectManager();		// オブジェクトセレクト用
	private ArrayList<Texture> 	array_tex = new ArrayList<Texture>();	// テクスチャ用配列
	private int 				loopCnt = 0;							// ループカウンタ用
	private int 				objectClickFlg = -1;					// オブジェクト用フラグ
	private Boolean				cameraMove = false;
	private boolean 			menuClickFlg = false;					// メニュー用フラグ

	/**
	 * Constructor
	 * @param editor
	 * @param fileName		background file name
	 */
	public MapEditorScreen(ScrollNinjaEditor editor, String fileName) {
		this.editor = editor;
		this.fileName = fileName;
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(w , h);
		batch = new SpriteBatch();

		//===テクスチャ読み込み
		texture = new Texture(this.fileName);							// 最背面
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		array_tex.add(texture);
		texture = new Texture(Gdx.files.internal("data/arrow-down.png"));	// インポート
		array_tex.add(texture);
		texture = new Texture(Gdx.files.internal("data/arrow-up.png"));	// エクスポート
		array_tex.add(texture);
		texture = new Texture(Gdx.files.internal("data/menu.png"));		// メニュー
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
		manager = MapObjectManager.create();			// 生成
		
		
		
		// - 複数化 - 
		for (loopCnt = 0 ; loopCnt < manager.getMapObjectList().size() ; loopCnt ++){
			if(loopCnt % 3 == 0)
				table.row();
			manager.getMapObjectList().get(loopCnt).getSp().setSize(64,64);
			sd = new SpriteDrawable();												// 上書きが必要
			sd.setSprite(manager.getMapObjectList().get(loopCnt).getSp());		// 上書きではないので注意
			final ObjectButton objB = new ObjectButton(sd, manager.getMapObjectList().get(loopCnt));
			
			objB.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event ,float x,float y){
					MapObject mapobj = new MapObject(objB.mapObject);										 // クリックされたオブジェクト情報を読み込み
					manager.setFrontObject(mapobj);															 // オブジェクトをセット
					manager.getFrontObjects().get(loopCnt).setPosition(camera.position.x,camera.position.y); // カメラに映るように描画
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
		scroTable.setLayoutEnabled(false);
		scro.setWidth(200);
		scro.setHeight(h);
		scroTable.setX(w - scro.getWidth());
		scroTable.add(scro);
		
		// - インポート、エクスポートボタン - 
		for(loopCnt = 1 ; loopCnt < 3 ; loopCnt ++){	// array_tex.get(0)は最背面で使用しているためカウンタは1から
			table = new Table();
			table.setLayoutEnabled(false);				// この設定で任意の設定が可能
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
				importButton.setSize(32, 32);
				table.setX((loopCnt - 1) * importButton.getWidth());					// X座標
				table.setY(h - importButton.getHeight());	// Y座標
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
				exportButton.setSize(32, 32);
				table.setX((loopCnt - 1) * exportButton.getWidth());					// X座標
				table.setY(h - exportButton.getHeight());	// Y座標
				table.add(exportButton);
				stage.addActor(table);
			}
		}
		
		// - ボタン  -
		table = new Table();
		table.setLayoutEnabled(false);
		region = new TextureRegion(array_tex.get(3), 0, 0, array_tex.get(3).getWidth(), array_tex.get(3).getHeight());
		sprite = new Sprite(region);
		sd = new SpriteDrawable();
		sd.setSprite(sprite);
		imageButton = new ImageButton(sd);
		imageButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y){
				if(!menuClickFlg)
				{
					stage.addActor(scroTable);
					menuPositionX = scroTable.getX();
					table.setX(w - imageButton.getWidth() - scro.getWidth());
					Gdx.app.log("tag", "" + stage.getRoot().getChildren());
					menuClickFlg = true;
				}
				else
				{
					stage.getRoot().removeActor(scroTable);
					table.setX(w - imageButton.getWidth());
					Gdx.app.log("tag", "" + x);
					menuClickFlg = false;
				}
			}
		});
		imageButton.setHeight(h);
		table.setX(w - imageButton.getWidth());
		table.setY(h - imageButton.getHeight());
		table.add(imageButton);
		stage.addActor(table);
	}

	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
		//===カメラ移動
		if(Gdx.input.isKeyPressed(Keys.SPACE))
		{
			oldmousePositionX = mousePositionX;
			oldmousePositionY = mousePositionY;
			
			mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x;
			mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y;
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT))
			{
				cameraMove = true;

				camera.position.x -= (mousePositionX - oldmousePositionX)/2;
				camera.position.y += (mousePositionY - oldmousePositionY)/2;
			}	
		}
		else
		{
			cameraMove = false;
		}
		if((Gdx.input.isKeyPressed(Keys.CONTROL_LEFT ) || (Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))))
		{
			if(Gdx.input.isKeyPressed(Keys.NUM_0))
			{
				camera.zoom = 2.0f;
			}
		}
		camera.update();	

		//===オブジェクトクリック
		if(objectClickFlg == -1){
			for(loopCnt = 0 ; loopCnt < manager.getFrontObjects().size() ; loopCnt ++){
				mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x * camera.zoom;
				mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y * camera.zoom;
				if(manager.getFrontObjects().get(loopCnt).getSp().getBoundingRectangle().contains(mousePositionX,-mousePositionY)){
					if (Gdx.input.isButtonPressed(Buttons.LEFT)){
						objectClickFlg = loopCnt;
						break;
					}
					if (Gdx.input.isButtonPressed(Buttons.RIGHT)){
						manager.getFrontObjects().remove(loopCnt);
						break;
					}
				}
			}
		}

		else{
			if (Gdx.input.isButtonPressed(0)){
				oldmousePositionX = mousePositionX;
				oldmousePositionY = mousePositionY;
				mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x * camera.zoom;
				mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y * camera.zoom;
				if(manager.getFrontObjects().get(objectClickFlg).getSp().getBoundingRectangle().contains(mousePositionX,-mousePositionY)){
					objectPositionX = mousePositionX - manager.getFrontObjects().get(objectClickFlg).getSp().getWidth() / 2;
					objectPositionY = mousePositionY + manager.getFrontObjects().get(objectClickFlg).getSp().getHeight() / 2;
					manager.getFrontObjects().get(objectClickFlg).setPosition(objectPositionX, -objectPositionY);

				}
				else
					objectClickFlg = -1;
			}
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
		manager.drawFrontObjects(batch);			// オブジェボタン描画
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
	
	/**
	 * Import process
	 */
	public void fileImport(){
		File current = new File("./bin/data");
		JFileChooser FileChooser = new JFileChooser(current.getAbsolutePath());
		
		//ファイル選択フィルター宣言
		ExtendsFileFilter filter[] = {
			new ExtendsFileFilter(".json","JSON  ファイル(*.json)"),
		};
		
		//フィルター設定
		for(int i = 0; i < filter.length ; i ++)
			FileChooser.addChoosableFileFilter(filter[i]);
		
		int res = FileChooser.showOpenDialog(FileChooser);

		if(res == JFileChooser.APPROVE_OPTION) {
			File file = FileChooser.getSelectedFile();
			//開いたファイルの種類のチェック
			for(int i = 0 ;i < filter.length ; i++)	{	
				//開いたファイルが正しい場合
				if(filter[i].accept(file)) {
					JsonRead read = new JsonRead(Gdx.files.absolute(file.getAbsolutePath()).path());
					
					for(int node = 0;read.getRootNode(node) != null;node++)	{
						MapObject setObj = null;
						//スプライトの種類チェック
						for(MapObject obj:manager.getMapObjectList()) {
							String label = read.getObjectString("label", node);
															
							if(label.matches(obj.getLabelName())) {	
								setObj = new MapObject(obj);
								break;
							}
						}
						setObj.setPosition(read.getObjectFloat("x", node), read.getObjectFloat("y", node));
						manager.setFrontObject(setObj);
					}
				}
			}
		}
	}

	/**
	 * Export process
	 */
	public void fileExport() {
		File current = new File("./bin/data");
		JFileChooser fileChooser = new JFileChooser(current.getAbsolutePath());
		int select = fileChooser.showSaveDialog(fileChooser);
		
		if(select == JFileChooser.APPROVE_OPTION) {	
			JsonWrite write = new JsonWrite();
			for(MapObject obj : manager.getFrontObjects())	{		
				write.addObject();
				write.putObject("name", obj.getFileName());
				write.putObject("label", obj.getLabelName());
				write.putObject("x", obj.getX());
				write.putObject("y", obj.getY());
			}
			write.writeData(fileChooser.getSelectedFile().toString() + ".json");
		}
		Gdx.app.log("tag","エクスポート:" + exportButton);
	}
	
	/*private boolean checkBoundingSprite(Sprite target,Sprite other)
	{
		if(target.getBoundingRectangle().contains(other.getBoundingRectangle()))
		{
			return true;
		}
		else
			return false;
	}*/


}