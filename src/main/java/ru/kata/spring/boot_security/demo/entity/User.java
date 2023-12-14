package ru.kata.spring.boot_security.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username")
    private String username;
    @Column(name = "First_Name")
    private String FirstName;
    @Column(name = "Last_Name")
    private String LastName;
    @Column(name = "password")
    private String password;
    @Column(name = "Salary")
    private int Salary;
    @Column(name = "Department")
    private String Department;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Transient
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public User() {
    }

    public User(Long id, String username, String firstName, String lastName, String password, int salary, String department, Set<Role> roles) {
        this.id = id;
        this.username = username;
        FirstName = firstName;
        LastName = lastName;
        this.password = password;
        Salary = salary;
        Department = department;
        this.roles = roles;
    }
    public String generateUsername() {
        return this.getFirstName() + "_" + this.getLastName() + "_" + this.getId();
    }
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getSalary() {
        return Salary;
    }

    public String getDepartment() {
        return Department;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "roles=" + roles +
                '}';
    }
}