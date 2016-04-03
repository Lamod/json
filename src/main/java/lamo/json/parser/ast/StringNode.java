package lamo.json.parser.ast;

import lamo.json.parser.token.*;

public class StringNode extends ValueNode {

    private Token token;

    public StringNode(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public String toString() {
        return "<String: " + token.getText() + ">";
    }

}
