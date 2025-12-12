package com.example.qrlockerapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qrlockerapp.retrofit.ReservaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun FormScreen(
    idTaquilla: String,
    nombreTaquilla: String,
    viewModel: ReservaViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var fechaHora by remember { mutableStateOf("") }

    // Crea un diálogo de selección de fecha.
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Cuando se selecciona una fecha, muestra un diálogo de selección de hora.
            val timePickerDialog = TimePickerDialog(
                context, { _, hourOfDay, minute ->
                    // Cuando se selecciona una hora, formatea la fecha y la hora y actualiza el estado.
                    fechaHora = String.format(
                        "%04d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true // true para formato de 24 horas.
            )
            timePickerDialog.show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000), Color(0xFF1A1A1A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "QrLocker",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clock_image),
                    contentDescription = "image1",
                )
                Text(
                    text = "Indica hasta cuándo quieres usar la taquilla seleccionando una fecha y hora de fin de reserva.",
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }


            Column(
                horizontalAlignment = Alignment.Start
            ) {

                Text(color = Color.White, text = "Taquilla: $nombreTaquilla")

                OutlinedTextField(
                    value = fechaHora,
                    onValueChange = { },
                    label = { Text("Selecciona la fecha") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        // Icono de calendario que muestra el diálogo de selección de fecha al hacer clic.
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    })

                Spacer(modifier = Modifier.height(24.dp))
                // Botón para crear la reserva.
                Button(
                    onClick = {
                        // Comprueba si se ha seleccionado una fecha.
                        if (fechaHora.isNotEmpty()) {
                            // Llama al ViewModel para crear la reserva.
                            viewModel.crearReserva(idTaquilla, fechaHora) { success, msg ->
                                if (success) {
                                    // Muestra un mensaje de éxito.
                                    Toast.makeText(context, "Reserva creada", Toast.LENGTH_SHORT)
                                        .show()
                                    // Navega a la pantalla de inicio después de un breve retraso.
                                    CoroutineScope(Dispatchers.Main).launch {
                                        delay(2000L)
                                        navController.navigate("home") {
                                            // Limpia el back stack hasta la pantalla de inicio.
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                } else {
                                    // Muestra un mensaje de error.
                                    Toast.makeText(
                                        context, "Error al crear la reserva", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            // Muestra un mensaje si no se ha seleccionado una fecha.
                            Toast.makeText(
                                context, "La fecha no puede estar vacia", Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                ) {
                    Text(
                        text = "Reservar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

}
