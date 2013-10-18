package org.genshin.scrollninjaeditor;

import javax.swing.filechooser.FileFilter;

public class ExtendsFileFilter extends FileFilter
{
	private String extension;
	private String msg;

	
	public ExtendsFileFilter(String extension ,String msg){
		this.extension = extension;
		this.msg = msg;
	}

	public boolean accept(java.io.File f) {
		return f.getName().endsWith(extension);
	}

	public String getDescription() {
		return msg;
	}
}