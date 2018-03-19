package entity;

public final class StatusTask {

    /*Класс констант соответсвующих данным в таблице STATUS_TASKS*/

    public static final String DONE = "1"; //выполнена
    public static final String NOT_DONE = "2"; //не выполнена
    public static final String PERFORMED = "3"; // выполняется
    public static final String OVERDUE = "4"; // просрочена
    public static final String CANCELED = "5"; // в архив


    private StatusTask() {
    }
}
