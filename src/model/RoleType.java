package model;

import java.util.HashMap;
import java.util.Map;

public enum RoleType {

    DIRECTOR(1), WRITER(2), ACTOR(3);

    private final int value;
    private static Map map = new HashMap<>(); // want to map values 1,2... to roles.

    RoleType(int code)  { this.value = code; }
    public int getValue() { return value; }

    static {
        for (RoleType roleType : RoleType.values()) {
            map.put(roleType.value, roleType);
        }
    }

    public static RoleType getEnum(int value) {
        return (RoleType) map.get(value);
    }
}
