package me.whipmegrandma.grandmakits.command.grandmakit;

import me.whipmegrandma.grandmakits.file.KitData;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

public final class GiveSubcommand extends SimpleSubCommand {
	protected GiveSubcommand(SimpleCommandGroup parent) {
		super(parent, "give|g");

		this.setMinArguments(2);
		this.setUsage("<player> <name>");
	}

	@Override
	protected void onCommand() {
		Player player = findPlayer(args[0]);

		checkBoolean(KitData.isKit(args[1]), "{1} is not a kit. Available: " + Common.join(KitData.getKitNames()));

		KitData kit = KitData.findKit(args[1]);

		kit.give(getPlayer());
		Common.tell(getPlayer(), "&7Received kit &e" + kit.getName() + "&7!");
	}

	@Override
	protected List<String> tabComplete() {

		if (args.length == 1)
			return completeLastWordPlayerNames();

		if (args.length == 2)
			return completeLastWord(KitData.getKitNames());

		return NO_COMPLETE;
	}
}
