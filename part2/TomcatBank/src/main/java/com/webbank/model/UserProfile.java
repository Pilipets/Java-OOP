package com.webbank.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="USER_PROFILE")
public class UserProfile implements Serializable{

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    //@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
   // @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "userProfiles")
    //private Set<User> users = new HashSet<>();

    @Column(name="TYPE", unique=true, nullable=false)
    private String type;// = UserProfileType.USER.getUserProfileType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    /*public Set<User> getUsers() {
        return users;

    }
    public void setUsers(Set<User> users) {
        this.users = users;
    }*/
}