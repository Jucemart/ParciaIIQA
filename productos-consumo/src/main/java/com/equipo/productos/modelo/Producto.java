package com.equipo.productos.modelo;

import java.math.BigDecimal;

/**
 * Interfaz del dominio que define el contrato básico de un producto.
 * Representa las propiedades fundamentales que debe tener cualquier producto
 * en el sistema de gestión de precios.
 */
public interface Producto {
    
    /**
     * Obtiene el nombre del producto.
     * 
     * @return el nombre del producto, no puede ser null ni vacío
     */
    String getNombre();
    
    /**
     * Obtiene el precio base del producto antes de aplicar cualquier regla.
     * 
     * @return el precio base como BigDecimal, debe ser mayor que cero
     */
    BigDecimal getPrecioBase();
}
