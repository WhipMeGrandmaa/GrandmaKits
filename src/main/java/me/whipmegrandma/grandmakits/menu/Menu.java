package me.whipmegrandma.grandmakits.menu;

import me.whipmegrandma.grandmakits.api.MenuEdit;
import me.whipmegrandma.grandmakits.file.KitData;
import me.whipmegrandma.grandmakits.file.PlayerData;
import me.whipmegrandma.grandmakits.file.Settings;
import me.whipmegrandma.grandmakits.prompt.CooldownPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Menu extends MenuPagged<KitData> {

	private BukkitRunnable timer;

	public Menu(Player player) {
		super(9, KitData.getKits());

		this.setViewer(player);
		this.setTitle(Settings.kitMenuTitle);
	}

	@Override
	protected org.bukkit.inventory.ItemStack convertToItemStack(KitData item) {
		return item.getItemRepresentation() != null ? item.toItemRepresentation(this.getViewer().getUniqueId()) : null;
	}

	@Override
	protected void onPageClick(Player player, KitData item, ClickType click) {

		if (click.isRightClick()) {

			new KitPreview(item).displayTo(player);

		} else if (click.isLeftClick()) {

			if (player.isOp()) {

				item.give(player);
				this.animateTitle("&7Received kit &e" + item.getName() + "&7!");

				return;
			}

			if (!PlayerUtil.hasPerm(player, "grandmakits.kit." + item.getName().toLowerCase())) {
				this.animateTitle("&7Insufficient permissions.");

				return;
			}

			UUID uuid = player.getUniqueId();

			long timeNow = TimeUtil.currentTimeSeconds();
			Long timeSaved = item.kitDelay.get(uuid) != null ? item.kitDelay.get(uuid) : -1;
			long timeDifference = timeNow - timeSaved;

			if (timeDifference < item.getDelay()) {
				long delay = item.getDelay() - timeDifference;

				this.animateTitle("&7You must wait " + this.plural(delay, "second"));

			} else {


				item.give(player);

				this.animateTitle("&7Received kit &e" + item.getName() + "&7!");

				item.kitDelay.put(uuid, timeNow);
				
				PlayerData cache = PlayerData.from(player);
				cache.save();
			}
		}
	}

	private class KitPreview extends MenuPagged<ItemStack> {

		private final KitData data;

		KitPreview(KitData data) {
			super(9 * 3, Menu.this, data.toItems());

			this.data = data;

			this.setTitle("&e" + data.getName() + " Kit Preview");
		}

		@Override
		protected ItemStack convertToItemStack(ItemStack item) {
			return item;
		}

		@Override
		protected void onPageClick(Player player, ItemStack item, ClickType click) {

		}
	}

	public static class KitEdit extends MenuEdit {

		KitData kit;
		@Position(36)
		Button cooldownButton;

		public KitEdit(KitData kit, Player player) {
			super(null);

			this.kit = kit;

			this.setTitle("&e" + kit.getName());
			this.setSize(9 * 5);

			this.cooldownButton = new ButtonConversation(new CooldownPrompt(kit), CompMaterial.BOOK, "&fCooldown",
					"",
					"&7Click to",
					"edit the cooldown.",
					"",
					"Current: " + kit.getDelay() + " seconds.");

		}

		@Override
		protected ItemStack getDropAt(int slot) {

			if (slot == 44)
				return this.kit.toItemRepresentation(this.getViewer().getUniqueId());

			return slot < this.kit.toItems().size() && slot < 36 ? this.kit.toItems().get(slot) : null;
		}

		@Override
		protected void onMenuClose(StrictMap<Integer, ItemStack> items) {
			List<ItemStack> list = new ArrayList<>();

			for (int i = 0; i <= 44; i++)
				if (items.get(i) != null && i != 44) {

					ItemStack item = items.get(i);
					list.add(item);
					this.kit.revertItems(list);

				} else if (i == 44) {

					ItemStack itemRepresentation = items.get(i);
					this.kit.revertItemRepresentation(itemRepresentation);

				}
		}

		@Override
		protected int getInfoButtonPosition() {
			return 40;
		}

		@Override
		protected String[] getInfo() {
			return new String[]{
					"Set the kit cool down",
					"in the bottom left.",
					"",
					"Set the item representation",
					"in the bottom right."
			};
		}

		@Override
		public org.mineacademy.fo.menu.Menu newInstance() {
			return new KitEdit(this.kit, this.getViewer());
		}

	}

	@Override
	public org.mineacademy.fo.menu.Menu newInstance() {
		return new Menu(this.getViewer());
	}

	public String plural(final long count, final String ofWhat) {
		final String exception = getException(count, ofWhat);

		return exception != null ? exception : "&e" + count + "&7 " + ofWhat + (count == 0 || count > 1 && !ofWhat.endsWith("s") ? "s" : "");
	}

	private String getException(final long count, final String ofWhat) {
		final SerializedMap exceptions = SerializedMap.ofArray(
				"life", "lives",
				"class", "classes",
				"wolf", "wolves",
				"knife", "knives",
				"wife", "wives",
				"calf", "calves",
				"leaf", "leaves",
				"potato", "potatoes",
				"tomato", "tomatoes",
				"hero", "heroes",
				"torpedo", "torpedoes",
				"veto", "vetoes",
				"foot", "feet",
				"tooth", "teeth",
				"goose", "geese",
				"man", "men",
				"woman", "women",
				"mouse", "mice",
				"die", "dice",
				"ox", "oxen",
				"child", "children",
				"person", "people",
				"penny", "pence",
				"sheep", "sheep",
				"fish", "fish",
				"deer", "deer",
				"moose", "moose",
				"swine", "swine",
				"buffalo", "buffalo",
				"shrimp", "shrimp",
				"trout", "trout",
				"spacecraft", "spacecraft",
				"cactus", "cacti",
				"axis", "axes",
				"analysis", "analyses",
				"crisis", "crises",
				"thesis", "theses",
				"datum", "data",
				"index", "indices",
				"entry", "entries",
				"boss", "bosses");

		return exceptions.containsKey(ofWhat) ? count + " " + (count == 0 || count > 1 ? exceptions.getString(ofWhat) : ofWhat) : null;
	}
}
