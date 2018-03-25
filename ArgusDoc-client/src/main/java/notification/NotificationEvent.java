package notification;

import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.util.ArrayList;

public class NotificationEvent {
    private ArrayList<Integer> listNewTask = new ArrayList<Integer>();
    private ArrayList<Integer> listNewFromEmpTask = new ArrayList<Integer>();
    private ArrayList<Integer> listNewLetter = new ArrayList<Integer>();
    private ArrayList<Integer> listNewEvent = new ArrayList<Integer>();

    public void newTask(String message, int idSumm){
        if (!listNewTask.contains(idSumm)) {
            listNewTask.add(idSumm);
            System.out.println(idSumm);
            String title = "У вас новые задачи:";
            NotificationType notificationType = NotificationType.INFORMATION;

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setAnimationType(AnimationType.POPUP);
            tray.setNotificationType(notificationType);
            tray.showAndWait();
        }
    }
    public void overdueTask(String message){
        String title = "У вас есть просроченные задачи:";
        NotificationType notificationType = NotificationType.WARNING;

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.setNotificationType(notificationType);
        tray.showAndDismiss(Duration.millis(5000));
    }

    public void  newFromEmpTask(String message, int idSumm){
        if (!listNewFromEmpTask.contains(idSumm)) {
            listNewFromEmpTask.add(idSumm);
            System.out.println(idSumm);
            String title = "Ваши задачи выполнены:";
            NotificationType notificationType = NotificationType.INFORMATION;

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setAnimationType(AnimationType.POPUP);
            tray.setNotificationType(notificationType);
            tray.showAndWait();

        }
    }

    public void newLetter(String message, int idSumm){
        if (!listNewLetter.contains(idSumm)) {
            listNewLetter.add(idSumm);
            System.out.println(idSumm);
            String title = "У вас новые письма:";
            NotificationType notificationType = NotificationType.INFORMATION;

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setAnimationType(AnimationType.FADE);
            tray.setNotificationType(notificationType);
            tray.showAndWait();
        }
    }

    public void newEvent(String message) {

            String title = "Напоминание:";
            NotificationType notificationType = NotificationType.INFORMATION;

            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setAnimationType(AnimationType.SLIDE);
            tray.setNotificationType(notificationType);
            tray.showAndDismiss(Duration.millis(5000));

    }
}
