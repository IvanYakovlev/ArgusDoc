package notification;

import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class NotificationEvent {
    public void newTask(){
        String title = "Новое сообщение";
        String message = "You've successfully created your first Tray Notification";
        NotificationType notificationType = NotificationType.INFORMATION;

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setAnimationType(AnimationType.POPUP);
        tray.setNotificationType(notificationType);
        tray.showAndWait();
    }
}
