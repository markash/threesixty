package com.github.markash.threesixty.web.views;

public enum Styles {
    FONT_SEMI_BOLD("font-semibold"),

    H_FULL("h-full"),

    PAD_TOP_10PX("pad-top-10px")
    ;

    private final String className;

    Styles(final String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
