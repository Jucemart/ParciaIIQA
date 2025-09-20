package com.equipo.productos.reglas;

import com.equipo.productos.modelo.Producto;
import java.math.BigDecimal;

/**
 * Interfaz común para todas las reglas de precio.
 * Define el contrato que deben cumplir todas las políticas de precios
 * que se pueden componer en el motor de precios.
 * 
 * Las reglas de precio siguen el patrón Strategy y pueden ser encadenadas
 * para aplicar múltiples transformaciones al precio de un producto.
 */
public interface ReglaPrecio {
    
    /**
     * Aplica la regla de precio al subtotal dado.
     * 
     * @param subtotal el subtotal actual sobre el cual aplicar la regla
     * @param producto el producto al cual se está calculando el precio
     * @param cantidad la cantidad de productos
     * @return el nuevo subtotal después de aplicar la regla
     * @throws IllegalArgumentException si algún parámetro es inválido
     */
    BigDecimal aplicar(BigDecimal subtotal, Producto producto, int cantidad);
}
