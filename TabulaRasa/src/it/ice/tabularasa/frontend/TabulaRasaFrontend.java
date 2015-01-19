package it.ice.tabularasa.frontend;

import it.ice.nttz.Nttz;
import it.ice.nttz.model.Action;
import it.ice.nttz.model.Agent;
import it.ice.nttz.model.Entity;
import it.ice.nttz.model.Property;
import it.ice.tabularasa.core.model.creature.Creature;

import java.util.logging.Logger;

public class TabulaRasaFrontend {
	public static void main(String[] args) {
		Nttz nttz = new Nttz();
		nttz.loadEntityDefinitions("file:///home/ice/Documenti/Progetti/tabularasa/TabulaRasa/res/entities.xml");

		nttz.summon("apple");
		nttz.summon("vegetables");

		Entity fridge = nttz.create("fridge");
		Entity appleInFridge = nttz.create("apple in fridge");
		Entity sparkle = nttz.create("sparkle");
		fridge.addRelationship(Nttz.CONTAINING, appleInFridge);
		fridge.addRelationship(Nttz.CONTAINING, sparkle);
		nttz.summon(fridge);

		Entity puyo = nttz.create("blob").setName("Puyo");
		nttz.summon(puyo);

		int round = 0;
		while (true) {
			Logger.getAnonymousLogger().info("+--- ROUND " + (round++) + " ---+");
			nttz.act();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (round == 2) {
				puyo.getProperty(Creature.PROPERTY_NOT_HUNGRY).setValue(
						Property.MIN_VALUE);
			} else if (round == 18) {
				Action open = new Action("Open");
				open.setTarget(fridge);
				((Agent) puyo).engage(open);
			}
		}
	}
}
