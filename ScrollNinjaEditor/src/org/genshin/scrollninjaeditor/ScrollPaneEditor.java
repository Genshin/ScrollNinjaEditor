package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ScrollPaneEditor extends Table{
	private int				loopCnt = 0;
	private Skin			skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	private ScrollPane		menuScrollPane;
	private ScrollPane		layerFrontScrollPane;
	private ScrollPane		layerBackScrollPane;
	private MapObject		mapObject;
	private Table			table = new Table();
	private Table			frontTable = new Table();
	private Table			backTable = new Table();
	private SpriteDrawable	spriteDrawble;
	private LayerManager	layerManager;
	private Label			label;
	
	/**
	 * Constructor
	 */
	public ScrollPaneEditor(){
		layerManager = LayerManager.getInstance();
		this.setFillParent(true);
		this.top();
		this.right();
		this.debug();
	}
	
	/**
	 * menuCreate
	 * @param stage
	 * @param manager
	 * @param table
	 * @param spriteDrawble
	 * @param camera
	 */
	public void menuCreate( final MapObjectManager manager,
							final OrthographicCamera camera,
							LayerManager layer){
		for (loopCnt = 0 ; loopCnt < manager.getMapObjectList().size() ; loopCnt ++){
			if(loopCnt % 3 == 0)
				table.row();
			manager.getMapObjectList().get(loopCnt).getSp().setSize(64,64);
			
			spriteDrawble = new SpriteDrawable();
			spriteDrawble.setSprite(manager.getMapObjectList().get(loopCnt).getSp());
			layerManager = layer;
			final ObjectButton objB = new ObjectButton(spriteDrawble, 
														manager.getMapObjectList().get(loopCnt));
			objB.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event ,float x,float y){
					mapObject = new MapObject(objB.mapObject);
					mapObject.setPosition(camera.position.x,camera.position.y);
					layerManager.getLayer(layerManager.getSelectLayerNum()).setMapObject(mapObject);
				}
			});
			label = new Label(manager.getMapObjectList().get(loopCnt).getLabelName(),skin);	
			objB.add(label);
			table.add(objB);
		}
		
		menuScrollPane = new ScrollPane(table,skin);
		menuScrollPane.setFlickScroll(false);
		menuScrollPane.setFadeScrollBars(true);
		menuScrollPane.setScrollingDisabled(false, false);
		menuScrollPane.removeListener(menuScrollPane.getListeners().get(0));
		this.add(menuScrollPane).top().right().size(menuScrollPane.getWidth(), Gdx.graphics.getHeight() / 2).colspan(2);
	}
	
	/**
	 * layerFrontCreate
	 */
	public void layerFrontCreate(){
		for (loopCnt = layerManager.getFrontLayers().size() - 1 ; loopCnt >= 0 ; loopCnt --){
			frontTable.add(layerManager.getFrontLayer(loopCnt).getButton()).top().expand();
			frontTable.row();
		}
		layerFrontScrollPane = new ScrollPane(frontTable,skin);
		layerFrontScrollPane.setFlickScroll(false);
		layerFrontScrollPane.setFadeScrollBars(true);
		layerFrontScrollPane.setScrollingDisabled(false, false);
		layerFrontScrollPane.removeListener(layerFrontScrollPane.getListeners().get(0));
		this.add(layerFrontScrollPane).left().top().fillX().size(menuScrollPane.getWidth()/2, Gdx.graphics.getHeight() / 2);
	}
	
	/**
	 * layerBackCreate
	 */
	public void layerBackCreate(){
		for (loopCnt = layerManager.getBackLayers().size() - 1 ; loopCnt >= 0 ; loopCnt --){
			backTable.add(layerManager.getBackLayer(loopCnt).getButton()).top().expand();
			backTable.row();
		}
		layerBackScrollPane = new ScrollPane(backTable,skin);
		layerBackScrollPane.setFlickScroll(false);
		layerBackScrollPane.setFadeScrollBars(true);
		layerBackScrollPane.setScrollingDisabled(false, false);
		layerBackScrollPane.removeListener(layerBackScrollPane.getListeners().get(0));
		this.add(layerBackScrollPane).left().top().fillX().size(menuScrollPane.getWidth()/2, Gdx.graphics.getHeight() / 2);
	}
	
	/**
	 * addFront
	 */
	public void addFront(){
		frontTable = new Table();
		for (loopCnt = layerManager.getFrontLayers().size() - 1 ; loopCnt >= 0 ; loopCnt --){
			frontTable.add(layerManager.getFrontLayer(loopCnt).getButton()).top().expand();
			frontTable.row();
		}
		layerFrontScrollPane = new ScrollPane(frontTable,skin);
		layerFrontScrollPane.setFlickScroll(false);
		layerFrontScrollPane.setFadeScrollBars(true);
		layerFrontScrollPane.setScrollingDisabled(false, false);
		layerFrontScrollPane.removeListener(layerFrontScrollPane.getListeners().get(0));
		this.addAll();
	}
	
	/**
	 * addBack
	 */
	public void addBack(){
		backTable = new Table();
		for (loopCnt = layerManager.getBackLayers().size() - 1 ; loopCnt >= 0 ; loopCnt --){
			backTable.add(layerManager.getBackLayer(loopCnt).getButton()).top().expand();
			backTable.row();
		}
		layerBackScrollPane = new ScrollPane(backTable,skin);
		layerBackScrollPane.setFlickScroll(false);
		layerBackScrollPane.setFadeScrollBars(true);
		layerBackScrollPane.setScrollingDisabled(false, false);
		layerBackScrollPane.setClamp(false);
		layerBackScrollPane.removeListener(layerBackScrollPane.getListeners().get(0));
		this.addAll();
	}

	/**
	 * getScrollPaneWidth
	 * @return scrollPane.getPrefWidth()
	 */
	public float getScrollPaneWidth(){
		return menuScrollPane.getPrefWidth();
	}
	
	/**
	 * getTable
	 * @return this
	 */
	public Table getTable(){
		return this;
	}
	
	/**
	 * addAll
	 */
	public void addAll(){
		this.clear();
		this.add(menuScrollPane).top().right().colspan(2);
		this.row();
		this.add(layerFrontScrollPane).left().top().fillX().size(menuScrollPane.getWidth()/2, Gdx.graphics.getHeight() / 2);
		this.add(layerBackScrollPane).left().top().fillX().size(menuScrollPane.getWidth()/2, Gdx.graphics.getHeight() / 2);
	}
}