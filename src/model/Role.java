package model;

public class Role {


    private int value;
    private String name;

    public Role(int value) {
        this.value = value;
    }

    public Role(String name) {
        this.name = name;
        this.value = RoleType.ACTOR.getValue();
    }

    public Role(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
/*    @Override
    public String toString() {
        return value;
    }*/
}
