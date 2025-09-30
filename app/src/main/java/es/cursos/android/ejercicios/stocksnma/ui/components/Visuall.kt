package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.ui.theme.StocksNMATheme

@Preview(showBackground = true)
@Composable
fun SupplierForm_CardWithHeader() {
    StocksNMATheme {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Encabezado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        //.padding(16.dp)
                ) {
                    Column {
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text(
                                text = stringResource(R.string.product_supplier),
                                style = MaterialTheme.typography.titleLarge)
                            },
                            textStyle = MaterialTheme.typography.titleLarge,
                            singleLine = true,
                            isError = false,
                            //supportingText = { Text("") },
                            colors = coloresTextFields(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text(stringResource(R.string.product_description)) },
                            singleLine = false,
                            isError = false,
                            colors = coloresTextFields(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Contenido del formulario
                Column(
                    modifier = Modifier.padding(16.dp),
                    //verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    //SupplierPrices()
                    SupplierFields()
                }
            }
        }
    }
}


@Composable
fun SupplierPrices() {
    Text(
        text = "Precios",
        fontWeight = FontWeight.Bold,
        //style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(4.dp))
    Card {
        Row() {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = "0,00€",
                    onValueChange = {},
                    label = { Text("Precio de venta") },
                    singleLine = true,
                    colors = campoColores()
                )
                //textField("Precio de compra", "")
                //textField("Precio de venta", "")
            }

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_8dp)))

            Column(
                modifier = Modifier.weight(1f)) {
                //Text("Precio de compra", fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = "0,00€",
                    onValueChange = {},
                    label = { Text("Precio de compra") },
                    singleLine = true,
                    colors = campoColores()
                )
            }
        }
    }
}


@Composable
fun SupplierFields() {
    val colores = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.background
    )

    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text("Nombre del proveedor") },
        supportingText = { Text("") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = colores
    )
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text("Nombre del contacto") },
        //supportingText = { Text("Persona de referencia dentro del proveedor") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = colores
    )
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text("Teléfono") },
        //supportingText = { Text("Número de contacto del proveedor") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        colors = colores
    )
    CustomDropDownMenuSuppliersExposed(
        selectedSupplier = "",
        onSupplierSelected = {},
    )

    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text("Dirección") },
        //supportingText = { Text("Dirección física u oficina del proveedor") },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        maxLines = 3,
        colors = colores
    )
}




@Composable
fun campoColores() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.background,
    disabledContainerColor = MaterialTheme.colorScheme.background
)

@Composable
fun textField(label: String, value: String, keyboard: KeyboardType = KeyboardType.Text) {
    TextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

