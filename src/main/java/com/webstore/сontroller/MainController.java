package com.webstore.сontroller;

import com.webstore.entity.Account;
import com.webstore.entity.Role;
import com.webstore.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Контроллер для регистрации, авторизации
 * и переадресации на страницы в зависимости от роли пользователя
 */
@Controller
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MainController {

    private final AccountService accountService;

    private Account getCurrentAccount() {
        String email;
        if (SecurityContextHolder.getContext().getAuthentication().toString().contains("given_name")) {
            email = ((DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        } else {
            email = ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        }
        return (Account) accountService.loadUserByUsername(email);
    }

    /**
     * Стартовая страница
     *
     * @return страница с предложением зарегистрироваться или авторизоваться.
     */
    @GetMapping(value = "")
    public String printWelcomePage() {
        return "welcome";
    }

    /**
     * @return переход на страницу для пользователя с ролью USER
     */
    @GetMapping(value = "/user")
    public String printUserPage(Model model) {
        model.addAttribute("currentAccount", getCurrentAccount());
        return "userpage";
    }

    /**
     * @return переход на страницу для пользователя с ролью ADMIN
     */
    @GetMapping(value = "/admin")
    public String printAdminPage(Model model) {
        model.addAttribute("currentAccount", getCurrentAccount());
        model.addAttribute("allUsers", accountService.findAll());
        model.addAttribute("allRoles", Arrays.stream(Role.values()).collect(Collectors.toList()));
        return "adminpage";
    }

    /**
     * @return переход на страницу для пользователя с ролью MODERATOR
     */
    @GetMapping(value = "/moderator")
    public String printModeratorPage(Model model) {
        model.addAttribute("currentAccount", getCurrentAccount());
        return "moderatorpage";
    }

    /**
     * метод для перехода на страницу регистрации
     *
     * @param model - сущность для передачи юзера в html
     * @return страница с формой регистрации
     */
    @GetMapping(value = "/registration")
    public String printRegistrationPage(Model model) {
        model.addAttribute("user", new Account());
        return "registration";
    }

    /**
     * необходимо отредактировать этот метод для проверки, регистрации и сохранения
     * нового пользователя в БД
     *
     * @param model - сущность для обмена информацией между методом и html.
     * @param user  - юзер, желающий зарегистрироваться.
     * @return В случае успешной регистрации нового пользователя переход на страницу авторизации,
     * в ином случае - registration.html
     */
    @PostMapping(value = "/registration")
    public String registrationNewUser(@ModelAttribute Account user, Model model) {
        accountService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "login";
    }
}
