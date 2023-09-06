package com.mycompany.myapp.domain.enumeration;

public enum TimeSlotValue {
    ONE_AM("1:00 AM", 1),
    HALF_PAST_ONE_AM("1:30 AM", 2),
    TWO_AM("2:00 AM", 3),
    HALF_PAST_TWO_AM("2:30 AM", 4),
    THREE_AM("3:00 AM", 5),
    HALF_PAST_THREE_AM("3:30 AM", 6),
    FOUR_AM("4:00 AM", 7),
    HALF_PAST_FOUR_AM("4:30 AM", 8),
    FIVE_AM("5:00 AM", 9),
    HALF_PAST_FIVE_AM("5:30 AM", 10),
    SIX_AM("6:00 AM", 11),
    HALF_PAST_SIX_AM("6:30 AM", 12),
    SEVEN_AM("7:00 AM", 13),
    HALF_PAST_SEVEN_AM("7:30 AM", 14),
    EIGHT_AM("8:00 AM", 15),
    HALF_PAST_EIGHT_AM("8:30 AM", 16),
    NINE_AM("9:00 AM", 17),
    HALF_PAST_NINE_AM("9:30 AM", 18),
    TEN_AM("10:00 AM", 19),
    HALF_PAST_TEN_AM("10:30 AM", 20),
    ELEVENT_AM("11:00 AM", 21),
    HALF_PAST_ELEVENT_AM("11:30 AM", 22),
    TWELVE_AM("12:00 AM", 23),
    HALF_PAST_TWELVE_AM("12:30 AM", 24),

    ONE_PM("1:00 PM", 25),
    HALF_PAST_ONE_PM("1:30 PM", 26),
    TWO_PM("2:00 PM", 27),
    HALF_PAST_TWO_PM("2:30 PM", 28),
    THREE_PM("3:00 PM", 29),
    HALF_PAST_THREE_PM("3:30 PM", 30),
    FOUR_PM("4:00 PM", 31),
    HALF_PAST_FOUR_PM("4:30 PM", 32),
    FIVE_PM("5:00 PM", 33),
    HALF_PAST_FIVE_PM("5:30 PM", 34),
    SIX_PM("6:00 PM", 35),
    HALF_PAST_SIX_PM("6:30 PM", 36),
    SEVEN_PM("7:00 PM", 37),
    HALF_PAST_SEVEN_PM("7:30 PM", 38),
    EIGHT_PM("8:00 PM", 39),
    HALF_PAST_EIGHT_PM("8:30 PM", 40),
    NINE_PM("9:00 PM", 41),
    HALF_PAST_NINE_PM("9:30 PM", 42),
    TEN_PM("10:00 PM", 43),
    HALF_PAST_TEN_PM("10:30 PM", 44),
    ELEVENT_PM("11:00 PM", 45),
    HALF_PAST_ELEVENT_PM("11:30 PM", 46),
    TWELVE_PM("12:00 PM", 47),
    HALF_PAST_TWELVE_PM("12:30 PM", 48);

    private String value;
    private Integer number;

    TimeSlotValue(String value, Integer number) {
        this.value = value;
        this.number = number;
    }

    TimeSlotValue() {}

    public String getValue() {
        return value;
    }

    public Integer getNumber() {
        return number;
    }

    public static TimeSlotValue findByValue(String value) {
        TimeSlotValue result = null;
        for (TimeSlotValue timeSlotValue : TimeSlotValue.values()) {
            if (timeSlotValue.value.equals(value)) {
                result = timeSlotValue;
                break;
            }
        }
        return result;
    }
}
