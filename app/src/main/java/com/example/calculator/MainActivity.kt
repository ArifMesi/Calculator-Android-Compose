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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // === [BARU]: State untuk fitur Dark Mode ===
                var isDarkMode by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // === [BARU]: Warna latar aplikasi berubah dinamis sesuai tema ===
                    color = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
                ) {
                    KalkulatorGoogleStyle(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { isDarkMode = !isDarkMode }
                    )
                }
            }
        }
    }
}

@Composable
fun KalkulatorGoogleStyle(
    isDarkMode: Boolean,                      // === [BARU]: Parameter status Dark Mode ===
    onToggleDarkMode: () -> Unit             // === [BARU]: Parameter callback ganti tema ===
) {
    var ekspresi by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf("0") }

    // === [BARU]: Penentuan warna dinamis berdasarkan Light / Dark Mode ===
    val warnaLatarLayar = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val warnaTeksUtama = if (isDarkMode) Color.White else Color(0xFF1F1F1F)
    val warnaTeksSekunder = if (isDarkMode) Color.LightGray else Color.Gray

    val warnaAngka = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF2F2F2)
    val warnaFungsi = if (isDarkMode) Color(0xFF3B3B3B) else Color(0xFFE8ECEF)
    val warnaSamaDengan = Color(0xFF1A73E8) // Biru khas Google

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
                .background(warnaLatarLayar) // === [BARU]: Diubah ke warnaLatarLayar ===
                .padding(24.dp)
        ) {
            // === [UBAH]: Ganti IconButton lama dengan Switch ===
            Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggleDarkMode() },
                modifier = Modifier.align(Alignment.TopStart),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF1A73E8),
                    uncheckedThumbColor = Color(0xFF5F6368),
                    uncheckedTrackColor = Color(0xFFE8ECEF)
                )
            )

            Column(
                modifier = Modifier.align(Alignment.BottomEnd),
                horizontalAlignment = Alignment.End
            ) {
                if (ekspresi.isNotEmpty()) {
                    Text(
                        text = ekspresi,
                        fontSize = 20.sp,
                        color = warnaTeksSekunder, // === [BARU]: Diubah ke warnaTeksSekunder ===
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    text = hasil,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Normal,
                    color = warnaTeksUtama, // === [BARU]: Diubah ke warnaTeksUtama ===
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
            // Baris 1: AC, ⌫ (Backspace), ± (Plus/Minus), ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TombolKalkulator("AC", warnaFungsi, warnaTeksUtama, Modifier.weight(1f)) {
                    ekspresi = ""
                    hasil = "0"
                }

                // === [BARU]: Tombol Backspace (⌫) ===
                TombolKalkulator("⌫", warnaFungsi, warnaTeksUtama, Modifier.weight(1f)) {
                    if (hasil.length > 1) {
                        hasil = hasil.dropLast(1)
                    } else {
                        hasil = "0"
                    }
                }

                // === [BARU]: Tombol Plus/Minus (±) ===
                TombolKalkulator("±", warnaFungsi, warnaTeksUtama, Modifier.weight(1f)) {
                    if (hasil != "0") {
                        hasil = if (hasil.startsWith("-")) {
                            hasil.removePrefix("-")
                        } else {
                            "-$hasil"
                        }
                    }
                }

                TombolKalkulator("÷", warnaFungsi, warnaTeksUtama, Modifier.weight(1f)) {
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
                TombolKalkulator("7", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "7") }
                TombolKalkulator("8", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "8") }
                TombolKalkulator("9", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "9") }
                TombolKalkulator("×", warnaFungsi, warnaTeksUtama, Modifier.weight(1f)) {
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
                TombolKalkulator("4", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "4") }
                TombolKalkulator("5", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "5") }
                TombolKalkulator("6", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "6") }
                TombolKalkulator("-", warnaFungsi, warnaTeksUtama, Modifier.weight(1f)) {
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
                TombolKalkulator("1", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "1") }
                TombolKalkulator("2", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "2") }
                TombolKalkulator("3", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "3") }
                TombolKalkulator("+", warnaFungsi, warnaTeksUtama, Modifier.weight(1f)) {
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
                TombolKalkulator("0", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) { hasil = tambahAngka(hasil, "0") }
                TombolKalkulator(".", warnaAngka, warnaTeksUtama, Modifier.weight(1f)) {
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