package it.ice.nttz.model.actions;

import it.ice.nttz.model.Action;
import it.ice.nttz.model.Agent;
import it.ice.nttz.model.Entity;

public class Order extends Action {
	public Order() {
		super();
	}

	public Order(Entity master, String action) {
		super();

		String[] tokens = action.split(" ");

		if (action.startsWith("start") || action.startsWith("stop")) {
			setArgument("order", tokens[0]);
			String[] newTokens = new String[tokens.length - 1];
			for (int i = 0; i < newTokens.length; i++) {
				newTokens[i] = tokens[i + 1];
			}
			tokens = newTokens;
		}

		Action order = new Action(tokens[0].replaceFirst("^(.*)(ing)$", "$1"));
		if (tokens.length > 1) {
			String owner = tokens[1];
			if (tokens.length == 2) {
				if (owner.equals("me")) {
					order.applyTo(master);
				} else if (owner.equals("yourself")) {
					order.applyTo(agent);
				}
			} else if (tokens.length == 3) {
				String target = tokens[2];
				if (owner.equals("my")) {
					order.applyTo(master.s(target));
				} else if (owner.equals("your")) {
					order.applyTo(agent.s(target));
				}
			}
		}

		setArgument("action", order);
	}

	@Override
	public void perform() {
		Agent slave = (Agent) target;
		String order = (String) getArgument("order");
		Action action = (Action) getArgument("action");
		if ("start".equals(order)) {
			slave.engage(action.setPermanent(true));
		} else if ("stop".equals(order)) {
			slave.disengage(action);
		} else {
			slave.engage(action);
		}

		logger.info("[" + agent + "] ordered " + target + " to "
				+ (order == null ? action : order + " " + action.toOrderedString()));
	}

	@Override
	public String toString() {
		Action action = (Action) getArgument("action");
		return super.toString() + (action == null ? " anything" : " to " + action);
	}
}
