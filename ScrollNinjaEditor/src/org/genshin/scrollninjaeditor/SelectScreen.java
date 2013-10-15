package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Screen;

public class SelectScreen implements Screen {
	ScrollNinjaEditor editor;
	String fileName;

	/**
	 * Constructor
	 * @param editor
	 */
	public SelectScreen(ScrollNinjaEditor editor) {
		this.editor = editor;
	}
	
	/**
	 * Update process
	 * @param delta		delta time
	 */
	public void update(float delta) {
		
		// 「作成」ボタンがクリックされたらマップエディタ画面に遷移。
		// fileNameは表示する背景のファイル名
		// editor.setScreen(new MapEditorScreen(editor, fileName));
	}
	
	/**
	 * Draw process
	 * @param delta		delta time
	 */
	public void draw(float delta) {
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}