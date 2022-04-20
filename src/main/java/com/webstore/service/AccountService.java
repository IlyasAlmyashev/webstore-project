package com.webstore.service;

import com.webstore.entity.Account;
import com.webstore.entity.Role;
import com.webstore.exception.NoContentException;
import com.webstore.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {

        return Optional.of(accountRepository.findAll())
                .orElseThrow(NoContentException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = accountRepository.findByEmail(email);
        user.setLastAccountActivity(LocalDateTime.now());
        return user;
    }

    public Account saveUser(Account user){
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10)));
//        user.setRole(Role.ROLE_USER);
        user.setAccountCreatedTime(LocalDateTime.now());
        user.setLastAccountActivity(LocalDateTime.now());
        accountRepository.save(user);
        return user;
    }

    public void updateUser(Long id, Account user) {
        accountRepository.save(user);
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public Account findById(Long id) {
        return accountRepository.getById(id);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

}
