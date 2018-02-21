package dao;

import javafx.collections.ObservableList;
import model.Event;

public interface EventDao {
    public void addEvent(Event event);
    public void removeEvent(Event event);
    public ObservableList<Event>  listSelectedDayEvent(int authUserId, java.sql.Date selectedDate);
    public ObservableList<Event> listAllEvent(int authUserId);

}
