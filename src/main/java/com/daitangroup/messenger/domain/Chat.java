package com.daitangroup.messenger.domain;

import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Objects;
import java.util.Set;

public class Chat {

    private long id;

    private String name;

    private Set<User> members;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public void addMember(User elem) {
        members.add(elem);
    }

    public void removeMember(User elem) {
        members.remove(elem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id == chat.id &&
                Objects.equals(name, chat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
