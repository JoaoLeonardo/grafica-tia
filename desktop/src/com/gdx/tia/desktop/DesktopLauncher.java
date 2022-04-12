package com.gdx.tia.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.tia.TacticalInfiltrationAction;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tactical Infiltration Action";
		config.useGL30 = true;
		config.width = 1024;
		config.height = 720;
		//config.fullscreen = true;
		new LwjglApplication(new TacticalInfiltrationAction(), config);
	}
}
