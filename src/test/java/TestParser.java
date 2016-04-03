import lamo.json.parser.ast.*;
import lamo.json.parser.Lexer;
import lamo.json.parser.Parser;

import java.io.InputStreamReader;
import java.io.FileInputStream;

public class TestParser {

	public static void main(String[] args) {
		for (String fileName: args) {
			try {
				System.out.println("=== " + fileName + " ===");

				FileInputStream input = new FileInputStream(fileName);
                Lexer lexer = new Lexer(new InputStreamReader(input));
				Parser parser = new Parser(lexer);
				ValueNode root = parser.parse();
				if (root != null) {
					System.out.println(root.description());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
