package me.whipmegrandma.grandmakits.command.grandmakit;

import me.whipmegrandma.grandmakits.file.KitData;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

public final class RemoveSubcommand extends SimpleSubCommand {
	protected RemoveSubcommand(SimpleCommandGroup parent) {
		super(parent, "remove|r");

		this.setMinArguments(1);
		this.setUsage("<name>");

	}

	@Override
	protected void onCommand() {

		String arg = args[0];

		checkBoolean(KitData.isKit(arg), arg + " doesn't exist. Available: " + Common.join(KitData.getKitNames()));

		KitData.findKit(arg).onRemove();

		Common.tell(getPlayer(), "Successfully removed " + arg + " kit!");
	}

	@Override
	protected List<String> tabComplete() {
		return args.length == 1 ? completeLastWord(KitData.getKitNames()) : NO_COMPLETE;
	}

}
