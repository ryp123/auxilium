package ca.usask.auxilium.Services;

/**
 * Created by gongcheng on 2018-03-29.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by L on 10/05/2017.
 * Copyright (c) 2017 Centroida. All rights reserved.
 */

public class LikeService extends Service {

    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        //Saving action implementation

        return null;
    }
}