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
	private ArrayList<MapObject> mapObjects = new ArrayList<MapObject>();
	
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

            mapObjectList = objectMapper.readValue(Gdx.files.internal("data/objects/objects.json").read(), new TypeReference<ArrayList<MapObject>>(){});

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
	 * draw sprite
	 * @param batch		SproteBatch
	 */
	public void drawMapObjects(SpriteBatch batch) {
		if (mapObjects != null) {
			for (MapObject obj : mapObjects) {
				obj.draw(batch);

			}
		}
	}
	
	/**
	 * add mapObject to mapObjects
	 * @param obj
	 */
	public void setMapObject(MapObject obj) {
		mapObjects.add(obj);
	}
	
	/**
	 * remove mapObject from mapObjects
	 * @param obj
	 */
	public void removeMapObject(MapObject obj) {
		mapObjects.remove(obj);
	}
	
	/**
	 * @param index
	 * @return	mapObject
	 */
	public MapObject getMapObject(int index) {
		return mapObjects.get(index);
	}
	
	/**
	 * @return mapObjects
	 */
	public ArrayList<MapObject> getMapObjects() {
		return mapObjects;
	}

	/**
	 * @return mapObjectList
	 */
	public ArrayList<MapObject> getMapObjectList() {
		return mapObjectList;
	}
}
