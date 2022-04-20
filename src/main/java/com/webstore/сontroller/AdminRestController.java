package com.webstore.сontroller;

import com.webstore.entity.Account;
import com.webstore.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Tag(name = "Admin Private Controller", description = "Admin API")
@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/adminApi")
public class AdminRestController {

    private AccountService accountService;

    /**
     * @return Результат работы метода userService.getAllUsers() в виде коллекции юзеров
     * в теле ResponseEntity
     */
    @GetMapping
    @Operation(summary ="Получение списка всех юзеров")
    public ResponseEntity<List<Account>> getAllUsers() {
        log.debug("получаем список всех юзеров");

        ResponseEntity<List<Account>> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(accountService.findAll());
        } catch (PersistenceException e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Значение ID юзера
     * @return Результат работы метода userService.getOneUser(id) в виде объекта User
     * в теле ResponseEntity
     */
    @GetMapping("/{id}")
    @Operation(summary ="Получение юзера по id")
    public ResponseEntity<Account> getUserById(@PathVariable Long id) {
        log.debug("получаем юзера с айди {}", id);

        ResponseEntity<Account> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(accountService.findById(id));
        } catch (PersistenceException e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param user Создаваемый объект класса User
     * @return Результат работы метода userService.saveUser(user) в виде объекта User
     * в теле ResponseEntity
     */
    @PostMapping
    @Operation(summary ="Создание юзера")
    public ResponseEntity<Account> createUser(@RequestParam String user, @RequestBody @NotNull Account account) {
        log.debug("Старт метода ResponseEntity<User> createUser(@RequestBody @NotNull User user) с параметром {}", account);

        ResponseEntity<Account> responseEntity;
        try {
            responseEntity = new ResponseEntity<>(accountService.saveUser(account), HttpStatus.CREATED);
        } catch (PersistenceException e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param user Обновляемый объект класса User
     * @return Результат работы метода userService.saveUser(user) в виде объекта User
     * в теле ResponseEntity
     */
    @PatchMapping
    @Operation(summary ="Обновление юзера")
    public ResponseEntity<Account> updateUser(@RequestBody @NotNull Account user) {
        log.debug("Старт метода ResponseEntity<User> updateUser(@RequestBody @NotNull User user) с параметром {}", user);

        ResponseEntity<Account> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(accountService.saveUser(user));
        } catch (PersistenceException e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Удаляемого объекта класса User
     * @return Объект ResponseEntity со статусом OK
     */
    @DeleteMapping("/{id}")
    @Operation(summary ="Удаление юзера")
    public ResponseEntity<Account> deleteUser(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<User> deleteUser(@PathVariable Long id) с параметром {}", id);

        ResponseEntity<Account> responseEntity;
        try {
            accountService.deleteById(id);
            responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (PersistenceException e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }
}
