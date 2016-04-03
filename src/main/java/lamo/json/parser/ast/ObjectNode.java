package lamo.json.parser.ast;

import java.util.List;
import java.util.ArrayList;

public class ObjectNode extends ValueNode {

    private List<PairNode> pairs;

    public ObjectNode() {
        this.pairs = new ArrayList<PairNode>();
    }

    public List<PairNode> getPairs() {
        return pairs;
    }

    public void setPairs(List<PairNode> pairs) {
        this.pairs = pairs;
    }

    public int append(PairNode pair) {
        pairs.add(pair);
        return pairs.size() - 1;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<Object: ");
        for (PairNode n : pairs) {
            buf.append(n);
        }
        buf.append(">");

        return buf.toString();
    }

    public String description(int level) {
        StringBuffer buf = new StringBuffer();
        buf.append(descriptionLine(level, "<Object: {\n"));
        for (int i = 0; i < pairs.size(); ++i) {
            PairNode n = pairs.get(i);
            buf.append(n.getKey().description(level + 1));
            buf.append(" =\n");
            buf.append(n.getValue().description(level + 2));
            buf.append('\n');
        }
        buf.append("}>");

        return buf.toString();
    }

}
