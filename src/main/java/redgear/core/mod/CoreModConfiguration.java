package redgear.core.mod;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoreModConfiguration {

	private final File configFile;
	private final ArrayList<Property> properties = new ArrayList<Property>();
	private boolean hasChanged = false;

	public CoreModConfiguration(File file, CoreModUtils util) {
		configFile = file;
	}

	public void load() {
		for (String line : FileHelper.readLines(configFile))
			properties.add(new Property(line));
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void save() {
		if (hasChanged) {
			ArrayList<String> lines = new ArrayList<String>();

			for (Property prop : properties)
				if (prop.type != Type.Void)
					lines.add(prop.toString());

			FileHelper.writeLines(lines, configFile);
		}
	}

	private Property getProperty(String name, Type type) {
		for (Property prop : properties)
			if (prop.type == type && prop.name.equals(name))
				return prop;

		return null;
	}

	private Object getValue(String name, Object Default, Type type) {
		Property prop = getProperty(name, type);

		if (prop != null)
			return prop.value;
		else {
			properties.add(new Property(name, type, Default));
			hasChanged = true;
			return Default;
		}
	}

	public boolean getBoolean(String name, boolean Default) {
		return (Boolean) getValue(name, Default, Type.Boolean);
	}

	public int getInt(String name, int Default) {
		return (Integer) getValue(name, Default, Type.Integer);
	}

	private enum Type {
		Integer('I', Integer.class, "parseInt"), Boolean('B', Boolean.class, "parseBoolean"), String('S', String.class,
				null), Void('V', null, null);

		public char symbol;
		public Class<?> def;
		public String parseFunction;

		Type(char symbol, Class<?> def, String parseFunction) {
			this.symbol = symbol;
			this.def = def;
			this.parseFunction = parseFunction;
		}
	}

	private static class Property {
		public String name;
		public Type type;
		public Object value;

		/**
		 * Reads the line from the file. Correct format is: {@code t:name=value}
		 * As in {@code B:debugMode=true}
		 * 
		 * @param line
		 */
		Property(String line) {
			try {//just in case
				List<String> tokens = Arrays.asList(line.split("[=:]"));

				char typeTest = Character.toUpperCase(tokens.get(0).charAt(0));

				for (Type test : Type.values())
					if (typeTest == test.symbol) {
						if (test == Type.String)
							value = tokens.get(2);
						else
							value = test.def.getMethod(test.parseFunction, String.class).invoke(null, tokens.get(2));

						name = tokens.get(1);
						type = test;

						return;
					}
			} catch (Exception e) {
			}

			//if anything goes wrong, just fill in default values
			name = "";
			type = Type.Void;
			value = line;
		}

		Property(String name, Type type, Object value) {
			this.name = name;
			this.type = type;
			this.value = value;
		}

		@Override
		public String toString() {
			return type.symbol + ":" + name + "=" + value.toString();
		}

	}

}
