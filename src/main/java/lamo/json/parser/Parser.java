package lamo.json.parser;

import lamo.json.parser.ast.*;
import lamo.json.parser.token.*;

import java.io.Reader;
import java.util.List;
import java.util.ArrayList;

public class Parser {

    private Lexer lexer;
    private Token lookahead;

    private ValueNode root, current;

    public Parser(Reader reader) {
        this(new Lexer(reader));
    }

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        consume();
    }

    public void consume() {
        try {
            lookahead = lexer.nextToken();
        } catch (Exception e) {
            e.printStackTrace();
            lookahead = null;
        }
    }

    public Token ll() {
        return lookahead;
    }

    public Token match(TokenType x) throws Exception {
        final Token current = tryMatch(x);
        if (current == null) {
            throw new Exception("Not match token: " + x);
        }

        return current;
    }

    public Token tryMatch(TokenType x) {
        if (is(x)) {
            final Token current = ll();
            consume();
            return current;
        } else {
            return null;
        }
    }

    public boolean is(TokenType x) {
        return ll() != null && ll().getType() == x;
    }

    public ValueNode parse() throws Exception {
        ValueNode root = parseValue();

        if (!is(Lexer.TokenTypes.EOF)) {
            System.out.print("warning: reduntant tokens: ");

            do { 
                System.out.print(ll());
                consume();
            } while (!is(Lexer.TokenTypes.EOF));

            System.out.println();
        }

        return root;
    }

    private ValueNode parseValue() throws Exception {
        if (is(Lexer.TokenTypes.LPARENT)) {
            return parseObject();
        } else if (is(Lexer.TokenTypes.LBRACE)) {
            return parseArray();
        } else if (is(Lexer.TokenTypes.STRING)) {
            return parseString();
        } else if (is(Lexer.TokenTypes.NUMBER)) {
            return parseNumber();
        } else if (is(Lexer.TokenTypes.TRUE)) {
            return parseTrue();
        } else if (is(Lexer.TokenTypes.FALSE)) {
            return parseFalse();
        } else if (is(Lexer.TokenTypes.NULL)) {
            return parseNull();
        } else if (is(Lexer.TokenTypes.EOF)) {
            return null;
        } else {
            throw new Exception("Error Token: " + ll());
        }
    }

    private ObjectNode parseObject() throws Exception {
        final ObjectNode obj = new ObjectNode();

        match(Lexer.TokenTypes.LPARENT);

        obj.setPairs(parsePairs());

        match(Lexer.TokenTypes.RPARENT);

        return obj;
    }

    private ArrayList<PairNode> parsePairs() throws Exception {
        ArrayList<PairNode> nodes = new ArrayList<PairNode>();
        while (!is(Lexer.TokenTypes.RPARENT)) {
            if (tryMatch(Lexer.TokenTypes.COMMA) != null) {
                continue;
            }

            nodes.add(parsePair());
        }

        return nodes;
    }

    private PairNode parsePair() throws Exception {
        final StringNode key = new StringNode(match(Lexer.TokenTypes.STRING));
        match(Lexer.TokenTypes.COLON);
        final ValueNode value = parseValue();

        return new PairNode(key, value);
    }

    private ArrayNode parseArray() throws Exception {
        final ArrayNode arr = new ArrayNode();

        match(Lexer.TokenTypes.LBRACE);

        arr.setElements(parseElements());

        match(Lexer.TokenTypes.RBRACE);

        return arr;
    }

    private ArrayList<ValueNode> parseElements() throws Exception {
        ArrayList<ValueNode> nodes = new ArrayList<ValueNode>();
        while (!is(Lexer.TokenTypes.RBRACE)) {
            if (tryMatch(Lexer.TokenTypes.COMMA) != null) {
                continue;
            }

            nodes.add(parseValue());
        }

        return nodes;
    }

    private StringNode parseString() throws Exception {
        return new StringNode(match(Lexer.TokenTypes.STRING));
    }

    private NumberNode parseNumber() throws Exception {
        return new NumberNode(match(Lexer.TokenTypes.NUMBER));
    }

    private TrueNode parseTrue() throws Exception {
        match(Lexer.TokenTypes.TRUE);
        return new TrueNode();
    }

    private FalseNode parseFalse() throws Exception {
        match(Lexer.TokenTypes.FALSE);
        return new FalseNode();
    }

    private NullNode parseNull() throws Exception {
        match(Lexer.TokenTypes.NULL);
        return new NullNode();
    }

}
