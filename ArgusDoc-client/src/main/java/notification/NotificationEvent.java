package notification;

import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class NotificationEvent {
    public void newTask(String message){
        String title = "У вас новые задачи:";
        NotificationType notificationType = NotificationType.INFORMATION;

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setAnimationType(AnimationType.POPUP);
        tray.setNotificationType(notificationType);
        tray.showAndWait();
    }
    public void  newFromEmpTask(String message){
        String title = "Ваши задачи выполнены:";
        NotificationType notificationType = NotificationType.INFORMATION;

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setAnimationType(AnimationType.POPUP);
        tray.setNotificationType(notificationType);
        tray.showAndWait();
    }

    public void newLetter(String message){
        String title = "У вас новые письма:";
        NotificationType notificationType = NotificationType.INFORMATION;

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setAnimationType(AnimationType.POPUP);
        tray.setNotificationType(notificationType);
        tray.showAndWait();
    }
}
