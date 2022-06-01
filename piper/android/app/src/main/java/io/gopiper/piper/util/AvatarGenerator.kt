package io.gopiper.piper.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.text.TextPaint

object AvatarGenerator {
    var texSize = 0F
    val colors = listOf(
        Color.parseColor("#16a085"),
        Color.parseColor("#27ae60"),
        Color.parseColor("#2980b9"),
        Color.parseColor("#8e44ad"),
        Color.parseColor("#2c3e50"),
        Color.parseColor("#f39c12"),
        Color.parseColor("#d35400"),
        Color.parseColor("#c0392b"),
    )

    fun avatarImage(context: Context, size: Int, shape: Int, name: String): BitmapDrawable {
        val width = size
        val hieght = size

        texSize = calTextSize(size)
        val label = firstCharacter(name)
        val textPaint = textPainter(context)
        val painter = painter()
        val areaRect = Rect(0, 0, width, width)

        if (shape == 0) {
            painter.color = colors.random()
        } else {
            painter.color = Color.TRANSPARENT
        }

        val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawRect(areaRect, painter)

        //reset painter
        if (shape == 0) {
            painter.color = Color.TRANSPARENT
        } else {
            painter.color = colors.random()
        }

        val bounds = RectF(areaRect)
        bounds.right = textPaint.measureText(label, 0, 1)
        bounds.bottom = textPaint.descent() - textPaint.ascent()

        bounds.left += (areaRect.width() - bounds.right) / 2.0f
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f

        canvas.drawCircle(width.toFloat() / 2, hieght.toFloat() / 2, width.toFloat() / 2, painter)
        canvas.drawText(label, bounds.left, bounds.top - textPaint.ascent(), textPaint)
        return BitmapDrawable(context.resources, bitmap)

    }

    private fun firstCharacter(name: String): String {
        return name.first().toString().uppercase()
    }

    private fun textPainter(context: Context): TextPaint {
        val textPaint = TextPaint()
        textPaint.textSize = texSize * context.resources.displayMetrics.scaledDensity
        textPaint.color = Color.WHITE
        return textPaint
    }

    private fun painter(): Paint {
        return Paint()
    }

    private fun calTextSize(size: Int): Float {
        return (size / 3.125).toFloat()
    }
}