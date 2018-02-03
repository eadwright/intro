package lahaina.fragment;

import lahaina.runtime.LahainaException;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FragmentFactory {
	private static Map tagMap;
	private static Pattern interestingTagPattern;
	private static Pattern endSpecialPattern = Pattern.compile("/>", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	private static Pattern attrPattern1 = Pattern.compile("(.*\".*\")*", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
	private static Pattern attrPattern2 = Pattern.compile("\\w*=", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
	private static Pattern attrPattern3 = Pattern.compile("\"[.[^\"]]*\"", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
	private static Pattern attrPattern4 = Pattern.compile("\\\\>");

	private FragmentFactory() {}

	public synchronized static Fragment buildTree(String data, Class[] tags) {
		init(tags);
		return buildSubTree(data);
	}

	private static Fragment buildSubTree(String data) {
		data = data.trim();
		Matcher matcher = interestingTagPattern.matcher(data);

		if(!matcher.find())
			return new TextFragment(data);

		int start = matcher.start();
		if(start>0) {
			Fragment result = new TextFragment(data.substring(0, start));
			result.setNext(buildSubTree(data.substring(start)));

			return result;
		}

		String tagName = matcher.group().substring(1).trim().toLowerCase();

		if(tagName.equals("%") || tagName.equals("=") || tagName.equals("#"))
			return createCodeFragment(tagName.equals("="), !tagName.equals("#"), data);

		if(tagName.equals("!"))
			return createCommentFragment(data);

		if(tagName.equals("@"))
			return createDeclarationFragment(data);

		// Got this far, must be a custom tag

		return createTagFragment(tagName, data);
	}

	private static Fragment createCodeFragment(boolean isAssignment, boolean isInline, String data) {
		Matcher matcher = endSpecialPattern.matcher(data);

		if(!matcher.find())
			throw new LahainaException("Cant find end of code/assignment tag");

		CodeFragment result = new CodeFragment(isAssignment, isInline, data.substring(2,matcher.start()).trim());

		addEndToSpecial(matcher, data, result);

		return result;
	}

	private static Fragment createCommentFragment(String data) {
		Matcher matcher = endSpecialPattern.matcher(data);

		if(!matcher.find())
			throw new LahainaException("Cant find end of comment tag");

		CommentFragment result = new CommentFragment(data.substring(2,matcher.start()).trim());

		addEndToSpecial(matcher, data, result);

		return result;
	}

	private static Fragment createDeclarationFragment(String data) {
		Matcher matcher = endSpecialPattern.matcher(data);

		if(!matcher.find())
			throw new LahainaException("Cant find end of declaration tag");

		DeclarationFragment result = new DeclarationFragment();
		addAttributes(data.substring(2,matcher.start()).trim(), result);

		addEndToSpecial(matcher, data, result);

		return result;
	}

	private static Fragment createTagFragment(String tagName, String data) {
		Pattern pattern = getEndingTagPattern1(tagName);
		Matcher matcher = pattern.matcher(data);

		if(!matcher.find())
			throw new LahainaException("Cant find end of tag: "+tagName);

//		System.out.println("found group "+matcher.group());
//		System.out.println("text before "+data.substring(0,matcher.start()));

		Class clz = (Class)tagMap.get(tagName);
		boolean allowsContent = TagFragment.allowsContent(clz);

		int end;

		TagFragment result = new TagFragment(clz);

		if(tagName.length() < matcher.start()) {
			String attrText = data.substring(1+tagName.length(),matcher.start()+1);
			addAttributes(attrText, result);
		}

		if((matcher.group().length() == 2) && (!matcher.group().equals("/>"))) {
			if(!allowsContent)
				throw new LahainaException("Content not allowed in tag: "+tagName);

			int endOfOpening = matcher.end();

//			pattern = getEndingTagPattern2(tagName);
//			matcher = pattern.matcher(data);

//			if(!matcher.find())
//				throw new LahainaException("Cant find end of tag containing content, name: "+tagName+"\n\ndata: "+data);

			matcher = getMatcherForPattern2(data, tagName);

			String content = data.substring(endOfOpening,matcher.start());

			if(TagFragment.processesContent(clz))
				result.setContent(content);
			else
				result.setChild(buildSubTree(content));

			end = matcher.end();
		} else {
			if((matcher.group().charAt(0) == '/') ||
				(matcher.group().charAt(1) == '/'))
				end = matcher.end();
			else
				end = matcher.start()-matcher.group().length();
		}

		addEnd(end, data, result);

		return result;
	}

	private static Matcher getMatcherForPattern2(String data, String tagName) throws LahainaException {
		StringBuffer sb = new StringBuffer("<");
		sb.append(tagName);

		Pattern open = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE|Pattern.DOTALL);

		sb.setLength(0);
		sb.append("</");
		sb.append(tagName);
		sb.append(">");

		Pattern close = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE|Pattern.DOTALL);

		Matcher openM = open.matcher(data);
		Matcher closeM = close.matcher(data);

		int pos = openM.pattern().pattern().length();
		boolean foundClose;
		boolean foundOpen;
//		System.out.println("---");
		do {
			foundOpen = openM.find(pos);
			foundClose = closeM.find(pos);

/*			System.out.println("--start");
			System.out.println("found open "+foundOpen);
			if(foundOpen)
				System.out.println("open start "+openM.start());
			System.out.println("found close "+foundClose);
			if(foundClose)
				System.out.println("close start "+closeM.start());
			System.out.println("--end");*/

			if(!foundClose)
				throw new LahainaException("Cant find end of tag containing content, name: "+tagName+"\n\ndata: "+data);
			if(!foundOpen)
				return closeM;

			if(closeM.start() < openM.start())
				return closeM;

			pos = closeM.end();
		} while(true);

	}

	private static Pattern getEndingTagPattern1(String tagName) {
		StringBuffer sb = new StringBuffer("([\\w\"]>)|(/");
		sb.append(tagName);
		sb.append(">)|(");
		sb.append(tagName.charAt(tagName.length()-1));
		sb.append("/>)|(/>)");

//		System.out.println("ending pattern "+sb.toString());

		return Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	}

	private static void addEndToSpecial(Matcher matcher, String data, Fragment result) {
		int end = matcher.start() + 2;
		addEnd(end, data, result);
	}

	private static void addEnd(int end, String data, Fragment result) {
		if(end < data.length()) {
			String sub = data.substring(end);

			result.setNext(buildSubTree(sub));
		}
	}

	private static void addAttributes(String attrText, HasAttributes result) {
		Matcher matcher = attrPattern1.matcher(attrText);

		if(!matcher.find())
			return;

		attrText = matcher.group();

		matcher = attrPattern2.matcher(attrText);

		Matcher m2;
		String t, name, value;

		while(matcher.find()) {
			t = matcher.group();
			name = t.substring(0, t.length()-1);
			m2 = attrPattern3.matcher(attrText.substring(matcher.end()));

			m2.find();

			value = m2.group().substring(m2.start()+1, m2.end()-1);

			m2 = attrPattern4.matcher(value);
			value = m2.replaceAll(">");

			result.addAttribute(name, value);
		}
	}

	private static void init(Class[] tags) {
		createTagMap(tags);
		createPattern(tagMap.keySet().iterator());
	}

	private static void createTagMap(Class[] tags) {
		tagMap = new TreeMap(new ReverseStringComparator());

		String name;
		int j;

		if(tags != null)
			for(int i=0;i<tags.length;i++) {
				name = tags[i].getName();
				j = name.lastIndexOf('.');
				if(j != -1)
					name = name.substring(j+1);

				tagMap.put(name.toLowerCase(), tags[i]);
			}
	}

	// This sorts a map of tags keyed on tag name in reverse order, so
	// we look for tags with long names first!
	private static class ReverseStringComparator implements Comparator {
		public int compare(Object obj1, Object obj2) {
			String str1 = (String)obj1;
			String str2 = (String)obj2;

			return str2.compareTo(str1); // delibrate reverse order
		}

		public boolean equals(Object obj1, Object obj2) {
			return obj1.equals(obj2);
		}
	}

	private static void createPattern(Iterator tagNames) {
		StringBuffer sb = new StringBuffer("(((<!)|(<%)|(<@)|(<=)|(<#)))[\\x20\\n\\r]");

		while(tagNames.hasNext()) {
			sb.append("|(<");
			sb.append((String)tagNames.next());
			sb.append(')');
		}

		interestingTagPattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE|Pattern.MULTILINE|Pattern.DOTALL);
	}
}