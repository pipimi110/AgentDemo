package top.popko.agentdemo.util;

public class StringUtils {
    public StringUtils() {
    }

    public static boolean match(String source, String target) {
        return fullMatch(source, target);
    }

    public static int[] convertStringToIntArray(String string) {
        if (string.startsWith("P")) {
            string = string.substring(1);
        }

        String[] positions = string.split(",");
        int[] intPositions = new int[positions.length];
        int index = 0;
        String[] var4 = positions;
        int var5 = positions.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String pos = var4[var6];
            intPositions[index++] = Integer.parseInt(pos) - 1;
        }

        return intPositions;
    }

    private static boolean fullMatch(String source, String target) {
        return source == target;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean endsWithAny(String str, String[] DISABLE_EXT) {
        for(String ext : DISABLE_EXT){
            if(str.endsWith(ext)){
                return true;
            }
        }
        return false;
    }

    public static String normalize(String str, int maxLength) {
        int max = Math.max(maxLength, 5);
        if (str != null && str.length() != 0 && str.length() > max) {
            int middle = (max - 3) / 2;
            StringBuilder sb = new StringBuilder();
            sb.append(str, 0, 1 - max % 2 + middle);
            sb.append("...");
            sb.append(str, str.length() - middle, str.length());
            return sb.toString();
        } else {
            return str;
        }
    }
}
