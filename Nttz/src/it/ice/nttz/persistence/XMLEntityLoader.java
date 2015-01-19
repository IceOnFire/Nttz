package it.ice.nttz.persistence;

import it.ice.nttz.Nttz;
import it.ice.nttz.model.Action;
import it.ice.nttz.model.Agent;
import it.ice.nttz.model.Category;
import it.ice.nttz.model.Entity;
import it.ice.nttz.model.Property;

import java.net.URL;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XMLEntityLoader {
	private Nttz nttz;

	public XMLEntityLoader(Nttz nttz) {
		this.nttz = nttz;
	}

	public void load(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		load(document);
	}

	public void load(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		load(document);
	}

	private void load(Document document) {
		Element entitiesElement = document.getRootElement();
		for (int i = 0; i < entitiesElement.nodeCount(); i++) {
			Node entityNode = entitiesElement.node(i);
			if (entityNode instanceof Element) {
				Element entityElement = (Element) entityNode;
				Entity entity = load(entityElement);
				nttz.define(entity.getDefinition(), entity);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private Entity load(Element entityElement) {
		Entity entity = null;

		Attribute entityTypeAttribute = entityElement.attribute("type");
		if (entityTypeAttribute != null) {
			String supertype = entityTypeAttribute.getValue();
			Entity supertypeEntity = nttz.getEntityDefinitions().get(supertype);
			if (supertypeEntity != null) {
				entity = supertypeEntity.clone();
			} else {
				Class clazz = null;
				try {
					clazz = Class.forName(supertype);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (clazz != null) {
					try {
						entity = (Entity) clazz.newInstance();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			entity = new Entity();
		}

		Element nameElement = entityElement.element("name");
		if (nameElement != null) {
			entity.setName(nameElement.getText());
		}

		Element definitionElement = entityElement.element("definition");
		if (definitionElement != null) {
			entity.setDefinition(definitionElement.getText());
		}

		Element categoriesElement = entityElement.element("categories");
		if (categoriesElement != null) {
			for (int i = 0; i < categoriesElement.nodeCount(); i++) {
				Node categoryNode = categoriesElement.node(i);
				if (categoryNode instanceof Element) {
					Element categoryElement = (Element) categoryNode;
					Attribute nameAttribute = categoryElement.attribute("name");
					String name = nameAttribute.getValue();
					Attribute weightAttribute = categoryElement.attribute("weight");
					float weight = Float.valueOf(weightAttribute.getValue());
					Category category = entity.getCategory(name);
					if (category == null) {
						entity.addCategory(name);
						category = entity.getCategory(name);
					}
					category.setWeight(weight);
				}
			}
		}

		Element propertiesElement = entityElement.element("properties");
		if (propertiesElement != null) {
			for (int i = 0; i < propertiesElement.nodeCount(); i++) {
				Node propertyNode = propertiesElement.node(i);
				if (propertyNode instanceof Element) {
					Element propertyElement = (Element) propertyNode;
					Attribute nameAttribute = propertyElement.attribute("name");
					String name = nameAttribute.getValue();
					Property property = entity.getProperty(name);
					if (property == null) {
						property = new Property(name);
						entity.addProperty(property);
					}
					Attribute valueAttribute = propertyElement.attribute("value");
					if (valueAttribute != null) {
						float value = Float.valueOf(valueAttribute.getValue());
						property.setValue(value);
					}
					Attribute decreaseValueAttribute = propertyElement
							.attribute("decreaseValue");
					if (decreaseValueAttribute != null) {
						float decreaseValue = Float.valueOf(decreaseValueAttribute
								.getValue());
						property.setDecreaseValue(decreaseValue);
					}
				}
			}
		}

		Element sensorsElement = entityElement.element("sensors");
		if (sensorsElement != null) {
			for (int i = 0; i < sensorsElement.nodeCount(); i++) {
				Node sensorNode = sensorsElement.node(i);
				if (sensorNode instanceof Element) {
					Element sensorElement = (Element) sensorNode;
					Attribute propertyAttribute = sensorElement.attribute("property");
					String property = propertyAttribute.getValue();
					entity.addSensor(property);
				}
			}
		}

		Element childrenElement = entityElement.element("children");
		if (childrenElement != null) {
			for (int i = 0; i < childrenElement.nodeCount(); i++) {
				Node childNode = childrenElement.node(i);
				if (childNode instanceof Element) {
					Element childElement = (Element) childNode;
					Entity child = load(childElement);
					entity.addChild(child);
				}
			}
		}

		Element actionsElement = entityElement.element("actions");
		if (actionsElement != null) {
			for (int i = 0; i < actionsElement.nodeCount(); i++) {
				Node actionNode = actionsElement.node(i);
				if (actionNode instanceof Element) {
					Element actionElement = (Element) actionNode;
					Attribute actionTypeAttribute = actionElement.attribute("type");
					String type = actionTypeAttribute.getValue();
					Action action = null;

					if (type.contains(".")) {
						Class clazz = null;
						try {
							clazz = Class.forName(actionTypeAttribute.getValue());
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (clazz != null) {
							try {
								action = (Action) clazz.newInstance();
							} catch (InstantiationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						action = nttz.loadActionDefinition(actionTypeAttribute.getValue());
					}

					if (action != null) {
						Element targetElement = actionElement.element("target");
						if (targetElement != null) {
							action.setTarget(nttz.getEntityDefinitions().get(
									targetElement.getText()));
						}

						Element toolsElement = actionElement.element("tools");
						if (toolsElement != null) {
							for (int j = 0; j < toolsElement.nodeCount(); j++) {
								Node toolNode = toolsElement.node(j);
								if (toolNode instanceof Element) {
									Element toolElement = (Element) toolNode;
									action.addTool(entity.s(toolElement.getText()));
								}
							}
						}
						((Agent) entity).learn(action);
					}
				}
			}
		}

		nttz.define(entity.getDefinition(), entity);
		return entity;
	}
}
