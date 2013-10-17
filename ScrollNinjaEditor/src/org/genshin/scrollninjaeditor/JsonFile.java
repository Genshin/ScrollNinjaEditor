package org.genshin.scrollninjaeditor;

public class JsonFile
{

	private String name;
	private String label;
	private float x;
	private float y;
	
	public JsonFile()
	{
		
	}
	
	public JsonFile(String name,String label,float x,float y)
	{
		this.name = name;
		this.label = label;
		this.x = x;
		this.y = y;
	}
	
	public void SetName(String name)
	{
		this.name = name;
	}
	
	public void SetLabel(String label)
	{
		this.label = label;
	}
	
	public void SetX(float x)
	{
		this.x = x;
	}
	public void SetY(float y)
	{
		this.y = y;
	}
	
	public String GetName()
	{
		return this.name;
	}
	
	public String GetLabel()
	{
		return this.label;
	}
	
	public float GetX()
	{
		return this.x;
	}
	public float GetY()
	{
		return this.y;
	}

}