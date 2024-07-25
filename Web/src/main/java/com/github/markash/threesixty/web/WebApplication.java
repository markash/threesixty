package com.github.markash.threesixty.web;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Theme(value = "my-vaadin-app")
@SpringBootApplication(/*exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class*/)
public class WebApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
