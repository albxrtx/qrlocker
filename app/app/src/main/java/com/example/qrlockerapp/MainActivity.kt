package com.example.qrlockerapp

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.qrlockerapp.navigation.AppNavHost
import com.example.qrlockerapp.retrofit.TaquillaViewModel
import com.example.qrlockerapp.ui.theme.QrLockerAppTheme

import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fija la orientación de la pantalla en modo vertical.
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        
        setContent {
            QrLockerAppTheme() {
                AppNavHost()
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, taquillaViewModel: TaquillaViewModel = viewModel()) {

    val context = LocalContext.current
    // Crea un lanzador para la actividad de escaneo de códigos QR.
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            // Se ejecuta cuando el escáner devuelve un resultado.
            if (result.contents != null) {
                // Extrae el ID de la taquilla del contenido del QR.
                val idTaquilla = result.contents.substringAfterLast("/")
                if (idTaquilla.isNotBlank()) {
//                     Llamada al backend para obtener estado de la taquilla
                    taquillaViewModel.obtenerEstado(idTaquilla) { taquilla, error ->
//                        Comprobamos que la taquilla exista
                        if (taquilla != null) {
//                            Comprobamos que la taquilla no esté ocupada
                            if (!taquilla.reservado) {
                                // Navega a la pantalla de formulario si la taquilla está libre.
                                navController.navigate("form/$idTaquilla/${taquilla.nombre}")
                            } else {
                                // Muestra un mensaje si la taquilla está ocupada.
                                Toast.makeText(context, "Taquilla ocupada", Toast.LENGTH_SHORT)
                                    .show()
                            }
//                            Mostramos el error en caso de que haya
                        } else {
                            // Muestra un mensaje de error si la taquilla no se encuentra o hay otro problema.
                            Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                            navController.navigate("home")
                        }
                    }
                }
//                Mostramos un toast si se cancela el escaneo
            } else {
                // Muestra un toast si el usuario cancela el escaneo.
                Toast.makeText(context, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }

        })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000), Color(0xFF262626)
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
            Image(
                painter = painterResource(id = R.drawable.image1),
                contentDescription = "image1",
            )
            Text(
                text = "Escanea el código QR de la taquilla para comprobar su disponibilidad y realizar una reserva.",
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )


            // Botón para iniciar el escaneo de QR.
            Button(
                onClick = {
                    // Configura las opciones del escáner.
                    val options = ScanOptions().apply {
                        setCameraId(0) // Usa la cámara trasera.
                        setBeepEnabled(true) // Activa un sonido al escanear.
                        setBarcodeImageEnabled(true) // Guarda una imagen del código escaneado.
                    }
                    // Inicia el escáner de QR.
                    scanLauncher.launch(options)
                },
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
            ) {
                Text(
                    text = "Escanear QR",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
