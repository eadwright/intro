package intro;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class Tag2 extends BodyTagSupport {
	private int count;
	private int soFar;

	public void setCount(int n) {
		System.out.println("doing setCount "+n);
		count = n;
		soFar = 0;
	}

	public void doInitBody() throws JspException {
		pageContext.setAttribute("b",Integer.toString(soFar));
//		str = ""+bodyContent.getString();

//		System.out.println("doInitBody "+str);
	}

	public int doAfterBody() throws JspException {

		System.out.println("doAfterBody");

		try {
			String z = bodyContent.getString()+" "+soFar;
			bodyContent.clearBody();
			bodyContent.getEnclosingWriter().println(z);
		} catch (IOException e) {
			throw new JspException(e);
		}

		if(soFar++ < count) {
			pageContext.setAttribute("b2",Integer.toString(soFar));
			System.out.println("again");
			return EVAL_BODY_AGAIN;
		} else
			return SKIP_BODY;
	}

	public int doStartTag() throws JspException {
		System.out.println("doing start tag "+soFar);
		pageContext.setAttribute("b2","start");
		return EVAL_BODY_BUFFERED;
//		return EVAL_BODY_INCLUDE;
}

	public int doEndTag() throws JspException {
		System.out.println("doing end tag");
		pageContext.setAttribute("b2","end");
		return EVAL_PAGE;
	}
}