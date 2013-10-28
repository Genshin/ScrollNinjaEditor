package org.genshin.scrollninjaeditor;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton
 */
public class MapObjectManager {
	private static MapObjectManager instance = new MapObjectManager();
	
	private ArrayList<MapObject> mapObjectList = new ArrayList<MapObject>();
	private ArrayList<MapObject> objects = new ArrayList<MapObject>();
	private ArrayList<MapObject> frontObjects = new ArrayList<MapObject>();
	private ArrayList<MapObject> backObjects = new ArrayList<MapObject>();
	
	private boolean isInitialized = false;
	
	/**
	 * @return	instance
	 */
	public static MapObjectManager getInstance() {
		return instance;
	}
	
	/**
	 * if instance is not initialize, execution initialize method
	 * @return	instance
	 */
	public static MapObjectManager create() {
        if( !instance.isInitialized ) {
            instance.initialize();
            instance.isInitialized = true;
        }
        return instance;
    }
	
	/**
	 * initialize
	 */
	private void initialize() {
		// read json
		try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            mapObjectList = objectMapper.readValue(Gdx.files.internal("data/objects/objects.json").read(),
            									   new TypeReference<ArrayList<MapObject>>(){});
        }
        catch (JsonParseException e) { e.printStackTrace(); }
        catch (JsonMappingException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

		// set sprite
		if (mapObjectList != null) {
			for (int i = 0; i < mapObjectList.size(); i ++) {
				mapObjectList.get(i).setSprite();
			}
		}
	}
	
	/**
	 * draw front object's sprite
	 * @param batch		SpriteBatch
	 */
	public void drawObjects(SpriteBatch batch) {
		if (objects != null) {
			for (MapObject obj : objects) {
				obj.draw(batch);
			}
		}
	}
	
	/**
	 * draw front object's sprite
	 * @param batch		SpriteBatch
	 */
	public void drawFrontObjects(SpriteBatch batch) {
		if (frontObjects != null) {
			for (MapObject obj : frontObjects) {
				obj.draw(batch);
			}
		}
	}
	
	/**
	 * draw back object's sprite
	 * @param batch		SpriteBatch
	 */
	public void drawBackObject(SpriteBatch batch) {
		if (backObjects != null) {
			for (MapObject obj : backObjects) {
				obj.draw(batch);
			}
		}
	}
	
	
	public void setObject(MapObject obj) {
		objects.add(obj);
	}
	/**
	 * add mapObject to frontObjects
	 * @param obj
	 */
	public void setFrontObject(MapObject obj) {
		frontObjects.add(obj);
	}
	
	/**
	 * add mapObject to backObjects
	 * @param obj
	 */
	public void setBackObject(MapObject obj) {
		backObjects.add(obj);
	}
	
	
	
	public void removeObject(MapObject obj) {
		objects.remove(obj);
	}
	/**
	 * remove mapObject from frontObjects
	 * @param obj
	 */
	public void removefrontObject(MapObject obj) {
		frontObjects.remove(obj);
	}
	
	/**
	 * remove mapObject from backObjects
	 * @param obj
	 */
	public void removeBackObject(MapObject obj) {
		backObjects.remove(obj);
	}
	
	
	
	public MapObject getObject(int index) {
		return objects.get(index);
	}
	/**
	 * @param index
	 * @return front mapObject
	 */
	public MapObject getFrontObject(int index) {
		return frontObjects.get(index);
	}
	
	/**
	 * 
	 * @param index
	 * @return back mapObject
	 */
	public MapObject getBackObject(int index) {
		return backObjects.get(index);
	}
	
	
	public ArrayList<MapObject> getObjects() {
		return objects;
	}
	/**
	 * @return front mapObjects
	 */
	public ArrayList<MapObject> getFrontObjects() {
		return frontObjects;
	}
	
	/**
	 * @return back mapObjects
	 */
	public ArrayList<MapObject> getBackObjects() {
		return backObjects;
	}

	/**
	 * @return mapObjectList
	 */
	public ArrayList<MapObject> getMapObjectList() {
		return mapObjectList;
	}
}
