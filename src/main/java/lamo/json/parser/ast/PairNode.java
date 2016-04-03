package lamo.json.parser.ast;

public class PairNode extends ValueNode {

    private StringNode key;
    private ValueNode value;

    public PairNode(StringNode key, ValueNode value) {
        this.key = key;
        this.value = value;
    }

    public StringNode getKey() {
        return key;
    }

    public ValueNode getValue() {
        return value;
    }

    public String toString() {
        return "<Pair: " + key + " => " + value + ">";
    }

}
