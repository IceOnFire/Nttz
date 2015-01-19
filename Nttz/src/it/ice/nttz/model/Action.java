package it.ice.nttz.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Action {
	protected static Logger logger = Logger.getAnonymousLogger();

	protected String name;
	protected Agent agent;
	protected Entity target;
	protected Set<Entity> tools;
	protected Map<String, Object> arguments;
	protected boolean permanent;

	public Action() {
		this.name = getClass().getSimpleName();
		tools = new HashSet<Entity>();
		arguments = new HashMap<String, Object>();
		permanent = false;
	}

	public Action(String name) {
		this();
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Entity getTarget() {
		return target;
	}

	public Action applyTo(Entity target) {
		setTarget(target);
		return this;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public Set<Entity> getTools() {
		return tools;
	}

	public void setTools(Set<Entity> tools) {
		this.tools = new HashSet<Entity>(tools);
	}

	public Entity getTool() {
		return (Entity) tools.toArray()[0];
	}

	public Action with(Entity tool) {
		setTool(tool);
		return this;
	}

	public void setTool(Entity tool) {
		tools.clear();
		addTool(tool);
	}

	public void addTool(Entity tool) {
		tools.add(tool);
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = new HashMap<String, Object>(arguments);
	}

	public Object getArgument(String name) {
		return arguments.get(name);
	}

	public Action setArgument(String name, Object argument) {
		arguments.put(name, argument);
		return this;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public Action setPermanent(boolean permanent) {
		this.permanent = permanent;
		return this;
	}

	protected void reset() {
		// do nothing
	}

	protected void perform() {
		// do nothing
	}

	protected void done() {
		// do nothing
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Action)) {
			return false;
		}
		Action action = (Action) object;
		if (name.equals(action.name)// && agent.equals(action.agent)
				&& (target == null || target.equals(action.target))
				&& (tools.isEmpty() || tools.equals(action.tools))
				&& (arguments.isEmpty() || arguments.equals(action.arguments))) {
			return true;
		}
		return false;
	}

	@Override
	protected Action clone() {
		Action clone = null;
		try {
			clone = this.getClass().newInstance();
			clone.setName(name);
			clone.setTarget(target);
			/* 12/03/2011: new ones */
			clone.setTools(tools);
			clone.setArguments(arguments);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clone;
	}

	@Override
	public String toString() {
		String string = name.toLowerCase() + (target == null ? "" : " " + target)
				+ (tools.isEmpty() ? "" : " with " + tools);
		if (!arguments.isEmpty()) {
			string += "(";
			for (String key : arguments.keySet()) {
				string += key + ": " + arguments.get(key) + ", ";
			}
			string = string.substring(0, string.length() - 1) + ")";
		}
		return string;
	}

	public String toOrderedString() {
		String string = name.toLowerCase() + "ing"
				+ (target == null ? "" : " " + target)
				+ (tools.isEmpty() ? "" : " with " + tools);
		if (!arguments.isEmpty()) {
			string += "(";
			for (String key : arguments.keySet()) {
				string += key + ": " + arguments.get(key) + ", ";
			}
			string = string.substring(0, string.length() - 1) + ")";
		}
		return string;
	}
}
