package com.github.markash.threesixty.web.views.security;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Login")
@Menu(icon = "line-awesome/svg/lock-solid.svg", order = 0)
@Route(value = "login")
@RouteAlias(value = "login")
@Uses(Icon.class)
public class LoginView extends Div {
    public LoginView() {
        
        addClassName("login-view");

        LoginForm loginForm = new LoginForm();
        loginForm.getElement().getThemeList().add("dark");
        add(loginForm);
    }
}
