package com.example.hr_system.entities;


import com.example.hr_system.enums.Role;
import javax.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users_table")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String lastname;

    private String email;
    private String password;
    private String image;

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "user")
    private List<Notification> notification;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @OneToOne(cascade = CascadeType.ALL)
    private BlockedUser blockedUser;

    @OneToOne(cascade = CascadeType.ALL)
    private Employer employer;

    @OneToOne(cascade = CascadeType.ALL)
    private JobSeeker jobSeeker;

    private String lastVisit;

    private boolean registeredFromGoogle;
    private String verificationCode;
    private Boolean blocked;
    private int unReadMessages;
    private boolean isOnline;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        }
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
