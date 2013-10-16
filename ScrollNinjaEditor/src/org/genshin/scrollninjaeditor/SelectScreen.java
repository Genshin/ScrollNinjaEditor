package org.genshin.scrollninjaeditor;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.genshin.scrollninjaeditor.factory.TextureFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class SelectScreen implements Screen {
	ScrollNinjaEditor editor;
	String fileName;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Stage stage;
	private Image image;
	private static boolean loadflag = false;
	private static boolean changeflag = false;
	private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	private TextField fileLabel;
	
	
	private Texture texture;
	private Sprite sprite;
	private TextureRegion region;
	private SpriteDrawable sd;
	
	/**
	 * Constructor
	 * @param editor
	 */
	public SelectScreen(ScrollNinjaEditor editor) {
		this.editor = editor;
		
		float  w = Gdx.graphics.getWidth();
		float  h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1,h/w);
		batch = new SpriteBatch();
		
		//stage作成
		stage = new Stage(w,h,true);
		Gdx.input.setInputProcessor(stage);
		
		//----------------------------------------
		//画像選択ボタン
		//----------------------------------------
		//画像選択ボタン用table作成
		Table selTable = new Table();
		selTable.size( w/5*4 , h / 5 );
		selTable.translate(w/10, h/5*4);
		//ボタン作成
		TextButton openButton;
		openButton = new TextButton("OPEN",skin);
		fileLabel = new TextField(" ",skin);
		selTable.add(fileLabel).size(selTable.getWidth() - 64,32);
		selTable.right().add(openButton).size(64, 32);
		
		
		//----------------------------------------
		//プレビュー
		//----------------------------------------
		//プレビュー用table作成
		Table preTable = new Table();
		preTable.size(w/5*4, h/5*3);
		preTable.translate(w/10, h/5);
		setdata(Gdx.files.internal("data/test.png").toString());
		image = new Image(sd);
		preTable.add(image);
						
		//----------------------------------------
		//作成ボタン
		//----------------------------------------
		//画像選択ボタン用table作成
		Table creTable = new Table();
		creTable.size( w/5*4 , h / 5 );
		creTable.translate(w/10,0.0f);
		//ボタン作成
		TextButton createButton;
		createButton = new TextButton("CREATE",skin);
		creTable.add(createButton).size(64, 32);
		
		
		//ボタン機能設定
		openButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y)
			{
				JFileChooser FileChooser = new JFileChooser();
				
				//ファイル選択フィルター宣言
				ExtendFileFilter filter[] = {
					new ExtendFileFilter(".gif","GIF  ファイル(*.gif)"),
					new ExtendFileFilter(".jpg","JPEG ファイル(*.jpg)"),
					new ExtendFileFilter(".png","PNG  ファイル(*.png)"),
					new ExtendFileFilter(".json","JSON ファイル(*.json)"),
				};
				
				//フィルター設定
				for(int i = 0; i < filter.length ; i ++)
					FileChooser.addChoosableFileFilter(filter[i]);
				
				int res = FileChooser.showOpenDialog(FileChooser);
				
				if(res == JFileChooser.APPROVE_OPTION)
				{
					File file = FileChooser.getSelectedFile();
					//開いたファイルの種類のチェック
					for(int i = 0 ;i < filter.length ; i++)
					{
						//開いたファイルが正しい場合
						if(filter[i].accept(file))
						{
							if(i < 3)
							{
								changeTex(Gdx.files.absolute(file.getAbsolutePath()).path());	//画像切替
								fileLabel.setText(file.getName());
								fileName = Gdx.files.absolute(file.getAbsolutePath()).path();	//パス保存
								loadflag = true;
								break;
							}
							else
							{
								MapObjectManager map = new MapObjectManager();
								map.create();
							}
						}
					}
					
				}
				
			}
		});
		
		createButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y){
				if(loadflag)
				{
					changeflag = true;
				}
			}
		});
				
		selTable.debug();
		preTable.debug();
		creTable.debug();
		stage.addActor(selTable);
		stage.addActor(preTable);
		stage.addActor(creTable);
	}
	
	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
		
		// 「作成」ボタンがクリックされたらマップエディタ画面に遷移。
		// fileNameは表示する背景のファイル名
		// editor.setScreen(new MapEditorScreen(editor, fileName));
		if(changeflag)
			editor.setScreen(new MapEditorScreen(editor, fileName));
	}
	
	/**
	 * Draw process
	 * @param delta		delta time
	 */
	public void draw(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
		Table.drawDebug(stage);			//テーブル枠組み描画
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
		stage.dispose();
	}
	
	private void setdata(String str) {
		//this.texture = new Texture(str);
		this.texture = TextureFactory.getInstance().get(str);
		this.region = new TextureRegion(texture,0,0,texture.getWidth(),texture.getHeight());
		this.sprite = new Sprite(region);
		this.sd = new SpriteDrawable(sprite);
		
	}
	
	private void changeTex(String str)
	{
		Gdx.app.log("", "" + str);
		setdata(str);
		
		image.setDrawable(sd);
	}
	
	class ExtendFileFilter extends FileFilter
	{
		private String extension;
		private String msg;
		
		public ExtendFileFilter(String extension ,String msg){
			this.extension = extension;
			this.msg = msg;
		}

		public boolean accept(java.io.File f) {
			return f.getName().endsWith(extension);
		}

		public String getDescription() {
			return msg;
		}
	}

	
}