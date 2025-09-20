package com.equipo.productos.reglas;

import com.equipo.productos.modelo.Producto;
import java.math.BigDecimal;
public class ReglaDescuentoFijo implements ReglaPrecio {
    
    private final BigDecimal descuentoPorUnidad;

    public ReglaDescuentoFijo(BigDecimal descuentoPorUnidad) {
        this.descuentoPorUnidad = validarDescuento(descuentoPorUnidad);
    }

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

    public BigDecimal getDescuentoPorUnidad() {
        return descuentoPorUnidad;
    }

    private BigDecimal validarDescuento(BigDecimal descuento) {
        if (descuento == null) {
            throw new IllegalArgumentException("El descuento no puede ser null");
        }
        if (descuento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
        }
        return descuento;
    }

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
