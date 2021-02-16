package org.wit.placemark.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.placemark.R
import org.wit.placemark.models.PlacemarkModel

class MainActivity : AppCompatActivity(), AnkoLogger {

    var placemark = ArrayList<PlacemarkModel>()

    val aPlacemark = PlacemarkModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark)

        btnAdd.setOnClickListener() {

          aPlacemark.title = placemarkTitle.text.toString()

            aPlacemark.description = placemarkDescription.text.toString()

            if (aPlacemark.title.isNotEmpty() && aPlacemark.description.isNotEmpty()) {
                placemark.add(aPlacemark.copy())

                info("add Button Pressed: $placemark")
            }
            else {
                toast ("Please Enter a title & description")
            }
        }
    }
}
