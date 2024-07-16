package za.co.yellowfire.threesixty.ui.view.period;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.EventRouter;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.ui.I8n;

public class PeriodSelectWindow extends Window {

    private EventRouter eventRouter;
    private ComboBox<PeriodModel> periodField = new ComboBox<>(I8n.Period.SINGULAR);
    public PeriodSelectWindow(
            final String caption,
            final ListDataProvider<PeriodModel> periodProvider) {

        super(caption);

        this.periodField.setDataProvider(periodProvider);

        MVerticalLayout layout =
                new MVerticalLayout()
                        .with(
                                new MLabel("Please select the period for the assessment."),
                                periodField,
                                new MHorizontalLayout()
                                        .with(
                                                new MButton(I8n.Button.OK, this::onOK),
                                                new MButton(I8n.Button.CANCEL, this::onCancel)));

        setContent(layout);
    }

    public Registration addPeriodSelectListener(
            final PeriodSelectListener listener) {

        return getEventRouter()
                .addListener(
                        PeriodSelectEvent.class,
                        listener,
                        PeriodSelectListener.class.getDeclaredMethods()[0]);
    }

    private void onOK(
            final Button.ClickEvent event) {


        getEventRouter()
                .fireEvent(
                        new PeriodSelectEvent(
                                this,
                                this.periodField.getSelectedItem().orElse(null),
                                PeriodSelectEvent.Action.OK));
        UI.getCurrent()
                .removeWindow(this);
    }

    private void onCancel(
            final Button.ClickEvent event) {

        getEventRouter()
                .fireEvent(
                        new PeriodSelectEvent(
                                this,
                                null,
                                PeriodSelectEvent.Action.CANCEL));
        UI.getCurrent()
                .removeWindow(this);
    }

    private EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }
}
