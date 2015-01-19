package it.ice.nttz.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
	private Properties properties;

	public PropertyReader(String filename) {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("res/" + filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getProp(String name) {
		return properties.getProperty(name);
	}

	public int getIntProp(String name) {
		return Integer.parseInt(getProp(name));
	}

	public float getFloatProp(String name) {
		return Float.parseFloat(getProp(name));
	}
}
