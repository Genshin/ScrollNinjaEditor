package org.genshin.scrollninjaeditor;

import java.io.File;

import javax.swing.JFileChooser;

import org.genshin.scrollninjaeditor.factory.TextureFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
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
	private ScrollNinjaEditor editor;
	private String fileName;
	private SpriteBatch batch;
	private Stage stage;
	private Image image;
	private static boolean loadflag = false;
	private static boolean changeflag = false;
	private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	private TextField fileText;

	private Texture texture;
	private TextureRegion region;
	private Sprite sprite;
	private SpriteDrawable sd;
	
	private float ratioX;
	private float ratioY;
	
	/**
	 * Constructor
	 * @param editor
	 */
	public SelectScreen(ScrollNinjaEditor editor) {
		this.editor = editor;
		
		float  w = Gdx.graphics.getWidth();
		final float  h = Gdx.graphics.getHeight();

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
		fileText = new TextField(" ",skin);
		selTable.add(fileText).size(selTable.getWidth() - 64,32);
		selTable.right().add(openButton).size(64, 32);

		//----------------------------------------
		//プレビュー
		//----------------------------------------
		//プレビュー用table作成
		ratioX = w/5*4;
		ratioY = h/5*3; 
		final Table preTable = new Table();
		preTable.translate(0, h/5);
		setdata(null);
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
		openButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event,float x,float y) {
				File current = new File("./bin/data/stage");
				JFileChooser fileChooser = new JFileChooser(current.getAbsolutePath());
				
				//ファイル選択フィルター宣言
				ExtendsFileFilter filter = new ExtendsFileFilter(".png","PNG  ファイル(*.png)");
				
				//フィルター設定
				fileChooser.addChoosableFileFilter(filter);
				
				int res = fileChooser.showOpenDialog(fileChooser);
				
				if(res == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();

					//開いたファイルが正しい場合
					if(filter.accept(file)) {
						changeTex(file.getName());	//画像切替
						fileText.setText(file.getName());
						fileName = "data/stage/" + file.getName();	//パス保存
						preTable.setSize(ratioX, ratioY);
						preTable.setPosition(0.0f, h/5);
						preTable.translate(Gdx.graphics.getWidth() / 2 - preTable.getWidth() / 2, 0.0f);
						loadflag = true;
					}
				}
			}
		});
		
		createButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y){
				if(loadflag) {
					changeflag = true;
				}
			}
		});
				
		stage.addActor(selTable);
		stage.addActor(preTable);
		stage.addActor(creTable);
	}
	
	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
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
	
	/**
	 * 選択した画像をセットする
	 * @param str		ファイル名
	 */
	private void setdata(String str) {
		if(str != null)	{
			texture = TextureFactory.getInstance().get("data/stage/" + str);
			region = new TextureRegion(texture,0,0,texture.getWidth(),texture.getHeight());
			setRatio(texture.getWidth(), texture.getHeight());
			sprite = new Sprite(region);
			sd = new SpriteDrawable(sprite);
		}
		else {
			texture = null;
			region = null;
			sprite = null;
			sd = null;
		}
	}

	/**
	 * 選択した画像に変更する
	 * @param str	ファイル名
	 */
	private void changeTex(String str) {
		setdata(str);
		
		image.setDrawable(sd);
	}
	
	/**
	 * 選択した画像を表示する際の縦横比を調整
	 * @param width
	 * @param height
	 */
	private void setRatio(int width,int height)	{		
		ratioX = width / height;
		ratioX = ratioY * ratioX;
	}
}