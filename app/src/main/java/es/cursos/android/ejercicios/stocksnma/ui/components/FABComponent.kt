package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.utils.item.FABItem

/**
 * COMPOSABLE - FAB
 */
@Composable
fun CustomFABChild(
    fab: FABItem,
    isExpanded: Boolean,
    onStateChanged: () -> Unit,
) {
    val alpha: Float by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(durationMillis = 50),
        label = ""
    )

    val scale: Float by animateFloatAsState(
        targetValue = if (isExpanded) 1.0f else 0f,
        label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .alpha(animateFloatAsState(alpha, label = "").value)
            .scale(animateFloatAsState(scale, label = "").value)
    ) {

        /*
        Text(
            text = stringResource(id = fab.title),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
                .clickable(onClick = { fab.action })
        )

         */


        SmallFloatingActionButton(
            shape = CircleShape,
            modifier = Modifier.padding(horizontal = 4.dp),
            onClick = { fab.action() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(
                painter = painterResource(fab.icon),
                contentDescription = stringResource(id = fab.title)
            )
        }
    }
}


@Composable
fun CustomFAB(
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { action() },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = null,
        )
    }
}


@Composable
fun FABContainer(
    isVisible: Boolean,
    isExpanded: Boolean,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    if (isVisible) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier =
            if (isExpanded) {
                Modifier
                    .fillMaxSize()
                    .clickable { onDismiss() }
            } else {
                Modifier
            }
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                content()
            }
        }
    }
}
