package com.ilegra.desafio.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventTypeEnum {
    SALESMAN("001"),
    CLIENT("002"),
    SALE("003");

    private final String type;

    private static final Map<String, EventTypeEnum> enums = new HashMap<>();

    static {
        for (EventTypeEnum value : EventTypeEnum.values()) {
            enums.put(value.getType(), value);
        }
    }

    EventTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static EventTypeEnum valueOfType(String type) {
        return enums.get(type);
    }

}
