package com.liu.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ma.*
import org.jetbrains.anko.startActivity

class MaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ma)
        btnList.setOnClickListener {
            startActivity<MainActivity>()
        }
        btnMvvm.setOnClickListener {
            startActivity<MvvmActivity>()
        }
    }
}
