package me.whipmegrandma.grandmakits;

import me.whipmegrandma.grandmakits.file.KitData;
import me.whipmegrandma.grandmakits.file.PlayerData;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.Map;
import java.util.UUID;

public final class GrandmaKits extends SimplePlugin {

	@Override
	protected void onPluginStart() {
	}

	@Override
	protected void onReloadablesStart() {
		KitData.loadKits();

		Common.log("Successfully loaded:");

		for (String name : KitData.getKitNames())
			Common.log("- " + name);
		
		Button.setInfoButtonTitle("&6Grandma Kits");
	}

	@Override
	protected void onPluginStop() {
		for (Map.Entry<UUID, PlayerData> entry : PlayerData.cache.entrySet()) {
			PlayerData data = entry.getValue();

			data.save();
		}
	}

	public static GrandmaKits getInstance() {
		return (GrandmaKits) SimplePlugin.getInstance();
	}
}
