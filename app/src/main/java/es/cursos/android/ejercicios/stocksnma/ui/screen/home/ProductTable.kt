package es.cursos.android.ejercicios.stocksnma.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import es.cursos.android.ejercicios.stocksnma.utils.formatedPrice
import es.cursos.android.ejercicios.stocksnma.ui.components.TableCell
import es.cursos.android.ejercicios.stocksnma.ui.components.TableHeader

@Composable
fun ProductSearchTable(
    products: List<ProductWithSupplierAndCategory>,
    onProductClick: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_16dp))
            .horizontalScroll(rememberScrollState())
    ) {
        ProductTableHeader()
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(products) { product ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { onProductClick(product.product.id, product.product.name) }
                        .padding(end = dimensionResource(R.dimen.padding_16dp))
                ) {
                    ProductTableBody(product)
                }
            }
        }
    }
}


@Composable
fun ProductTableHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TableHeader(stringResource(R.string.product_name), Modifier.width(120.dp))
        TableHeader(stringResource(R.string.product_brand), Modifier.width(100.dp))/**/
        TableHeader(stringResource(R.string.product_description), Modifier.width(200.dp))
        TableHeader(stringResource(R.string.product_barcode), Modifier.width(100.dp))
        TableHeader(stringResource(R.string.product_cost_price), Modifier.width(100.dp))
        TableHeader(stringResource(R.string.product_sold_price), Modifier.width(100.dp))
        TableHeader(stringResource(R.string.product_stock), Modifier.width(80.dp))
        TableHeader(stringResource(R.string.product_min_stock), Modifier.width(100.dp))
        TableHeader(stringResource(R.string.product_max_stock), Modifier.width(100.dp))
        TableHeader(stringResource(R.string.product_supplier), Modifier.width(120.dp))
        TableHeader(stringResource(R.string.product_category), Modifier.width(120.dp))
    }
}

@Composable
fun ProductTableBody(product: ProductWithSupplierAndCategory) {
    TableCell(product.product.name, Modifier.width(120.dp))
    TableCell(product.product.brand ?: "", Modifier.width(100.dp))
    TableCell(product.product.description ?: "", Modifier.width(200.dp))
    TableCell(product.product.barcode ?: "", Modifier.width(100.dp))
    TableCell(formatedPrice(product.product.costPrice), Modifier.width(100.dp))
    TableCell(formatedPrice(product.product.price), Modifier.width(100.dp))
    TableCell(product.product.stock.toString(), Modifier.width(80.dp))
    TableCell(product.product.minStock.toString(), Modifier.width(100.dp))
    TableCell(product.product.maxStock.toString(), Modifier.width(100.dp))
    TableCell(product.supplier?.name ?: "Sin proveedor", Modifier.width(120.dp))
    TableCell(product.category?.name ?: "Sin categoría", Modifier.width(120.dp))
}
