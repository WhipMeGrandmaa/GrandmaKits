package me.whipmegrandma.grandmakits.file;

import org.mineacademy.fo.settings.YamlStaticConfig;

public class Settings extends YamlStaticConfig {

	public static String kitMenuTitle;

	@Override
	protected void onLoad() throws Exception {

		this.loadConfiguration("settings.yml");
	}


	private static void init() {
		kitMenuTitle = getString("Kit_Menu_Title");
	}
}
