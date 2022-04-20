package com.webstore.initialization;

import com.webstore.entity.Account;
import com.webstore.entity.Role;
import com.webstore.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultAccountsCreation {
    private final AccountService accountService;

    @PostConstruct
    public void init() {
        accountService.saveUser(Account.builder()
                .email("ilyas.almyashev.1991@gmail.com")
                .password("admin")
                .role(Role.ROLE_ADMIN)
                .firstName("Ильяс")
                .lastName("Альмяшев")
                .build());
    }
}
