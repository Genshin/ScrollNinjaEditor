package org.genshin.scrollninjaeditor;

import java.util.ArrayList;

import org.genshin.scrollninjaeditor.factory.TextureFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private Stage stage;
	private TextureRegion region;
	private Table table;
	private ImageButton imageButton;
	//private ObjectButton objB;
	private SpriteDrawable sd = new SpriteDrawable();

	private Sprite sprite2;
	private TextureRegion region2;
	
	private Skin skin;
	private ScrollPane scro;
	private Table scroTable;
	
	private int i;
	private float getMousePositionX = 0 , getMousePositionY = 0;
	private float getSpritePositionX = 0 , getSpritePositionY = 0;
	private boolean sprite_flg = true;
	
	private MapObjectManager mpobject = new MapObjectManager();
	
	private ArrayList<Stage> array_stage = new ArrayList<Stage>();
	private ArrayList<Texture> array_tex = new ArrayList<Texture>();
	private ArrayList<Sprite> array_sprite = new ArrayList<Sprite>();
	private ArrayList<Boolean> array_flg = new ArrayList<Boolean>();
	
	private int nCnt = 0;
	
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
		
		
		//----------------------------------------------------------------------
		//====最背面(選択マップ)
		
		texture = new Texture(this.fileName);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		region = new TextureRegion(texture, 0, 0, 4096, 2048);

		sprite = new Sprite(region);
	
		sprite.setSize(sprite.getRegionWidth(),sprite.getRegionHeight());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		//====ボタン
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		
		texture = new Texture(Gdx.files.internal("data/301.png"));
		array_tex.add(0,texture);
		texture = new Texture(Gdx.files.internal("data/311.png"));
		array_tex.add(1,texture);
		texture = new Texture(Gdx.files.internal("data/314.png"));
		array_tex.add(2,texture);
		
		region2 = new TextureRegion(array_tex.get(0),0,0,512,256);
		
		mpobject = MapObjectManager.create();
		
		// - 複数化 - 
		for (i = 0 ; i < 11 ; i ++)
		{
			if (i == 15 )
				table.row();
			Sprite a = mpobject.getMapObjectList().get(i).getSp();
			a.setSize(64, 64);
			sd = new SpriteDrawable();						// 上書きが必要
			sd.setSprite(a);							// 上書きではないので注意
			final ObjectButton objB = new ObjectButton(sd, mpobject.getMapObjectList().get(i));
			//objB.setSize(64, 64);
			
			objB.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event ,float x,float y)
				{
					MapObject mapobj = objB.getMapObject();
					mpobject.setMapObject(mapobj);
					nCnt ++;
				}
	
			});
			table.add(objB);
		}
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		scro = new ScrollPane(table,skin);
		scro.setFlickScroll(false);
		scro.setFadeScrollBars(true);				// ここでfalseなら常に。trueなら使用するとき。
		scro.setScrollingDisabled(false, false);	// 一番目は縦、二番目は横。これによりスクロールをするかしないか
		scroTable = new Table();
		scroTable.setLayoutEnabled(false);			// 任意に変更
		scroTable.setX(150);
		scroTable.setY(350);
		scro.size(600,10);
		scroTable.add(scro);
		stage.addActor(scroTable);
		array_stage.add(stage);
		
		//===オブジェクト
		
		/*sprite2 = new Sprite(region2);
		sprite2.setSize(sprite2.getRegionWidth(),sprite2.getRegionHeight());
		sprite2.setOrigin(sprite2.getWidth()/2, sprite2.getHeight()/2);
		sprite2.setPosition(-sprite2.getWidth()/2, -sprite2.getHeight()/2);
		getSpritePositionX = sprite2.getX();
		getSpritePositionY = sprite2.getY();
		
		array_sprite.add(sprite2);*/
		
	}
	
	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
		//------------------------------------------------------------------------
		//===カメラ移動
		if (Gdx.input.isKeyPressed(Keys.LEFT))
		{
			camera.position.x -= 50;
			if (camera.position.x < sprite.getX() + 512)
				camera.position.x = sprite.getX() + 512;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			camera.position.x += 50;
			if (camera.position.x > sprite.getOriginX() - 512)
				camera.position.x = sprite.getOriginX() - 512;
		}
		if (Gdx.input.isKeyPressed(Keys.UP))
		{
			camera.position.y += 50;
			if (camera.position.y > sprite.getOriginY() - 256)
				camera.position.y = sprite.getOriginY() - 256;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN))
		{
			camera.position.y -= 50;
			if (camera.position.y < sprite.getY() + 256)
				camera.position.y = sprite.getY() + 256;
		}
		
		//===マウスクリック
		
		getMousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x;
		getMousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y;
		
		// デバッグ用
		if (Gdx.input.isKeyPressed(Keys.P))
		{
			for (i = 0 ; i < 11 ; i ++)
			{
				Gdx.app.log("tag","1:" + (mpobject.getMapObjectList().get(i).getSp().getBoundingRectangle().contains(getMousePositionX,-getMousePositionY)));
				Gdx.app.log("tag","2:" + (mpobject.getMapObjectList().get(i).getSp().getX()));
				Gdx.app.log("tag","3:" + (mpobject.getMapObjectList().get(i).getSp().getY()));
			}
			Gdx.app.log("tag","4:" + getMousePositionX);
			Gdx.app.log("tag","5:" + (-getMousePositionY));
		}
		
		if (Gdx.input.isKeyPressed(Keys.A))
		{
			getSpritePositionX -= 50;
			//sprite2.setX(getSpritePositionX);
		//	mpobject.getMapObjectList().get(4).setPosition(getSpritePositionX, getSpritePositionY);
		}
		if (Gdx.input.isKeyPressed(Keys.D))
		{
			getSpritePositionX += 50;
			sprite2.setX(getSpritePositionX);
		}
		if (Gdx.input.isKeyPressed(Keys.W))
		{
			getSpritePositionY += 50;
			sprite2.setY(getSpritePositionY);
		}
		if (Gdx.input.isKeyPressed(Keys.S))
		{
			getSpritePositionY -= 50;
			sprite2.setY(getSpritePositionY);
		}
		
		//===スプライトクリック
		for(i = 0 ; i < nCnt ; i ++)
		{
			if (Gdx.input.isButtonPressed(0))
			{
				// クリックしたタイミング
				if(mpobject.getMapObjects().get(i).getSp().getBoundingRectangle().contains(getMousePositionX,-getMousePositionY))
				{
					getSpritePositionX = getMousePositionX - mpobject.getMapObjectList().get(i).getSp().getWidth() / 2;
					getSpritePositionY = getMousePositionY + mpobject.getMapObjectList().get(i).getSp().getHeight() / 2;
					mpobject.getMapObjects().get(i).getSp().setPosition(getSpritePositionX, -getSpritePositionY);
				}
			}
		}
		camera.update();
	}
	
	/**
	 * Draw process
	 * @param delta		delta time
	 */
	public void draw(float delta) {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		sprite.draw(batch);
		/*
		if(sprite_flg)
		{
			for (Sprite sprite : array_sprite)
			{
				sprite.draw(batch);
			}
		}
		*/
		mpobject.drawMapObjects(batch);
		
		batch.end();
		
		//===ボタン
		for (Stage stage : array_stage)
		{
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
		}
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

}