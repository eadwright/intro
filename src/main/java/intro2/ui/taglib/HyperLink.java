package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

public class HyperLink implements NonEmptyTag {
	public final static String HREF = "href";

	private State state;
	private boolean part1 = true;
	private boolean doingLink;
	private String href;

	public HyperLink(State state) {
		this.state = state;
	}

	protected boolean showNonLinks() {
		return true;
	}

	public boolean process(Writer out) throws java.io.IOException {
		if(part1) {
			href = state.getStringAttribute(HREF);
			if((href !=null) && (href.indexOf(".htm")==-1))
				href = state.getStringAttribute(href);

			doingLink = (href != null);

			if(doingLink) {
				out.write("<A HREF=\"");
				if(href.indexOf('#') != 0)
					Dots.printDots(out, state);
				out.write(href);
				out.write("\">");
			} else
				part1 = showNonLinks();
		} else {
			if(doingLink)
				out.write("</A>");
		}

		boolean result = part1;
		part1 = false;
		return result;
	}
}