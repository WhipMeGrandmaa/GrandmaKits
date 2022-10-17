package me.whipmegrandma.grandmakits.command;

import me.whipmegrandma.grandmakits.file.KitData;
import me.whipmegrandma.grandmakits.menu.Menu;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

@AutoRegister
public final class Kit extends SimpleCommand {

	public Kit() {
		super("kit");

		this.setPermission("grandmakits.command.kit");
	}

	@Override
	protected void onCommand() {

		if (args.length == 0)

			new Menu(getPlayer()).displayTo(getPlayer());

		else
			Common.tell(getPlayer(), "&7Invalid argument. Run &e/kit ? &7for help.");
	}

	@Override
	protected List<String> tabComplete() {
		return completeLastWord(KitData.getKitNames());
	}
}
