package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Game;

public class ScrollNinjaEditor extends Game {
	@Override
	public void create() {
		setScreen(new SelectScreen(this));
	}
}
