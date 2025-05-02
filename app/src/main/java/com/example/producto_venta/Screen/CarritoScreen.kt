package com.example.producto_venta.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.producto_venta.Clases.Product.Producto
import com.example.producto_venta.R
import com.example.producto_venta.Views.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Producto?>(null) }

    // Función para mostrar el diálogo de confirmación
    fun confirmDelete(producto: Producto) {
        productToDelete = producto
        showDeleteDialog = true
    }

    // Función para ejecutar la eliminación
    fun executeDelete() {
        productToDelete?.let { producto ->
            viewModel.eliminarDelCarrito(producto)
        }
        showDeleteDialog = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (viewModel.carrito.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total: $${"%.2f".format(viewModel.calcularTotal())}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Button(
                            onClick = {
                                viewModel.limpiarCarrito()
                                navController.popBackStack()
                            }
                        ) {
                            Text("Finalizar Compra")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // Diálogo de confirmación
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("¿Eliminar producto?") },
                text = { Text("¿Estás seguro de que quieres eliminar este producto del carrito?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            executeDelete()
                        }
                    ) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (viewModel.carrito.isEmpty()) {
            EmptyCartState()
        } else {
            CartItemList(
                carrito = viewModel.carrito,
                onDeleteItem = { producto -> confirmDelete(producto) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun EmptyCartState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "El carrito está vacío",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun CartItemList(
    carrito: List<Producto>,
    onDeleteItem: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = carrito,
            key = { producto -> producto.id }
        ) { producto ->
            CartItem(
                producto = producto,
                onDelete = { onDeleteItem(producto) }
            )
        }
    }
}

@Composable
fun CartItem(
    producto: Producto,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información del producto
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                producto.imagenUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = producto.nombre,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Column {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$${"%.2f".format(producto.precio)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Botón de eliminar
            IconButton(
                onClick = onDelete,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}