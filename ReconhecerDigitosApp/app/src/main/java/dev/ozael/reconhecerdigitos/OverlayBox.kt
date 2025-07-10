package dev.ozael.reconhecerdigitos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

@Composable
fun OverlayBox() {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val boxWidth = size.width * 0.5f
    val boxHeight = size.height * 0.3f
    val left = (size.width - boxWidth) / 2
    val top = (size.height - boxHeight) / 2

    drawRect(
      color = Color.Green,
      topLeft = androidx.compose.ui.geometry.Offset(left, top),
      size = androidx.compose.ui.geometry.Size(boxWidth, boxHeight),
      style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f)
    )
  }
}