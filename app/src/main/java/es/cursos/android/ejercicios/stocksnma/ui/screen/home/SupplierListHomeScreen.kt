package es.cursos.android.ejercicios.stocksnma.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.ui.components.ChildCheckBox
import es.cursos.android.ejercicios.stocksnma.ui.components.ParentCheckBox
import es.cursos.android.ejercicios.stocksnma.ui.components.TableCell
import es.cursos.android.ejercicios.stocksnma.ui.components.TableHeader


@Composable
fun SupplierTable(
    suppliersList: List<SupplierEntity>,
    selectedSuppliers: Set<String>,
    selectAllSuppliers: () -> Unit,
    unselectAllSuppliers: () -> Unit,
    toggleSupplierSelection: (String) -> Unit,
    navigateToSupplierDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val parentCheckBoxState = when {
        selectedSuppliers.size == suppliersList.size -> ToggleableState.On
        selectedSuppliers.isEmpty() -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    // Columna que contiene la tabla de proveedores (debe ser scrollable)
    Column(modifier = modifier) {

        // Fila que contiene el encabezado de la tabla
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            ParentCheckBox(
                state = parentCheckBoxState,
                onClick = {
                    if (selectedSuppliers.isEmpty()) selectAllSuppliers()
                    else unselectAllSuppliers()
                },
                modifier = Modifier.width(80.dp)
            )
            //TableHeader("¿NIF?", Modifier.width(100.dp))
            TableHeader("Proveedor", Modifier.width(160.dp))
            TableHeader("Contacto ", Modifier.width(120.dp))
            TableHeader("Teléfono", Modifier.width(100.dp))
            TableHeader("Email", Modifier.width(120.dp))
            TableHeader("Dirección", Modifier.width(200.dp))
        }

        HorizontalDivider()

        // Columna que contiene las filas de la tabla
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(suppliersList) { supplier ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToSupplierDetails(supplier.id) }
                ) {
                    ChildCheckBox(
                        checked = supplier.id in selectedSuppliers,
                        onCheckedChange = { toggleSupplierSelection(supplier.id) },
                        modifier = Modifier.width(80.dp)
                    )
                    //TableCell(supplier.id, Modifier.width(100.dp))
                    TableCell(supplier.name, Modifier.width(160.dp))
                    TableCell(supplier.contactName ?: "", Modifier.width(120.dp))
                    TableCell(supplier.phone ?: "", Modifier.width(100.dp))
                    TableCell(supplier.email ?: "", Modifier.width(120.dp))
                    TableCell(supplier.address ?: "", Modifier.width(200.dp))
                }

                HorizontalDivider()
            }
        }
    }
}


@Composable
fun SupplierSearchTable(
    suppliersList: List<SupplierEntity>,
    onSupplierClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Columna que contiene la tabla de proveedores (debe ser scrollable)
    Column(modifier = modifier) {

        // Fila que contiene el encabezado de la tabla
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TableHeader("Proveedor", Modifier.width(160.dp))
            TableHeader("Contacto ", Modifier.width(120.dp))
            TableHeader("Teléfono", Modifier.width(100.dp))
            TableHeader("Email", Modifier.width(120.dp))
            TableHeader("Dirección", Modifier.width(200.dp))
        }

        HorizontalDivider()

        // Columna que contiene las filas de la tabla
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(suppliersList) { supplier ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSupplierClick(supplier.id, supplier.name) }
                ) {
                    TableCell(supplier.name, Modifier.width(160.dp))
                    TableCell(supplier.contactName ?: "", Modifier.width(120.dp))
                    TableCell(supplier.phone ?: "", Modifier.width(100.dp))
                    TableCell(supplier.email ?: "", Modifier.width(120.dp))
                    TableCell(supplier.address ?: "", Modifier.width(200.dp))
                }

                HorizontalDivider()
            }
        }
    }
}
