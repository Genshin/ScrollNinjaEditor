package org.genshin.scrollninjaeditor;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;

public class SelectScreen implements Screen {
	ScrollNinjaEditor editor;
	String fileName;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Stage stage;
	private TextButton openButton;
	private TextButton createButton;
	private Image image;
	private static boolean loadflag = false;
	private static boolean changeflag = false;
	private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	
	//配列宣言
	private ArrayList<Texture> textures = new ArrayList<Texture>();
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private ArrayList<TextureRegion> regions = new ArrayList<TextureRegion>();
	private ArrayList<SpriteDrawable> sds = new ArrayList<SpriteDrawable>();
	

	
	//定数宣言
	static final int open = 0;
	static final int prev = 1;
	static final int create = 2;

	
	
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
		setdata(open,Gdx.files.internal("data/libgdx.png").toString());
		//ボタン作成
		openButton = new TextButton("OPEN",skin);
		
		selTable.right().add(openButton).size(64, 32);
		
		
		//----------------------------------------
		//プレビュー
		//----------------------------------------
		//プレビュー用table作成
		Table preTable = new Table();
		preTable.size(w/5*4, h/5*3);
		preTable.translate(w/10, h/5);
		setdata(prev,Gdx.files.internal("data/test.png").toString());
		image = new Image(sds.get(prev));
		preTable.add(image);
						
		//----------------------------------------
		//作成ボタン
		//----------------------------------------
		//画像選択ボタン用table作成
		Table creTable = new Table();
		creTable.size( w/5*4 , h / 5 );
		creTable.translate(w/10,0.0f);
		setdata(create,Gdx.files.internal("data/libgdx.png").toString());
		//ボタン作成
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
							changeTex(Gdx.files.absolute(file.getAbsolutePath()).path());	//画像切替
							fileName = Gdx.files.absolute(file.getAbsolutePath()).path();	//パス保存
							loadflag = true;
							break;
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
		for(Sprite sprite:sprites)
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
		textures.remove(textures);
		sprites.remove(sprites);
		regions.remove(regions);
		sds.remove(sds);
		stage.dispose();
	}
	
	private void setdata(int cnt ,String str) {
		textures.add(new Texture(str));
		Texture texture = textures.get(cnt);
		regions.add(new TextureRegion(texture,0,0,texture.getWidth(),texture.getHeight()));
		TextureRegion region = regions.get(cnt);
		sprites.add(new Sprite(region));
		Sprite sprite = sprites.get(cnt);
		sds.add(new SpriteDrawable(sprite));
		
	}
	
	private void changeTex(String str)
	{
		Texture texture = new Texture(str);
		textures.set(prev, texture);
		TextureRegion region = new TextureRegion(texture,0,0,texture.getWidth(),texture.getHeight());
		regions.set(prev, region);
		Sprite sprite = new Sprite(region);
		sprites.set(prev, sprite);
		SpriteDrawable sd = new SpriteDrawable(sprite);
		sds.set(prev, sd);
		
		image.setDrawable(sds.get(prev));
		
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