package entity;

public final class EventPeriodicity {
    /*Класс определяющий периодизацию события*/

    public static final int SINGLE_TIME = 0; // повторяется каждый день
    public static final int EVERY_DAY = 1; // повторяется каждый день
    public static final int EVERY_WEEK = 2; // повторяется каждую неделю
    public static final int EVERY_MONTH = 3; // повторяется каждый месяц

    private EventPeriodicity() {
    }
}
