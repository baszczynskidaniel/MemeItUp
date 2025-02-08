package org.example.project.meme.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.example.project.core.presentation.design_system.AutoResizedText
import org.example.project.meme.domain.Meme
import org.example.project.meme.domain.MemeTextContent


@Composable
fun MemeImageWithTexts(
    meme: Meme,
    modifier: Modifier = Modifier,

) {
    var imageLoadResult by remember {
        mutableStateOf<Result<Painter>?>(null)
    }

    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    BoxWithConstraints (
        modifier = Modifier


    ) {

        val painter = rememberAsyncImagePainter(
            model = meme.imageUrl,
            onSuccess = {
                println("sucess load image")
                imageLoadResult =
                    if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) {
                        Result.success(it.painter)
                    } else {
                        Result.failure(Exception("Invalid image size"))
                    }
            },
            onError = {
                println("error load image")
                it.result.throwable.printStackTrace()
                imageLoadResult = Result.failure(it.result.throwable)
            },

            )
        Box {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                modifier = modifier
                    .onSizeChanged { size -> imageSize = size }



            )
        }

        meme.content.forEachIndexed { index, text ->
            val top = text.top * imageSize.height
            val bottom = text.bottom * imageSize.height
            val right = text.right * imageSize.width
            val left = text.left * imageSize.width

            Box(
                modifier = Modifier
                    .absoluteOffset(
                        x = left.toDp() ,
                        y = top.toDp()
                    )
                    .width((right - left).toDp())
                    .height((bottom - top).toDp()),
                contentAlignment = Alignment.Center
            ) {
                AutoResizedText(
                    text = if(text.text.isBlank()) "Text ${index + 1}" else text.text,
                    color = Color.Black,
                    style = MaterialTheme.typography.displayMedium.copy(textAlign = TextAlign.Center, drawStyle = Stroke(width = 2f)),

                    minFontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}





@Composable
fun Float.toDp(): Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}