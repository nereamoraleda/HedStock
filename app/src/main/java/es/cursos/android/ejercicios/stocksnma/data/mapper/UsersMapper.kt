package es.cursos.android.ejercicios.stocksnma.data.mapper

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.UserDto
import es.cursos.android.ejercicios.stocksnma.domain.model.User

fun UserDto.toUser(): User = User(
    id = id,
    name = name,
    username = username,
    password = password ?: "",
    email = email ?: "",
    phone = phone ?: "",
    role = role,
    storeId = storeId,
    storeName = storeName,
    isActive = active,
    createdAt = createdAt
)


fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        email = email.ifBlank { null },
        phone = phone.ifBlank { null },
        username = username,
        password = password,
        role = role,
        storeId = storeId,
        storeName = null,
        active = isActive,
        createdAt = createdAt,
    )
}
