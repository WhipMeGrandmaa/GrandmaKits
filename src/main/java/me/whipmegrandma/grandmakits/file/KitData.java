package me.whipmegrandma.grandmakits.file;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.*;

@Getter
public class KitData extends YamlConfig {

	private final static ConfigItems<KitData> kits = ConfigItems.fromFile("", "kits.yml", KitData.class);

	public HashMap<UUID, Long> kitDelay = new HashMap<>();
	private String name;
	private Long delay;
	private Item itemRepresentation;
	private List<Item> items;

	private boolean remove;

	public static void create(String name) {
		KitData data = new KitData(name);

		Item item = new Item();
		item.material = CompMaterial.DIAMOND_SWORD;
		data.itemRepresentation = item;
		data.setDelay(10L);

		data.setPathPrefix(name);

		data.save();
		KitData.loadKits();
		//	kits.loadOrCreateItem(name);
	}

	private KitData(String name) {
		this.name = name;

		this.setPathPrefix(name);
		this.loadConfiguration(NO_DEFAULT, "kits.yml");
	}

	@Override
	protected void onLoad() {
		this.delay = this.getLong("Delay");
		this.itemRepresentation = this.loadItemRepresentation();
		this.items = this.loadItems();

	}

	@Override
	protected void onSave() {

		if (!remove) {

			this.set("Delay", this.delay);
			this.set("Item_Representation", itemRepresentation);
			this.set("Items", items);

		} else {

			String path = this.getPathPrefix();
			this.setPathPrefix(null);

			this.set(path, null);

		}

	}

	public void onRemove() {
		kits.removeItem(this);
		this.remove = true;

		this.save();
	}

	public void setDelay(Long delay) {
		this.delay = delay;

		this.save();
	}

	public List<org.bukkit.inventory.ItemStack> toItems() {
		List<org.bukkit.inventory.ItemStack> compiledItems = new ArrayList<>();

		for (Item itemStack : items) {
			ItemCreator creator = ItemCreator.of(itemStack.getMaterial())
					.material(itemStack.getMaterial())
					.amount(itemStack.getAmount())
					.glow(itemStack.glow)
					.name(itemStack.getName())
					.lore(itemStack.getLore());

			if (itemStack.getEnchant() != null)
				for (String enchants : itemStack.getEnchant()) {
					String[] compiling = enchants
							.replace(",", "")
							.split(" ");

					//Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(compiling[0].toLowerCase()));
					Enchantment enchantment = Enchantment.getByName(compiling[0].toUpperCase());
					int level = Integer.parseInt(compiling[1]);

					creator.enchant(enchantment, level);

				}

			compiledItems.add(creator.make());
		}

		return compiledItems;
	}

	public org.bukkit.inventory.ItemStack toItemRepresentation(UUID uuid) {
		Item itemStack = itemRepresentation;

		ItemCreator creator = ItemCreator.of(itemStack.getMaterial())
				.material(itemStack.getMaterial())
				.amount(itemStack.getAmount())
				.glow(itemStack.glow)
				.name(itemStack.getName())
				.lore(itemStack.getLore());

		if (itemStack.getEnchant() != null)
			for (String enchants : itemStack.getEnchant()) {
				String[] compiling = enchants
						.replace(",", "")
						.split(" ");

				//Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(compiling[0].toLowerCase()));
				Enchantment enchantment = Enchantment.getByName(compiling[0].toUpperCase());
				int level = Integer.parseInt(compiling[1]);

				creator.enchant(enchantment, level);

			}
		return creator.make();
	}

	public void give(Player player) {

		for (org.bukkit.inventory.ItemStack stack : this.toItems())
			PlayerUtil.addItemsOrDrop(player, stack);
	}

	private List<Item> loadItems() {
		List<Item> items = new ArrayList<>();

		for (Item item : this.getList("Items", Item.class)) {
			items.add(item);
		}

		return items;
	}

	private Item loadItemRepresentation() {
		Item item = new Item();

		item = this.get("Item_Representation", Item.class);

		return item;
	}

