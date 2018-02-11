package model;

public final class StatusTask {
    public static final String DONE = "1"; //выполнена
    public static final String NOT_DONE = "2"; //не выполнена
    public static final String PERFORMED = "3"; // выполняется
    public static final String OVERDUE = "4"; // просрочена
    public static final String CANCELED = "5"; // в архив


    private StatusTask() {
    }
}
