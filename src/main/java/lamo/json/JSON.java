package lamo.json;

import lamo.json.parser.Parser;
import lamo.json.parser.ast.*;

import java.io.Reader;
import java.io.StringReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;

public class JSON {

    public static Object parse(Reader reader) throws Exception {
        Parser parser = new Parser(reader);
        ValueNode root = parser.parse();
        Object result = Mapper.map(root);
        return result;
    }

    public static Object parse(String json) throws Exception {
        return parse(new StringReader(json));
    }

    public static Object parse(File file) throws Exception {
        FileInputStream input = new FileInputStream(file);
        return parse(new InputStreamReader(input)); 
    }

}
