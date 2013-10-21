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
	private ObjectNode fieldNode;
	
	public JsonWrite() {
		mapper = new ObjectMapper();
		rootNode = mapper.createArrayNode();
	}
	
	public void putObject(String object,String value) {
		objectNode.put(object,value);
	}
	
	public void putObject(String object,int value) {
		objectNode.put(object,value);
	}
	
	public void putObject(String object,float value) {
		objectNode.put(object,value);
	}
	
	public void setFieldNode(String object) {
		fieldNode = objectNode.putObject(object);
	}
	
	public void putObjectField(String field,String value) {
		fieldNode.put(field,value);
	}
	
	public void addObject()	{
		objectNode = rootNode.addObject();
	}
	
	public void writeData(String name) {
		try {
			mapper.writeValue(new File(name),rootNode);
		} catch (JsonGenerationException e) {
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

}