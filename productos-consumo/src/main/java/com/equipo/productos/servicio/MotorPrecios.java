package com.equipo.productos.servicio;

import com.equipo.productos.modelo.Producto;
import com.equipo.productos.reglas.ReglaPrecio;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * Motor de precios que orquesta el cálculo del precio final de un producto
 * aplicando una cadena de reglas de precio en orden secuencial.
 * 
 * El motor calcula primero el subtotal base (precio base * cantidad) y luego
 * aplica cada regla de precio en el orden especificado. El resultado final
 * se redondea a 2 decimales usando HALF_UP.
 * 
 * @author Julio Martínez jmartinezm45@miumg.edu.gt
 */
public class MotorPrecios {
    
    private final List<ReglaPrecio> reglas;
    
    /**
     * Constructor que inicializa el motor con una lista de reglas de precio.
     * 
     * @param reglas lista de reglas de precio a aplicar en orden, no puede ser null
     * @throws IllegalArgumentException si la lista de reglas es null
     */
    public MotorPrecios(List<ReglaPrecio> reglas) {
        this.reglas = validarReglas(reglas);
    }
    
    /**
     * Calcula el precio final de un producto aplicando todas las reglas configuradas.
     * 
     * @param producto el producto para el cual calcular el precio, no puede ser null
     * @param cantidad la cantidad de productos, debe ser mayor que cero
     * @return el precio final redondeado a 2 decimales
     * @throws IllegalArgumentException si algún parámetro es inválido
     */
    public BigDecimal calcularPrecioFinal(Producto producto, int cantidad) {
        validarParametrosCalculo(producto, cantidad);
        
        // Calcula el subtotal base: precio base * cantidad
        BigDecimal subtotal = producto.getPrecioBase().multiply(BigDecimal.valueOf(cantidad));
        
        // Aplica cada regla de precio en orden
        for (ReglaPrecio regla : reglas) {
            subtotal = regla.aplicar(subtotal, producto, cantidad);
        }
        
        // Redondea a 2 decimales usando HALF_UP
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula el precio final usando un subtotal inicial específico en lugar del precio base.
     * Útil para casos donde el subtotal ya ha sido calculado previamente.
     * 
     * @param subtotalInicial el subtotal inicial sobre el cual aplicar las reglas
     * @param producto el producto para el cual calcular el precio, no puede ser null
     * @param cantidad la cantidad de productos, debe ser mayor que cero
     * @return el precio final redondeado a 2 decimales
     * @throws IllegalArgumentException si algún parámetro es inválido
     */
    public BigDecimal calcularPrecioFinal(BigDecimal subtotalInicial, Producto producto, int cantidad) {
        validarParametrosCalculo(producto, cantidad);
        validarSubtotalInicial(subtotalInicial);
        
        BigDecimal subtotal = subtotalInicial;
        
        // Aplica cada regla de precio en orden
        for (ReglaPrecio regla : reglas) {
            subtotal = regla.aplicar(subtotal, producto, cantidad);
        }
        
        // Redondea a 2 decimales usando HALF_UP
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Obtiene la lista de reglas configuradas en el motor.
     * 
     * @return una copia de la lista de reglas para evitar modificaciones externas
     */
    public List<ReglaPrecio> getReglas() {
        return List.copyOf(reglas);
    }
    
    /**
     * Obtiene el número de reglas configuradas en el motor.
     * 
     * @return el número de reglas
     */
    public int getNumeroDeReglas() {
        return reglas.size();
    }
    
    /**
     * Valida que la lista de reglas sea válida.
     * 
     * @param reglas la lista de reglas a validar
     * @return la lista de reglas si es válida
     * @throws IllegalArgumentException si la lista es null
     */
    private List<ReglaPrecio> validarReglas(List<ReglaPrecio> reglas) {
        if (reglas == null) {
            throw new IllegalArgumentException("La lista de reglas no puede ser null");
        }
        
        // Verifica que no haya reglas null en la lista
        for (int i = 0; i < reglas.size(); i++) {
            if (reglas.get(i) == null) {
                throw new IllegalArgumentException("La regla en la posición " + i + " no puede ser null");
            }
        }
        
        return List.copyOf(reglas); // Crea una copia inmutable
    }
    
    /**
     * Valida los parámetros para el cálculo de precio.
     * 
     * @param producto el producto a validar
     * @param cantidad la cantidad a validar
     * @throws IllegalArgumentException si algún parámetro es inválido
     */
    private void validarParametrosCalculo(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }
    
    /**
     * Valida que el subtotal inicial sea válido.
     * 
     * @param subtotalInicial el subtotal inicial a validar
     * @throws IllegalArgumentException si el subtotal inicial es inválido
     */
    private void validarSubtotalInicial(BigDecimal subtotalInicial) {
        if (subtotalInicial == null) {
            throw new IllegalArgumentException("El subtotal inicial no puede ser null");
        }
        if (subtotalInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El subtotal inicial no puede ser negativo");
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MotorPrecios that = (MotorPrecios) obj;
        return Objects.equals(reglas, that.reglas);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(reglas);
    }
    
    @Override
    public String toString() {
        return String.format("MotorPrecios{reglas=%d}", reglas.size());
    }
}
