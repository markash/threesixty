package za.co.yellowfire.threesixty.ui.view.dashboard;

import com.github.markash.ui.component.card.CounterStatisticsCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CounterStatisticsCards {
    private List<CounterStatisticsCard> cards = new ArrayList<>();

    public CounterStatisticsCards() { }

    public CounterStatisticsCards(final List<CounterStatisticsCard> cards) {
        this.cards.addAll(cards);
    }

    public Stream<CounterStatisticsCard> stream() {
        return cards.stream();
    }
}
