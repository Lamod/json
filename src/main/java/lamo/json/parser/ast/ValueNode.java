package lamo.json.parser.ast;

import java.util.List;

public abstract class ValueNode {

	public static String descriptionLine(int level, String desc) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < level; ++i) {
			buf.append(' ');
		}
		buf.append(desc);

		return buf.toString();
	}

	public String description() {
		return description(0);
	}

	public String description(int level) {
		return descriptionLine(level, toString());
	}

}
