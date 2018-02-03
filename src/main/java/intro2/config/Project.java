package intro2.config;

import intro2.database.Cache;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Parses the XML config file, produces a Java object representing our
 * project. Uses DTD validation.
 */
public final class Project {
	public static final String ROOT_MODULE_NAME = "root";

	private SAXParserFactory factory;
	private String configDir;
	private long lastModified;

	private String name;
	private String description;
	private String dbFileName;
	private String outputPath;

	private Jar[] libraries;
	private SortedMap modules;

	public Project(String path) throws InvalidConfigDataException {
		File f = new File(path);

		if(!f.exists())
			throw new InvalidConfigDataException("No config file found");

		long ts = f.lastModified();

		factory = SAXParserFactory.newInstance();
		factory.setValidating(true);

		try {
			DefaultHandler handler = new ConfigHandler(new ValueReceiver());
			factory.newSAXParser().parse( new File(path), handler);
		} catch (SAXException e1) {
			throw new InvalidConfigDataException("Error parsing config file", e1);
		} catch (IOException e2) {
			throw new InvalidConfigDataException("Error parsing config file", e2);
		} catch (ParserConfigurationException e3) {
			throw new InvalidConfigDataException("Error parsing config file", e3);
		}

		check(ts);
	}

	private void check(long ts) throws InvalidConfigDataException {
		Module m;
		String[] p;
		int b,d;

		if(libraries.length==0)
			throw new InvalidConfigDataException("No libraries defined, project: "+getName());

		if(!modules.containsKey(ROOT_MODULE_NAME))
			throw new InvalidConfigDataException("Module root not defined");

		HashSet jarTracker = new HashSet();
		for(b=0;b<libraries.length;b++)
			if(!jarTracker.add(libraries[b].getLibPath()))
				throw new InvalidConfigDataException("Jar "+libraries[b].getLibPath()+" listed twice, project: "+getName());

		Iterator it = modules.keySet().iterator();
		while(it.hasNext()) {
			m = (Module)modules.get(it.next());
			m.sort();
		}

		it = modules.values().iterator();
		HashSet moduleNameTracker = new HashSet();
		while(it.hasNext()) {
			m = (Module)it.next();
			p = m.getPackages();
			for(d=0;d<p.length;d++)
				if(!moduleNameTracker.add(p[d]))
					throw new InvalidConfigDataException("Package "+p[d]+" referred to twice within modules, project: "+getName());
		}

		// If a database file has been specified, and
		// if any JARs are newer than our database file,
		// of our xml config file is newer, delete it
		if(dbFileName != null) {
			File dbf = new File(dbFileName);
			here:
			if((dbf != null) && dbf.exists()) {
				long l = dbf.lastModified();

				if(l < ts) { // xml config is newer
					dbf.delete();
					break here;
				}

				File f2;
				for(b=0;b<libraries.length;b++) {
					f2 = new File(libraries[b].getLibPath());

					if(f2.lastModified()>l) { // jar is newer
						dbf.delete();
//	    				dbFileName = null;
						break here;
					}
				}
			}
		}
	}

