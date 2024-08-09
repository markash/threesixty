package com.github.markash.threesixty.web.views.assessment;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import static com.github.markash.threesixty.web.views.Styles.FONT_SEMI_BOLD;
import static com.github.markash.threesixty.web.views.Styles.PAD_TOP_10PX;

public class Score extends Div {

    private final Span valueField;

    public Score(String label, String value) {

        Span labelField = new Span(label);
        labelField.addClassNames(
                FONT_SEMI_BOLD.getClassName(),
                PAD_TOP_10PX.getClassName()
        );

        this.valueField = new Span(value);
        this.valueField.addClassNames(
                PAD_TOP_10PX.getClassName()
        );

        add(labelField, valueField);
    }

    public void setValue(String value) {
        this.valueField.setText(value);
    }

    public String getValue() {
        return this.valueField.getText();
    }
}
