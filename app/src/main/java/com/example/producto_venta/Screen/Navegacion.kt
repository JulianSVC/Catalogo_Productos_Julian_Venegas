package com.example.producto_venta.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.producto_venta.Views.ProductViewModel

@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "catalogo") {
        composable("catalogo") {
            CatalogoScreen(navController, viewModel)
        }
        composable("detalle/{productoId}") { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId")?.toIntOrNull()
            val producto = viewModel.productos.find { it.id == productoId }
            producto?.let {
                DetalleProductoScreen(navController, viewModel, it)
            }
        }
        composable("agregarProducto") {
            AgregarProductoScreen(navController, viewModel)
        }
        composable("carrito") {
            CarritoScreen(navController, viewModel)
        }
    }
}