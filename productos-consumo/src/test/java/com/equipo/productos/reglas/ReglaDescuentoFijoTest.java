package com.equipo.productos.reglas;

import com.equipo.productos.modelo.Producto;
import com.equipo.productos.modelo.ProductoSimple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

/**
 * Pruebas unitarias para la clase ReglaDescuentoFijo.
 * Verifica que la regla de descuento fijo funcione correctamente
 * en diferentes escenarios.
 */
@DisplayName("ReglaDescuentoFijo")
class ReglaDescuentoFijoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new ProductoSimple("Producto Test", new BigDecimal("100.00"));
    }

    @Nested
    @DisplayName("Constructor con BigDecimal")
    class ConstructorConBigDecimal {

        @Test
        @DisplayName("Debe crear regla correctamente con descuento válido")
        void debeCrearReglaCorrectamente() {
            // Given
            BigDecimal descuento = new BigDecimal("10.00");

            // When
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(descuento);

            // Then
            assertEquals(new BigDecimal("10.00"), regla.getDescuentoPorUnidad());
        }

        @Test
        @DisplayName("Debe aceptar descuento de cero")
        void debeAceptarDescuentoDeCero() {
            // Given
            BigDecimal descuento = BigDecimal.ZERO;

            // When
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(descuento);

            // Then
            assertEquals(BigDecimal.ZERO, regla.getDescuentoPorUnidad());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el descuento es null")
        void debeLanzarExcepcionCuandoDescuentoEsNull() {
            // Given
            BigDecimal descuento = null;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ReglaDescuentoFijo(descuento)
            );
            assertEquals("El descuento no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el descuento es negativo")
        void debeLanzarExcepcionCuandoDescuentoEsNegativo() {
            // Given
            BigDecimal descuento = new BigDecimal("-5.00");

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ReglaDescuentoFijo(descuento)
            );
            assertEquals("El descuento no puede ser negativo", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor con double")
    class ConstructorConDouble {

        @Test
        @DisplayName("Debe crear regla correctamente con descuento double")
        void debeCrearReglaCorrectamenteConDouble() {
            // Given
            double descuento = 15.50;

            // When
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(descuento);

            // Then
            assertEquals(BigDecimal.valueOf(15.50), regla.getDescuentoPorUnidad());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el descuento double es negativo")
        void debeLanzarExcepcionCuandoDescuentoDoubleEsNegativo() {
            // Given
            double descuento = -7.25;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ReglaDescuentoFijo(descuento)
            );
            assertEquals("El descuento no puede ser negativo", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Método aplicar")
    class MetodoAplicar {

        @Test
        @DisplayName("Debe aplicar descuento correctamente con una unidad")
        void debeAplicarDescuentoCorrectamenteConUnaUnidad() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("5.00"));
            BigDecimal subtotal = new BigDecimal("100.00");
            int cantidad = 1;

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            assertEquals(new BigDecimal("95.00"), resultado);
        }

        @Test
        @DisplayName("Debe aplicar descuento correctamente con múltiples unidades")
        void debeAplicarDescuentoCorrectamenteConMultiplesUnidades() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("8.00"));
            BigDecimal subtotal = new BigDecimal("200.00");
            int cantidad = 3;

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            // Descuento total: 8.00 * 3 = 24.00
            // Resultado: 200.00 - 24.00 = 176.00
            assertEquals(new BigDecimal("176.00"), resultado);
        }

        @Test
        @DisplayName("Debe retornar cero cuando el descuento es mayor al subtotal")
        void debeRetornarCeroCuandoDescuentoEsMayorAlSubtotal() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("60.00"));
            BigDecimal subtotal = new BigDecimal("100.00");
            int cantidad = 2; // Descuento total: 120.00

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            assertEquals(BigDecimal.ZERO, resultado);
        }

        @Test
        @DisplayName("Debe funcionar correctamente con descuento cero")
        void debeFuncionarCorrectamenteConDescuentoCero() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(BigDecimal.ZERO);
            BigDecimal subtotal = new BigDecimal("150.00");
            int cantidad = 5;

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            assertEquals(new BigDecimal("150.00"), resultado);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el subtotal es null")
        void debeLanzarExcepcionCuandoSubtotalEsNull() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("5.00"));
            BigDecimal subtotal = null;
            int cantidad = 1;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> regla.aplicar(subtotal, producto, cantidad)
            );
            assertEquals("El subtotal no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el subtotal es negativo")
        void debeLanzarExcepcionCuandoSubtotalEsNegativo() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("5.00"));
            BigDecimal subtotal = new BigDecimal("-10.00");
            int cantidad = 1;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> regla.aplicar(subtotal, producto, cantidad)
            );
            assertEquals("El subtotal no puede ser negativo", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el producto es null")
        void debeLanzarExcepcionCuandoProductoEsNull() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("5.00"));
            BigDecimal subtotal = new BigDecimal("100.00");
            Producto productoNull = null;
            int cantidad = 1;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> regla.aplicar(subtotal, productoNull, cantidad)
            );
            assertEquals("El producto no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la cantidad es cero")
        void debeLanzarExcepcionCuandoCantidadEsCero() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("5.00"));
            BigDecimal subtotal = new BigDecimal("100.00");
            int cantidad = 0;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> regla.aplicar(subtotal, producto, cantidad)
            );
            assertEquals("La cantidad debe ser mayor que cero", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la cantidad es negativa")
        void debeLanzarExcepcionCuandoCantidadEsNegativa() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("5.00"));
            BigDecimal subtotal = new BigDecimal("100.00");
            int cantidad = -2;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> regla.aplicar(subtotal, producto, cantidad)
            );
            assertEquals("La cantidad debe ser mayor que cero", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Método toString")
    class MetodoToString {

        @Test
        @DisplayName("toString debe retornar representación correcta")
        void toStringDebeRetornarRepresentacionCorrecta() {
            // Given
            ReglaDescuentoFijo regla = new ReglaDescuentoFijo(new BigDecimal("12.50"));

            // When
            String resultado = regla.toString();

            // Then
            assertEquals("ReglaDescuentoFijo{descuentoPorUnidad=12.50}", resultado);
        }
    }
}
