package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSections


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSegmentedButton(
    selectedProductSection: ProductSections,
    onProductSectionChange: (ProductSections) -> Unit,
    modifier: Modifier = Modifier
) {
    val productSections = ProductSections.entries.toList()

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_16dp))
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        productSections.forEach { productSection ->
            SegmentedButton(
                selected = productSection == selectedProductSection,
                onClick = { onProductSectionChange(productSection) },
                shape = RoundedCornerShape(50.dp),
                label = {
                    Text(text = productSection.label, maxLines = 1, overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                icon = {},
                colors = segmentedButtonColors(),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GeneralSegmentedButton(
    selectedSection: T,
    onSectionChange: (T) -> Unit,
    sections: List<T>,
    label: @Composable (T) -> String,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_16dp))
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        sections.forEach { section ->
            SegmentedButton(
                selected = section == selectedSection,
                onClick = { onSectionChange(section) },
                shape = RoundedCornerShape(50.dp),
                label = {
                    Text(
                        text = label(section),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                icon = {},
                colors = segmentedButtonColors(),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun segmentedButtonColors() = SegmentedButtonDefaults.colors(
    activeContentColor = MaterialTheme.colorScheme.secondary,
    activeContainerColor = MaterialTheme.colorScheme.surface,
    activeBorderColor = MaterialTheme.colorScheme.primary,
    inactiveContentColor = MaterialTheme.colorScheme.surface,
    inactiveContainerColor = MaterialTheme.colorScheme.secondary,
    inactiveBorderColor = Color.Transparent
)