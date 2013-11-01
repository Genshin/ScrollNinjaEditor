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
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
	private boolean loadflag = false;
	private boolean changeflag = false;
	private TextField fileText;

	private float w;
	private float h;
	private Table preTable; 
	
	private float ratioX;
	private float ratioY;

	private String path;
	
	private float scaleW,scaleH;

	/**
	 * Constructor
	 * @param editor
	 */
	public SelectScreen(ScrollNinjaEditor editor) {
		this.editor = editor;

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

		//stage作成
		stage = new Stage(w,h,true);
		Gdx.input.setInputProcessor(stage);
		
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		//----------------------------------------
		//画像選択ボタン
		//----------------------------------------
		//画像選択ボタン用table作成
		Table selTable = new Table();
		selTable.size( w/5*4 , h / 5 );
		selTable.translate(w/10, h/5*4);
		//ボタン作成
		TextButton openButton = new TextButton("OPEN",skin);
		fileText = new TextField(" ",skin);
		fileText.setTouchable(Touchable.disabled);
		selTable.add(fileText).size(selTable.getWidth() - 64,32);
		selTable.right().add(openButton).size(64, 32);

		//----------------------------------------
		//プレビュー
		//----------------------------------------
		//プレビュー用table作成
		ratioX = w/5*4;
		ratioY = h/5*3; 
		preTable = new Table();
		preTable.setSize(ratioX, ratioY);
		preTable.translate(0, h/5);
		image = new Image();
		preTable.add(image);

		//----------------------------------------
		//作成ボタン
		//----------------------------------------
		//画像選択ボタン用table作成
		Table creTable = new Table();
		creTable.size( w/5*4 , h / 5 );
		creTable.translate(w/10,0.0f);
		//ボタン作成
		TextButton createButton = new TextButton("CREATE",skin);
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
						setThumbnail(file.getName());	//画像切替
						fileText.setText(file.getName());
						fileName = "data/stage/" + file.getName();	//パス保存
						preTable.setSize(ratioX, ratioY);
						preTable.setPosition(0.0f, h/5);
						preTable.translate((w - ratioX)/2, 0.0f);
										
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
			editor.setScreen(new MapEditorScreen(editor, fileName,scaleW,scaleH));
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
		scaleW = width / w;
		scaleH = height / h;
		if(loadflag) {
			setThumbnail(path);	//画像切替
			preTable.setSize(ratioX, ratioY);
			preTable.setPosition(0.0f, h/5);
			preTable.translate((w - ratioX)/2, 0.0f);
		}
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
		stage.dispose();
	}
	
	/**
	 * 選択した画像を取得する
	 * @param str		ファイル名
	 */
	private SpriteDrawable setImage(String str) {
		SpriteDrawable sd = null;

		if(str != null)	{
			Texture texture = TextureFactory.getInstance().get("data/stage/" + str);
			TextureRegion region = new TextureRegion(texture,0,0,texture.getWidth(),texture.getHeight());
			path = str;

			setRatio(texture.getWidth(), texture.getHeight());
			Sprite sprite = new Sprite(region);
			sd = new SpriteDrawable(sprite);
		}

		return sd;
	}

	/**
	 * 選択した画像のサムネイルをセットする
	 * @param str	ファイル名
	 */
	private void setThumbnail(String str) {		
		image.setDrawable(setImage(str));
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