package it.ice.nttz.model.actions;

import it.ice.nttz.Nttz;
import it.ice.nttz.model.Action;
import it.ice.nttz.model.Entity;
import it.ice.nttz.model.Property;

import java.util.Iterator;
import java.util.Set;

public class Fetch extends Action {
	private final static String FETCHING_CHILDREN = "fetching children",
			FETCHING_CONTAINED = "fetching contained entities";

	@Override
	public void reset() {
		agent.addState(FETCHING_CHILDREN);
	}

	@Override
	public void perform() {
		Entity rootContainer = null;
		if (agent.isInState(FETCHING_CHILDREN)) {
			rootContainer = agent.getSourceInRelationship(
					Nttz.CONTAINING);
		} else if (agent.isInState(FETCHING_CONTAINED)) {
			Set<Entity> rootContainers = agent
					.getTargetsInRelationship(FETCHING_CONTAINED);
			rootContainer = (Entity) rootContainers.toArray()[0];
			agent.removeRelationship(FETCHING_CONTAINED, rootContainer);
			if (!agent.isInState(FETCHING_CONTAINED)) {
				reset();
			}
		}

		logger.info("[" + agent + "] fetching children of "
				+ rootContainer);

		boolean targeting = false;
		Set<Entity> entities = rootContainer
				.getTargetsInRelationship(Nttz.CONTAINING);
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext() && !targeting) {
			Entity child = it.next();
			if (child != agent) {
				if (child.equals(target)) {
					Entity eyes = agent.s("eyes");
					eyes.addRelationship(Nttz.TARGETING, child);
					logger.info("[" + agent + "] I found " + child);
					targeting = true;
				} else {
					logger.info("[" + agent + "] is " + child
							+ " a container for anything?");
					Property contentVisible = child
							.getProperty(Nttz.CONTENT_VISIBLE);
					if (contentVisible != null
							&& contentVisible.getValue() >= Property.AVG_VALUE) {
						agent.addRelationship(FETCHING_CONTAINED, child);
						agent.removeState(FETCHING_CHILDREN);
						logger.info("[" + agent + "] yes, next time I'll check "
								+ child + "'s content");
					} else {
						logger.info("[" + agent + "] no, it isn't");
					}
				}
			} else {
				logger.info("[" + agent + "] skipping myself");
			}
		}
	}
}
