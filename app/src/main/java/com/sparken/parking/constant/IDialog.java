package com.sparken.parking.constant;

import android.content.DialogInterface;

/**
 * Created by root on 7/28/16.
 */
public interface IDialog {

    public abstract void PositiveMethod(DialogInterface dialog, int id);
    public abstract void NegativeMethod(DialogInterface dialog, int id);
}
