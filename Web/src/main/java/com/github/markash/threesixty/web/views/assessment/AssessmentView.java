package com.github.markash.threesixty.web.views.assessment;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Menu(icon = "line-awesome/svg/suitcase-solid.svg", order = 0)
@PageTitle("Assessment")
@Route("assessment")
@RouteAlias("assessment")
@Uses(Icon.class)
public class AssessmentView extends Composite<VerticalLayout> {
    
    
    public AssessmentView() {

        TextField idField = new TextField("Id");
        ComboBox<String> personField = new ComboBox<>("Person");
        ComboBox<String> raterField = new ComboBox<>("Rater");
        ComboBox<String> periodField = new ComboBox<>("Period");

        HorizontalLayout headerRow =
                new HorizontalLayout(
                        idField,
                        periodField,
                        raterField,
                        personField);
        headerRow.setWidthFull();

        Score ratingsField = new Score("Ratings: ", "0");
        Score weightingField = new Score("Weighting: ", "0");
        Score scoreField = new Score("Overall score: ", "0");
        Button addButton = new Button("Add", VaadinIcon.PLUS_CIRCLE_O.create());
        Button publishButton = new Button("Publish", VaadinIcon.PLAY_CIRCLE_O.create());

        HorizontalLayout scoreRow =
                new HorizontalLayout(
                        addButton,
                        publishButton,
                        ratingsField,
                        weightingField,
                        scoreField
                );
        scoreRow.setWidthFull();
        scoreRow.addClassName("bg-shade-10");
        scoreRow.setAlignSelf(FlexComponent.Alignment.CENTER, ratingsField, weightingField, scoreField);
        scoreRow.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        TabSheet ratingSheet = new TabSheet();
        ratingSheet.add("Rating 1", new RatingField());
        ratingSheet.add("Rating 2", new RatingField());
        ratingSheet.add("Rating 3", new RatingField());
        ratingSheet.setWidthFull();
        ratingSheet.setHeightFull();
        ratingSheet.setSizeFull();

        HorizontalLayout ratingRow =
                new HorizontalLayout(
                        ratingSheet
                );
        ratingRow.setHeightFull();
        ratingRow.setWidthFull();

        getContent().add(headerRow, scoreRow, ratingRow);
        getContent().setSizeFull();
    }
}