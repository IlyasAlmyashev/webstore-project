package com.webstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Родительская сущность для пользователя, админа, модератора
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@MappedSuperclass
@Builder
@Table(name = "ACCOUNTS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Account implements UserDetails, Cloneable {

    @Override
    public Account clone() throws CloneNotSupportedException {
        return (Account) super.clone();
    }

    //    @Override
//    public Account clone() {
//        Account account = new Account();
//        account.setEmail(this.email);
//        account.setFirstName(this.firstName);
//        account.setLastName(this.lastName);
//        return account;
//    }

    /**
     * Уникальный идентификатор
     */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long accountId;

    /**
     * Электронная почта
     */
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    /**
     * Пароль
     */
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    /**
     * Имя
     */
    @Column(name = "FIRST_NAME")
    private String firstName;

    /**
     * Фамилия
     */
    @Column(name = "LAST_NAME")
    private String lastName;

    /**
     * Роль
     */
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Время создания аккаунта
     */
    @Column(name = "ACCOUNT_CREATED_TIME", nullable = false)
    private LocalDateTime accountCreatedTime;

    /**
     * Время последней активности
     */
    @Column(name = "LAST_ACCOUNT_ACTIVITY", nullable = false)
    private LocalDateTime lastAccountActivity;

    /**
     * Подписчики пользователя
     */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ACCOUNT_SUBSCRIBERS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "SUBSCRIBER_ID"))
//    @Column(name = "USER_SUBSCRIBERS")
    private Set<Account> subscribers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
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
