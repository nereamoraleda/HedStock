package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import es.cursos.android.ejercicios.stocksnma.R

@Composable
fun GeneralIconButton(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = description
        )
    }
}


/**
 * COMPOSABLE - ICONO PARA VOLVER A LA SCREEN ANTERIOR
 */
@Composable
fun NavigateBackButton(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onNavigateBack() },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = stringResource(R.string.go_back_icon_description)
        )
    }
}


///**
// * COMPOSABLE - ICONO PARA ABRIR EL NAVIGATION DRAWER
// */
//@Composable
//fun IconButtonOpenNavDrawer(drawerState: DrawerState, scope: CoroutineScope) {
//    IconButton(onClick = {
//        scope.launch {
//            drawerState.apply {
//                if (isClosed) open() else close()
//            }
//        }
//    }
//    ) {
//        Icon(
//            painter = painterResource(R.drawable.ic_menu),
//            //tint = MaterialTheme.colorScheme.secondary,
//            contentDescription = stringResource(R.string.open_menu_drawer_icon_description)
//        )
//    }
//}


///**
// * COMPOSABLE - ICONO PARA ELIMINAR
// */
//@Composable
//fun IconButtonDelete(
//    onClick: () -> Unit,
//    enabled: Boolean = true,
//    modifier: Modifier = Modifier
//) {
//    IconButton(
//        onClick = onClick,
////        enabled = enabled,
//        modifier = modifier
//    ) {
//        Icon(
//            painter = painterResource(R.drawable.ic_delete),
//            contentDescription = null
//        )
//    }
//}
