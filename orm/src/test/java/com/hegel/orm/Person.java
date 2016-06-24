package com.hegel.orm;

import java.util.Objects;

public class Person {
    final public int id;
    final public String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        Person person;
        return this == o
                || !(o == null || getClass() != o.getClass())
                && id == (person = (Person) o).id
                && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
