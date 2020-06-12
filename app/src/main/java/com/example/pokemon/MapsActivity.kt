package com.example.pokemon

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class  MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermissions()
        loadPokemon()
    }

    val AccessLocation = 123
    fun checkPermissions(){
        if (Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),AccessLocation)
                return
            }
        }
        getUserLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            AccessLocation->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }
                else{
                    Toast.makeText(this,"Access Location is denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation(){
        Toast.makeText(this,"Location Access Now",Toast.LENGTH_SHORT).show()

        val myLocation = MyLocationLitener()
        val locationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

         val myThread=MyThread()
        myThread.start()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney)
            .title("Me")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
            .snippet("here is my location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))
    }

    var myLocation:Location?=null
    inner class MyLocationLitener: LocationListener {
        constructor(){
            myLocation = Location("me")
            myLocation!!.longitude=0.0
            myLocation!!.latitude=0.0
        }
        override fun onLocationChanged(location: Location?) {
            myLocation=location

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("Not yet implemented")
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("Not yet implemented")
        }

    }

    var oldLocation:Location?=null
    inner class MyThread:Thread{
        constructor():super(){
            //TODO

            oldLocation = Location("oldLocation")
            oldLocation!!.longitude=0.0
            oldLocation!!.latitude=0.0
        }
        override fun run() {
            while (true){
                try {

                    if (oldLocation!!.distanceTo(myLocation)==0f){
                        continue
                    }
                    oldLocation =myLocation
                runOnUiThread{
                    mMap.clear()
                    val sydney = LatLng(myLocation!!.longitude, myLocation!!.latitude)
                    mMap.addMarker(MarkerOptions().position(sydney)
                        .title("Me")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                        .snippet("here is my location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))

                    //show pokemon
                    for (i in 0..listOfPockemons.size-1){

                        var newPokemon = listOfPockemons[i]
                        if (newPokemon.isCatch==false){
                            val pockyLocation = LatLng(newPokemon.location!!.longitude, newPokemon.location!!.latitude)
                            mMap.addMarker(MarkerOptions().position(pockyLocation)
                                .title(newPokemon.name)
                                .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!))
                                .snippet(newPokemon.des+" ,power is: "+newPokemon.power))

                            if (myLocation!!.distanceTo(newPokemon.location)<2)
                            {
                                myPower = myPower+newPokemon.power!!
                                newPokemon.isCatch = true
                                listOfPockemons[i] = newPokemon
                                Toast.makeText(applicationContext,"you catch a new pokemon + the new power is :$myPower",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                    Thread.sleep(1000)
                }catch (ex : Exception){

                }
            }
        }
    }

    var myPower :Double=0.0

    var listOfPockemons = ArrayList<Pokemon>()
    fun loadPokemon(){
        var pokemon1 =Pokemon(R.drawable.charmander,"charmander","charmander lives in japan",55.0,-34.0, 155.0)
        var pokemon2 =Pokemon(R.drawable.bulbasaur, "bulbasaur", "bulbasaur lives in usa", 71.0, -34.0, 152.0)
        var pokemon3 =Pokemon(R.drawable.squirtle,"squirtle","squirtle lives in egypt",30.0,-34.0, 149.0)
        var pokemon4 =Pokemon(R.drawable.mario,"mario","mario lives in all world",100.0,-34.0, 147.0)
        listOfPockemons.add(pokemon1)
        listOfPockemons.add(pokemon2)
        listOfPockemons.add(pokemon3)
        listOfPockemons.add(pokemon4)

    }
}




