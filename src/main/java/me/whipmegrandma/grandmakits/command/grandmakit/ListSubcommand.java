package me.whipmegrandma.grandmakits.command.grandmakit;

import me.whipmegrandma.grandmakits.file.KitData;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public final class ListSubcommand extends SimpleSubCommand {
	protected ListSubcommand(SimpleCommandGroup parent) {
		super(parent, "list|l");

	}

	@Override
	protected void onCommand() {
		if (!KitData.getKitNames().isEmpty())
			Common.tell(getSender(), Common.join(KitData.getKitNames()) + ".");
		else
			Common.tell(getSender(), "There are no kits.");

	}

	@Override
	protected java.util.List<String> tabComplete() {
		return NO_COMPLETE;
	}

}
