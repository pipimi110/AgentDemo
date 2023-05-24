package top.popko.agentdemo.handler.hookpoint.models.policy;

import java.util.HashSet;
import java.util.Set;


public class TaintPosition {
    public static final String OBJECT = "O";
    public static final String RETURN = "R";
    public static final String PARAM_PREFIX = "P";
    public static final String OR = "\\|";
    public static final TaintPosition POS_OBJECT = new TaintPosition("O");
    public static final TaintPosition POS_RETURN = new TaintPosition("R");

    public static final String ERR_POSITION_EMPTY = "taint position can not empty";

    public static final String ERR_POSITION_INVALID = "taint position invalid";

    public static final String ERR_POSITION_PARAMETER_INDEX_INVALID = "taint position parameter index invalid";

    private final String value;

    private final int parameterIndex;

    public TaintPosition(String value) {
        if (value == null) {
            throw new IllegalArgumentException("taint position can not empty");
        }
        value = value.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("taint position can not empty");
        }

        int index = -1;
        boolean isParameter = value.startsWith("P");
        if (isParameter) {
            String idx = value.substring(1).trim();
            try {
                index = Integer.parseInt(idx) - 1;
                if (index < 0) {
                    throw new NumberFormatException("position index can not be negative: " + String.valueOf(index));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("taint position parameter index invalid: " + value + ", " + e.getMessage());
            }
            this.value = "P" + idx;
        } else {
            if (!"O".equals(value) && !"R".equals(value)) {
                throw new IllegalArgumentException("taint position invalid: " + value);
            }
            this.value = value;
        }

        this.parameterIndex = index;
    }

    public boolean isObject() {
        return equals(POS_OBJECT);
    }


    public boolean isReturn() {
        return equals(POS_RETURN);
    }

    public boolean isParameter() {
        return (this.parameterIndex >= 0);
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }

    public static Set<TaintPosition> parse(String position) throws TaintPositionException {
        if (position == null || position.isEmpty()) {
            throw new TaintPositionException("taint position can not be empty");
        }
        return parse(position.split("\\|"));
    }

    private static Set<TaintPosition> parse(String[] positions) throws TaintPositionException {
        if (positions == null || positions.length == 0) {
            throw new TaintPositionException("taint positions can not be empty");
        }
        Set<TaintPosition> tps = new HashSet<>();
        for (String position : positions) {
            position = position.trim();
            if (position.startsWith("P")) {
                Set<TaintPosition> paramPos = parseParameter(position.substring(1));
                if (!paramPos.isEmpty()) {
                    tps.addAll(paramPos);
                }
            } else if (!position.isEmpty()) {


                tps.add(parseOne(position));
            }
        }

        return tps;
    }

    private static Set<TaintPosition> parseParameter(String indiesStr) throws TaintPositionException {
        String[] indies = indiesStr.split(",");
        Set<TaintPosition> tps = new HashSet<>();
        for (String index : indies) {
            tps.add(parseOne("P" + index));
        }
        return tps;
    }

    private static TaintPosition parseOne(String position) throws TaintPositionException {
        try {
            return new TaintPosition(position);
        } catch (IllegalArgumentException e) {
            throw new TaintPositionException(e.getMessage(), e.getCause());
        }
    }

    public static boolean hasObject(Set<TaintPosition> positions) {
        if (positions == null) {
            return false;
        }
        return positions.contains(POS_OBJECT);
    }

    public static boolean hasReturn(Set<TaintPosition> positions) {
        if (positions == null) {
            return false;
        }
        return positions.contains(POS_RETURN);
    }


    public static boolean hasParameter(Set<TaintPosition> positions) {
        if (positions == null) {
            return false;
        }
        for (TaintPosition position : positions) {
            if (position.isParameter()) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasParameterIndex(Set<TaintPosition> positions, int index) {
        if (positions == null || index < 0) {
            return false;
        }
        for (TaintPosition position : positions) {
            if (position.getParameterIndex() == index) {
                return true;
            }
        }

        return false;
    }


    public String toString() {
        return this.value;
    }


    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof TaintPosition)) {
            return false;
        }

        TaintPosition position = (TaintPosition) obj;
        return this.value.equals(position.value);
    }


    public int hashCode() {
        return this.value.hashCode();
    }
}

