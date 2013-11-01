package org.genshin.scrollninjaeditor;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonWrite
{
	private ObjectMapper mapper;
	private ArrayNode rootNode;
	private ObjectNode objectNode;
	
	public JsonWrite() {
		mapper = new ObjectMapper();
		rootNode = mapper.createArrayNode();
		
	}
	/**
	 * @param object  ObjectName
	 * @param value   ObjectValue
	 */
	public void putObject(String object,String value) {
		objectNode.put(object,value);
	}
	
	/**
	 * @param object  ObjectName
	 * @param value   ObjectValue
	 */
	public void putObject(String object,int value) {
		objectNode.put(object,value);
	}
	
	/**
	 * @param object  ObjectName
	 * @param value   ObjectValue
	 */
	public void putObject(String object,float value) {
		objectNode.put(object,value);
	}
	
	public void addObject()	{
		objectNode = rootNode.addObject();
		
	}
	

	
	
	/**
	 * @param name filename
	 */
	public void writeData(String name) {
		try {
			mapper.writeValue(new File(name),rootNode);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}