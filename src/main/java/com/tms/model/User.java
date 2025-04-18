package com.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Scope("prototype")
@Component
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String firstname;

    @Column(name = "second_name", nullable = false, length = 20)
    private String secondName;

    private Integer age;

    @Column(length = 50)
    private String email;

    @Column(length = 1)
    private String sex;

    @Column(name = "telephone_number", length = 20)
    private String telephoneNumber;


    @Column(nullable = false)
    private Timestamp created;


    @Column
    private Timestamp updated;

    @JsonIgnore
    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private Security securityInfo;

    public User() {}

    public User(Long id, String firstname, String secondName, Integer age, String email, String sex, String telephoneNumber, Timestamp created, Timestamp updated, Boolean isDeleted, Security securityInfo) {
        this.id = id;
        this.firstname = firstname;
        this.secondName = secondName;
        this.age = age;
        this.email = email;
        this.sex = sex;
        this.telephoneNumber = telephoneNumber;
        this.created = created;
        this.updated = updated;
        this.isDeleted = isDeleted;
        this.securityInfo = securityInfo;
    }

    @PrePersist
    public void prePersist() {
        this.created = new Timestamp(System.currentTimeMillis());
        this.updated = new Timestamp(System.currentTimeMillis());
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updated = new Timestamp(System.currentTimeMillis());
    }
}