import lamo.json.JSON;

import java.io.File;

public class TestJSON {

    public static void main(String[] args) {
        try {
            JSON.parse("{\"key\": 110 }");
        } catch (Exception e) {
            e.printStackTrace();
        }

		for (String fileName: args) {
			try {
				System.out.println("=== " + fileName + " ===");

                Object obj = JSON.parse(new File(fileName));
                System.out.println(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

}
