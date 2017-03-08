package com.appster.dentamatch.interfaces;

/**
 * Created by bawenderyandra on 08/03/17.
 */

public interface NotificationClickListener {
    public void onNotificationItemClick(int position, int notifId, int notificationType);

    public void onAcceptRejectClick(int position, int notifId, int inviteStatus);

    public void onDelete(int position, int notifId, int notificationType);
}
