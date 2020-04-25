package model;

import java.util.Date;
import java.util.UUID;

public class Person {
    private UUID id;
    private String name;
    private Date birthday;
    private String birthplace;
    private String profession;
    private Role role;

    public Person(UUID id, String name, Date birthday, String birthplace) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.birthplace = birthplace;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Person(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }
}