	public String getDBFileName() {
		return dbFileName;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	void setName(String name) {
		this.name = name;
	}

	void setDescription(String description) {
		this.description = description;
	}

	void setDBFileName(String dbFileName) {
		this.dbFileName = dbFileName;
	}

	void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	void addLibrary(Jar j) {
		if(libraries == null)
			libraries = new Jar[] { j };
		else {
			Jar[] na = new Jar[libraries.length+1];
			System.arraycopy(libraries,0,na,0,libraries.length);
			na[libraries.length] = j;
			libraries = na;
		}
	}

	public Jar[] getLibraries() {
		return libraries;
	}

	public Jar getLibrary(String path) {
		for(int i=0;i<libraries.length;i++)
			if(libraries[i].getLibPath().equals(path))
				return libraries[i];

		return null;
	}

	void addModule(Module m) {
		if(modules == null)
			modules = new TreeMap();
		modules.put(m.getName(),m);
	}

	public SortedMap getModules() {
		return modules;
	}

	public Module getModule(String moduleName) {
		return (Module)modules.get(moduleName);
	}

	public static class Jar implements Comparable {
		private String name;
		private String path;

		Jar() {}

		public int compareTo(Object obj) {
			return name.compareTo(((Jar)obj).getName());
		}

		void setName(String name) {
			Jar.this.name = name;
		}

		void setLibPath(String libPath) throws InvalidConfigDataException {
			if(!(new File(libPath).exists()))
				throw new InvalidConfigDataException("Can't find JAR "+libPath);

			path = libPath;
		}

		public String getName() {
			return name;
		}

		public String getLibPath() {
			return path;
		}
	}

	public static class Module implements Comparable {
		private String name;
		private String description;
		private String[] packages;
		private Module parent;
		private Module[] children;
		private transient boolean noSet = false;
		private transient long no;

		Module() {}

		public int compareTo(Object obj) {
			return name.compareTo(((Module)obj).getName());
		}

		public String getOutputFilename() {
			return getOutputFilename(null);
		}

		public String getOutputFilename(String postfix) {
			no = getNo();

			StringBuffer sb = new StringBuffer("module/");
			sb.append(Long.toString(no, 32));
			sb.append(".html");

			if(postfix != null)
				sb.append(postfix);

			return sb.toString();
		}

		private static String getModuleFilename(long no1, long no2, String prefix) {
			StringBuffer sb = new StringBuffer("module/");
			sb.append(prefix);
			sb.append('/');
			sb.append(Long.toString(no1, 32));
			sb.append('/');
			sb.append(Long.toString(no2, 32));
			sb.append(".html");
			return sb.toString();
		}

		public static String getModuleUsesFilename(long no1, long no2) {
			return getModuleFilename(no1, no2, "uses");
		}

		public static String getModuleUsedByFilename(long no1, long no2) {
			return getModuleFilename(no1, no2, "usedby");
		}

		public long getNo() {
			if(!noSet) {
				no = Cache.getUniqueFilenameValue(name);
				noSet = true;
			}
			return no;
		}

		void addPackage(String s) {
			if(packages == null)
				packages = new String[] { s };
			else {
				String[] na = new String[packages.length+1];
				System.arraycopy(packages,0,na,0,packages.length);
				na[packages.length] = s;
				packages = na;
			}
		}

		public String[] getPackages() {
			return packages;
		}

		void addModule(Module m) {
			if(children == null)
				children = new Module[] { m };
			else {
				Module[] na = new Module[children.length+1];
				System.arraycopy(children,0,na,0,children.length);
				na[children.length] = m;
				children = na;
			}
		}

		public boolean hasSubModule(Module other) {
			if(children == null)
				return false;

			return Arrays.binarySearch(children, other) >= 0;
		}

		public Module[] getSubModules() {
			return children;
		}

		void setParent(Module m) {
			parent = m;
		}

		public Module getParent() {
			return parent;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		void setName(String name) {
			Module.this.name = name;
		}

		void setDescription(String description) {
			Module.this.description = description;
		}

		void sort() {
			if(packages != null)
				Arrays.sort(packages);
			if(children != null)
				Arrays.sort(children, new ModComp());
		}

		private static class ModComp implements Comparator {
			public int compare(Object o1, Object o2) {
				Module m1 = (Module)o1;
				Module m2 = (Module)o2;

				return m1.getName().compareTo(m2.getName());
			}
		}
	}

	private class ValueReceiver {
		private boolean doingModules = false;
		private boolean doingLibraries = false;
		private Module current, child;
		private Jar lib;

		public void startingTag(String tag) {
			if(tag.equals("module")) {
				doingModules = true;

				Module parent = current;

				current = new Module();

				current.setParent(parent);
			}

			if(tag.equals("lib")) {
				lib = new Jar();
				doingLibraries = true;
			}
		}

		public void endingTag(String tag) {
			if(tag.equals("lib")) {
				doingLibraries = false;
				addLibrary(lib);
			}

			if(tag.equals("module")) {
				addModule(current);
				child = current;
				current = current.getParent();
				if(current == null)
					doingModules = false;
				else
					current.addModule(child);
			}
		}

		public void receiveValue(String tag, String value) throws SAXException {
			if(!doingModules && !doingLibraries) {
				if(tag.equals("name"))
					setName(value);
				if(tag.equals("description"))
					setDescription(value);
				if(tag.equals("dbfile"))
					setDBFileName(value);
				if(tag.equals("outputpath"))
					setOutputPath(value);
			}

			if(doingLibraries) {
				if(tag.equals("name"))
					lib.setName(value);
				if(tag.equals("jar"))
					try {
						lib.setLibPath(value);
					} catch (InvalidConfigDataException e) {
						throw new SAXException(e);
					}
			}

			if(doingModules) {
				if(tag.equals("name"))
					current.setName(value);
				if(tag.equals("description"))
					current.setDescription(value);
				if(tag.equals("package"))
					current.addPackage(value.replace('.','/'));
			}
		}
	}

	public static class InvalidConfigDataException extends Exception {
		public InvalidConfigDataException(String msg) {
			super(msg);
		}

		public InvalidConfigDataException(String msg, Throwable rootCause) {
			super(msg, rootCause);
		}
	}

	private static class ConfigHandler extends DefaultHandler {
		private static String PROJECT_DTD = "introproject.dtd";

		private ValueReceiver vr;
		private String currentElement;

		public ConfigHandler(ValueReceiver vr) {
			this.vr = vr;
		}

		public void error(SAXParseException e) throws SAXException {
			throw e;
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			currentElement = getName(localName, qName);

			vr.startingTag(currentElement);
		}

		public void endElement(String uri, String localName, String qName) {
			currentElement = getName(localName, qName);

			vr.endingTag(currentElement);
		}

		private String getName(String lName, String qName) {
			if ("".equals(lName))
				return qName;
			else
				return lName;
		}

		public void characters(char[] ch, int start, int length) throws SAXException {
			vr.receiveValue(currentElement, new String(ch, start, length));
		}

		public InputSource resolveEntity(String publicID, String systemID) {
			if(hasOnEnd(systemID, PROJECT_DTD))
				return getInputSource();
			else
				return null;
		}

		private boolean hasOnEnd(String str, String fragment) {
			return (str.indexOf(fragment)==str.length()-fragment.length()) &&
					(str.indexOf(fragment)!=-1);
		}

		private InputSource getInputSource() {
			StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\r");
			buffer.append("\n\r");

			buffer.append("<!ELEMENT project (name,description?,dbfile?,outputpath,lib+,module)>\n\r");
			buffer.append("<!ELEMENT name (#PCDATA)>\n\r");
			buffer.append("<!ELEMENT description (#PCDATA)>\n\r");
			buffer.append("<!ELEMENT lib (name,jar)>\n\r");
			buffer.append("<!ELEMENT jar (#PCDATA)>\n\r");
			buffer.append("<!ELEMENT package (#PCDATA)>\n\r");
			buffer.append("<!ELEMENT dbfile (#PCDATA)>\n\r");
			buffer.append("<!ELEMENT outputpath (#PCDATA)>\n\r");
			buffer.append("<!ELEMENT module (name,description?,package+,module*)>\n\r");

			return new InputSource(new StringReader(buffer.toString()));
		}
	}
}