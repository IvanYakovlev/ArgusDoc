package model;

public final class StatusTask {
    public static final int DONE = 1; //выполнена
    public static final int NOT_DONE = 2; //не выполнена
    public static final int PERFORMED = 3; // выполняется
    public static final int OVERDUE = 4; // просрочена
    public static final int CANCELED = 5; // в архив


    private StatusTask() {
    }
}
