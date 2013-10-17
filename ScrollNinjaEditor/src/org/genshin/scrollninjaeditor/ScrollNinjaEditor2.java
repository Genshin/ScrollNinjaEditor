package org.genshin.scrollninjaeditor;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class ScrollNinjaEditor2 implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private Stage stage;
	private TextureRegion region;
	
	private static int nCnt = 0;
	//private String filePath;
	
	@Override
	public void create()
	{		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		//ステージ作成
		stage = new Stage(512.0f,512.0f,true);
		Gdx.input.setInputProcessor(stage);
		
		//テーブル作成
		Table table = new Table();
		table.setFillParent(true);	//テーブルをステージの中央に対応
		table.setX(-128);
		table.setY(0);

		Table table2 = new Table();
		table2.setFillParent(true);	//テーブルをステージの中央に対応
		table2.setX(128);
		table2.setY(0);


		//ボタン作成
		ImageButton openButton;
		ImageButton saveButton;

		//テクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/test.png"));	//テクスチャ読み込み
		region = new TextureRegion(texture,0,0,512,512);				//テクスチャ表示域の設定
		sprite = new Sprite(region);
		SpriteDrawable sd = new SpriteDrawable(sprite);

		
		//ボタン作成
		openButton =  new ImageButton(sd);
		saveButton =  new ImageButton(sd);
		
		//ボタンマウス判定
		openButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				JFileChooser fileChooser = new JFileChooser();
				
				ExtendFileFilter filter[] = {
					new ExtendFileFilter(".json", "JSON ファイル(*.json)")	,
				};
				
				
				for(int i = 0; i < filter.length ; i++)
					fileChooser.addChoosableFileFilter(filter[i]);
				
				int a = fileChooser.showOpenDialog(fileChooser);
				if(a == JFileChooser.APPROVE_OPTION)
				{
					
					File file = fileChooser.getSelectedFile();
					if(filter[0].accept(file))
					{
						//-------------------------
						//Jsonファイル読み込み
						//-------------------------
					
						ArrayList<JsonFile> jsonDatas = new ArrayList<JsonFile>();
	
						//ObjectMapperの作成
						ObjectMapper loadMapper = new ObjectMapper();
						Gdx.app.log("","" + file.getAbsolutePath());

						//ルートノードの取得
						try {
							JsonNode rootNode = loadMapper.readValue(new File(file.getAbsolutePath()), JsonNode.class);
							
							JsonNode currentNode;
							
							for(int i = 0 ; (currentNode = rootNode.get(i)) != null; i++)
							{
								JsonFile jsonData;
								//"name"オブジェクトのノードを取得
								JsonNode nameNode = currentNode.get("name");
								Iterator<String> nameNodeFields = nameNode.fieldNames();
								jsonDatas.add(new JsonFile());
								jsonData = jsonDatas.get(nCnt);
								while(nameNodeFields.hasNext())
								{
									String nameNodeField = nameNodeFields.next(); 
									if( nameNodeField == "first")
									{
										jsonData.SetFirstName(nameNode.get(nameNodeField).toString());
									}
									else if(nameNodeField == "last")
									{
										jsonData.SetLastName(nameNode.get(nameNodeField).toString());
									}
										
								}
								
								//"email"オブジェクトのノードを取得
								JsonNode mailNode = currentNode.get("email");
								jsonData = jsonDatas.get(nCnt);
								jsonData.SetEmail(mailNode.textValue());
							
								jsonDatas.set(nCnt, jsonData);
								
								nCnt++;
							}
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
				
						//読み込んだJsonファイルのデータをログ表示
						for(int i = 0 ; i < nCnt ;i++)
						{
							JsonFile jsonData;
							jsonData = jsonDatas.get(i);
							Gdx.app.log("First Name","" + jsonData.GetFirstName());
							Gdx.app.log("Last Name","" + jsonData.GetLastName());
							Gdx.app.log("Mail","" + jsonData.GetEmail());
						}
					}	
				}
			}
		});
		
		saveButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				JFileChooser fileChooser = new JFileChooser();
				
				int select = fileChooser.showSaveDialog(fileChooser);
				
				if(select == JFileChooser.APPROVE_OPTION)
				{
					ArrayList<JsonFile> jsonDatas2 = new ArrayList<JsonFile>();
					//データ仮置き
					jsonDatas2.add(new JsonFile("ggg","hhh","iii"));
					jsonDatas2.add(new JsonFile("jjj","kkk","lll"));
					
					//-------------------------
					//Jsonファイル書き出し
					//-------------------------
					ObjectMapper writeMapper = new ObjectMapper();
					
					//ルートノードを配列として作成
					ArrayNode rootNode = writeMapper.createArrayNode();
					
					for(JsonFile json:jsonDatas2)
					{
						
						ObjectNode object = rootNode.addObject();
						ObjectNode name = object.putObject("name");
						name.put("first", "" + json.GetFirstName());
						name.put("last",json.GetLastName());
						object.put("email","" + json.GetEmail());
					}
					try {
						File file = fileChooser.getSelectedFile();
						writeMapper.writeValue(new File(file.getName()),rootNode);
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
			
		});
	
		table.add(openButton).size(128, 128);		//サイズ指定
		table2.add(saveButton).size(128,128);

		table.debug();
		table2.debug();

		stage.addActor(table);
		stage.addActor(table2);

	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		stage.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
		Table.drawDebug(stage);			//テーブル枠組み描画

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	class ExtendFileFilter extends FileFilter
	{
		private String extension,msg;
		public ExtendFileFilter(String extension, String msg)
		{
		
			this.extension = extension;
			this.msg = msg;
		}
		public boolean accept(java.io.File f)
		{
			return f.getName().endsWith(extension);
		}
		public String getDescription()
		{
			return msg;
		}
	}
	
	class JsonFile
	{
		private String firstname;
		private String lastname;
		private String email;
		
		public JsonFile()
		{
			
		}
		
		public JsonFile(String firstname,String lastname,String email)
		{
			this.firstname = firstname;
			this.lastname = lastname;
			this.email = email;			
		}
		
		public void SetFirstName(String firstname)
		{
			this.firstname = firstname;
		}
		
		public void SetLastName(String lastname)
		{
			this.lastname = lastname;
		}
		
		public void SetEmail(String email)
		{
			this.email = email;
		}
		
		public String GetFirstName()
		{
			return this.firstname;
		}
		
		public String GetLastName()
		{
			return this.lastname;
		}
		
		public String GetEmail()
		{
			return this.email;
		}
	}
}
