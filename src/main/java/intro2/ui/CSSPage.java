package intro2.ui;

public final class CSSPage implements lahaina.runtime.Page {

	public void CSSPage() {}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		out.write("/* Javadoc style sheet */\r\n");
		out.write("\r\n");
		out.write("/* Define colors, fonts and other style attributes here to override the defaults */\r\n");
		out.write("\r\n");
		out.write("/* Page background color */\r\n");
		out.write("body { background-color: #FFFFFF }\r\n");
		out.write("\r\n");
		out.write("/* Table colors */\r\n");
		out.write(".TableHeadingColor	   { background: #CCCCFF } /* Dark mauve */\r\n");
		out.write(".TableSubHeadingColor  { background: #EEEEFF } /* Light mauve */\r\n");
		out.write(".TableRowColor		   { background: #FFFFFF } /* White */\r\n");
		out.write("\r\n");
		out.write("/* Font used in left-hand frame lists */\r\n");
		out.write(".FrameTitleFont	  { font-size: 10pts; font-family: Helvetica, Arial, san-serif }\r\n");
		out.write(".FrameHeadingFont { font-size: 10pts; font-family: Helvetica, Arial, san-serif }\r\n");
		out.write(".FrameItemFont	  { font-size: 10pts; font-family: Helvetica, Arial, san-serif }\r\n");
		out.write("\r\n");
		out.write("/* Example of smaller, sans-serif font in frames */\r\n");
		out.write("/* .FrameItemFont  { font-size: 10pt; font-family: Helvetica, Arial, sans-serif } */\r\n");
		out.write("\r\n");
		out.write("/* Navigation bar fonts and colors */\r\n");
		out.write(".NavBarCell1	{ background-color:#EEEEFF;}/* Light mauve */\r\n");
		out.write(".NavBarCell1Rev { background-color:#00008B;}/* Dark Blue */\r\n");
		out.write(".NavBarFont1	{ font-family: Arial, Helvetica, sans-serif; color:#000000;}\r\n");
		out.write(".NavBarFont1Rev { font-family: Arial, Helvetica, sans-serif; color:#FFFFFF;}\r\n");
		out.write("\r\n");
		out.write(".NavBarCell2	{ font-family: Arial, Helvetica, sans-serif; background-color:#FFFFFF;}\r\n");
		out.write(".NavBarCell3	{ font-family: Arial, Helvetica, sans-serif; background-color:#FFFFFF;}\r\n");
		out.write("\r\n");
		out.write(".Logo	 { color: red; font-size: xx-large; font-family: Helvetica, Arial, san-serif } /* Large, red */\r\n");
		out.write(".RKey	 { color: red; font-family: Helvetica, Arial, san-serif }\r\n");
		out.write(".NKey	 { color: green; font-family: Helvetica, Arial, san-serif }\r\n");
		out.write(".RefKey	 { font-family: Helvetica, Arial, san-serif }");
	}
}
