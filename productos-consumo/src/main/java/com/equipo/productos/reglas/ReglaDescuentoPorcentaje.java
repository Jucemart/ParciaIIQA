package com.equipo.productos.reglas;

import com.equipo.productos.modelo.Producto;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Implementación de regla de precio que aplica un descuento porcentual sobre el subtotal.
 * Esta regla descuenta un porcentaje del subtotal total, donde el porcentaje debe estar
 * en el rango [0,1] (por ejemplo, 0.15 para un 15% de descuento).
 * 
 * Ejemplo: Si el porcentaje es 0.20 (20%) y el subtotal es $100, 
 * el precio final será $80.
 * 
 * @author Julio Martínez jmartinezm45@miumg.edu.gt
 */
public class ReglaDescuentoPorcentaje implements ReglaPrecio {
    
    private final BigDecimal porcentajeDescuento;
    
    /**
     * Constructor que establece el porcentaje de descuento.
     * 
     * @param porcentajeDescuento el porcentaje a descontar en el rango [0,1], donde 0 es sin descuento y 1 es 100% de descuento
     * @throws IllegalArgumentException si el porcentaje es negativo, mayor que 1, o null
     */
    public ReglaDescuentoPorcentaje(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = validarPorcentaje(porcentajeDescuento);
    }
    
    /**
     * Constructor alternativo que acepta el porcentaje como double por conveniencia.
     * 
     * @param porcentajeDescuento el porcentaje a descontar como double en el rango [0,1]
     * @throws IllegalArgumentException si el porcentaje está fuera del rango válido
     */
    public ReglaDescuentoPorcentaje(double porcentajeDescuento) {
        this(BigDecimal.valueOf(porcentajeDescuento));
    }
    
    @Override
    public BigDecimal aplicar(BigDecimal subtotal, Producto producto, int cantidad) {
        validarParametros(subtotal, producto, cantidad);
        
        // Calcula el factor de descuento: (1 - porcentaje)
        BigDecimal factorDescuento = BigDecimal.ONE.subtract(porcentajeDescuento);
        
        // Aplica el descuento multiplicando el subtotal por el factor
        return subtotal.multiply(factorDescuento);
    }
    
    /**
     * Obtiene el porcentaje de descuento configurado.
     * 
     * @return el porcentaje de descuento en el rango [0,1]
     */
    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
    
    /**
     * Valida que el porcentaje de descuento sea válido.
     * 
     * @param porcentaje el porcentaje a validar
     * @return el porcentaje si es válido
     * @throws IllegalArgumentException si el porcentaje es null, negativo o mayor que 1
     */
    private BigDecimal validarPorcentaje(BigDecimal porcentaje) {
        if (porcentaje == null) {
            throw new IllegalArgumentException("El porcentaje de descuento no puede ser null");
        }
        if (porcentaje.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El porcentaje de descuento no puede ser negativo");
        }
        if (porcentaje.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("El porcentaje de descuento no puede ser mayor que 1");
        }
        return porcentaje;
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ReglaDescuentoPorcentaje that = (ReglaDescuentoPorcentaje) obj;
        return Objects.equals(porcentajeDescuento, that.porcentajeDescuento);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(porcentajeDescuento);
    }
    
    @Override
    public String toString() {
        return String.format("ReglaDescuentoPorcentaje{porcentajeDescuento=%s}", porcentajeDescuento);
    }
}
