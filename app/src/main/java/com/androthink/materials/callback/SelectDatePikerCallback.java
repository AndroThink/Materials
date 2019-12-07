package com.androthink.materials.callback;

import java.util.Date;

public interface SelectDatePikerCallback {
    void onDateSelected(Date date);

    void onCancel();
}
