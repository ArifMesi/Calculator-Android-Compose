package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F9FA)
                ) {
                    KalkulatorGoogleStyle()
                }
            }
        }
    }
}

@Composable
fun KalkulatorGoogleStyle() {
    var ekspresi by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf("0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- 1. LAYAR DISPLAY ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                if (ekspresi.isNotEmpty()) {
                    Text(
                        text = ekspresi,
                        fontSize = 20.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    text = hasil,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF1F1F1F),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 2. GRID TOMBOL ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Skema Warna
            val warnaAngka = Color(0xFFF2F2F2)
            val warnaFungsi = Color(0xFFE8ECEF)
            val warnaSamaDengan = Color(0xFF1A73E8)

            // Baris 1: %, AC, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TombolKalkulator("%", warnaFungsi, Color(0xFF1F1F1F), Modifier.weight(1f)) {
                    if (hasil != "0") hasil += "%"
                }
                TombolKalkulator("AC", warnaFungsi, Color(0xFF1F1F1F), Modifier.weight(1f)) {
                    ekspresi = ""
                    hasil = "0"
                }
                TombolKalkulator("÷", warnaFungsi, Color(0xFF1F1F1F), Modifier.weight(1f)) {
                    if (hasil != "0") {
                        ekspresi = "$hasil ÷ "
                        hasil = "0"
                    }
                }
            }

            // Baris 2: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TombolKalkulator("7", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "7") }
                TombolKalkulator("8", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "8") }
                TombolKalkulator("9", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "9") }
                TombolKalkulator("×", warnaFungsi, Color(0xFF1F1F1F), Modifier.weight(1f)) {
                    if (hasil != "0") {
                        ekspresi = "$hasil × "
                        hasil = "0"
                    }
                }
            }

            // Baris 3: 4, 5, 6, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TombolKalkulator("4", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "4") }
                TombolKalkulator("5", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "5") }
                TombolKalkulator("6", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "6") }
                TombolKalkulator("-", warnaFungsi, Color(0xFF1F1F1F), Modifier.weight(1f)) {
                    if (hasil != "0") {
                        ekspresi = "$hasil - "
                        hasil = "0"
                    }
                }
            }

            // Baris 4: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TombolKalkulator("1", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "1") }
                TombolKalkulator("2", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "2") }
                TombolKalkulator("3", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "3") }
                TombolKalkulator("+", warnaFungsi, Color(0xFF1F1F1F), Modifier.weight(1f)) {
                    if (hasil != "0") {
                        ekspresi = "$hasil + "
                        hasil = "0"
                    }
                }
            }

            // Baris 5: 0, ., =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TombolKalkulator("0", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) { hasil = tambahAngka(hasil, "0") }
                TombolKalkulator(".", warnaAngka, Color(0xFF1F1F1F), Modifier.weight(1f)) {
                    if (!hasil.contains(".")) hasil += "."
                }
                TombolKalkulator("=", warnaSamaDengan, Color.White, Modifier.weight(2f)) {
                    if (ekspresi.isNotEmpty()) {
                        val hitung = hitungHasil(ekspresi, hasil)
                        ekspresi = ""
                        hasil = hitung
                    }
                }
            }
        }
    }
}

// --- FUNGSI KOMPONEN TOMBOL ---
@Composable
fun TombolKalkulator(
    label: String,
    warnaLatar: Color,
    warnaTeks: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(warnaLatar)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = warnaTeks
        )
    }
}

// --- LOGIKA HITUNG DERET ---
fun tambahAngka(saatIni: String, angka: String): String {
    return if (saatIni == "0") angka else saatIni + angka
}

fun hitungHasil(ekspresi: String, angkaKeduaStr: String): String {
    try {
        val bagian = ekspresi.trim().split(" ")
        if (bagian.size < 2) return angkaKeduaStr

        val angka1 = bagian[0].toDoubleOrNull() ?: 0.0
        val operator = bagian[1]
        val angka2 = angkaKeduaStr.toDoubleOrNull() ?: 0.0

        val res = when (operator) {
            "+" -> angka1 + angka2
            "-" -> angka1 - angka2
            "×" -> angka1 * angka2
            "÷" -> if (angka2 != 0.0) angka1 / angka2 else Double.NaN
            else -> angka2
        }

        return if (res.isNaN()) {
            "Error"
        } else if (res % 1.0 == 0.0) {
            res.toLong().toString()
        } else {
            res.toString()
        }
    } catch (e: Exception) {
        return "Error"
    }
}