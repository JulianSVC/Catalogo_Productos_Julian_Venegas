package com.example.producto_venta.Views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.producto_venta.Clases.Product.Producto

class ProductViewModel : ViewModel() {
    private var _productos by mutableStateOf(emptyList<Producto>())
    private var _carrito by mutableStateOf(emptyList<Producto>())

    val productos: List<Producto>
        get() = _productos

    val carrito: List<Producto>
        get() = _carrito

    fun agregarProducto(producto: Producto) {
        _productos = _productos + producto
    }

    fun agregarAlCarrito(producto: Producto) {
        _carrito = _carrito + producto
    }

    fun eliminarDelCarrito(producto: Producto) {
        _carrito = _carrito.filter { it.id != producto.id }
    }

    fun limpiarCarrito() {
        _carrito = emptyList()
    }

    fun calcularTotal(): Double {
        return _carrito.sumOf { it.precio }
    }
}