	@ToString
	@Getter
	private static class Item implements ConfigSerializable {

		private CompMaterial material = CompMaterial.DIAMOND_SWORD;
		private Integer amount = 1;
		private boolean glow = false;
		private List<String> enchant = new ArrayList<>();
		private String name = ItemUtil.bountifyCapitalized(this.material);
		private List<String> lore = new ArrayList<>();

		@Override
		public SerializedMap serialize() {
			SerializedMap map = new SerializedMap();

			map.put("Material", this.material.toString());
			map.put("Amount", this.amount);
			map.put("Glow", glow);
			map.put("Enchant", this.enchant);
			map.put("Name", this.name);
			map.put("Lore", this.lore);

			return map;
		}

		public static Item deserialize(SerializedMap map) {
			Item item = new Item();

			item.material = CompMaterial.fromString(map.getString("Material"));

			item.amount = map.containsKey("Amount") ? map.getInteger("Amount") : 1;

			item.glow = map.containsKey("Glow") ? map.getBoolean("Glow") : false;

			item.enchant = map.containsKey("Enchant") ? map.getStringList("Enchant") : null;

			item.name = map.containsKey("Name") ? map.getString("Name") : ItemUtil.bountify(item.material);

			item.lore = map.containsKey("Lore") ? map.getStringList("Lore") : null;

			return item;

		}
	}

	public void revertItems(List<ItemStack> list) {
		List<Item> compiledItems = new ArrayList<>();

		for (ItemStack itemStack : list) {
			Item item = new Item();
			List<String> compiledEnchants = new ArrayList<>();

			ItemMeta meta = itemStack.getItemMeta();

			item.material = CompMaterial.fromMaterial(itemStack.getType());

			item.amount = itemStack.getAmount() != 0 ? itemStack.getAmount() : 1;

			item.glow = meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);

			item.name = meta.getDisplayName().equals("") ? ItemUtil.bountifyCapitalized(item.material) : meta.getDisplayName();

			item.lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

			for (Map.Entry<Enchantment, Integer> entrySet : itemStack.getEnchantments().entrySet()) {
				item.glow = false;

				String enchantment = entrySet.getKey().getKey().toString().replace("minecraft:", "");
				Integer level = entrySet.getValue();

				String enchant = enchantment.substring(0, 1).toUpperCase() + enchantment.substring(1) + ", " + level;

				compiledEnchants.add(enchant);

			}
			if (item.glow != true)
				item.enchant = compiledEnchants;

			compiledItems.add(item);
		}

		this.items = compiledItems;
		this.save();

	}

	public void revertItemRepresentation(ItemStack itemStack) {
		Item item = new Item();
		List<String> compiledEnchants = new ArrayList<>();

		ItemMeta meta = itemStack.getItemMeta();

		item.material = CompMaterial.fromMaterial(itemStack.getType());

		item.amount = itemStack.getAmount() != 0 ? itemStack.getAmount() : 1;

		item.glow = meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);

		item.name = meta.getDisplayName().equals("") ? ItemUtil.bountifyCapitalized(item.material) : meta.getDisplayName();

		item.lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

		for (Map.Entry<Enchantment, Integer> entrySet : itemStack.getEnchantments().entrySet()) {
			item.glow = false;

			String enchantment = entrySet.getKey().getKey().toString().replace("minecraft:", "");
			Integer level = entrySet.getValue();

			String enchant = enchantment.substring(0, 1).toUpperCase() + enchantment.substring(1) + ", " + level;

			compiledEnchants.add(enchant);

		}
		if (item.glow != true)
			item.enchant = compiledEnchants;

		this.itemRepresentation = item;
		this.save();

	}

	public static KitData findKit(String name) {
		return kits.findItem(name);
	}

	public static boolean isKit(String name) {
		return kits.isItemLoaded(name);
	}

	public static void loadKits() {
		kits.loadItems();
	}

	public static Set<String> getKitNames() {
		return kits.getItemNames();
	}

	public static Collection<KitData> getKits() {
		return kits.getItems();
	}

}


