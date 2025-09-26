package es.cursos.android.ejercicios.stocksnma.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDivider

@Composable
fun UserSectionHomeBodyScreen(
    users: List<User>,
    navigateToUserDetails: (Long) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (users.isEmpty()) {
            Text("No se encontraron usuarios, \n ¡Empecemos creando uno!")
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(users) { user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { user.id?.let { navigateToUserDetails(it) } }
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp)

                    ) {
                        Column() {
                            Image(
                                painter = painterResource(id = /*user.photo ?: */R.drawable.img_user_no_photo),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column() {
                            Text(text = user.name, fontSize = 16.sp)
                            Text(text = user.role)
                        }
                    }
                    if (users.indexOf(user) != users.lastIndex) GeneralHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

//    Row(
//        verticalAlignment = Alignment.Bottom,
//        horizontalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize().padding(30.dp)
//    ) {
//        Text(text = if (users.isEmpty()) "0 usuarios" else "${users.size} usuarios")
//    }
