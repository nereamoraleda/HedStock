package es.cursos.android.ejercicios.stocksnma.ui.screen.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.ProductEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.domain.model.Product
import es.cursos.android.ejercicios.stocksnma.ui.components.CategoryDialog
import es.cursos.android.ejercicios.stocksnma.ui.components.ConfirmationDialog
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomDropDownMenuCategoriesExposed
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomDropDownMenuSuppliersExposed
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomRowAndTextFieldPrices
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomRowAndTextFieldStocks
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.DetailsCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralIconButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.ShowMessageErrorText
import es.cursos.android.ejercicios.stocksnma.ui.components.colorsSimpleTextField
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSections
import es.cursos.android.ejercicios.stocksnma.utils.validations.ProductValidationState


@Composable
fun ProductoDetailsScreen(
    viewModel: ProductDetailsViewModel,
    idProduct: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(idProduct) {
        viewModel.getProductById(idProduct)
        viewModel.getSuppliers()
        viewModel.getCategories()
    }


    // Variables - Productos Details ViewModel
    val state by viewModel.uiState.collectAsState()


    // Estado de la UI
    when (val uiState = state) {
        is DetailsUiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_16dp))
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }

        is DetailsUiState.Success -> {
            ProductDetailsBodyScreen(uiState, viewModel, navigateBack)
        }

        is DetailsUiState.NotFound -> {
            Text("Producto no encontrado")
        }

        is DetailsUiState.Error -> {
            Text("Error: ${uiState.messageError}")
        }
    }
}


@Composable
fun ProductDetailsBodyScreen(
    stateProduct: DetailsUiState.Success<ProductEntity>,
    viewModel: ProductDetailsViewModel,
    onNavigateBack: () -> Unit
) {
    val product = stateProduct.item
    val tempProduct by viewModel.tempProduct.collectAsState()
    var isEditing by rememberSaveable { mutableStateOf(false) }

    // Variables - Botón Segmentado
    var activeProductSection by remember { mutableStateOf(ProductSections.INFO) }

    val categoryEditId = remember { mutableIntStateOf(0) }
    val categoryOldName = remember { mutableStateOf("") }

    var showDeleteProductsConfirmation by remember { mutableStateOf(false) }
    var showCreateCategoryDialog by remember { mutableStateOf(false) }
    var showEditCategoryDialog by remember { mutableStateOf(false) }
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }

    if (showDeleteProductsConfirmation) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_delete_selected_products_title),
            message = stringResource(R.string.confirm_delete_selected_products),
            confirmButtonText = stringResource(R.string.button_accept),
            onDismissRequest = { showDeleteProductsConfirmation = false },
            onConfirmAction = {
                viewModel.deleteProduct(product)
                //showDeleteProductsConfirmation = false
                onNavigateBack()
            }
        )
    }

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
                viewModel.editCategory(
                    CategoryEntity(
                        id = categoryEditId.intValue,
                        name = newCategoryName
                    )
                )
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
                viewModel.deleteCategory(
                    CategoryEntity(
                        id = categoryEditId.intValue,
                        name = categoryOldName.value
                    )
                )
                showDeleteCategoryDialog = false
            }
        )
    }


    // -- UI --
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.product_details_title),
                navigationButton = { if (!isEditing) NavigateBackButton(onNavigateBack) },
                actionButton = {
                    GeneralIconButton(
                        icon = R.drawable.ic_edit,
                        onClick = { isEditing = !isEditing }
                    )
                    if (isEditing) {
                        GeneralIconButton(
                            icon = R.drawable.ic_delete,
                            onClick = { showDeleteProductsConfirmation = true }
                        )
                    }

                },
                modifier = Modifier
            )
        },
        bottomBar = {
            if (isEditing) {
                ButtonsBottomBar(
                    acceptButtonEnabled = stateProduct.isEntryValid,
                    onAcceptAction = {
                        Log.d(
                            "UpdateDebug", "Product: $tempProduct"
                        )

                        val updatedProduct = product.copy(
                            name = tempProduct.name,
                            description = tempProduct.description,
                            brand = tempProduct.brand,
                            supplierId = if (tempProduct.supplierId == "") null else tempProduct.supplierId,
                            categoryId = tempProduct.category.toIntOrNull(),
                            barcode = tempProduct.barcode,
                            costPrice = tempProduct.costPrice.toDoubleOrNull() ?: 0.00,
                            price = tempProduct.price.toDoubleOrNull() ?: 0.00,
                            stock = tempProduct.stock.toIntOrNull() ?: 0,
                            minStock = tempProduct.minStock.toIntOrNull() ?: 0,
                            maxStock = tempProduct.maxStock.toIntOrNull() ?: 0
                        )

                        Log.d(
                            "UpdateDebug", "Product: $updatedProduct"
                        )
                        updatedProduct.let {
                            viewModel.updateProduct(it) // Guardar en la base de datos
                            isEditing = false           // Salir del modo edición
                        }
                    },
                    onCancelAction = { viewModel.setInitialValues(product); isEditing = false },
                    modifier = Modifier
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            GeneralSegmentedButton(
                selectedSection = activeProductSection,
                onSectionChange = { activeProductSection = it },
                sections = ProductSections.entries.toList(),
                label = {
                    when (it) {
                        ProductSections.INFO -> stringResource(R.string.product_section_info)
                        ProductSections.PRICE -> stringResource(R.string.product_section_price)
                        ProductSections.STOCK -> stringResource(R.string.product_section_stock)
                    }
                }
            )


            ProductInfoCard(
                viewModel,
                tempProduct = tempProduct,
                isEditing = isEditing,
                selectedProductSection = activeProductSection,
                showCreateCategoryDialog = { showCreateCategoryDialog = true },
                showEditCategoryDialog = { name, id ->
                    showEditCategoryDialog = true
                    categoryEditId.intValue = id
                    categoryOldName.value = name
                },
                showDeleteCategoryDialog = { name, id ->
                    showDeleteCategoryDialog = true
                    categoryEditId.intValue = id
                    categoryOldName.value = name
                },
            )
        }
    }
}


