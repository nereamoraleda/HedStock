package es.cursos.android.ejercicios.stocksnma.ui.screen.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.domain.model.Product
import es.cursos.android.ejercicios.stocksnma.ui.components.*
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSections
import es.cursos.android.ejercicios.stocksnma.utils.ProductValidationState


@Composable
fun ProductCreationScreen(
    viewModel: ProductCreationViewModel,
    barcodeScanner: String? = null,
    navigateBack: () -> Unit,
    onProductCreated: () -> Unit
) {
    // VARIABLES

    // Variables - TextFields
    //val coroutineScope = rememberCoroutineScope()
    var showCreateCategoryDialog by remember { mutableStateOf(false) }
    var showEditCategoryDialog by remember { mutableStateOf(false) }
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }

    var barcode by remember { mutableStateOf(barcodeScanner) }
    val categoryEditId = remember { mutableIntStateOf(0) }
    val categoryOldName = remember { mutableStateOf("") }

    var activeProductSection by remember { mutableStateOf(ProductSections.INFO) }

    if (showCreateCategoryDialog) {
        CategoryDialog(
            title = stringResource(R.string.category_create_title),
            onDismissRequest = { showCreateCategoryDialog = false },
            onAcceptAction = {
                viewModel.createCategory(CategoryEntity(name = it))
                showCreateCategoryDialog = false
            },
        )
    }

    if (showEditCategoryDialog) {
        CategoryDialog(
            title = stringResource(R.string.category_edit_title),
            text = categoryOldName.value,
            onDismissRequest = { showEditCategoryDialog = false },
            onAcceptAction = { newCategoryName ->
                viewModel.editCategory(CategoryEntity(id = categoryEditId.intValue, name = newCategoryName))
                showEditCategoryDialog = false
            },
            acceptButtonText = stringResource(R.string.button_edit)
        )
    }

    if (showDeleteCategoryDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.category_delete_title),
            message = stringResource(R.string.category_delete_message, categoryOldName.value),
            confirmButtonText = stringResource(R.string.button_delete),
            onDismissRequest = { showDeleteCategoryDialog = false },
            onConfirmAction = {
                viewModel.deleteCategory(CategoryEntity(id = categoryEditId.intValue, name = categoryOldName.value))
                showDeleteCategoryDialog = false
            }
        )
    }

    val categoriesList by viewModel.categoriesList.collectAsState()



    // Variables - DropDownMenu (Select supplier)
    val suppliersList by viewModel.suppliersList.collectAsState()
    val validationState by viewModel.validationState.collectAsState()


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),

        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.product_create_title),
                navigationButton = { IconButtonGoBack(navigateBack); viewModel.cleanUiState() }
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                enabled = viewModel.productUiState.isEntryValid,
                onAcceptAction = {
                    viewModel.saveProduct()
                    onProductCreated()
                    barcode = null
                },
                onCancelAction =  {
                    viewModel.cleanUiState()
                    barcode = null
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())

        ) {
            CustomSegmentedButton(
                selectedProductSection = activeProductSection,
                onProductSectionChange = { activeProductSection = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )

            ProductDetailsCard(
                productUiState = viewModel.productUiState,
                onProductValueChange = viewModel::updateUiState,
                suppliersList = suppliersList,
                categoriesList = categoriesList,
                barcodeScanner = barcode,
                validationState = validationState,
                activeProductSection = activeProductSection,
                onShowCreateDialog = { showCreateCategoryDialog = true },
                onShowEditDialog = { name, id ->
                    showEditCategoryDialog = true
                    categoryEditId.intValue = id
                    categoryOldName.value = name
                },
                onShowDeleteDialog = { name, id ->
                    showDeleteCategoryDialog = true
                    categoryEditId.intValue = id
                    categoryOldName.value = name
                }
            )
        }
    }
}


