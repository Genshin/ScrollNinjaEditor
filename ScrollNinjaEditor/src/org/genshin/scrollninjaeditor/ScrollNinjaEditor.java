package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Game;

public class ScrollNinjaEditor extends Game {
	SelectScreen selectScreen;

	@Override
	public void create() {
		setScreen(new SelectScreen(this));
		
		// 直接マップエディタに移動
		//setScreen(new MapEditorScreen(this, "data/3144.png"));
	}
}
