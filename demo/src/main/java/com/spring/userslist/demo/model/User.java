package com.spring.userslist.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "`training`")
    private Boolean training = false;

    public User() {

    }

    public User(String firstName, String lastName, String email) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Boolean gettraining() {
        return training;
    }
    public void settraining(boolean training) {
        this.training = training;
    }

    public String toJSON()
    {
        // Returning attributes of organisation
        String json = "{"
            + " \"firstName\" : \"" + this.firstName + "\", "
            + "\"lastName\" : \"" + this.lastName + "\", "
            + "\"email\" : \"" + this.email + "\", "
            + "\"training\" : \"" + this.training + "\", "
            + "\"email\" : \"" + this.email + "\"" 
            + "}";
        return json;
    }

    @Override public String toString()
    {
        // Returning attributes of organisation
        return "firstName=" + this.firstName
            + ", lastName=" + this.lastName
            + ", email=" + this.email 
            + ", training=" + this.training;
    }
}