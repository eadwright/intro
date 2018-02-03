<%@ include file="header_eng.jsp"%>
<h3>List Projects</h3>
<br>
<il:listProj>
<il:link cmd="2" project="<%= project %>" name="root"><%= desc %></il:link>
</il:listProj>
<%@ include file="footer_eng.html"%>