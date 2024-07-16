package za.co.yellowfire.threesixty.ui.component.field;

import org.vaadin.viritin.label.MLabel;

import java.text.DecimalFormat;

public class MDoubleLabel extends MLabel {
    private static DecimalFormat FORMAT = new DecimalFormat("#0.00");

    public MDoubleLabel() {
        this(0.0);
    }

    public MDoubleLabel(double content) {
        super(FORMAT.format(content));
    }

    public void setValue(double value) {
        super.setValue(FORMAT.format(value));
    }
}
