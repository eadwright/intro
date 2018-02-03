package intro;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class Tag1 extends TagSupport {
	private int soFar;

	public void setPageContext(PageContext pc) {
		super.setPageContext(pc);
		soFar = 0;
	}

	public int doAfterBody() throws JspException {
		System.out.println("doAfterBody");

		if(soFar++ < 3) {
			pageContext.setAttribute("b",Integer.toString(soFar));
			System.out.println("again");
			return EVAL_BODY_AGAIN;
		} else
			return SKIP_BODY;
	}

	public int doStartTag() throws JspException {
		System.out.println("doing start tag "+soFar);
		pageContext.setAttribute("b","start");
//		return EVAL_BODY_BUFFERED;
		return EVAL_BODY_INCLUDE;
}

	public int doEndTag() throws JspException {
		System.out.println("doing end tag");
		pageContext.setAttribute("b","end");
		return EVAL_PAGE;
	}
}