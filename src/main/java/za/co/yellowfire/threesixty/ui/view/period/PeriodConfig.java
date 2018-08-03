package za.co.yellowfire.threesixty.ui.view.period;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.DateField;
import com.vaadin.ui.renderers.DateRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.PeriodService;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@SuppressWarnings("unused")
public class PeriodConfig {

    @Bean @PrototypeScope
    PeriodEntityEditForm periodEntityEditForm(final AssessmentService assessmentService) {
        return new PeriodEntityEditForm(assessmentService);
    }

    @Bean
    EntitySupplier<PeriodModel, Serializable> periodSupplier(final PeriodService periodService) {
        return id -> Optional.of(new PeriodModel(periodService.findById((String) id)));
    }

    @Bean
    BlankSupplier<PeriodModel> blankPeriodSupplier() {
        return PeriodModel::new;
    }

    @Bean
    EntityPersistFunction<PeriodModel> periodPersistFunction(final PeriodService periodService) {
        return period -> {
            try {
                return new PeriodModel(periodService.save(period.getWrapped()));
            } catch (PersistenceException e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return period;
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<PeriodModel> periodListDataProvider(final PeriodService periodService) {
        List<PeriodModel> list = periodService.getPeriodRepository().findAll().stream().map(PeriodModel::new).collect(Collectors.toList());
        return new ListDataProvider<>(list);
    }

    @Bean
    @PrototypeScope
    ListDataProvider<PeriodModel> activePeriodListDataProvider(final PeriodService periodService) {
        List<PeriodModel> list = periodService.getPeriodRepository().findByActive(true).stream().map(PeriodModel::new).collect(Collectors.toList());
        return new ListDataProvider<>(list);
    }

    @Bean
    TableDefinition<PeriodModel> periodTableDefinition() {
        DateRenderer dateRenderer = new DateRenderer("yyyy-MM-dd", "");
        TableDefinition<PeriodModel> tableDefinition = new TableDefinition<>(PeriodEditView.VIEW_NAME);
        tableDefinition.column(DateField.class).withHeading(I8n.Period.Columns.START).forProperty(PeriodModel.FIELD_ID).identity().display(PeriodModel.FIELD_START);
        tableDefinition.column(DateField.class).withHeading(I8n.Period.Columns.END).forProperty(PeriodModel.FIELD_END).renderer(dateRenderer);
        tableDefinition.column(DateField.class).withHeading(I8n.Period.Columns.DEADLINE_PUBLISH).forProperty(PeriodModel.FIELD_DEADLINE_PUBLISHED).renderer(dateRenderer);
        tableDefinition.column(DateField.class).withHeading(I8n.Period.Columns.DEADLINE_COMPLETE).forProperty(PeriodModel.FIELD_DEADLINE_COMPLETED).renderer(dateRenderer);
        tableDefinition.column(Boolean.class).withHeading(I8n.Period.Columns.ACTIVE).forProperty(PeriodModel.FIELD_ACTIVE);
        return tableDefinition;
    }
}
