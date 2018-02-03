package intro;

import intro.analysis.Database;
import intro.info2.ClassInfo2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Servlet1 extends HttpServlet {
	private static final String CONTENT_TYPE = "text/html";

	private Database db;

	/**Initialize global variables*/
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		db = LoadJarAnalysis.loadDatabase();
	}
	/**Process the HTTP Get request*/
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	/**Process the HTTP Post request*/
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);

		String queryString = request.getQueryString();

		if(queryString!=null) {
			int i=queryString.indexOf("pkg=");
			if(i!=-1) {
				doListClasses(request,response,queryString.substring(i+4));
				return;
			}

			i=queryString.indexOf("cl=");
			if(i!=-1) {
				doShowClass(request,response,queryString.substring(i+3));
				return;
			}
		}

		doListPackages(request,response);
	}

	private void doListPackages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Intro Servlet1 list packages</title></head>");
		out.println("<body>");
		out.println("<h3>Packages in project</h3><p><ul>");

		for(int i=0;i<db.packages.size();i++) {
			out.print("<li><a href=/intro/is?pkg=");
			out.print(db.packages.get(i));
			out.print(">");
			out.print(db.packages.get(i));
			out.println("</a>");
		}

		out.println("</ul>");
		out.println("</body></html>");
	}

	private void doListClasses(HttpServletRequest request, HttpServletResponse response, String packageName) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Intro Servlet1 list classes</title></head>");
		out.println("<body>");
		out.println("<a href=\"/intro/is\">home</a><br>");
		out.println("<h3>Classes in package "+packageName+"</h3><p><ul>");

		List classes = (List)db.packagesToClasses.get(packageName);
		ClassInfo2 cl2;

		for(int i=0;i<classes.size();i++) {
			cl2 = (ClassInfo2)classes.get(i);

			out.print("<li>");
			out.println(asRef(cl2));
		}

		out.println("</ul>");
		out.println("</body></html>");
	}

	private void doShowClass(HttpServletRequest request, HttpServletResponse response, String className) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Intro Servlet1 show class detail</title></head>");
		out.println("<body>");
		out.println("<a href=\"/intro/is\">home</a><br>");

		ClassInfo2 cl2 = (ClassInfo2)db.map2.get(className);

		String str1,str2,str3;
		if(cl2.isInteface()) {
			str1 = "interface";
			str2 = "Superinterface";
			str3 = "Extends";
		} else {
			str1 = "class";
			str2 = "Superclass";
			str3 = "Implements";
		}

		out.println("<h3> "+cl2.getAccessFlagsAsString()+" "+str1+" "+className+"</h3><p><ul>");

		out.print("<li>"+str2+": "); // superclass / interface
		if(cl2.superClass==null)
			out.println("none");
		else
			out.println(asRef(cl2.superClass));

		out.print("<li>"+str3+": "); // implements / extends
		if(cl2.interfaces.length==0)
			out.println("none");
		else {
			out.println("<ul>");
			for(int i=0;i<cl2.interfaces.length;i++)
				out.println("<li>"+asRef(cl2.interfaces[i]));
			out.println("</ul>");
		}

		ClassInfo2.Reference ref;
		if(!cl2.isInteface()) {
			out.println("<li>Created by: ");
			if(cl2.lists.createdBy.size()==0)
				out.println("none");
			else {
				out.println("<ul>");
				for(int i=0;i<cl2.lists.createdBy.size();i++) {
					ref = (ClassInfo2.Reference)cl2.lists.createdBy.get(i);
					out.print("<li> Class ");
					out.print(asRef(ref.sourceClass));
					out.print(" method ");
					out.print(ref.sourceMethod.getSignature());
				}
				out.println("</ul>");
			}
		}

		if(cl2.isInteface()) {
			out.println("<li>Directly implemented by: ");
			if(cl2.lists.implementedBy.size()==0)
				out.println("none");
			else {
				out.println("<ul>");
				for(int i=0;i<cl2.lists.implementedBy.size();i++) {
					out.print("<li> ");
					out.print(asRef((String)cl2.lists.implementedBy.get(i)));
				}
				out.println("</ul>");
			}
		}

		out.println("<li>Sub"+str1+": "); // subinterfaces etc
		if(cl2.lists.subClasses.size()==0)
			out.println("none");
		else {
			out.println("<ul>");
			for(int i=0;i<cl2.lists.subClasses.size();i++)
				out.println("<li>"+asRef((String)cl2.lists.subClasses.get(i)));
			out.println("</ul>");
		}

		out.println("<li>Class referenced by: ");
		if(cl2.lists.usedBy.size()==0)
			out.println("none");
		else {
			out.println("<ul>");
			for(int i=0;i<cl2.lists.usedBy.size();i++) {
				ref = (ClassInfo2.Reference)cl2.lists.usedBy.get(i);
				out.print("<li> Class ");
				out.print(asRef(ref.sourceClass));
				out.print(" method ");
				out.print(ref.sourceMethod.getSignature());
			}
			out.println("</ul>");
		}

		out.println("<li>Superclass fields resolved via this class: ");
		if(cl2.lists.superClassFieldUsage.size()==0)
			out.println("none");
		else {
			out.println("<ul>");
			for(int i=0;i<cl2.lists.superClassFieldUsage.size();i++) {
				ref = (ClassInfo2.Reference)cl2.lists.superClassFieldUsage.get(i);
				out.print("<li> Class ");
				out.print(asRef(ref.sourceClass));
				out.print(" method ");
				out.print(ref.sourceMethod.getSignature());
				out.print(" accesses superclass ");
				out.println(asRef(ref.actualDestinationClass));
				out.print(" field ");
				out.println(ref.destinationField.getSignature());
			}
			out.println("</ul>");
		}

		out.println("<li>Superclass methods resolved via this class: ");
		if(cl2.lists.superClassMethodCalls.size()==0)
			out.println("none");
		else {
			out.println("<ul>");
			for(int i=0;i<cl2.lists.superClassMethodCalls.size();i++) {
				ref = (ClassInfo2.Reference)cl2.lists.superClassMethodCalls.get(i);
				out.print("<li> Class ");
				out.print(asRef(ref.sourceClass));
				out.print(" method ");
				out.print(ref.sourceMethod.getSignature());
				out.print(" accesses superclass ");
				out.println(asRef(ref.actualDestinationClass));
				out.print(" method ");
				out.println(ref.destinationMethod.getSignature());
			}
			out.println("</ul>");
		}

		if(cl2.fields.length==0)
			out.println("<li>Fields: none");
		else {
			out.println("<li>Fields:<ul>");
			for(int i=0;i<cl2.fields.length;i++) {
				out.print("<li>");
				out.println(cl2.fields[i].toString());
			}
			out.println("</ul>");
		}

		if(cl2.methods.length==0)
			out.println("<li>Methods: none");
		else {
			out.println("<li>Methods:<ul>");
			for(int i=0;i<cl2.methods.length;i++) {
				out.print("<li>");
				out.println(cl2.methods[i].toString());
			}
			out.println("</ul>");
		}


		out.println("</ul>");
		out.println("</body></html>");
	}


	public static String asRef(ClassInfo2 cl2) {
		return asRef(cl2.thisClass);
	}

	public static String asRef(String className) {
		StringBuffer buf = new StringBuffer("<a href=/intro/is?cl=");
		buf.append(className);
		buf.append(">");
		buf.append(className);
		buf.append("</a>");
		return buf.toString();
	}
}