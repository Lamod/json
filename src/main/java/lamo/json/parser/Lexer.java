package lamo.json.parser;

import lamo.json.parser.token.*;

import java.io.Reader;
import java.io.IOException;

public class Lexer {

    public final static char EOF_CHAR = (char)-1;

    public enum TokenTypes implements TokenType {
        EOF("EOF", -1),
        UNKNOWN("UNKNOWN", 0),

        LPARENT("LPARENT", 1),
        RPARENT("RPARENT", 2),
        LBRACE("LBRACE", 3),
        RBRACE("RBRACE", 4),
        COMMA("COMMA", 5),
        EQUALS("EQUALS", 6),
        COLON("COLON", 7),

        STRING("STRING", 8),
        NUMBER("NUMBER", 9),
        TRUE("TRUE", 10),
        FALSE("FALSE", 11),
        NULL("NULL", 12);

        private String name;
        private int value;

        private TokenTypes(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

    }

    public interface Matcher {

        public boolean match(char a);

    }

    private Reader reader;
    private int p = -1;
    private char c = EOF_CHAR;

    public Lexer(Reader reader) {
        this.reader = reader;
        consume();
    }

    public char consume() {
        ++p;
        char pre = c;
        try {
            c = (char)reader.read();
        } catch(IOException e) {
            c = EOF_CHAR;
        }

        return pre;
    }

    public char match(char x) throws Exception {
        if (x == c) {
            return consume();
        } else {
            throw new Exception("Not matched " + x + "(" + c + ")");
        }
    }

    public String match(String x) throws Exception {
        for (int i = 0; i < x.length(); ++i) {
            match(x.charAt(i));
        }

        return x;
    }

    public char[] match(Matcher matcher, int count) throws Exception {
        char[] chars = new char[count];
        for (int i = 0; i < count; ++i) {
            if (matcher.match(c)) {
                chars[i] = c;
                consume();
            } else {
                throw new Exception("Not matched " + c);
            }
        }

        return chars;
    }

    public Token nextToken() throws Exception {
        while (c != EOF_CHAR) {
            switch (c) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    consume();
                    continue;
                case '{':
                    consume();
                    return new Token(TokenTypes.LPARENT, "{");
                case '}':
                    consume();
                    return new Token(TokenTypes.RPARENT, "}");
                case '[':
                    consume();
                    return new Token(TokenTypes.LBRACE, "[");
                case ']':
                    consume();
                    return new Token(TokenTypes.RBRACE, "]");
                case ',':
                    consume();
                    return new Token(TokenTypes.COMMA, ",");
                case '=':
                    consume();
                    return new Token(TokenTypes.EQUALS, "=");
                case ':':
                    consume();
                    return new Token(TokenTypes.COLON, ":");
                case '"':
                    return STRING();
                case 't':
                    return TRUE();
                case 'f':
                    return FALSE();
                case 'n':
                    return NULL();
                default:
                    if (c == '-' || isDigit()) {
                        return NUMBER();
                    }

                    throw new Exception("Error Character: " + (short)c);
            }
        }

        return new Token(TokenTypes.EOF, "<EOF>");
    }

    private Token STRING() throws Exception {
        match('"');

        String chars = CHARS();

        match('"');
        return new Token(TokenTypes.STRING, chars);
    } 

    private String CHARS() throws Exception {
        StringBuffer buf = new StringBuffer();
        while (c != '"') {
            if (c == '\\') {
                consume();
                switch (c) {
                    case '"':
                        consume();
                        buf.append('"');
                        break;
                    case '\\':
                        consume();
                        buf.append('\\');
                        break;
                    case '/':
                        consume();
                        buf.append('/');
                        break;
                    case 'b':
                        consume();
                        buf.append('\b');
                        break;
                    case 'f':
                        consume();
                        buf.append('\f');
                        break;
                    case 't':
                        consume();
                        buf.append('\t');
                        break;
                    case 'n':
                        consume();
                        buf.append('\n');
                        break;
                    case 'r':
                        consume();
                        buf.append('\r');
                        break;
                    case 'u':
                        consume();
                        buf.append(UNICODE());
                        break;
                    default:
                        throw new Exception("Error Character in string: " + c);
                }
            } else {
                buf.append(c);
                consume();
            }
        }

        return buf.toString();
    }

    private char UNICODE() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append("0x");
        buf.append(match(this::isHexDigit, 4));

        String hex = buf.toString();
        int value = Integer.decode(hex);
        return (char)value;
    }

    private Token NUMBER() throws Exception {
        String ns = INT();
        if (c == '.') {
            ns += FRAC();
            if (c == 'e' || c == 'E') {
                ns += EXP();
            }
        } else if (c == 'e' || c == 'E') {
            ns += EXP();
        }

        return new Token(TokenTypes.NUMBER, ns);
    }

    private String INT() throws Exception {
        StringBuffer buffer = new StringBuffer();
        if (c == '-') {
            buffer.append(consume());
        }
        if (c == '0') {
            buffer.append(consume());
        } else {
            buffer.append(DIGITS());
        }
        
        return buffer.toString();
    }

    private String EXP() throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(match((x) -> x == 'e' || x == 'E', 1));
        if (c == '+' || c == '-') {
            buffer.append(consume());
        }
        buffer.append(DIGITS());

        return buffer.toString();
    }

    private String FRAC() throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(match('.'));
        buffer.append(DIGITS());

        return buffer.toString();
    }

    private String DIGITS() throws Exception {
        StringBuffer buffer = new StringBuffer();
        do {
            buffer.append(match(this::isDigit, 1));
        } while(isDigit());

        return buffer.toString();
    }

    private Token TRUE() throws Exception {
        return new Token(TokenTypes.TRUE, match("true"));
    }

    private Token FALSE() throws Exception {
        return new Token(TokenTypes.FALSE, match("false"));
    }

    private Token NULL() throws Exception {
        return new Token(TokenTypes.NULL, match("null"));
    }

    private boolean isDigit() {
        return isDigit(c);
    }

    private boolean isDigit(char x) {
        return x >= '0' && c <= '9';
    }

    private boolean isHexDigit() {
        return isHexDigit(c);
    }

    private boolean isHexDigit(char x) {
        return (x >= '0' && x <= '9') ||
            (x >= 'a' && x <= 'f') ||
            (x >= 'A' && x <= 'F');
    }

}
