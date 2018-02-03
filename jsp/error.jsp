<%@ page isErrorPage="true" %>
<HTML>
<BODY>
<H1>Error</H1>
<BR>An error occured. Error Message is: <%= exception.getMessage() %><BR>
Stack Trace is : <PRE><FONT COLOR="RED"><%
java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
java.io.PrintWriter pw = new java.io.PrintWriter(cw,true);
exception.printStackTrace(pw);

if(exception instanceof javax.servlet.jsp.JspException) {
	Throwable cause = ((javax.servlet.jsp.JspException)exception).getRootCause();
	if(cause != null) {
		pw.println("Cause:");
		cause.printStackTrace(pw);

		Throwable cause2 = cause.getCause();

		if(cause2 != null) {
			pw.println("Nested cause:");
			cause2.printStackTrace(pw);
		}
	}
}

out.println(cw.toString());
%></FONT></PRE>
<BR></BODY>
</HTML>
