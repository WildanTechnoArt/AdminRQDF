package com.wildan.adminrqdf.view

import androidx.appcompat.app.AlertDialog

class ApprovedView {

    interface View {
        fun onSuccess(message: String)
        fun handleError(message: String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter {
        fun postData(userId: String?, data: HashMap<String, String?>)
    }
}