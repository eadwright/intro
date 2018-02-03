package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Fragment {
	private String text;
	private String tagName;
	private boolean hasContent = false;
	private String content;
	private Map attributes;
	private boolean hasNext = false;
	private int nextIndex;
	private boolean percent = false;

	private boolean textOnly = false;
	private boolean comment = false;
	private boolean declaration = false;
	private boolean code = false;

	private Fragment(String text) throws FragmentProcessException {
		this.text = text.trim();

//		System.out.println("all = "+text);

		Pattern pattern = Pattern.compile("<((\\\\>)|([^>]))*>", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE|Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);

		if(!matcher.find()) {
//			System.out.println("pattern not found");
			hasContent = true;
			textOnly = true;
			content = text;
			return;
		}

		int start = matcher.start();
		int end = matcher.end();

		if(start>0) {
			hasContent = true;
			textOnly = true;
			content = text.substring(0,start);
			hasNext = true;
			nextIndex = start;
			return;
		}

//		System.out.println("start = "+start);
//		System.out.println("end = "+end);

//		System.out.println("gives string:: "+matcher.group()+" ::");

		boolean percent = false;
		if(text.charAt(start+1) == '%') {
			if(hasContent)
				throw new FragmentProcessException("Content not allowed within special <% ... /> type tags");

			percent = true;

			if(text.charAt(start+2) == '-')
				comment = true;

			if(text.charAt(start+2) == '@')
				declaration = true;

			if((!comment)&&(!declaration))
				code = true;
		}

		pattern = Pattern.compile("<([^/>\\s])*", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
		matcher = pattern.matcher(text.substring(start, end));

		if(!matcher.find())
			throw new FragmentProcessException("Can't find tag");

		tagName = matcher.group().substring(1).toLowerCase();

//		System.out.println("tagName = "+tagName);

		int attrIndex = tagName.length()+2;

		if(percent) {
			content = text.substring(attrIndex, end-2);

//			System.out.println("(code) content = "+code);

		} else {
			hasContent = (end < text.length()-1) && (text.charAt(end-2) != '/');

//			System.out.println("hasContent = "+hasContent);

			if(attrIndex<end) {
				pattern = Pattern.compile("(.*\".*\")*", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
				matcher = pattern.matcher(text.substring(attrIndex, end));

				boolean result = matcher.find();
//				System.out.println("attribute search = "+result);

				int a = matcher.start()+attrIndex;
				int b = matcher.end()+attrIndex;

				String tmp = text.substring(a, b);
//				System.out.println("looking in "+tmp);

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

		if(hasContent) {
			pattern = Pattern.compile("</"+tagName+">", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
			matcher = pattern.matcher(text.substring(end));

			if(!matcher.find())
				throw new FragmentProcessException("Can't find end tag after content, tag is "+tagName);

			content = text.substring(end, matcher.start()+end);

//			System.out.println("complete content: "+content);
//			System.out.println("--");

			nextIndex = matcher.end()+end;

//				System.out.println("hasContent, nextIndex "+nextIndex+" len "+text.length());
//				System.out.println("the rest "+text.substring(nextIndex));
//				System.out.println("so content is "+content);
		} else
			nextIndex = end;

		hasNext = (nextIndex < text.trim().length());

//		System.out.println("tag "+tagName+" hasNext "+hasNext);
	}

	public String getName() {
		return tagName;
	}

	public boolean containsContent() {
		return hasContent;
	}

	public String getContent() {
		return content;
	}

	public Fragment getSubFragment() throws FragmentProcessException {
//		System.out.println("create sub-frag of "+tagName+" content "+content);
		if(textOnly)
			return null;
		else
			return new Fragment(content);
	}

	public Map getAttributes() {
		return attributes;
	}

	public boolean hasNext() {
		return hasNext;
	}

	public Fragment getNext() throws FragmentProcessException {
		return new Fragment(text.substring(nextIndex));
	}

	public boolean isDeclaration() {
		return declaration;
	}

	public boolean isCode() {
		return code;
	}

	public boolean isComment() {
		return comment;
	}

	public static Fragment getInitial(BufferedReader br) throws FragmentProcessException, IOException {
		StringBuffer sb = new StringBuffer();

		String line = null;
		while((line = br.readLine()) != null) {
			sb.append(line);
			if(line.charAt(line.length()-1)!='>')
				sb.append('\n');
		}
		br.close();

		String complete = sb.toString();

		return new Fragment(complete);
	}
}