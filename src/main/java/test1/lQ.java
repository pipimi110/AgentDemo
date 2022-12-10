package test1;

public enum lQ {
    RF(0, (String) null),
    Ql(1, "qwe"),
    yO(2,"yo"),
    PW(3,"pw");
    private final int ea;
    private final String qD;

    private lQ(int var3, String var4) {
        if (var3 < 0) {
            throw new IllegalArgumentException();
        } else {
            this.ea = var3;
            this.qD = var4;
        }
    }

    @Override
    public String toString() {
        return "lQ{" +
                "ea=" + ea +
                ", qD='" + qD + '\'' +
                '}';
    }
}