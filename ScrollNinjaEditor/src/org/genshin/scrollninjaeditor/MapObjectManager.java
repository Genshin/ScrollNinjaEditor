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
	 * @return mapObjectList
	 */
	public ArrayList<MapObject> getMapObjectList() {
		return mapObjectList;
	}
}
