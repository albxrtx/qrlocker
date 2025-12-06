package com.example.qrlockerapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.sp
import com.example.qrlockerapp.ui.theme.QrLockerAppTheme

import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            enableEdgeToEdge()
            QrLockerAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF000000), // Negro oscuro
                                    Color(0xFF1A1A1A)  // Negro menos oscuro
                                )
                            )
                        )
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "QrLocker",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
        )

        ScanButton(
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Composable
fun ScanButton(modifier: Modifier = Modifier) {
    var resultadoEscaneo by remember { mutableStateOf("Escanear") }

    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(), onResult = { result ->
            resultadoEscaneo = result.contents ?: "Sin resultado"
        })
    Button(
        onClick = { scanLauncher.launch(ScanOptions()) },
        modifier = Modifier.height(50.dp).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
    ) {
        Text(
            text = resultadoEscaneo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    QrLockerAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF393939), // Negro oscuro
                            Color(0xFF000000)  // Negro menos oscuro
                        )
                    )
                )
        ) {
            HomeScreen()
        }
    }
}
