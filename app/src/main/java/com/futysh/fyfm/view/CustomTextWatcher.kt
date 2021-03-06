package com.futysh.fyfm.view

import android.text.Editable
import android.text.TextWatcher

abstract class CustomTextWatcher : TextWatcher {

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

}