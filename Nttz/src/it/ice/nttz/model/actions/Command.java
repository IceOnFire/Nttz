package it.ice.nttz.model.actions;

import it.ice.nttz.Nttz;
import it.ice.nttz.model.Action;
import it.ice.nttz.model.Entity;

public class Command extends Action {
	public Command(Nttz nttz, String command) {
		super();

		String[] tokens = command.split(" ");

		setName(tokens[0].replaceFirst("^(.*)(ing)$", "$1"));
		tokens = pop(tokens);
		if (tokens.length > 0) {
			String targetOwnersName = tokens[0];
			tokens = pop(tokens);
			String targetName = null;
			if (targetOwnersName.endsWith("'s") && tokens.length > 0) {
				targetOwnersName = targetOwnersName.replaceFirst("^(.*)'s$", "$1");
				targetName = tokens[0];
				tokens = pop(tokens);
			}

			Entity targetOwner = nttz.getEntity(targetOwnersName);
			if (targetName == null) {
				applyTo(targetOwner);
			} else {
				applyTo(targetOwner.s(targetName));
			}

			if (tokens.length > 1 && "with".equals(tokens[0])) {
				tokens = pop(tokens);
				String toolOwnerName = tokens[0];
				tokens = pop(tokens);
				String toolName = null;
				if (toolOwnerName.endsWith("'s") && tokens.length > 0) {
					toolOwnerName = toolOwnerName.replaceFirst("^(.*)'s$", "$1");
					toolName = tokens[0];
					tokens = pop(tokens);
				}

				Entity toolOwner = nttz.getEntity(toolOwnerName);
				if (toolName == null) {
					setTool(toolOwner);
				} else {
					setTool(toolOwner.s(toolName));
				}
			}
		}
	}

	private String[] pop(String[] tokens) {
		return remove(0, tokens);
	}

	private String[] remove(int index, String[] tokens) {
		String[] newTokens = new String[tokens.length - 1];
		for (int i = 0; i < index; i++) {
			newTokens[i] = tokens[i];
		}
		for (int i = index + 1; i < tokens.length; i++) {
			newTokens[i - 1] = tokens[i];
		}
		return newTokens;
	}
}
