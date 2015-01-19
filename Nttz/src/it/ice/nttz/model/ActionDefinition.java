package it.ice.nttz.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import bsh.EvalError;
import bsh.Interpreter;

public class ActionDefinition extends Action {
	private Interpreter i;

	public ActionDefinition() {
		super();
	}

	public ActionDefinition(String name) {
		super(name);
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		i = new Interpreter();
		try {
			i.source("res/actions/" + name.toLowerCase() + ".bsh");
			i.set("logger", logger);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setAgent(Agent agent) {
		super.setAgent(agent);
		try {
			i.set("agent", agent);
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setTarget(Entity target) {
		super.setTarget(target);
		try {
			i.set("target", target);
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setTools(Set<Entity> tools) {
		super.setTools(tools);
		try {
			i.set("tools", tools);
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void reset() {
		try {
			i.eval("reset()");
		} catch (EvalError e) {
			// optional method: do nothing
		}
	}

	@Override
	protected void perform() {
		try {
			i.eval("perform()");
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void done() {
		try {
			i.eval("done()");
		} catch (EvalError e) {
			// optional method: do nothing
		}
	}
}
