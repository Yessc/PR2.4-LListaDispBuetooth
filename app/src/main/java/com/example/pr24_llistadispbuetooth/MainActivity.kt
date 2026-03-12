package com.example.pr24_llistadispbuetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    // RecyclerView que mostrará la lista de dispositivos
    private lateinit var recyclerView: RecyclerView

    // Adapter de la RecyclerView
    private lateinit var deviceAdapter: BluetoothAdapterRecycler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)

        // Definimos que la RecyclerView será una lista vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Comprobamos permisos y cargamos los dispositivos Bluetooth
        checkPermissionAndLoad()
    }

    private fun checkPermissionAndLoad() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 o superior
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Pedimos el permiso
                requestPermissions(
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    100
                )
                return
            }
        }
        // Si ya tenemos permiso, cargamos los dispositivos
        loadBluetoothDevices()
    }

    @SuppressLint("MissingPermission") // Evita error de compilador en Android 12+
    private fun loadBluetoothDevices() {


        val btAdapter = BluetoothAdapter.getDefaultAdapter()

        val pairedDevices = btAdapter?.bondedDevices ?: emptySet()

        // Convertimos los dispositivos reales a nuestra data class
        val deviceList = pairedDevices.map {
            BluetoothDeviceItem(
                name = it.name,
                address = it.address
            )
        }

        // Creamos el adapter de la RecyclerView
        // Pasamos la lista y la función de click de cada item
        deviceAdapter = BluetoothAdapterRecycler(deviceList) { device ->
            // Acción al hacer click sobre un item: mostramos diálogo
            showDeviceDialog(device)
        }

        // Asignamos el adapter a la RecyclerView
        recyclerView.adapter = deviceAdapter
    }

    /**
     * Mostrar un diálogo con los detalles del dispositivo seleccionado
     */
    private fun showDeviceDialog(device: BluetoothDeviceItem) {
        AlertDialog.Builder(this)
            .setTitle(device.name ?: "Dispositivo Bluetooth") // Nombre o texto por defecto
            .setMessage("Dirección MAC:\n${device.address}")  // Dirección MAC
            .setPositiveButton("OK", null) // Botón OK
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Comprobamos que es nuestro request y que se ha concedido
        if (requestCode == 100 &&
            grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
        ) {
            // Volvemos a cargar los dispositivos
            loadBluetoothDevices()
        }
    }
}
