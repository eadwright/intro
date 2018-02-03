<%@ page errorPage="error.jsp"%>
<%@ taglib uri="/testLib" prefix="tl" %>
<html>
<head>
<title>Tag testing</title>
</head><body>
<h1>Tag testing</h1>
<h3>Tag1</h3>
<tl:Tag1>
test <%= b %><br>
</tl:Tag1>
<b>done</b><br>
<%= b %>
<hr>
<h3>Tag2</h3>
<tl:Tag2 count="6">
<br>fred
</tl:Tag2>
<br><b>done</b><br>
<%= b2 %>
<hr>
<h3>Tag3</h3>
<tl:Tag3>
test<br>
</tl:Tag3>
<b>done</b><br>
<%= t3 %>
<hr>
<h3>Tag4</h3>
<tl:Tag4>
test<br>
</tl:Tag4>
<b>done</b><br>
<%= t4 %>
<hr>
</body></html>