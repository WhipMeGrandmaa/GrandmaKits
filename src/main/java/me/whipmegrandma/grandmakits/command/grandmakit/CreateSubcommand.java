package me.whipmegrandma.grandmakits.command.grandmakit;

import me.whipmegrandma.grandmakits.file.KitData;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

public final class CreateSubcommand extends SimpleSubCommand {
	protected CreateSubcommand(SimpleCommandGroup parent) {
		super(parent, "create|c");

		this.setMinArguments(1);
		this.setUsage("<name>");

	}

	@Override
	protected void onCommand() {
		String arg = args[0];
		System.out.println(this.getPermission());
		checkBoolean(!KitData.isKit(arg), arg + " already exists. Available: " + Common.join(KitData.getKitNames()));

		KitData.create(arg);

		Common.tell(getPlayer(), "Successfully created " + arg + " kit!");

	}

	@Override
	protected List<String> tabComplete() {
		return NO_COMPLETE;
	}

}
