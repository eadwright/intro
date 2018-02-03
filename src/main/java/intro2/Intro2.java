package intro2;

import intro2.config.Project;
import intro2.database.Engine;
import intro2.output.Output;

//import intro2.config.Project.InvalidConfigDataException;

public class Intro2 {
	public static void main(String[] argv) {
		if(argv.length != 1) {
			System.out.println("invalid arguments, require path to XML config file");
			return;
		}

		try {
			Project project = new Project(argv[0]);
			Engine.getDatabase(project);

			Output.writeHtml();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}