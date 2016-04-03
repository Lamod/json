package lamo.json.parser.ast;

import java.util.List;
import java.util.ArrayList;

public class ArrayNode extends ValueNode {

    private List<ValueNode> elements;

    public ArrayNode() {
        elements = new ArrayList<ValueNode>();
    }

    public void setElements(List<ValueNode> elements) {
        this.elements = elements;
    }

    public List<ValueNode> getElements() {
        return elements;
    }

    public int append(ValueNode e) {
        elements.add(e);
        return elements.size() - 1;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<Array: ");
        for (ValueNode n : elements) {
            buf.append(n);
        }
        buf.append(">");

        return buf.toString();
    }

    public String description(int level) {
        StringBuffer buf = new StringBuffer();
        buf.append(descriptionLine(level, "<Array: [\n"));
        for (int i = 0; i < elements.size(); ++i) {
            buf.append(elements.get(i).description(level + 1));
            buf.append('\n');
        }
        buf.append(descriptionLine(level, "]>"));

        return buf.toString();
    }

}
