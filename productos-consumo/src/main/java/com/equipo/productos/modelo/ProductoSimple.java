package com.equipo.productos.modelo;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductoSimple implements Producto {
    
    private final String nombre;
    private final BigDecimal precioBase;

    public ProductoSimple(String nombre, BigDecimal precioBase) {
        this.nombre = validarNombre(nombre);
        this.precioBase = validarPrecioBase(precioBase);
    }
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

    private String validarNombre(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre del producto no puede ser null");
        }
        if (nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        return nombre.trim();
    }
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
