import lamo.json.parser.token.*;
import lamo.json.parser.Lexer;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class TestLexer {

	public static void main(String[] args) throws Exception {
		for (String fileName: args) {
			try {
				System.out.println("=== " + fileName + " ===");

				FileInputStream input = new FileInputStream(fileName);
				Lexer lexer = new Lexer(new InputStreamReader(input));
				for (Token t = lexer.nextToken();
						t.getType() != Lexer.TokenTypes.EOF;
						t = lexer.nextToken()) {

					System.out.println(t);
				}
			} catch(IOException e) {
				System.out.println(e);
			}
		}
	}

}
