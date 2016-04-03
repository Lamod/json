package lamo.json;

import lamo.json.parser.ast.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Mapper {

    public static Object map(ValueNode n) {
        return mapValue(n);
    }

    private static Object mapValue(ValueNode n) {
        if (n == null) {
            return null;
        }

        if (n instanceof ObjectNode) {
            return mapObject((ObjectNode) n);
        } else if (n instanceof ArrayNode) {
            return mapArray((ArrayNode) n);
        } else if (n instanceof NumberNode) {
            return mapNumber((NumberNode) n);
        } else if (n instanceof StringNode) {
            return mapString((StringNode) n);
        } else if (n instanceof TrueNode) {
            return true;
        } else if (n instanceof FalseNode) {
            return false;
        } else if (n instanceof NullNode) {
            return null;
        }

        return null;
    }

    private static Map<String, Object> mapObject(ObjectNode n) {
        List<PairNode> ps = n.getPairs();
        int size = ps.size();

        Map<String, Object> map = new HashMap<String, Object>(size);
        for (PairNode pair : ps) {
            map.put(mapString(pair.getKey()), mapValue(pair.getValue()));
        }

        return map;
    }

    private static List<Object> mapArray(ArrayNode n) {
        List<ValueNode> es = n.getElements();
        int size = es.size();

        List<Object> arr = new ArrayList<Object>(size);
        for (int i = 0; i < size; ++i) {
            arr.add(mapValue(es.get(i)));
        }

        return arr;
    }

    private static Number mapNumber(NumberNode n) {
        return Double.parseDouble(n.getToken().getText());
    }

    private static String mapString(StringNode n) {
        return n.getToken().getText();
    }

}
