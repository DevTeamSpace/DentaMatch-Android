/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.interfaces;

/**
 * Created by bawenderyandra on 08/03/17.
 * To inject activity reference.
 */

public interface NotificationClickListener {
     void onNotificationItemClick(int position, int notifId, int notificationType);
     void onAcceptRejectClick(int position, int notifId, int inviteStatus);
     void onDelete(int position, int notifId, int notificationType);
}
