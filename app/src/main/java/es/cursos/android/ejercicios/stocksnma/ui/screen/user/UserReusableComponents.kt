package es.cursos.android.ejercicios.stocksnma.ui.screen.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R

@Composable
fun UserFormHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_12dp)),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_16dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_user_no_photo),
            contentDescription = stringResource(id = R.string.profile_image_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .padding(dimensionResource(id = R.dimen.padding_4dp))
                .clip(CircleShape)
                .clickable { /*TODO*/ }
        )

        ElevatedButton(
            onClick = { /*TODO*/ },
            enabled = false
        ) {
            Text(text = stringResource(id = R.string.user_add_photo))
        }
    }
}
