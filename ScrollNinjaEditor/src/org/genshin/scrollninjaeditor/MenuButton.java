package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MenuButton extends ImageButton{
	
	private boolean 		menuClickFlg = false;
	
	public MenuButton(SpriteDrawable sd){
		super(sd);
	}
	
	public void create(final Table table,final float screenWidth,final MapEditorStage mapEditorStage){
		this.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y){
	
				if(!menuClickFlg){
					mapEditorStage.addScrollPane();
					table.getChildren().get(2).setX(screenWidth - getWidth() - mapEditorStage.getPaneWidth());
					menuClickFlg = true;
				}
				else{
					mapEditorStage.removeButton();
					table.getChildren().get(2).setX(screenWidth - getWidth());
					menuClickFlg = false;
				}
			
			}
		});
		
	}

}
