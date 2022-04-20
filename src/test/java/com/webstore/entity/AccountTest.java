package com.webstore.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountTest {

    Account account = Account.builder()
            .email("Ivan")
            .firstName("Petrov")
            .lastName("ivan@mail.ru")
            .build();

    @Test
    void testClone() throws CloneNotSupportedException {
        Account newAccount = account.clone();
        Assertions.assertEquals(newAccount.getEmail(), account.getEmail());
        Assertions.assertEquals(newAccount.getFirstName(), account.getFirstName());
        Assertions.assertEquals(newAccount.getLastName(), account.getLastName());
    }
}