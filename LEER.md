# Minimarket - Documentacion basica del sstema

## CRUD Relacional de Productos

La gestión de productos se realiza de forma relacional utilizando JPA y una base de datos MySQL. A continuación se detallan los componentes principales:

### Modelo de Datos (Relacional)

- **Entidad `Product`**: Almacena la información básica como nombre, descripción, precio, marca y stock.
- **Entidad `Category`**: Cada producto pertenece a una categoría que define sus reglas de validación.
- **Entidad `Image`**: Relación 1:N con `Product`. Permite almacenar múltiples URLs de imágenes para un mismo producto.
- **Atributos Dinámicos**: Los productos almacenan especificaciones técnicas en formato JSON (columna `attributes`), lo que permite flexibilidad para diferentes tipos de productos (ej. talla para ropa, RAM para electrónica).

### Operaciones CRUD

- **Crear (`POST /products`)**: Crea un nuevo producto, valida sus atributos según la categoría, inicializa el stock y lo indexa opcionalmente en el motor de búsqueda.
- **Leer (`GET /products/{id}`)**: Obtiene la información detallada del producto, incluyendo sus imágenes y stock actual.
- **Actualizar (`PUT /products/{id}`)**: Permite modificar cualquier campo del producto. Si el stock cambia, se registra automáticamente una transacción en el historial de inventario (RESTOCK o SALE).
- **Eliminar (`DELETE /products/{id}`)**: Elimina el producto y sus registros asociados (imágenes) de la base de datos.

---

## Guia de uso

### Autenticación

Para realizar acciones privadas, debe obtener un token JWT.

- **Login**: `POST /auth/login`
- **Credenciales por Defecto**: `admin@gmail.com` / `123456789`
- **Uso**: Incluya el token en el encabezado `Authorization: Bearer <token>`.

### Flujo de Compra

1. **Carrito**: Agregue productos mediante `POST /cart/items`.
2. **Checkout**: Finalice la compra con `POST /checkout/initialize` enviando los datos de envío. El proceso es síncrono y descuenta el stock inmediatamente.
3. **Pedidos**: Consulte sus compras en `GET /orders`.

---

## Motor de Busqueda (Elasticsearch) [NO RECOMENDADO]

El sistema está diseñado para que Elasticsearch sea **opcional**, puess se
necesita de tener elastic search y actualmente este comentado todas las dependencias en la aplicacion para evitar conflictos. La idea es que este presente
en futuras entregas.

- Si está habilitado (`app.elasticsearch.enabled=true`), los productos se sincronizan automáticamente para permitir búsquedas avanzadas y filtrado por facetas.
- Si está deshabilitado, el sistema sigue funcionando perfectamente utilizando la base de datos relacional para las operaciones principales.
