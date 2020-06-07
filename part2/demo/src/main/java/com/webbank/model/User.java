package com.webbank.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private int id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @Column(name = "ENABLED", nullable = false)
  private boolean enabled;

  @NotEmpty
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinTable(name = "USER_USER_PROFILE",
          joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "id") },
          inverseJoinColumns = { @JoinColumn(name = "USER_PROFILE_ID", referencedColumnName = "id") })
  private Set<UserProfile>  userProfiles = new HashSet<>();

  public int getId(){ return id; }
  public void setId(int id) {this.id = id;}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set<UserProfile> getUserProfiles() {
    return userProfiles;
  }

  public void setUserProfiles(Set<UserProfile> userProfiles) {
    this.userProfiles = userProfiles;
  }
}
