package com.expensetracker.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expensetracker.app.ui.theme.CategoryColorHex

data class DonutSlice(val value: Float, val color: Color)

@Composable
fun DonutChart(
    slices: List<DonutSlice>,
    centerLabel: String,
    centerSubLabel: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 200.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 28.dp
) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val total = slices.sumOf { it.value.toDouble() }.toFloat()
            if (total > 0f) {
                var startAngle = -90f
                val strokePx = strokeWidth.toPx()
                slices.forEach { slice ->
                    val sweep = (slice.value / total) * 360f
                    drawArc(
                        color = slice.color,
                        startAngle = startAngle,
                        sweepAngle = sweep * 0.96f, // small gap between slices
                        useCenter = false,
                        style = Stroke(width = strokePx, cap = androidx.compose.ui.graphics.StrokeCap.Butt),
                        size = androidx.compose.ui.geometry.Size(this.size.width - strokePx, this.size.height - strokePx),
                        topLeft = androidx.compose.ui.geometry.Offset(strokePx / 2, strokePx / 2)
                    )
                    startAngle += sweep
                }
            }
        }
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(centerLabel, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(centerSubLabel, style = MaterialTheme.typography.labelSmall)
        }
    }
}

/** Fallback color for slices whose category was deleted or is null. */
fun fallbackSliceColor(index: Int): Color =
    Color(CategoryColorHex[index % CategoryColorHex.size])
