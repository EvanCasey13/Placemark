package org.wit.placemark.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_placemark.*
import kotlinx.android.synthetic.main.activity_placemark_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel

class PlacemarkActivity : AppCompatActivity(), AnkoLogger {

    var aPlacemark = PlacemarkModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark)

        app = application as MainApp
        var edit = false
        val titlePopReturn: String = getString(R.string.text_returnTitle)

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            aPlacemark = intent.extras?.getParcelable<PlacemarkModel>("placemark_edit")!!
            placemarkTitle.setText(aPlacemark.title)
            placemarkDescription.setText(aPlacemark.description)
            btnAdd.setText(R.string.button_savePlacemark)
        }

        btnAdd.setOnClickListener() {
            aPlacemark.title = placemarkTitle.text.toString()
            aPlacemark.description = placemarkDescription.text.toString()
            if (aPlacemark.title.isEmpty()) {
                toast(titlePopReturn)
            } else {
                if (edit){
                    app.placemarks.update(aPlacemark.copy())
                } else {
                    app.placemarks.create(aPlacemark.copy())
                }
            }
            info("add Button Pressed: $placemarkTitle")
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> startActivityForResult<PlacemarkListActivity>(0)

        }
        return super.onOptionsItemSelected(item)
    }

}
