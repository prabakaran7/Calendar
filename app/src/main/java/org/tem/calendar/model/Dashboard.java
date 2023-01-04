package org.tem.calendar.model;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard {
    private final String name;
    private final int imageResourceId;
    private final Class<? extends AppCompatActivity> activityClass;
    private final Bundle bundle;

    private Dashboard(String name, int imageResourceId, Class<? extends AppCompatActivity> activityClass, Bundle bundle) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.activityClass = activityClass;
        this.bundle = bundle;
    }

    public static Dashboard of(String name, int imageResourceId, Class<? extends AppCompatActivity> activityClass, Bundle bundle) {
        return new Dashboard(name, imageResourceId, activityClass, bundle);
    }

    public static Dashboard of(Context context, int nameResourceId, int imageResourceId, Class<? extends AppCompatActivity> activityClass, Bundle bundle) {
        return of(context.getString(nameResourceId), imageResourceId, activityClass, bundle);
    }

    public static Dashboard of(Context context, int nameResourceId, int imageResourceId, Class<? extends AppCompatActivity> activityClass) {
        return of(context.getString(nameResourceId), imageResourceId, activityClass, null);
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public Bundle getExtras() {
        return bundle;
    }

    @NonNull
    @Override
    public String toString() {
        return "Dashboard{" +
                "name='" + name + '\'' +
                ", imageResourceId=" + imageResourceId +
                '}';
    }
}
