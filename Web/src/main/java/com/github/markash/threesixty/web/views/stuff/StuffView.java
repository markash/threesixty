package com.github.markash.threesixty.web.views.stuff;

import com.github.markash.threesixty.web.Person;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Stuff")
@Menu(icon = "line-awesome/svg/pencil-ruler-solid.svg", order = 0)
@Route(value = "")
@RouteAlias(value = "")
@Uses(Icon.class)
public class StuffView extends Composite<VerticalLayout> {

    //private CountryService countryService;

    @Autowired
    public StuffView(/*final CountryService countryService*/) {
        //this.countryService = countryService;

        Button buttonPrimary = new Button();
//        Grid<Country> basicGrid = new Grid<>(Country.class);
        Grid<Object> basicGrid = new Grid<>(Object.class);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Button");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        basicGrid.setWidth("100%");
        basicGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(basicGrid);
        getContent().add(buttonPrimary);
        getContent().add(basicGrid);
    }

    private void setGridSampleData(
            final Grid<Object> grid) {

//        grid.setItems(query -> countryService.findCountries(
//                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
//                .stream());
    }


}
