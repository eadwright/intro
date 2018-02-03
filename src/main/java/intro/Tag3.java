package intro;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class Tag3 implements Tag {
	private Tag parent;
	private PageContext pageContext;

	public void setParent(Tag p) {
		parent = p;
	}

	public Tag getParent() {
		return parent;
	}

	public void release() {
		parent = null;
		pageContext = null;
	}

	public void setPageContext(PageContext pc) {
		pageContext = pc;
	}

	public int doStartTag() throws JspException {
		System.out.println("doing start tag");
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		System.out.println("doing end tag");
		pageContext.setAttribute("t3","end");
		return EVAL_PAGE;
	}
}