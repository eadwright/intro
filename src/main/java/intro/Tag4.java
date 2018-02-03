package intro;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;

public class Tag4 implements IterationTag {
	private Tag parent;
	private PageContext pageContext;
	int loop;

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
		loop = 3;
	}

	public int doStartTag() throws JspException {
		System.out.println("doing start tag");
		return EVAL_BODY_INCLUDE;
	}

	public int doAfterBody() throws JspException {
		pageContext.setAttribute("t4",Integer.toString(loop));
		loop--;

		if(loop==0)
			return SKIP_BODY;
		else
			return EVAL_BODY_AGAIN;
	}

	public int doEndTag() throws JspException {
		System.out.println("doing end tag");
		pageContext.setAttribute("t4","end");
		return EVAL_PAGE;
	}
}