@Composable
fun ProductDetailsCard(
    productUiState: ProductCreationViewModel.ProductUiState,
    onProductValueChange: (Product) -> Unit,
    suppliersList: List<SupplierEntity>,
    categoriesList: List<CategoryEntity>,
    barcodeScanner: String?,
    validationState: ProductValidationState,
    onShowCreateDialog: () -> Unit,
    onShowEditDialog: (String, Int) -> Unit,
    onShowDeleteDialog: (String, Int) -> Unit,
    activeProductSection: ProductSections,
    modifier: Modifier = Modifier
) {
    val product = productUiState.productItem
    var initialBarcode by remember { mutableStateOf(false) }
    if (barcodeScanner != null && !initialBarcode) {
        onProductValueChange(product.copy(barcode = barcodeScanner))
        initialBarcode = true
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium)),
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Column {
                        TextField(
                            value = product.name,
                            onValueChange = { onProductValueChange(product.copy(name = it)) },
                            isError = validationState.nameError != null,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.product_form_name),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            },
                            textStyle = MaterialTheme.typography.titleLarge,
                            colors = colorsSimpleTextField(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        ShowMessageErrorText(validationState.nameError)

                        TextField(
                            value = product.description,
                            onValueChange = { onProductValueChange(product.copy(description = it)) },
                            placeholder = {
                                Text(text = stringResource(R.string.product_description),)
                            },
                            colors = colorsSimpleTextField(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            when (activeProductSection) {
                ProductSections.INFO -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CustomOutlinedTextField(
                            label = R.string.product_brand,
                            value = product.brand,
                            onValueChange = { onProductValueChange(product.copy(brand = it)) },
                        )

                        CustomDropDownMenuCategoriesExposed(
                            onCategorySelected = { onProductValueChange(product.copy(category = it)) },
                            categoryOptions = categoriesList,
                            onClickCreateCategory = { onShowCreateDialog() },
                            onClickEditCategory = { name, id -> onShowEditDialog(name, id) },
                            onClickDeleteCategory = { name, id -> onShowDeleteDialog(name, id) },
                        )

                        CustomDropDownMenuSuppliersExposed(
                            selectedSupplier = product.supplierId,
                            onSupplierSelected = { newSupplierId ->
                                onProductValueChange(
                                    product.copy(
                                        supplierId = newSupplierId
                                    )
                                )
                            },
                            supplierOptions = suppliersList
                        )


                        CustomOutlinedTextField(
                            label = R.string.product_barcode,
                            value = product.barcode,
                            onValueChange = {
                                val cleanedBarcode = it.filter { char -> char.isDigit() }
                                onProductValueChange(product.copy(barcode = cleanedBarcode))
                            },
                            isError = validationState.barcodeError != null,
                            messageError = validationState.barcodeError
                        )
                    }
                }

                ProductSections.PRICE -> {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
                        CustomRowAndTextFieldPrices(
                            title = R.string.product_cost_price,
                            value = product.costPrice,
                            onValueChange = { newCostPrice -> onProductValueChange(product.copy(costPrice = newCostPrice)) },
                            isError = validationState.costPriceError != null
                        )
                        ShowMessageErrorText(validationState.costPriceError)


                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))


                        CustomRowAndTextFieldPrices(
                            title = R.string.product_sold_price,
                            value = product.price,
                            onValueChange = { newPrice -> onProductValueChange(product.copy(price = newPrice)) },
                            isError = validationState.priceError != null
                        )
                        ShowMessageErrorText(validationState.priceError)
                    }
                }

                ProductSections.STOCK -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CustomRowAndTextFieldStocks(
                            title = R.string.product_stock,
                            value = product.stock,
                            onValueChange = {
                                val cleanedStock = it.filter { char -> char.isDigit() }
                                onProductValueChange(product.copy(stock = cleanedStock))
                            },
                            isError = validationState.stockError != null,
                        )
                        ShowMessageErrorText(validationState.stockError)


                        CustomRowAndTextFieldStocks(
                            title = R.string.product_min_stock,
                            value = product.minStock,
                            onValueChange = {
                                val cleanedStock = it.filter { char -> char.isDigit() }
                                onProductValueChange(product.copy(minStock = cleanedStock))
                            },
                            isError = validationState.minStockError != null,
                        )
                        ShowMessageErrorText(validationState.minStockError)


                        CustomRowAndTextFieldStocks(
                            title = R.string.product_max_stock,
                            value = product.maxStock,
                            onValueChange = {
                                val cleanedStock = it.filter { char -> char.isDigit() }
                                onProductValueChange(product.copy(maxStock = cleanedStock))
                            },
                            isError = validationState.maxStockError != null,
                        )
                        ShowMessageErrorText(validationState.maxStockError)
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorMessageStockText(errorMessage: String) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
    )
}
