package org.genshin.scrollninjaeditor;


import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MapEditorScreen implements Screen {
	private ScrollNinjaEditor editor;
	private String fileName;

	private Camera				camera;
	private SpriteBatch 		batch;									// バッチ用
	private Texture				texture;								// 画像用
	private Sprite 				backSprite;								// 背景用
	private Sprite 				sprite;									// スプライト用
	//private Stage 				stage;									// ステージ用
	private TextureRegion 		region;									// 画像の注視位置設定用
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
	private ArrayList<Texture> 	array_tex = new ArrayList<Texture>();	// テクスチャ用配列
	private int 				loopCnt = 0;							// ループカウンタ用
	private int 				objectClickFlg = -1;					// オブジェクト用フラグ
	private boolean				cameraMove = false;
	private boolean 			menuClickFlg = false;					// メニュー用フラグ
	private MapEditorStage 		mapEditorStage;

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
		mapEditorStage = new MapEditorStage();
		Gdx.input.setInputProcessor(mapEditorStage);
		manager = MapObjectManager.create();			// 生成
		table = new Table();
		table.setFillParent(true);
		table.debug();
		
		//====スクロールペイン
		mapEditorStage.createScrollPane( manager, camera);
		
		// - インポート、エクスポートボタン - 
		for(loopCnt = 1 ; loopCnt < 3 ; loopCnt ++){	// array_tex.get(0)は最背面で使用しているためカウンタは1から
			region = new TextureRegion(array_tex.get(loopCnt % 3),0,0,array_tex.get(loopCnt % 3).getWidth(),array_tex.get(loopCnt % 3).getHeight());
			sprite = new Sprite(region);
			spriteDrawble = new SpriteDrawable();
			spriteDrawble.setSprite(sprite);
			if(loopCnt == 1){
				Import importButton = new Import(spriteDrawble);
				table.add(importButton).top().left().size(32,32);
				mapEditorStage.addButton(table);
			}
			else if(loopCnt == 2){
				Export exportButton = new Export(spriteDrawble);
				table.add(exportButton).top().left().size(32,32);
				mapEditorStage.addButton(table);
			}
		}
		
		// - メニューボタン  -
		region = new TextureRegion(array_tex.get(3), 0, 0, array_tex.get(3).getWidth(), array_tex.get(3).getHeight());
		sprite = new Sprite(region);
		spriteDrawble = new SpriteDrawable();
		spriteDrawble.setSprite(sprite);
		imageButton = new ImageButton(spriteDrawble);
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
		/*if(!cameraMove)
		{
			if(objectClickFlg == -1){
					
				for(loopCnt = 0 ; loopCnt < manager.getFrontObjects().size() ; loopCnt ++){
					mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x * camera.zoom;
					mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y * camera.zoom;
					if(manager.getFrontObjects().get(loopCnt).getSp().getBoundingRectangle().contains(mousePositionX,-mousePositionY)){
						if (Gdx.input.isButtonPressed(Buttons.LEFT)){
							
							objectPositionX = manager.getFrontObjects().get(loopCnt).getX();
	                        objectPositionY = manager.getFrontObjects().get(loopCnt).getY();
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
						objectPositionX += mousePositionX - oldmousePositionX ;
						objectPositionY += mousePositionY - oldmousePositionY ;
						manager.getFrontObjects().get(objectClickFlg).setPosition(objectPositionX, -objectPositionY);
	
					}
					else
						objectClickFlg = -1;					
				}
				
				
			}
		}*/	
        if(!cameraMove)
        {
                if(objectClickFlg == -1){
                        for(loopCnt = 0 ; loopCnt < manager.getFrontObjects().size() ; loopCnt ++){
                                mousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x * camera.zoom;
                                mousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y * camera.zoom;
                                if(manager.getFrontObjects().get(loopCnt).getSp().getBoundingRectangle().contains(mousePositionX,-mousePositionY)){
                                        if (Gdx.input.isButtonPressed(Buttons.LEFT)){
                                                objectPositionX = manager.getFrontObjects().get(loopCnt).getSp().getX();
                                                objectPositionY = -manager.getFrontObjects().get(loopCnt).getSp().getY();
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
                                        objectPositionX += mousePositionX - oldmousePositionX;
                                        objectPositionY += mousePositionY - oldmousePositionY;
                                        manager.getFrontObjects().get(objectClickFlg).setPosition(objectPositionX, -objectPositionY);
                                                      
                                 }
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