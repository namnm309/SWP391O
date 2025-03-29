package com.example.SpringBootTurialVip.enums;

public enum AgeGroup {
    AGE_0_3("0-3 tháng", 0, 3),
    AGE_4_6("4-6 tháng", 4, 6),
    AGE_7_12("7-12 tháng", 7, 12),
    AGE_13_24("13-24 tháng", 13, 24),
    AGE_25_PLUS("25 tháng trở lên", 25, Integer.MAX_VALUE);

    private final String label;
    private final int minMonth;
    private final int maxMonth;

    AgeGroup(String label, int minMonth, int maxMonth) {
        this.label = label;
        this.minMonth = minMonth;
        this.maxMonth = maxMonth;
    }

    public String getLabel() { return label; }
    public int getMinMonth() { return minMonth; }
    public int getMaxMonth() { return maxMonth; }

    public static AgeGroup fromRange(int minAge, int maxAge) {
        for (AgeGroup group : values()) {
            if (minAge >= group.minMonth && maxAge <= group.maxMonth) {
                return group;
            }
        }
        return AGE_25_PLUS;
    }
}