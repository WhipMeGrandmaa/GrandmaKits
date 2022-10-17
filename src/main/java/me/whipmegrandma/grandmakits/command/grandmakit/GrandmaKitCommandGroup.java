package me.whipmegrandma.grandmakits.command.grandmakit;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.ReloadCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.Arrays;

@AutoRegister
public final class GrandmaKitCommandGroup extends SimpleCommandGroup {

	public GrandmaKitCommandGroup() {
		super("grandmakits|gk");
	}

	@Override
	protected void registerSubcommands() {
		this.registerSubcommand(new ReloadCommand());
		this.registerSubcommand(new GiveSubcommand(this));
		this.registerSubcommand(new EditSubcommand(this));
		this.registerSubcommand(new CreateSubcommand(this));
		this.registerSubcommand(new ListSubcommand(this));
		this.registerSubcommand(new RemoveSubcommand(this));
	}

	@Override
	protected String[] getHelpHeader() {
		return new String[]{Common.colorize("{prefix} The following commands are available:")};
	}


	@Override
	protected java.util.List<SimpleComponent> getNoParamsHeader() {
		return Arrays.asList(SimpleComponent.of("{prefix} Use /grandmakits ? to list the commands."));

	}
}
