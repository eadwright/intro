<%@ include file="header_eng.jsp"%>
<h3>Show Module <%= request.getParameter("name") %>: <il:moduleDesc/></h3>
<br>
<h5>Sub-Modules</h5>
<il:listSubMod none = "None">
<il:link cmd="2" name="<%= name %>"><%= name %></il:link>: <%= desc %>
</il:listSubMod><%@ include file="footer_eng.html"%>
