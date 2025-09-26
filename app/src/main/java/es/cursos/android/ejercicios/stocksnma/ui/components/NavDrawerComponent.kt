package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.utils.items.NavDrawerItem
import es.cursos.android.ejercicios.stocksnma.utils.enums.HomeSections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AppNavigationDrawer(
    navItems: List<NavDrawerItem>,
    selectedItem: HomeSections,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    ModalDrawerSheet(
        drawerShape = RoundedCornerShape(topEnd = 24.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        //modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.85f)
    ) {
        HeaderNavDrawer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        )

        GeneralHorizontalDivider()

        BodyNavDrawer(
            items = navItems,
            selectedItem = selectedItem,
            drawerState = drawerState,
            scope = scope,
        )
    }
}


/**
 * COMPOSABLE - HEADER NAVIGATION DRAWER
 */
@Composable
fun HeaderNavDrawer(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_logo_import),
            modifier = Modifier.size(36.dp),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )
    }
}


/**
 * COMPOSABLE - BODY NAVIGATION DRAWER
 */
@Composable
fun BodyNavDrawer(
    items: List<NavDrawerItem>,
    selectedItem: HomeSections,
    drawerState: DrawerState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = dimensionResource(R.dimen.padding_small))
            .padding(vertical = dimensionResource(R.dimen.padding_medium))
            .verticalScroll(rememberScrollState())
    ) {
        items.forEach { item ->
            if (item is NavDrawerItem.Item) {
                val selected = when (item.title) {
                    R.string.purchase_order_title -> selectedItem == HomeSections.PURCHASE_ORDERS
                    R.string.product_title -> selectedItem == HomeSections.PRODUCTS
                    R.string.discount_title -> selectedItem == HomeSections.DISCOUNTS
                    R.string.supplier_title -> selectedItem == HomeSections.SUPPLIERS
                    R.string.user_title -> selectedItem == HomeSections.USERS
                    R.string.settings_title -> selectedItem == HomeSections.SETTINGS
                    R.string.about_title -> selectedItem == HomeSections.ABOUT
                    else -> false
                }

                ItemNavDrawer(
                    label = item.title,
                    selected = selected,
                    iconSelected = item.iconSelected,
                    iconUnselected = item.iconUnselected,
                    count = item.count,
                    action = {
                        item.action()
                        scope.launch { drawerState.close() }
                    }
                )
            }

            else {
                HorizontalDivider(color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}


@Composable
fun ItemNavDrawer(
    label: Int,
    selected: Boolean,
    iconSelected: Int,
    iconUnselected: Int,
    count: Int? = null,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationDrawerItem(
        label = { Text(text = stringResource(label)) },
        selected = selected,
        icon = {
            Icon(
                if (selected) painterResource(iconSelected)
                else painterResource(iconUnselected),
                contentDescription = null
            )
        },
        onClick = { action() },
        badge = { if (count != null) Text(text = count.toString())},
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.surface,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            selectedBadgeColor = MaterialTheme.colorScheme.primary,
            unselectedContainerColor = Color.Transparent
        )
    )
}

/*
@Composable
fun CustomBadge(count: Int) {
    //badge = { CustomBadge(item.count) }, -> Añadir al composable NavigationDrawerItem

    Badge(
        containerColor = Color.Green,
        contentColor = Color.Black
    ) {
        Text(text = count.toString())
    }
}
 */
