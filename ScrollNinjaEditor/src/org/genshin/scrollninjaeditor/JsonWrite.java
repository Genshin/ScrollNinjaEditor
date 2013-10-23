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
	//private ArrayNode arrayNode;
	private ObjectNode objectNode;
	private ObjectNode fieldNode;
	
	public JsonWrite() {
		mapper = new ObjectMapper();
		rootNode = mapper.createArrayNode();
		
	}
	/**
	 * @param object  ObjectName
	 */
	/*public void putObject(String object) {
		objectNode = objectNode.putObject(object);
	}*/
	
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
	
	/**
	 * @param object  ObjectName
	 */
	public void setFieldNode(String object) {
		fieldNode = objectNode.putObject(object);
	}
	
	/**
	 * @param field  FieldName
	 * @param value  FieldValue
	 */
	public void putObjectField(String field,String value) {
		fieldNode.put(field,value);
	}
	/**
	 * @param object  ObjectName
	 * @param value   ObjectValue
	 */
	/*public void putObjectField(String object,int value) {
		fieldNode.put(object,value);
	}*/
	
	/**
	 * @param object  ObjectName
	 * @param value   ObjectValue
	 */
	/*public void putObjectField(String object,float value) {
		fieldNode.put(object,value);
	}*/
	
	public void addObject()	{
		objectNode = rootNode.addObject();
		
	}
	
	/*public void addObject()	{
		objectNode = arrayNode.addObject();
		
	}
	public void addArray() {
		arrayNode = rootNode.addArray();
	}*/
	
	
	
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