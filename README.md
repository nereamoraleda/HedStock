# HedStock (_En desarrollo..._)
Aplicación de inventario para una empresa y sus tiendas, desarrollada para dispositivos móviles Android. 
Permite gestionar los proveedores, las tiendas, los usuarios/empleados, el catálogo de productos de la empresa y los productos de cada tienda (_pedidos en proceso..._).
Para acceder, se deberá de autenticar con su nombre de usuario y contraseña.
Dependiendo del rol que tenga asignado, tendrá disponible o no ciertas funciones o vistas de la aplicación.

## Index
- [Tecnologías](#id1)
- [Base de datos](#id2)
- [Arquitectura](#id3)
- [Cómo ejecutar la app](#id4)

---

## Tecnologías<a name="id1"></a>
- Kotlin
- Jetpack Compose
- MVVM
- Hilt
- Retrofit

---

## Base de datos<a name="id2"></a>
El proyecto utiliza **PostgreSQL** como sistema gestor de base de datos. 
La aplicación se conecta a la base de datos mediante **JPA/Hibernate**.

### Diagrama de la base de datos

### Estructura
La base de datos se compone de 10 tablas.
- store:               Almacena la información de las tiendas de la empresa.
- users                Almacena la información de los usuarios (empleados).
- roles                Contiene los diferentes roles que pueden tener los usuarios.
- supplier             Almacena la información de los proveedores.
- product              Contiene la información general de los productos de la empresa
- store_product        Relaciona los productos con las tiendas y almacena la información específica de un producto en cada tienda.
- category             Contiene las diferentes categorías de productos.
- purchase_order       Almacena la información de los pedidos realizados. (Actualmente no utilizada por la aplicación.)
- purchase_order_item  Almacena los productos incluidos en cada pedido.   (Actualmente no utilizada por la aplicación.)
- stock_movement       Registra los movimientos de stock realizados.      (Actualmente no utilizada por la aplicación.)

### Relaciones principales

### Configuración
1. Instalar MySQL.
2. Crear la base de datos:

```sql
CREATE DATABASE stocks;
```

### Funcionalidades pendientes

---

## Arquitectura<a name="id3"></a>

---

## Cómo ejecutar la app (Añadir parte de Spring Boot y ajustar puertos)<a name="id4"></a>
1. Clonar el repositorio (Android Studio):
2. Clonar el repositorio (Spring Boot):
3. Abrir el proyecto en Android Studio (versión ...)
4. Sincronizar Gradle
5. Ejecutar en un emulador o dispositivo Android (API 24+)
