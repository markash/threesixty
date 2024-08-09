package com.github.markash.threesixty.web.views.assessment;

import com.github.markash.threesixty.web.views.Styles;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import static com.github.markash.threesixty.web.views.Styles.H_FULL;

public class RatingField extends Tab {

    public RatingField() {

        HorizontalLayout firstRow = getRow01();

        TextArea measurementField = new TextArea("Measurement");
        measurementField.setRequired(true);
        measurementField.setHeightFull();
        measurementField.setWidthFull();

        VerticalLayout firstColumn =
                new VerticalLayout(measurementField);
        firstColumn.setHeightFull();
        firstColumn.setPadding(false);

        TextArea selfAssessment = new TextArea("Self assessment");
        selfAssessment.setHeight(50, Unit.PERCENTAGE);
        selfAssessment.setWidthFull();

        TextArea otherAssessment = new TextArea("Other assessment");
        otherAssessment.setHeight(50, Unit.PERCENTAGE);
        otherAssessment.setWidthFull();

        VerticalLayout secondColumn =
                new VerticalLayout(selfAssessment, otherAssessment);
        secondColumn.setHeightFull();
        secondColumn.setPadding(false);

        HorizontalLayout secondRow = new HorizontalLayout(firstColumn, secondColumn);
        secondRow.setWidthFull();
        secondRow.setHeightFull();
        secondRow.setPadding(false);

        VerticalLayout layout = new VerticalLayout(firstRow, secondRow);
        layout.setSizeFull();
        layout.setPadding(false);

        add(layout);
        addClassName(H_FULL.getClassName());
    }

    private static HorizontalLayout getRow01() {
        ComboBox<String> areaField = new ComboBox<>("Area");
        areaField.setRequired(true);

        ComboBox<String> weightField = new ComboBox<>("Weight");
        areaField.setRequired(true);

        ComboBox<String> ratingField = new ComboBox<>("Rating");

        TextField scoreField = new TextField("Score", "0", "Score");
        scoreField.setEnabled(false);

        HorizontalLayout firstRow =
                new HorizontalLayout(
                        areaField,
                        weightField,
                        ratingField,
                        scoreField);
        firstRow.setWidthFull();
        firstRow.setPadding(false);

        return firstRow;
    }
}
