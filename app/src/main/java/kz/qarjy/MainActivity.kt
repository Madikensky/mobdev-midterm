package kz.qarjy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kz.qarjy.ui.QarjyApp
import kz.qarjy.ui.theme.QarjyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QarjyTheme {
                QarjyApp()
            }
        }
    }
}
