package org.genshin.scrollninjaeditor;

import java.awt.event.MouseListener;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ScrollNinjaEditor1 implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	//-------------------------------------------------------------
	private Texture texture1;
	private Sprite sprite1;
	private Sprite sprite2;
	private Sprite sprite3;
	private Stage stage;
	private Stage stage1;
	private Stage stage2;
	private Table table;
	private Table table1;
	private Table table2;
	private boolean streach = true;
	public TextureRegion region;
	public TextureRegion region1;
	public TextureRegion region2;
	public ImageButton imageButton;
	public ImageButton imageButton1;
	public SpriteDrawable sd = new SpriteDrawable();
	public SpriteDrawable sd1 = new SpriteDrawable();
	public SpriteDrawable sd2 = new SpriteDrawable();
	public Table scroTable;
	public ScrollPane scro;
	public Skin skin;
	
	public float posx = 0 ,posy = 0;
	public float dSPosX = 0,dSPosY = 0;
	public float dPosX = 0,dPosY = 0;
	public float dEPosX = 0,dEPosY = 0;
	public float objPosX,objPosY;
	public boolean objFlg = false;

	ArrayList<Stage> array_stage = new ArrayList<Stage>();
	ArrayList<Table> array_table = new ArrayList<Table>();
	ArrayList<TextureRegion> array_region = new ArrayList<TextureRegion>();

	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();

		texture = new Texture(Gdx.files.internal("data/3144.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture, 0, 0, 4096, 2048);

		sprite = new Sprite(region);
	
		sprite.setSize(3.0f, 3.0f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		texture = new Texture(Gdx.files.internal("data/311.png"));
		texture1 = new Texture(Gdx.files.internal("data/3144.png"));

		//------------------------------------------------------------------------
		// ===ステージ

		stage = new Stage(600,600,streach);
		Gdx.input.setInputProcessor(stage);

		table = new Table();

		region = new TextureRegion(texture1,0,0,4096,2048);
		sprite1 = new Sprite(region);
		sd.setSprite(sprite1);
		imageButton = new ImageButton(sd);

		table.add(imageButton);
		
		// スクロールビュー
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		scro = new ScrollPane(table,skin);
		scro.setScrollX(posx);
		scro.setScrollY(posy);
		scro.setFlickScroll(false);					// フリックストロークの判定
		scro.setColor(1,1,1,1);						// カラー
		scro.setFadeScrollBars(false);				// ここでfalseなら常に。trueなら使用しとき。
		scro.setScrollingDisabled(false, false);	// 一番目は縦、二番目は横。これによりスクロールをするかしないか
		scroTable = new Table();
		scroTable.setLayoutEnabled(false);			// falseで任意の形(？)   trueで自動調整(？)
		scroTable.setPosition(300, 50);				// ウインドウの左下が(0,0)
		scro.size(700, 350);						// これで大きさ調整可
		scroTable.add(scro);

		stage.addActor(scroTable);;
		//---------------------------------------------------------------------------
		
		//====ボタン
		stage1 = new Stage(300,300,streach);
		//Gdx.input.setInputProcessor(stage1)
		
		table1 = new Table();
		table1.setFillParent(true);
		table1.setX(-250);
		
		region1 = new TextureRegion(texture,0,0,32,32);
		sprite2 = new Sprite(region1);
		sd1.setSprite(sprite2);
		imageButton = new ImageButton(sd1);
		
		table1.add(imageButton);
		
		stage1.addActor(table1);
		
		//-------------------------------------------------------------------------------------
		
		//====オブジェクト
		stage2 = new Stage(300,300,streach);
		Gdx.input.setInputProcessor(stage2);

		table2 = new Table();
		table2.setFillParent(true);
		objPosX = posx;
		objPosY = posy;
		table2.setX(objPosX);
		table2.setY(20);
		
		region2 = new TextureRegion(texture,0,0,32,32);
		sprite3 = new Sprite(region2);
		sd2.setSprite(sprite3);
		imageButton1 = new ImageButton(sd2);
		
		table2.add(imageButton1);
		
		stage2.addActor(table2);
		//-------------------------------------------------------------------------------------
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Keys.LEFT))
		{
			posx -= 5.0f;
			
			if (posx < 0)
				posx = 0;
			
			Gdx.app.log("tag",""+ posx);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			posx += 5.0f;
			
			if (posx > scro.getMaxX())
				posx = scro.getMaxX();
			
			Gdx.app.log("tag",""+ (posx + 828));
	
		}
		scro.setScrollX(posx);
		if (Gdx.input.isKeyPressed(Keys.UP))
		{
			posy -= 5.0f;
			
			if (posy < 0)
				posy = 0;
			
			Gdx.app.log("tag",""+ posy);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN))
		{
			posy += 5.0f;
			
			if (posy > scro.getMaxY())
				posy = scro.getMaxY();
			
			Gdx.app.log("tag",""+ (posy + 478));
		}		
		scro.setScrollY(posy);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();

		// ステージ
	
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		//ボタン
		stage1.act(Gdx.graphics.getDeltaTime());
		stage1.draw();
		
		// オブジェクト
		if(objFlg)
		{
			stage2.act(Gdx.graphics.getDeltaTime());
			stage2.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
