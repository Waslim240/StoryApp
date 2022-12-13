package com.waslim.storyapp.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.waslim.storyapp.R

class EmailEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateEmail(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun validateEmail(email: String) {
        when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty() -> showError()
            else -> return
        }
    }

    private fun showError() {
        error = context.getString(R.string.email_harus_valid)
    }
}