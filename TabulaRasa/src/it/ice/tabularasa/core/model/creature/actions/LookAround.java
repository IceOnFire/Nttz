package it.ice.tabularasa.core.model.creature.actions;

import it.ice.nttz.Nttz;
import it.ice.nttz.model.Action;
import it.ice.nttz.model.Entity;
import it.ice.nttz.model.Property;
import it.ice.tabularasa.core.model.creature.Creature;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LookAround extends Action {
	@Override
	public void perform() {
		logger.info("[" + agent + "] looking around for something to eat");
		Creature creature = (Creature) agent;

		Entity rootContainer = agent.getSourceInRelationship(Nttz.CONTAINING);
		Set<Entity> contents = rootContainer
				.getTargetsInRelationship(Nttz.CONTAINING);
		Set<Entity> targetableContents = new HashSet<Entity>(contents);
		targetableContents.remove(agent);
		for (Entity content : contents) {
			Property contentVisible = content.getProperty(Nttz.CONTENT_VISIBLE);
			if (contentVisible != null
					&& contentVisible.getValue() >= Property.AVG_VALUE) {
				Set<Entity> contentOfContent = content
						.getTargetsInRelationship(Nttz.CONTAINING);
				targetableContents.addAll(contentOfContent);
			}
		}

		Iterator<Entity> it = targetableContents.iterator();
		while (it.hasNext() && !agent.isInState(Creature.STATE_EATING)) {
			Entity targetableContent = it.next();
			Entity belief = creature.recallBelief(targetableContent);
			if (belief != null && belief.hasProperty("taste")
					&& belief.getPropertyValue("taste") > Property.AVG_VALUE) {
				// Set<Entity> beliefs = creature.recallBeliefsHavingProperty("taste",
				// true, Property.AVG_VALUE);
				// if (beliefs.contains(targetableContent)) {
				agent.addRelationship(Creature.STATE_EATING, targetableContent);
				logger.info("[" + creature + "] I think I'll eat " + targetableContent);
			}
		}

		if (!agent.isInState(Creature.STATE_EATING)) {
			logger.info("[" + creature
					+ "] I don't see anything tasty as far as I know");
			it = targetableContents.iterator();
			while (it.hasNext() && !agent.isInState(Creature.STATE_TASTING)) {
				Entity targetableContent = it.next();
				Entity belief = creature.recallBelief(targetableContent);
				if (belief == null || !belief.hasProperty("taste")) {
					agent.addRelationship(Creature.STATE_TASTING, targetableContent);
					logger.info("[" + creature + "] I think I'll taste "
							+ targetableContent);
				}
			}
		}
	}

	@Override
	protected void done() {
		super.done();
		agent.removeState(Creature.STATE_LOOKING_AROUND);
	}
}
