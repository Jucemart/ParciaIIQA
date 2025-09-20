package com.equipo.productos.modelo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Implementación mínima de la interfaz Producto.
 * Es un POJO (Plain Old Java Object) que contiene las validaciones básicas
 * necesarias para garantizar la integridad de los datos del producto.
 */
public class ProductoSimple implements Producto {
    
    private final String nombre;
    private final BigDecimal precioBase;
    
    /**
     * Constructor que crea un producto con validaciones.
     * 
     * @param nombre el nombre del producto, no puede ser null ni vacío
     * @param precioBase el precio base del producto, debe ser mayor que cero
     * @throws IllegalArgumentException si algún parámetro no cumple las validaciones
     */
    public ProductoSimple(String nombre, BigDecimal precioBase) {
        this.nombre = validarNombre(nombre);
        this.precioBase = validarPrecioBase(precioBase);
    }
    
    /**
     * Constructor alternativo que acepta el precio como double por conveniencia.
     * 
     * @param nombre el nombre del producto, no puede ser null ni vacío
     * @param precioBase el precio base del producto como double, debe ser mayor que cero
     * @throws IllegalArgumentException si algún parámetro no cumple las validaciones
     */
    public ProductoSimple(String nombre, double precioBase) {
        this(nombre, BigDecimal.valueOf(precioBase));
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public BigDecimal getPrecioBase() {
        return precioBase;
    }
    
    /**
     * Valida que el nombre del producto sea válido.
     * 
     * @param nombre el nombre a validar
     * @return el nombre si es válido
     * @throws IllegalArgumentException si el nombre es null, vacío o solo contiene espacios
     */
    private String validarNombre(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre del producto no puede ser null");
        }
        if (nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        return nombre.trim();
    }
    
    /**
     * Valida que el precio base sea válido.
     * 
     * @param precio el precio a validar
     * @return el precio si es válido
     * @throws IllegalArgumentException si el precio es null, negativo o cero
     */
    private BigDecimal validarPrecioBase(BigDecimal precio) {
        if (precio == null) {
            throw new IllegalArgumentException("El precio base no puede ser null");
        }
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio base debe ser mayor que cero");
        }
        return precio;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductoSimple that = (ProductoSimple) obj;
        return Objects.equals(nombre, that.nombre) && 
               Objects.equals(precioBase, that.precioBase);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombre, precioBase);
    }
    
    @Override
    public String toString() {
        return String.format("ProductoSimple{nombre='%s', precioBase=%s}", nombre, precioBase);
    }
}
