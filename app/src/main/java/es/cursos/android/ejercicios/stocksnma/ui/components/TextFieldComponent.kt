package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.utils.getCurrencySymbol
import es.cursos.android.ejercicios.stocksnma.utils.isValidPhoneNumber
import es.cursos.android.ejercicios.stocksnma.utils.isValidPrice

@Composable
fun GeneralOutlinedTextField(
    value: String,
    valueLength: Int? = null,
    onValueChange: (String) -> Unit,
    label: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (valueLength != null) { if (it.length <= valueLength) onValueChange(it) }
            else { onValueChange(it) }
        },
        label = { Text(text = label) },
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = minLines,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = coloresOutlinedTextField(),
        modifier = modifier.fillMaxWidth()
    )
}


@Composable
fun GeneralPhoneOutlinedTextField(
    value: String,
    valueLength: Int? = null,
    onValueChange: (String) -> Unit,
    label: String,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (isValidPhoneNumber(it)) {
                if (valueLength != null) { if (it.length <= valueLength) onValueChange(it) }
                else { onValueChange(it) }
            }
        },
        label = { Text(text = label) },
        supportingText = supportingText,
        isError = isError,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        colors = coloresOutlinedTextField(),
        modifier = modifier.fillMaxWidth()
    )
}


@Composable
fun GeneralPriceOutlinedTextField(
    value: String,
    valueLength: Int? = null,
    onValueChange: (String) -> Unit,
    label: String,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (isValidPrice(it)) {
                if (valueLength != null) { if (it.length <= valueLength) onValueChange(it) }
                else { onValueChange(it) }
            }
        },
        label = { Text(text = label) },
        supportingText = supportingText,
        isError = isError,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = coloresOutlinedTextField(),
        modifier = modifier.fillMaxWidth()
    )
}


@Composable
fun GeneralTextFieldTitle(
    value: String,
    valueLength: Int? = null,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = {
            if (valueLength != null) { if (it.length <= valueLength) onValueChange(it) }
            else { onValueChange(it) }
        },
        placeholder = {
            Text(
                label,
                style = MaterialTheme.typography.titleLarge
            )
        },
        textStyle = MaterialTheme.typography.titleLarge,
        readOnly = readOnly,
        isError = isError,
        maxLines = 2,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = colorsSimpleTextField(),
        modifier = Modifier.fillMaxWidth()
    )
}



@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: Int,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    isError: Boolean = false,
    messageError: String? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        enabled = enabled,
        label = { Text(stringResource(label)) },
        singleLine = singleLine,
        isError = isError,
        supportingText = { if (isError && messageError != null) Text(messageError) },
        colors = coloresOutlinedTextField(),
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun CustomRowAndTextFieldStocks(
    title: Int,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(title),
            fontWeight = FontWeight.Bold,
            modifier =
            if (enabled) Modifier.weight(1f).alpha(1f)
            else Modifier.weight(1f).alpha(0.5f)
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            isError = isError,
            placeholder = { Text(text = "0") },
            suffix = { Text(text = "u.") },
            singleLine = true,
            enabled = enabled,
            textStyle = TextStyle(fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.ci_gamedev))),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = coloresTextFields(),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun CustomRowAndTextFieldPrices(
    title: Int,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(title),
            fontWeight = FontWeight.Bold,
            modifier =
            if (enabled) Modifier.weight(1f).alpha(1f)
            else Modifier.weight(1f).alpha(0.5f)
        )
        TextField(
            value = value,
            onValueChange = {
                // Permitimos solo números, coma o punto, y hasta 2 decimales
                val regex = Regex("^[0-9]*[.,]?[0-9]{0,2}$")
                if (it.matches(regex)) {
                    // Reemplazamos coma por punto para mantener coherencia interna
                    val formattedPrice = it.replace(",", ".")
                    onValueChange(formattedPrice)
                } else if (it.isEmpty()) {
                    // Permitir borrar todo
                    onValueChange("")
                }
            },
            isError = isError,
            placeholder = { Text(text = "0.00") },
            suffix = { Text(text = getCurrencySymbol()) },
            singleLine = true,
            enabled = enabled,
            textStyle = TextStyle(fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.ci_gamedev))),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = coloresTextFields(),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun ShowMessageErrorText(message: String? = null, isEditing: Boolean = true, modifier: Modifier = Modifier) {
    if (message != null && isEditing) {
        Text(
            text = message,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_16dp))
                .padding(bottom = dimensionResource(R.dimen.padding_16dp))
        )
    }
}

@Composable
fun supportingErrorText(vararg errors: String?): (@Composable (() -> Unit))? {
    val error = errors.firstOrNull { it != null }
    return error?.let { { Text(text = it) } }
}


@Composable
fun colorsSimpleTextField() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,

    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
)

@Composable
fun coloresTextFields() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent
)


@Composable
fun coloresOutlinedTextField() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface

    //disabledBorderColor = Color.Transparent,
    //focusedBorderColor = MaterialTheme.colorScheme.primary,
    //unfocusedBorderColor = MaterialTheme.colorScheme.surface
)