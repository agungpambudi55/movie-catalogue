package com.dicoding.picodiploma.moviecatalouge.remoteView

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        WidgetRemoteViewsFactory(
            this.applicationContext
        )
}