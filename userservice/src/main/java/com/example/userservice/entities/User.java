package com.example.userservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column
    private Double weight; // kg

    @Column
    private Double height; // cm

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "initial_activity_level")
    @Enumerated(EnumType.STRING)
    private InitialActivityLevel initialActivityLevel;

    @Column(name = "activity_level_set_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activityLevelSetAt;

    // Constructors
    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String id) {
        this.id = id;
    }

    // Enum for Gender
    public enum Gender {
        MALE, FEMALE, OTHER
    }

    // Enum for Initial Activity Level
    public enum InitialActivityLevel {
        SEDENTARY(1.2, "Sedentary", "Little or no exercise"),
        LIGHTLY_ACTIVE(1.375, "Lightly Active", "Light exercise/sports 1-3 days/week"),
        MODERATELY_ACTIVE(1.55, "Moderately Active", "Moderate exercise/sports 3-5 days/week"),
        VERY_ACTIVE(1.725, "Very Active", "Hard exercise/sports 6-7 days a week"),
        EXTRA_ACTIVE(1.9, "Extra Active", "Very hard exercise/sports & physical job or 2x training");

        private final double factor;
        private final String displayName;
        private final String description;

        InitialActivityLevel(double factor, String displayName, String description) {
            this.factor = factor;
            this.displayName = displayName;
            this.description = description;
        }

        public double getFactor() { return factor; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }

    // UserDetails implementation
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email; // Email will be used as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public InitialActivityLevel getInitialActivityLevel() {
        return initialActivityLevel;
    }

    public void setInitialActivityLevel(InitialActivityLevel initialActivityLevel) {
        this.initialActivityLevel = initialActivityLevel;
        this.activityLevelSetAt = new Date(); // Tự động set thời gian
    }

    public Date getActivityLevelSetAt() {
        return activityLevelSetAt;
    }

    public void setActivityLevelSetAt(Date activityLevelSetAt) {
        this.activityLevelSetAt = activityLevelSetAt;
    }
} 