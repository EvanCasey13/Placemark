package org.wit.placemark.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_placemark.*
import kotlinx.android.synthetic.main.activity_placemark.placemarkTitle
import kotlinx.android.synthetic.main.activity_placemark_list.*
import kotlinx.android.synthetic.main.card_placemark.*
import org.jetbrains.anko.*
import org.wit.placemark.R
import org.wit.placemark.helpers.readImage
import org.wit.placemark.helpers.readImageFromPath
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.Location
import org.wit.placemark.models.PlacemarkModel

class PlacemarkActivity : AppCompatActivity(), AnkoLogger {

    var aPlacemark = PlacemarkModel()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1

    val LOCATION_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark)

        app = application as MainApp
        var edit = false
        val titlePopReturn: String = getString(R.string.text_returnTitle)

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (aPlacemark.zoom != 0f) {
                location.lat =  aPlacemark.lat
                location.lng = aPlacemark.lng
                location.zoom = aPlacemark.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            aPlacemark = intent.extras?.getParcelable<PlacemarkModel>("placemark_edit")!!
            placemarkTitle.setText(aPlacemark.title)
            placemarkDescription.setText(aPlacemark.description)
            placemarkImage.setImageBitmap(readImageFromPath(this, aPlacemark.image))
            btnAdd.setText(R.string.button_savePlacemark)
            chooseImage.setText(R.string.button_changeImage)
        }

        btnAdd.setOnClickListener() {
            aPlacemark.title = placemarkTitle.text.toString()
            aPlacemark.description = placemarkDescription.text.toString()
            placemarkImage.setImageBitmap(readImageFromPath(this, aPlacemark.image))
            if (aPlacemark.title.isEmpty()) {
                toast(titlePopReturn)
            } else {
                if (edit) {
                    app.placemarks.update(aPlacemark.copy())
                } else {
                    app.placemarks.create(aPlacemark.copy())
                }
            }
            info("add Button Pressed: $placemarkTitle")
            setResult(RESULT_OK)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    aPlacemark.image = data.getData().toString()
                    placemarkImage.setImageBitmap(readImage(this, resultCode, data))
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    aPlacemark.lat = location.lat
                    aPlacemark.lng = location.lng
                    aPlacemark.zoom = location.zoom
                }
            }
        }
    }
}
