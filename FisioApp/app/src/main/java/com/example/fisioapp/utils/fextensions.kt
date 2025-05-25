package com.example.fisioapp.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

fun String.convertirFecha(): String? {
    // Dividir la fecha en partes (día, mes, año)
    val partes = this.split("/")

    if (partes.size < 3) {
        return null
    }

    // Obtener el día, mes y año
    val dia = partes[0].padStart(2, '0')   // Aseguramos que el día tenga 2 dígitos
    val mes = partes[1].padStart(2, '0')   // Aseguramos que el mes tenga 2 dígitos
    val año = partes[2]

    // Retornar la fecha en formato aaaa-mm-dd
    return "$año-$mes-$dia"
}

fun String.convertirFechaDesdeBaseDeDatos(): String {
    // Cortamos la parte de la fecha ISO, quitando la hora y la zona horaria
    val fechaSinHora = this.substring(0, 10)  // "2025-04-05"

    // Parseamos la fecha sin la hora (solo año, mes y día)
    val fecha = LocalDate.parse(fechaSinHora, DateTimeFormatter.ISO_DATE)

    // Sumamos un día para corregir el desfase (si la fecha viene un día antes)
    val fechaCorregida = fecha.plusDays(1)

    // Formateamos la fecha en el formato deseado "d/MM/yyyy"
    return fechaCorregida.format(DateTimeFormatter.ofPattern("d/M/yyyy"))
}


// Función para mostrar el DatePicker, un dialog personalizado para que el usuario pueda seleccionar la fecha
fun EditText.mostrarDatePicker(requireContext: Context, maxToday: Boolean) {
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

    // Restringimos fechas si es una fecha de nacimiento
    if (maxToday) {
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
    }

    // Mostramos el DatePicker
    datePickerDialog.show()
}