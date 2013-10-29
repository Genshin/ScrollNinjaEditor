package org.genshin.scrollninjaeditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ScrollPaneStage extends Table{
	private int loopCnt = 0;
	private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));	// スキンファイルを読み込み;
	private ScrollPane menuScrollPane;
	private ScrollPane layerFrontScrollPane;
	private ScrollPane layerBackScrollPane;
	private MapObject mapobj;
	private Table table = new Table();
	private Table frontTable = new Table();
	private Table backTable = new Table();
	private SpriteDrawable spriteDrawble;
	private LayerManager layerManager;
	private Label	label;
	
	/**
	 * Constructor
	 */
	public ScrollPaneStage(){
		layerManager = LayerManager.getInstance();
		this.setFillParent(true);
		this.top();
		this.right();
		this.debug();
	}
	
	/**
	 * Create
	 * @param stage
	 * @param manager
	 * @param table
	 * @param spriteDrawble
	 * @param camera
	 */
	public void menuCreate(final MapObjectManager manager,final OrthographicCamera camera,LayerManager layer){
		for (loopCnt = 0 ; loopCnt < manager.getMapObjectList().size() ; loopCnt ++){
			if(loopCnt % 3 == 0)
				table.row();
			manager.getMapObjectList().get(loopCnt).getSp().setSize(64,64);
			
			spriteDrawble = new SpriteDrawable();												// 上書きが必要
			spriteDrawble.setSprite(manager.getMapObjectList().get(loopCnt).getSp());			// 上書きではないので注意
			layerManager = layer;
			final ObjectButton objB = new ObjectButton(spriteDrawble, manager.getMapObjectList().get(loopCnt));
			objB.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event ,float x,float y){
					mapobj = new MapObject(objB.mapObject);											 // クリックされたオブジェクト情報を読み込み
					mapobj.setPosition(camera.position.x,camera.position.y);
					//manager.setFrontObject(mapobj);												 // オブジェクトをセット
					layerManager.getLayer(layerManager.getSelectLayer()).setMapObject(mapobj);
				}
			});
			label = new Label(manager.getMapObjectList().get(loopCnt).getLabelName(),skin);	
			objB.add(label);
			table.add(objB);
		}
		
		menuScrollPane = new ScrollPane(table,skin);					// スクロールに情報を読み込み
		menuScrollPane.setFlickScroll(false);							// フリックの有無
		menuScrollPane.setFadeScrollBars(true);							// ここでfalseなら常に。trueなら使用するとき。
		menuScrollPane.setScrollingDisabled(false, false);				// 一番目は縦、二番目は横。これによりスクロールをするかしない
		menuScrollPane.setSize(100, 200);
		menuScrollPane.removeListener(menuScrollPane.getListeners().get(0));
		this.add(menuScrollPane).top().right().colspan(2);
	}
	
	public void layerFrontCreate(){
		for (loopCnt = 0 ; loopCnt < layerManager.getFrontLayers().size() ; loopCnt ++){
			frontTable.add(layerManager.getFrontLayer(loopCnt).getButton());
			frontTable.row();
		}
		layerFrontScrollPane = new ScrollPane(frontTable,skin);
		layerFrontScrollPane.setFlickScroll(false);							// フリックの有無
		layerFrontScrollPane.setFadeScrollBars(true);							// ここでfalseなら常に。trueなら使用するとき。
		layerFrontScrollPane.setScrollingDisabled(false, false);				// 一番目は縦、二番目は横。これによりスクロールをするかしない
		layerFrontScrollPane.removeListener(layerFrontScrollPane.getListeners().get(0));
		this.add(layerFrontScrollPane).left().top();
	}
	public void layerBackCreate(){
		for (loopCnt = 0 ; loopCnt < layerManager.getBackLayers().size() ; loopCnt ++){
			backTable.add(layerManager.getBackLayer(loopCnt).getButton());
			backTable.row();
		}
		layerBackScrollPane = new ScrollPane(backTable,skin);
		layerBackScrollPane.setFlickScroll(false);							// フリックの有無
		layerBackScrollPane.setFadeScrollBars(true);							// ここでfalseなら常に。trueなら使用するとき。
		layerBackScrollPane.setScrollingDisabled(false, false);				// 一番目は縦、二番目は横。これによりスクロールをするかしない
		layerBackScrollPane.removeListener(layerBackScrollPane.getListeners().get(0));
		this.add(layerBackScrollPane).left().top();
	}
	
	public void addFront(){
		frontTable = new Table();
		for (loopCnt = 0 ; loopCnt < layerManager.getFrontLayers().size() ; loopCnt ++){
			frontTable.add(layerManager.getFrontLayer(loopCnt).getButton());
			frontTable.row();
		}
		layerFrontScrollPane = new ScrollPane(frontTable,skin);
		layerFrontScrollPane.setFlickScroll(false);							// フリックの有無
		layerFrontScrollPane.setFadeScrollBars(true);							// ここでfalseなら常に。trueなら使用するとき。
		layerFrontScrollPane.setScrollingDisabled(false, false);				// 一番目は縦、二番目は横。これによりスクロールをするかしない
		layerFrontScrollPane.removeListener(layerFrontScrollPane.getListeners().get(0));
		this.addAll();
	}
	
	public void addBack(){
		backTable = new Table();
		for (loopCnt = 0 ; loopCnt < layerManager.getBackLayers().size() ; loopCnt ++){
			backTable.add(layerManager.getBackLayer(loopCnt).getButton());
			backTable.row();
		}
		layerBackScrollPane = new ScrollPane(backTable,skin);
		layerBackScrollPane.setFlickScroll(false);							// フリックの有無
		layerBackScrollPane.setFadeScrollBars(true);							// ここでfalseなら常に。trueなら使用するとき。
		layerBackScrollPane.setScrollingDisabled(false, false);				// 一番目は縦、二番目は横。これによりスクロールをするかしない
		layerBackScrollPane.removeListener(layerBackScrollPane.getListeners().get(0));
		this.addAll();
	}

	/**
	 * getScrollPaneWidth
	 * @return scrollPane.getWidth()
	 */
	public float getScrollPaneWidth(){
		return menuScrollPane.getPrefWidth();
	}
	
	public Table getTable(){
		return this;
	}
	
	public void addAll(){
		this.clear();
		this.add(menuScrollPane).top().right().colspan(2);
		this.row();
		this.add(layerFrontScrollPane).left().top();
		this.add(layerBackScrollPane).left().top();
	}
}