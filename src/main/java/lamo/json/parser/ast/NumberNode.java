package lamo.json.parser.ast;

import lamo.json.parser.token.*;

public class NumberNode extends ValueNode {

    private Token token;

    public NumberNode(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public String toString() {
        return "<Number: " + token.getText() + ">";
    }

}
