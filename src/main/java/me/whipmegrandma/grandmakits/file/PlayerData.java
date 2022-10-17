package me.whipmegrandma.grandmakits.file;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData extends YamlConfig {

	public static HashMap<UUID, PlayerData> cache = new HashMap<>();

	private UUID uuid;

	private PlayerData(UUID uuid) {
		this.uuid = uuid;

		cache.put(uuid, this);

		this.loadConfiguration(NO_DEFAULT, "playerdata/" + uuid.toString() + ".yml");
	}

	@Override
	protected void onLoad() {

		for (KitData data : KitData.getKits()) {
			data.kitDelay.put(uuid, this.getLong(data.getName()));
		}
	}

	@Override
	protected void onSave() {

		Player player = Bukkit.getPlayer(uuid);

		for (KitData data : KitData.getKits()) {
			if (PlayerUtil.hasPerm(player, "grandmakits.kit." + data.getName().toLowerCase())) {

				String name = data.getName();
				Long timeNow = TimeUtil.currentTimeSeconds();
				Long timeSaved = data.kitDelay.get(this.uuid) != null ? data.kitDelay.get(this.uuid) : -1;
				Long timeDifference = timeNow - timeSaved;
				
				if (timeSaved == -1 || timeDifference >= data.getDelay())
					this.set(name, null);
				else
					this.set(name, timeSaved);
			}
		}
	}

	public void remove() {
		this.save();

		for (KitData data : KitData.getKits())
			data.kitDelay.remove(this.uuid);

		cache.remove(uuid);
	}

	public static PlayerData from(Player player) {
		UUID uuid = player.getUniqueId();
		PlayerData data = cache.get(uuid);

		if (data == null) {
			data = new PlayerData(uuid);
			cache.put(uuid, data);
		}

		return data;
	}
}
