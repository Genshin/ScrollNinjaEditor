package org.genshin.scrollninjaeditor;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class TestScreen implements Screen {
	ScrollNinjaEditor editor;
	String fileName = "background.png";
	MapObjectManager manager;
	ArrayList<MapObject> mapObjects;

	/**
	 * Constructor
	 * @param editor
	 */
	public TestScreen(ScrollNinjaEditor editor) {
		this.editor = editor;
		manager = MapObjectManager.create();
		
		for (int i = 0; i < 10; i++) {
			MapObject obj = manager.getMapObjectList().get(i);
			Random random = new Random();
			obj.setPosition(random.nextInt(300), random.nextInt(300));
			manager.setMapObject(obj);
		}
		
		mapObjects = new ArrayList<MapObject>();
		mapObjects = manager.getMapObjects();
		
		for (MapObject o : mapObjects) {
			Gdx.app.log("file", "" + o.getFileName());
			Gdx.app.log("label", "" + o.getLabelName());
			Gdx.app.log("x", "" + o.getX());
			Gdx.app.log("y", "" + o.getY());
		}
		
		/**
		 *  mapObjectsの各名前、ラベル名、x、y と　fileName(background.png) をjsonで出力したい
		 */
		JFileChooser fileChooser = new JFileChooser();
		int select = fileChooser.showSaveDialog(fileChooser);
		if(select == JFileChooser.APPROVE_OPTION)
		{
			JsonWrite write = new JsonWrite();
			for(MapObject obj : mapObjects)
			{
				
				write.addObject();
				write.putObject("file", obj.getFileName());
				write.putObject("label", obj.getLabelName());
				write.putObject("x", obj.getX());
				write.putObject("y", obj.getY());
			}
			write.writeData(fileChooser.getSelectedFile().toString() + ".json");
		}
	}
	
	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
	}
	
	/**
	 * Draw process
	 * @param delta		delta time
	 */
	public void draw(float delta) {
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
	}

}