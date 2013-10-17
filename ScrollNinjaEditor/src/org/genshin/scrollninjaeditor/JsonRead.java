package org.genshin.scrollninjaeditor;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRead
{
	private ObjectMapper mapper;
	private JsonNode rootNode;
	private JsonNode currentNode;

	
	public JsonRead(String path)
	{
		
		mapper = new ObjectMapper();
		try {
			rootNode = mapper.readValue(new File(path),JsonNode.class);
		} catch (JsonParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	

	public String getObjectString(String object, int nodeNo)
	{
		currentNode = rootNode.get(nodeNo);
		
		JsonNode getNode = currentNode.get(object);
		
		return getNode.toString();
	}
	
	public int getObjectInt(String object, int nodeNo)
	{
		currentNode = rootNode.get(nodeNo);
		
		JsonNode getNode = currentNode.get(object);
		
		return getNode.intValue();
	}
	public float getObjectFloat(String object, int nodeNo)
	{
		currentNode = rootNode.get(nodeNo);
		
		JsonNode getNode = currentNode.get(object);
		
		return getNode.floatValue();
	}
	
	public String getObjectFieldString(String object,String field , int nodeNo)
	{
		currentNode = rootNode.get(nodeNo);
		
		JsonNode objNode = currentNode.get(object);
		Iterator<String> nodeFields = objNode.fieldNames();
		while(nodeFields.hasNext())
		{
			String nodeField = nodeFields.next();
			if(nodeField == field)
			{
				return objNode.get(nodeField).toString();
			}
		}
		
		return null;
		
	}
	
	public int getObjectFieldInt(String object,String field , int nodeNo)
	{
		currentNode = rootNode.get(nodeNo);
		
		JsonNode objNode = currentNode.get(object);
		Iterator<String> nodeFields = objNode.fieldNames();
		while(nodeFields.hasNext())
		{
			String nodeField = nodeFields.next();
			if(nodeField == field)
			{
				return objNode.get(nodeField).asInt();
			}
		}
		
		return 0;
		
	}

	public float getObjectFieldFloat(String object,String field , int nodeNo)
	{
		currentNode = rootNode.get(nodeNo);
		
		JsonNode objNode = currentNode.get(object);
		Iterator<String> nodeFields = objNode.fieldNames();
		while(nodeFields.hasNext())
		{
			String nodeField = nodeFields.next();
			if(nodeField == field)
			{
				return objNode.get(nodeField).floatValue();
			}
		}
		
		return 0;
		
	}
	public JsonNode getRootNode(int nodeNo)
	{
		return rootNode.get(nodeNo);
	}
	
}