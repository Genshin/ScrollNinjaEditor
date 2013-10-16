package org.genshin.scrollninjaeditor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton
 */
public class MapObjectManager {
	private static MapObjectManager instance = new MapObjectManager();
	
	private ArrayList<MapObject> mapObjectList;
	private ArrayList<MapObject> mapObjects;
	
	private boolean isInitialized = false;
	
	public static MapObjectManager getInstance() {
		return instance;
	}
	
	public static MapObjectManager create()
    {
        if( !instance.isInitialized )
        {
            instance.initialize();
            instance.isInitialized = true;
        }
        return instance;
    }
	
	private void initialize() {
		try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            //mapObjectList = objectMapper.readValue(Gdx.files.internal("data/objects/objects.json").read(), new TypeReference<ArrayList<MapObject>>(){});
            mapObjectList = objectMapper.readValue(Gdx.files.internal("data/objects/test.json").read(), new TypeReference<ArrayList<MapObject>>(){});
        }
        catch (JsonParseException e) { e.printStackTrace(); }
        catch (JsonMappingException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
		
		if (mapObjectList != null) {
			for (int i = 0; i < mapObjectList.size(); i ++) {
				//Gdx.app.log("first", i + "   " + mapObjectList.get(i).getfirstName());
				//Gdx.app.log("last", i + "   " + mapObjectList.get(i).getlastName());
				//Gdx.app.log("email", i + "   " + mapObjectList.get(i).getEmail());
				
			}
		}
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void setMapObject(MapObject obj) {
		mapObjects.add(obj);
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void removeMapObject(MapObject obj) {
		mapObjects.remove(obj);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public MapObject getMapObject(int index) {
		return mapObjects.get(index);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<MapObject> getMapObjects() {
		return mapObjects;
	}

}
