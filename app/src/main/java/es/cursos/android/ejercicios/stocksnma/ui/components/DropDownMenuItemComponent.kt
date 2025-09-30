package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.utils.items.DropDownMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralExposedDropDownBox(
    expandedMenu: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    valueSelected: String,
    label: String,
    contentDropDownMenu: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expandedMenu,
        onExpandedChange = { onExpandedChange(!expandedMenu) }
    ) {
        GeneralOutlinedTextField(
            value = valueSelected,
            onValueChange = {},
            label = label,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenu) },
            readOnly = true,
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandedMenu,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            contentDropDownMenu()
        }
    }
}


@Composable
fun DobleDropDownMenu(
    sortOptions: List<DropDownMenuItem> = emptyList(),
    groupOptions: List<DropDownMenuItem> = emptyList(),
    isMenuExpanded: Boolean,
    onClickIconButton: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        GeneralIconButton(
            onClick = { onClickIconButton() },
            icon = R.drawable.ic_menu_filter,
        )

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { onDismissRequest() },
            content = {
                Column {
                    sortOptions.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                item.action()
                                onDismissRequest()
                            },
                            text = {
                                Text(
                                    text = stringResource(item.title),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_8dp))
                                )
                            },
                            leadingIcon = {
                                if (item.selected) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = null
                                    )
                                }
                            }
                        )

                        if (item != sortOptions.last()) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        }
                    }
                }

                HorizontalDivider(
                    thickness = 6.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Column {
                    groupOptions.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                item.action()
                                onDismissRequest()
                            },
                            text = {
                                Text(
                                    text = stringResource(item.title),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_8dp))
                                )
                            },
                            leadingIcon = {
                                if (item.selected) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = null
                                    )
                                }
                            }
                        )

                        if (item != groupOptions.last()) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        }
                    }
                }
            }
        )
    }
}

/**
 * COMPOSABLE - DROPDOWN MENU E ITEMS
 */
@Composable
fun FilterDropDownMenu(
    filterOptions: List<DropDownMenuItem> = emptyList(),
    isMenuExpanded: Boolean,
    onClickIconButton: () -> Unit,
    onDismissRequest: () -> Unit,
    itemSelected: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        GeneralIconButton(
            onClick = { onClickIconButton() },
            icon = R.drawable.ic_menu_filter,
        )

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { onDismissRequest() },
            content = {
                Column {
                    filterOptions.forEach { item ->
                        DropdownMenuItem(
                            onClick = item.action,
                            text = {
                                Text(
                                    text = stringResource(item.title),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_8dp))
                                )
                            },
                            leadingIcon = {
                                if (item.selected) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = null
                                    )
                                }
                            }
                        )

                        if (item != filterOptions.last()) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        }
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenuSuppliersExposed(
    selectedSupplier: String,
    supplierOptions: List<SupplierEntity> = emptyList(),
    onSupplierSelected: (String) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
    messageError: String? = null
) {

    // VARIABLE - Menú expandido (Boolean)
    val selectedSupplierName = supplierOptions.find { it.id == selectedSupplier }?.name ?: ""
    var isExpanded by remember { mutableStateOf(false) }
//    var search by remember { mutableStateOf(selectedSupplierName) }
//    val filteredOptions = supplierOptions.filter {
//        it.name.contains(selectedSupplier, ignoreCase = true)
//    }


    // ELEMENTOS COMPOSABLE - Menú desplegable
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { if (enabled) isExpanded = !isExpanded },
    ) {
        OutlinedTextField(
            value = selectedSupplierName,
            onValueChange = {  },
            placeholder = { Text(text = stringResource(R.string.select_supplier)) },
            label = { Text(text = stringResource(R.string.product_supplier)) },
            readOnly = true,
            enabled = enabled,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = messageError ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = colorsDropDownMenu(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            when {
                supplierOptions.isEmpty() -> {
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.no_suppliers_available)) },
                            onClick = { isExpanded = false }
                        )
                    }
                }

                else -> {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.no_supplier)) },
                        onClick = {
                            onSupplierSelected("")
                            isExpanded = false
                        },
                    )
                    GeneralHorizontalDivider()
                    supplierOptions.forEach { supplier ->
                        DropdownMenuItem(
                            text = { Text(text = supplier.name) },
                            onClick = {
                                onSupplierSelected(supplier.id)
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenuCategoriesExposed(
    selectedCategory: String? = null,
    onCategorySelected: (String) -> Unit,
    onClickCreateCategory: () -> Unit,
    onClickEditCategory: (String, Int) -> Unit,
    onClickDeleteCategory: (String, Int) -> Unit,
    categoryOptions: List<CategoryEntity> = emptyList(),
    enabled: Boolean = true,
    isError: Boolean = false,
    messageError: String? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedCategoryId = selectedCategory?.toIntOrNull()
    val selectedCategoryName = categoryOptions.find { it.id == selectedCategoryId }?.name ?: ""
    var searchQuery by remember { mutableStateOf(selectedCategoryName) }
    val filteredOptions = categoryOptions.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        OutlinedTextField(
            value = searchQuery.ifBlank { "" },
            onValueChange = {
                searchQuery = it
                isExpanded = true

//                // Si el usuario en vez de seleccionar el item lo escribe
//                val matchedCategory = categoryOptions.find { category ->
//                    category.name.equals(it, ignoreCase = true)
//                }
//
//                if (matchedCategory != null) {
//                    onCategorySelected(matchedCategory.id.toString())
//                } else {
//                    onCategorySelected("") // No coincide con ninguna categoría válida
//                }
            },
            singleLine = true,
            readOnly = false,
            enabled = enabled,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = messageError ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            placeholder = { Text(stringResource(R.string.select_category)) },
            label = { Text(stringResource(R.string.product_category)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = colorsDropDownMenu(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.create_category)) },
                onClick = {
                    onClickCreateCategory()
                    isExpanded = false
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            )

            GeneralHorizontalDivider()

            DropdownMenuItem(
                text = { Text(stringResource(R.string.product_no_category)) },
                onClick = {
                    onCategorySelected("")
                    isExpanded = false
                },
            )

            when {
                categoryOptions.isEmpty() -> {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.no_categories_available)) },
                        onClick = { isExpanded = false }
                    )
                }

                filteredOptions.isEmpty() -> {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.search_no_results)) },
                        onClick = { isExpanded = false }
                    )
                }

                else -> {
                    filteredOptions.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            trailingIcon = {
                                Row {
                                    GeneralIconButton(
                                        onClick = { onClickEditCategory(category.name, category.id) },
                                        icon = R.drawable.ic_edit
                                    )
                                    GeneralIconButton(
                                        onClick = { onClickDeleteCategory(category.name, category.id) },
                                        icon = R.drawable.ic_delete
                                    )
                                }

                            },
                            onClick = {
                                searchQuery = category.name
                                onCategorySelected(category.id.toString())
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun colorsDropDownMenu() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.background,
    disabledContainerColor = MaterialTheme.colorScheme.background,
    disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
)
