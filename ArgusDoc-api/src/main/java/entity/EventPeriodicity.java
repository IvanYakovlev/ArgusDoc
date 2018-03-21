package entity;

public final class EventPeriodicity {
    /*Класс определяющий периодизацию события*/

    public static final int SINGLE_TIME = 0; // повторяется каждый день
    public static final int DAILY = 1; // повторяется каждый день
    public static final int WEEKLY = 2; // повторяется каждую неделю
    public static final int MONTHLY = 3; // повторяется каждый месяц

    private EventPeriodicity() {
    }
}
