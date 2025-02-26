package com.example.fisioapp.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

// Función para codificar el email
fun String.encodeNode(): String {
    return this.replace("@", "_AT_").replace(".", "_DOT_")
}

// Función para decodificar el email
fun String.emailExtract(): String {
    return this.replace("_AT_", "@").replace("_DOT_", ".")
}

// Función para formatear la fecha
fun Long.fechaFormateada(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    return format.format(date)
}

// Función para mostrar el DatePicker, un dialog personalizado para que el usuario pueda seleccionar la fecha
fun EditText.mostrarDatePicker(requireContext: Context) {
    val calendario = Calendar.getInstance()
    val año = calendario.get(Calendar.YEAR)
    val mes = calendario.get(Calendar.MONTH)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)

    // Creamos el DatePicker
    val datePickerDialog = DatePickerDialog(
        requireContext,
        { _, añoSeleccionado, mesSeleccionado, diaSeleccionado ->
            val fecha = "$diaSeleccionado/${mesSeleccionado + 1}/$añoSeleccionado"
            this.setText(fecha)
        },
        año,
        mes,
        dia
    )

    // Restringimos fechas futuras
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    // Mostramos el DatePicker
    datePickerDialog.show()
}