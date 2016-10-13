package za.co.yellowfire.threesixty.domain.statistics;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Optional;

import za.co.yellowfire.threesixty.domain.statistics.CounterStatistic.CounterFormat;

public class CounterStatistic implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String type;
	private Optional<Number> value;
	private CounterFormat format = CounterFormat.INTEGER;
	private Number defaultValue = 0L;
	
	public CounterStatistic(String type, Optional<Number> value) {
		this(type, value, CounterFormat.INTEGER, 0L);
	}
	
	public CounterStatistic(String type, Number value) {
		this(type, Optional.ofNullable(value));
	}
	
	public CounterStatistic(String type, Optional<Number> value, CounterFormat format) {
		this(type, value, format, 0L);
	}

	public CounterStatistic(String type, Optional<Number> value, CounterFormat format, Number defaultValue) {
		this.type = type;
		this.value = value;
		this.format = format;
		this.defaultValue = defaultValue;
	}

	

	public String getType() { return type; }
	public Optional<Number> getValue() { return value; }
	public String getFormattedValue() {
		switch (format) {
			case INTEGER: return DecimalFormat.getIntegerInstance().format(getValue().orElse(defaultValue));
			case NUMBER: return DecimalFormat.getNumberInstance().format(getValue().orElse(defaultValue));
			case PERCENTAGE: return DecimalFormat.getPercentInstance().format(getValue().orElse(defaultValue));
			default: return DecimalFormat.getIntegerInstance().format(getValue().orElse(defaultValue));
		}
	}
	
	@Override
	public String toString() {
		return String.format(
				"CounterStatistics [type=%s, value=%s, format=%s, defaultValue=%s]", 
				type, 
				value, 
				format,
				defaultValue);
	}

	public static enum CounterFormat {
		INTEGER,
		NUMBER,
		PERCENTAGE
	}
}
