package com.example.pr24_llistadispbuetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BluetoothAdapterRecycler(
    private val devices: List<BluetoothDeviceItem>,
    private val onClick: (BluetoothDeviceItem) -> Unit
) : RecyclerView.Adapter<BluetoothAdapterRecycler.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val details: TextView = view.findViewById(R.id.tvDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_device, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        holder.name.text = device.name ?: "Dispositiu sense nom"
        holder.details.text = device.address

        holder.itemView.setOnClickListener {
            onClick(device)
        }
    }

    override fun getItemCount() = devices.size
}
