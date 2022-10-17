package me.whipmegrandma.grandmakits.command.grandmakit;

import me.whipmegrandma.grandmakits.file.KitData;
import me.whipmegrandma.grandmakits.menu.Menu;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

public final class EditSubcommand extends SimpleSubCommand {

	protected EditSubcommand(SimpleCommandGroup parent) {
		super(parent, "edit|e");

		this.setMinArguments(1);
		this.setUsage("<name>");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		checkBoolean(KitData.isKit(args[0].toLowerCase()), args[0] + " is not a kit. Available: " + Common.join(KitData.getKitNames()));

		KitData kit = KitData.findKit(args[0]);

		new Menu.KitEdit(kit, getPlayer()).displayTo(getPlayer());

	}

	@Override
	protected List<String> tabComplete() {
		return args.length == 1 ? completeLastWord(KitData.getKitNames()) : NO_COMPLETE;
	}
}
