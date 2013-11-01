package org.genshin.scrollninjaeditor;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRead {
	private ObjectMapper mapper;
	private JsonNode rootNode;
	private JsonNode currentNode;

	/**
	 * Constructor
	 * @param path   JsonFile path
	 */
	public JsonRead(String path) {
		
		mapper = new ObjectMapper();
		try {
			rootNode = mapper.readValue(new File(path),JsonNode.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param object ObjectName
	 * @param nodeNo NodeNo
	 * @return	ObjectValue toString
	 */
	public String getObjectString(String object, int nodeNo) {
		currentNode = rootNode.get(nodeNo);
		
		JsonNode getNode = currentNode.get(object);
		
		return getNode.textValue();
	}
	
	/**
	 * @param object ObjectName
	 * @param nodeNo NodeNo
	 * @return	ObjectValue toInt
	 */
	public int getObjectInt(String object, int nodeNo) {
		currentNode = rootNode.get(nodeNo);
		
		JsonNode getNode = currentNode.get(object);
		
		return getNode.intValue();
	}
	
	/**
	 * @param object ObjectName
	 * @param nodeNo NodeNo
	 * @return	ObjectValue toFloat
	 */
	public float getObjectFloat(String object, int nodeNo) {
		currentNode = rootNode.get(nodeNo);
		
		JsonNode getNode = currentNode.get(object);
		
		return getNode.floatValue();
	}
		
	
	/**
	 * @param nodeNo NodeNo
	 * @return Node
	 */
	public JsonNode getRootNode(int nodeNo)	{
		return rootNode.get(nodeNo);
	}
	
}