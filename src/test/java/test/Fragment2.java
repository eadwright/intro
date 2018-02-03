package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Fragment2 {
	private static int counter = 0;
	private int id = counter++;

	private String[] tags;

	private String text;
	private String tagName;
	private String content;
	private Map attributes;
	private boolean hasChildren = false;
	private boolean hasNext = false;
	private int nextIndex;
	private boolean percent = false;

	private Fragment2(String text, String[] tags) throws FragmentProcessException {
		this.tags = tags;
		this.text = text;//.trim();

		String p = getPattern1(tags);

//		System.out.println(id+" looking for "+p);

		Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE|Pattern.MULTILINE|Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);

		if(!matcher.find()) {
//			System.out.println(id+" no pattern found:: "+text+" ::");
			content = text;
			return;
		}
//		System.out.println(id+" pattern found:: "+matcher.group()+" ::");

		int start = matcher.start();

		if(start>0) {
//			System.out.println(id+" start "+start);
			content = text.substring(0,start);
//			System.out.println(id+" content "+content);
			hasNext = true;
			nextIndex = start;
//			System.out.println(id+" rest "+text.substring(nextIndex));
			return;
		}

		int end = 0;

//		tagName = text.substring(1,text.indexOf(' '));
		tagName = matcher.group().substring(1).trim();
//		System.out.println("gives tagName:: "+tagName+" ::");

//		System.out.println(id+" start 0, tagName "+tagName);
//		System.out.println("gives string:: "+matcher.group()+" ::");

		boolean special = isSpecial(text.charAt(1));
		boolean hasCode = hasCode(text.charAt(1));
		if(hasCode) {
//			if(hasContent)
//				throw new FragmentProcessException("Content not allowed within special <% ... /> type tags");

			pattern = Pattern.compile("/>", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE|Pattern.DOTALL);
			matcher = pattern.matcher(text);

			if(!matcher.find())
				throw new FragmentProcessException("Cant find end of tag i.e. />");

			end = matcher.start()+2;

			content = text.substring(start+2,matcher.start()).trim();
		} else {

			if(special)
				p = getPattern2("");
			else
				p = getPattern2(tagName);

			pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE|Pattern.MULTILINE|Pattern.DOTALL);
			matcher = pattern.matcher(text);

			if(!matcher.find())
				throw new FragmentProcessException("Cant find end of tag");

			end = matcher.start()+matcher.group().length();

			String attrText = text.substring(1+tagName.length(),matcher.start()+1);

//			System.out.println("*** attrText !"+attrText+"!");
//			System.out.println("*** compare !"+matcher.group()+"!");
			if((!special) && (matcher.group().length() == 2)) { // has content
				int end2 = matcher.end();

				p = getPattern3(tagName);

				pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE|Pattern.MULTILINE|Pattern.DOTALL);
				matcher = pattern.matcher(text);

				if(!matcher.find())
					throw new FragmentProcessException("Cant find end of tag");

				content = text.substring(end2,matcher.start());
				hasChildren = true;
				end = matcher.end();
			}

			// process attributes here

			if(attrText.length()>0) {
				pattern = Pattern.compile("(.*\".*\")*", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
				matcher = pattern.matcher(attrText);

				matcher.find();

				String tmp = matcher.group();
//				System.out.println("*** looking in "+tmp);

				pattern = Pattern.compile("\\w*=", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
				matcher = pattern.matcher(tmp);

				pattern = Pattern.compile("\"[.[^\"]]*\"", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);

				Matcher m2;
				Pattern p2;
				String t, n, v;

				attributes = new HashMap();

				while(matcher.find()) {
					t = matcher.group();
					n = t.substring(0, t.length()-1);
//					System.out.println("attr = "+n);

//					System.out.println("looking for value in "+tmp.substring(matcher.end()));
					m2 = pattern.matcher(tmp.substring(matcher.end()));

					m2.find();

					v = m2.group().substring(m2.start()+1, m2.end()-1);

					p2 = Pattern.compile("\\\\>");
					m2 = p2.matcher(v);
					v = m2.replaceAll(">");

//					System.out.println("value = "+v);

					attributes.put(n, v);
				}
			}
		}

		if(end<text.length()) {
			hasNext = true;
			nextIndex = end;
		}

//		System.out.println(id+" content: "+content);
//		System.out.println(id+" hasNext "+hasNext);
//		System.out.println(id+" remaining "+text.substring(nextIndex));
	}

	private boolean isSpecial(char c) {
		return (c == '!') || (c == '%') || (c == '@') || (c == '=');
	}

	private boolean hasCode(char c) {
		return (c == '%') || (c == '=');
	}

	private String getPattern1(String[] tags) { // look for starting tag
		StringBuffer sb = new StringBuffer("((<!)|(<%)|(<@)|(<=)");

		if(tags != null)
			for(int i=0;i<tags.length;i++) {
				sb.append("|(<");
				sb.append(tags[i]);
				sb.append(')');
			}

		sb.append(")\\x20");

		return sb.toString();
	}

	private String getPattern2(String name) { // for ending tags
//		StringBuffer sb = new StringBuffer("(/>)|( /");
		StringBuffer sb = new StringBuffer("([\\w\"]>)|(/");
		sb.append(name);
		sb.append(">)");

		return sb.toString();
	}

	private String getPattern3(String name) { // for ending tags with tagname only
		StringBuffer sb = new StringBuffer("</");
		sb.append(name);
		sb.append(">");

		return sb.toString();
	}

	public String getName() {
		return tagName;
	}

	public boolean hasChildren() {
		return hasChildren;
	}

	public String getContent() {
		if(hasChildren)
			return null;
		else
			return content;
	}

	public Fragment2 getChild() throws FragmentProcessException {
//		System.out.println("create sub-frag of "+tagName+" content "+content);
		if(hasChildren)
			return new Fragment2(content, tags);
		else
			return null;
	}

	public Map getAttributes() {
		return attributes;
	}

	public boolean hasNext() {
		return hasNext;
	}

	public Fragment2 getNext() throws FragmentProcessException {
		return new Fragment2(text.substring(nextIndex), tags);
	}

	public static Fragment2 getInitial(BufferedReader br, String[] tags) throws FragmentProcessException, IOException {
		StringBuffer sb = new StringBuffer();

		String line = null;
		while((line = br.readLine()) != null) {
			sb.append(line);
//			if(line.charAt(line.length()-1)!='>')
				sb.append('\n');
		}
		br.close();

		return new Fragment2(sb.toString(), tags);
	}
}