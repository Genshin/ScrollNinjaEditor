package org.genshin.scrollninjaeditor;

import java.util.ArrayList;

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
	
	private Sprite sprite1;
	private TextureRegion region1;
	private Table table;
	private ImageButton imageButton;
	private SpriteDrawable sd = new SpriteDrawable();

	private Sprite sprite2;
	private TextureRegion region2;
	
	private int i;
	private float getSpriteMousePosX = 0 , getSpriteMousePosY = 0 ;
	private float getMousePositionX = 0 , getMousePositionY = 0;
	private float getSpritePositionX = 0 , getSpritePositionY = 0;
	private float width = 0 , hight = 0;
	private int size = 2;
	
	private ArrayList<Stage> array_stage = new ArrayList<Stage>();
	private ArrayList<Texture> array_tex = new ArrayList<Texture>();
	private ArrayList<Table> array_table = new ArrayList<Table>();
	
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
	
		// - 複数化 - 
		for (i = 0 ; i < 15 ; i ++)
		{
			region1 = new TextureRegion(array_tex.get(i % 3),0,0,64,64);	
			sprite1 = new Sprite(region1);
			sd = new SpriteDrawable();						// 上書きが必要
			sd.setSprite(sprite1);							// 上書きではないので注意
			imageButton = new ImageButton(sd);

			table.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event,float x,float y)
				{
					if((x / 64.0f > 0.0f) && (x / 64.0f < 1.0f))
						region2 = new TextureRegion(array_tex.get(0),0,0,512,256);
					
					else if((x / 64.0f > 1.0f) && (x / 64.0f < 2.0f))
						region2 = new TextureRegion(array_tex.get(1),0,0,512,256);
					
					else if((x / 64.0f > 2.0f) && (x / 64.0f < 3.0f))
						region2 = new TextureRegion(array_tex.get(2),0,0,512,256);
					
					sprite2 = new Sprite(region2);
					sprite2.setSize(sprite2.getRegionWidth(),sprite2.getRegionHeight());
					sprite2.setOrigin(sprite2.getWidth()/2, sprite2.getHeight()/2);
					sprite2.setPosition(sprite.getX(),sprite.getY());
					getSpritePositionX = sprite.getX();
					getSpritePositionY = sprite.getY();
				}
			});
			//imageButton.setPosition(30 + i * 100, 400);		// 座標移動
			table.add(imageButton);
			table.setFillParent(true);
			table.left();
			table.setX(32);
			table.setY(200);
			array_table.add(table);
			stage.addActor(array_table.get(i));
		}
		array_stage.add(stage);
		
		//===オブジェクト
		
		sprite2 = new Sprite(region2);
		sprite2.setSize(sprite2.getRegionWidth(),sprite2.getRegionHeight());
		sprite2.setOrigin(sprite2.getWidth()/2, sprite2.getHeight()/2);
		sprite2.setPosition(0,0);
		getSpritePositionX = sprite.getX();
		getSpritePositionY = sprite.getY();
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
		camera.update();
		
		//===マウスクリック
		/*
		getMousePositionX = Gdx.input.getX();
		getMousePositionY = Gdx.input.getY();
		*/
		
		getMousePositionX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2) + camera.position.x;
		getMousePositionY = (Gdx.input.getY() - Gdx.graphics.getHeight() /2) - camera.position.y;
		
		// デバッグ用
		if (Gdx.input.isKeyPressed(Keys.P))
		{
			//Gdx.app.log("tag", "y" + );
	/*		Gdx.app.log("スプライト", "x" + sprite.getX());
			Gdx.app.log("スプライト", "y" + sprite.getY());
			Gdx.app.log("スプライト2", "x" + sprite2.getX());
			Gdx.app.log("スプライト2", "y" + sprite2.getY());
			Gdx.app.log("スプライト", "x" + sprite.getOriginX());
			Gdx.app.log("スプライト", "y" + sprite.getOriginY());
			Gdx.app.log("スプライト2", "x" + sprite2.getOriginX());
			Gdx.app.log("スプライト2", "y" + sprite2.getOriginY());
			Gdx.app.log("スプライト", "x" + sprite.getWidth());
			Gdx.app.log("スプライト", "y" + sprite.getHeight());
			Gdx.app.log("スプライト2", "x" + sprite2.getWidth());
			Gdx.app.log("スプライト2", "y" + sprite2.getHeight());*/
			//Gdx.app.log("判定", "" + sprite2.getBoundingRectangle().contains((Gdx.input.getX() - 512) * 4,((Gdx.input.getY() + 512) * -1) + 190));
			Gdx.app.log("マウスの座標X", "x:" + getMousePositionX);
			Gdx.app.log("マウスの座標Y", "y:" + getMousePositionY);
			Gdx.app.log("カメラ座標","X" + camera.position.x);
			Gdx.app.log("カメラ座標","Y" + camera.position.y);
			Rectangle rect = sprite2.getBoundingRectangle();
			Gdx.app.log("レクト", "X" + rect.getX());
			Gdx.app.log("レクト", "Y" + rect.getY());
			Gdx.app.log("レクト", "W" + rect.getWidth());
			Gdx.app.log("レクト", "H" + rect.getHeight());
			Gdx.app.log("スプライト2のX値", "" + sprite2.getBoundingRectangle().getX());
			Gdx.app.log("スプライト2のY値", "" + sprite2.getBoundingRectangle().getY());
			Gdx.app.log("スプライト2のW値", "" + sprite2.getBoundingRectangle().getWidth());
			Gdx.app.log("スプライト2のH値", "" + sprite2.getBoundingRectangle().getHeight());
			/*
			Gdx.app.log("tag","X" + getMousePositionX);
			Gdx.app.log("tag","Y" + getMousePositionY);
			*/
		}
		
		if (Gdx.input.isKeyPressed(Keys.A))
		{
			getSpritePositionX -= 50;
			sprite2.setX(getSpritePositionX);
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
		
		// クリックしたタイミング
		if(sprite2.getBoundingRectangle().contains(getMousePositionX,getMousePositionY))
		{
			if (Gdx.input.justTouched())
			{
				Gdx.app.log("マウス座標","X:" + getMousePositionX);
				Gdx.app.log("マウス座標","Y:" + getMousePositionY);
				Gdx.app.log("スプライト２座標","X:" + sprite2.getBoundingRectangle().getX());
				Gdx.app.log("スプライト２座標","Y:" + sprite2.getBoundingRectangle().getY());
				Gdx.app.log("スプライト２高さ","H:" + sprite2.getBoundingRectangle().getHeight());
				Gdx.app.log("スプライト２幅","W:" + sprite2.getBoundingRectangle().getWidth());
			}
		}
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
		sprite2.draw(batch);
		
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