@Composable
fun ProductInfoCard(
    viewModel: ProductDetailsViewModel,
    tempProduct: Product,
    isEditing: Boolean,
    selectedProductSection: ProductSections,
    showCreateCategoryDialog: () -> Unit,
    showEditCategoryDialog: (String, Int) -> Unit,
    showDeleteCategoryDialog: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val validationState by viewModel.validationState.collectAsState()
    val supplierList by viewModel.suppliers.collectAsState()
    val categoryList by viewModel.categories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_16dp))
    ) {
        DetailsCard(
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Encabezado
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary)
                            //.padding(16.dp)
                    ) {
                        // Encabezado Card
                        EncabezadoCard(
                            tempProduct = tempProduct,
                            onValueChange = viewModel::updateField,
                            validacionState = validationState,
                            isEditing = isEditing,
                            modifier = Modifier.padding(dimensionResource(R.dimen.padding_16dp))
                        )
                    }

                    Column(
                        verticalArrangement = if (selectedProductSection == ProductSections.INFO) Arrangement.spacedBy(0.dp) else Arrangement.spacedBy(
                            16.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_16dp))
                    ) {
                        // Contenido Card
                        when (selectedProductSection) {
                            ProductSections.INFO -> {
                                GeneralInfoSection(
                                    suppliersList = supplierList,
                                    categoriesList = categoryList,
                                    tempProduct = tempProduct,
                                    onValueChange = viewModel::updateField,
                                    validacionState = validationState,
                                    isEditing = isEditing,
                                    showCreateCategoryDialog = showCreateCategoryDialog,
                                    showEditCategoryDialog = showEditCategoryDialog,
                                    showDeleteCategoryDialog = showDeleteCategoryDialog
                                )
                            }

                            ProductSections.PRICE -> PricesSection(
                                tempProduct = tempProduct,
                                onValueChange = viewModel::updateField,
                                validationState = validationState,
                                isEditing = isEditing
                            )

                            ProductSections.STOCK -> InventorySection(
                                tempProduct = tempProduct,
                                onValueChange = viewModel::updateField,
                                validationState = validationState,
                                isEditing = isEditing
                            )
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun EncabezadoCard(
    tempProduct: Product,
    onValueChange: (String, String) -> Unit,
    validacionState: ProductValidationState,
    isEditing: Boolean,
    modifier: Modifier = Modifier
) {
    Column() {
        TextField(
            value = tempProduct.name,
            onValueChange = { onValueChange("name", it) },
            isError = validacionState.nameErrorMessage != null,
            enabled = isEditing,
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
        ShowMessageErrorText(validacionState.nameErrorMessage, modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_16dp)))

        TextField(
            value = tempProduct.description,
            onValueChange = { onValueChange("description", it) },
            enabled = isEditing,
            placeholder = {
                Text(text = stringResource(R.string.product_description),)
            },
            colors = colorsSimpleTextField(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun GeneralInfoSection(
    suppliersList: List<SupplierEntity>,
    categoriesList: List<CategoryEntity>,
    tempProduct: Product,
    onValueChange: (String, String) -> Unit,
    validacionState: ProductValidationState,
    isEditing: Boolean,
    showCreateCategoryDialog: () -> Unit,
    showEditCategoryDialog: (String, Int) -> Unit,
    showDeleteCategoryDialog: (String, Int) -> Unit
) {

    CustomDropDownMenuSuppliersExposed(
        selectedSupplier = tempProduct.supplierId,
        onSupplierSelected = { onValueChange("supplierId", it) },
        supplierOptions = suppliersList,
        enabled = isEditing,
        isError = validacionState.supplierErrorMessage != null,
        messageError = validacionState.supplierErrorMessage
    )


    CustomOutlinedTextField(
        value = tempProduct.brand,
        onValueChange = { onValueChange("brand", it) },
        label = R.string.product_brand,
        enabled = isEditing
    )

    CustomDropDownMenuCategoriesExposed(
        selectedCategory = tempProduct.category,
        onCategorySelected = { onValueChange("category", it) },
        onClickCreateCategory = { showCreateCategoryDialog() },
        onClickEditCategory = { name, id -> showEditCategoryDialog(name, id) },
        onClickDeleteCategory = { name, id -> showDeleteCategoryDialog(name, id) },
        categoryOptions = categoriesList,
        enabled = isEditing,
        //isError = validacionState.categoryError != null,
        //messageError = validacionState.categoryError
    )


    CustomOutlinedTextField(
        value = tempProduct.barcode,
        onValueChange = { onValueChange("barcode", it) },
        label = R.string.product_barcode,
        enabled = isEditing,
        isError = validacionState.barcodeErrorMessage != null,
        messageError = validacionState.barcodeErrorMessage,
    )
}


@Composable
fun InventorySection(
    tempProduct: Product,
    onValueChange: (String, String) -> Unit,
    validationState: ProductValidationState,
    isEditing: Boolean
) {
    CustomRowAndTextFieldStocks(
        title = R.string.product_stock,
        value = tempProduct.stock,
        onValueChange = { onValueChange("stock", it) },
        isError = validationState.stockErrorMessage != null,
        enabled = isEditing
    )
    ShowMessageErrorText(validationState.stockErrorMessage, isEditing)


    CustomRowAndTextFieldStocks(
        title = R.string.product_min_stock,
        value = tempProduct.minStock,
        onValueChange = { onValueChange("minStock", it) },
        isError = validationState.minStockErrorMessage != null,
        enabled = isEditing
    )
    ShowMessageErrorText(validationState.minStockErrorMessage, isEditing)


    CustomRowAndTextFieldStocks(
        title = R.string.product_max_stock,
        value = tempProduct.maxStock,
        onValueChange = { onValueChange("maxStock", it) },
        isError = validationState.maxStockErrorMessage != null,
        enabled = isEditing
    )
    ShowMessageErrorText(validationState.maxStockErrorMessage, isEditing)
}


@Composable
fun PricesSection(
    tempProduct: Product,
    onValueChange: (String, String) -> Unit,
    validationState: ProductValidationState,
    isEditing: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_16dp)),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomRowAndTextFieldPrices(
            title = R.string.product_cost_price,
            value = tempProduct.costPrice,
            onValueChange = { onValueChange("costPrice", it) },
            isError = validationState.costPriceErrorMessage != null,
            enabled = isEditing
        )
        ShowMessageErrorText(validationState.costPriceErrorMessage, isEditing)

        CustomRowAndTextFieldPrices(
            title = R.string.product_sold_price,
            value = tempProduct.price,
            onValueChange = { onValueChange("price", it) },
            isError = validationState.sellingPriceErrorMessage != null,
            enabled = isEditing
        )
        ShowMessageErrorText(validationState.sellingPriceErrorMessage, isEditing)
    }
}
