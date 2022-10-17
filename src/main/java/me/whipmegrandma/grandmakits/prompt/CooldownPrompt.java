package me.whipmegrandma.grandmakits.prompt;

import me.whipmegrandma.grandmakits.file.KitData;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimplePrompt;


public class CooldownPrompt extends SimplePrompt {
	private final KitData data;

	public CooldownPrompt(KitData data) {
		this.data = data;
	}

	@Override
	protected String getPrompt(ConversationContext context) {
		return "&7Type in the cooldown. Current: " + data.getDelay() + " seconds.";
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {

		if (!Valid.isInteger(input))
			return false;

		Long cooldown = Long.parseLong(input);

		return cooldown >= 0;
	}

	@Override
	protected String getFailedValidationText(ConversationContext context, String invalidInput) {

		if (!Valid.isInteger(invalidInput))
			return "The cooldown must be a number.";

		Long cooldown = Long.parseLong(invalidInput);

		return "The cooldown must be greater than 0.";
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {

		Long cooldown = Long.parseLong(s);

		data.setDelay(cooldown);

		return END_OF_CONVERSATION;
	}
}
