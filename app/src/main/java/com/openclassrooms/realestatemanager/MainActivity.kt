package com.openclassrooms.realestatemanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.configureTextViewMain()
        this.configureTextViewQuantity()
    }

    private fun configureTextViewMain() = textViewMain.apply {
        textSize = 15f
        text = "Le premier bien immobilier enregistré vaut "
    }

    private fun configureTextViewQuantity() = textViewQuantity.apply {
        textSize = 20f
        text = Utils.convertDollarToEuro(100).toString()
    }
}
