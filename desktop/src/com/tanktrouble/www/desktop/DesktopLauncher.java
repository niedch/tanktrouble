package com.tanktrouble.www.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Start.MainClass;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		/*Toolkit toolkit = Toolkit.getDefaultToolkit();
		config.height = toolkit.getScreenSize().height;
		config.width = toolkit.getScreenSize().width;
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");*/
		new LwjglApplication(new MainClass(), config);
	}
}
