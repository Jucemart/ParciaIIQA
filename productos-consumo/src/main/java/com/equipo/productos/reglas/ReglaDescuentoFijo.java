package com.equipo.productos.reglas;

import com.equipo.productos.modelo.Producto;
import java.math.BigDecimal;

/**
 * Implementación de regla de precio que aplica un descuento fijo por unidad.
 * Esta regla descuenta un monto fijo del subtotal por cada unidad del producto.
 * 
 * Ejemplo: Si el descuento es $5 por unidad y hay 3 unidades, 
 * el descuento total será $15.
 */
public class ReglaDescuentoFijo implements ReglaPrecio {
    
    private final BigDecimal descuentoPorUnidad;
    
    /**
     * Constructor que establece el descuento fijo por unidad.
     * 
     * @param descuentoPorUnidad el monto a descontar por cada unidad, debe ser mayor o igual a cero
     * @throws IllegalArgumentException si el descuento es negativo o null
     */
    public ReglaDescuentoFijo(BigDecimal descuentoPorUnidad) {
        this.descuentoPorUnidad = validarDescuento(descuentoPorUnidad);
    }
    
    /**
     * Constructor alternativo que acepta el descuento como double por conveniencia.
     * 
     * @param descuentoPorUnidad el monto a descontar por cada unidad como double
     * @throws IllegalArgumentException si el descuento es negativo
     */
    public ReglaDescuentoFijo(double descuentoPorUnidad) {
        this(BigDecimal.valueOf(descuentoPorUnidad));
    }
    
    @Override
    public BigDecimal aplicar(BigDecimal subtotal, Producto producto, int cantidad) {
        validarParametros(subtotal, producto, cantidad);
        
        BigDecimal descuentoTotal = descuentoPorUnidad.multiply(BigDecimal.valueOf(cantidad));
        BigDecimal nuevoSubtotal = subtotal.subtract(descuentoTotal);
        
        // Asegurar que el subtotal no sea negativo
        return nuevoSubtotal.max(BigDecimal.ZERO);
    }
    
    /**
     * Obtiene el descuento por unidad configurado.
     * 
     * @return el descuento por unidad
     */
    public BigDecimal getDescuentoPorUnidad() {
        return descuentoPorUnidad;
    }
    
    /**
     * Valida que el descuento sea válido.
     * 
     * @param descuento el descuento a validar
     * @return el descuento si es válido
     * @throws IllegalArgumentException si el descuento es null o negativo
     */
    private BigDecimal validarDescuento(BigDecimal descuento) {
        if (descuento == null) {
            throw new IllegalArgumentException("El descuento no puede ser null");
        }
        if (descuento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
        }
        return descuento;
    }
    
    /**
     * Valida los parámetros del método aplicar.
     * 
     * @param subtotal el subtotal a validar
     * @param producto el producto a validar
     * @param cantidad la cantidad a validar
     * @throws IllegalArgumentException si algún parámetro es inválido
     */
    private void validarParametros(BigDecimal subtotal, Producto producto, int cantidad) {
        if (subtotal == null) {
            throw new IllegalArgumentException("El subtotal no puede ser null");
        }
        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El subtotal no puede ser negativo");
        }
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }
    
    @Override
    public String toString() {
        return String.format("ReglaDescuentoFijo{descuentoPorUnidad=%s}", descuentoPorUnidad);
    }
}
