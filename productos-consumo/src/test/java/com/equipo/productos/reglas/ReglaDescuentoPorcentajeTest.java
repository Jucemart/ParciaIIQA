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
 * Pruebas unitarias para la clase ReglaDescuentoPorcentaje.
 * Verifica que la regla de descuento porcentual funcione correctamente
 * en diferentes escenarios.
 * 
 * @author Julio Martínez jmartinezm45@miumg.edu.gt
 */
@DisplayName("ReglaDescuentoPorcentaje")
class ReglaDescuentoPorcentajeTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new ProductoSimple("Producto Test", new BigDecimal("100.00"));
    }

    @Nested
    @DisplayName("Constructor con BigDecimal")
    class ConstructorConBigDecimal {

        @Test
        @DisplayName("Debe crear regla correctamente con porcentaje válido")
        void debeCrearReglaCorrectamente() {
            // Given
            BigDecimal porcentaje = new BigDecimal("0.15");

            // When
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(porcentaje);

            // Then
            assertEquals(new BigDecimal("0.15"), regla.getPorcentajeDescuento());
        }

        @Test
        @DisplayName("Debe aceptar porcentaje de cero")
        void debeAceptarPorcentajeDeCero() {
            // Given
            BigDecimal porcentaje = BigDecimal.ZERO;

            // When
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(porcentaje);

            // Then
            assertEquals(BigDecimal.ZERO, regla.getPorcentajeDescuento());
        }

        @Test
        @DisplayName("Debe aceptar porcentaje de uno (100%)")
        void debeAceptarPorcentajeDeUno() {
            // Given
            BigDecimal porcentaje = BigDecimal.ONE;

            // When
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(porcentaje);

            // Then
            assertEquals(BigDecimal.ONE, regla.getPorcentajeDescuento());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el porcentaje es null")
        void debeLanzarExcepcionCuandoPorcentajeEsNull() {
            // Given
            BigDecimal porcentaje = null;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ReglaDescuentoPorcentaje(porcentaje)
            );
            assertEquals("El porcentaje de descuento no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el porcentaje es negativo")
        void debeLanzarExcepcionCuandoPorcentajeEsNegativo() {
            // Given
            BigDecimal porcentaje = new BigDecimal("-0.10");

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ReglaDescuentoPorcentaje(porcentaje)
            );
            assertEquals("El porcentaje de descuento no puede ser negativo", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el porcentaje es mayor que 1")
        void debeLanzarExcepcionCuandoPorcentajeEsMayorQueUno() {
            // Given
            BigDecimal porcentaje = new BigDecimal("1.50");

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ReglaDescuentoPorcentaje(porcentaje)
            );
            assertEquals("El porcentaje de descuento no puede ser mayor que 1", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor con double")
    class ConstructorConDouble {

        @Test
        @DisplayName("Debe crear regla correctamente con porcentaje double")
        void debeCrearReglaCorrectamenteConDouble() {
            // Given
            double porcentaje = 0.25;

            // When
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(porcentaje);

            // Then
            assertEquals(BigDecimal.valueOf(0.25), regla.getPorcentajeDescuento());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el porcentaje double es negativo")
        void debeLanzarExcepcionCuandoPorcentajeDoubleEsNegativo() {
            // Given
            double porcentaje = -0.05;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ReglaDescuentoPorcentaje(porcentaje)
            );
            assertEquals("El porcentaje de descuento no puede ser negativo", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el porcentaje double es mayor que 1")
        void debeLanzarExcepcionCuandoPorcentajeDoubleEsMayorQueUno() {
            // Given
            double porcentaje = 1.25;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ReglaDescuentoPorcentaje(porcentaje)
            );
            assertEquals("El porcentaje de descuento no puede ser mayor que 1", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Método aplicar")
    class MetodoAplicar {

        @Test
        @DisplayName("Debe aplicar descuento del 15% correctamente")
        void debeAplicarDescuentoDelQuincePorCientoCorrectamente() {
            // Given
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.15"));
            BigDecimal subtotal = new BigDecimal("100.00");
            int cantidad = 1;

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            // 100.00 * (1 - 0.15) = 100.00 * 0.85 = 85.00
            assertEquals(0, new BigDecimal("85.00").compareTo(resultado));
        }

        @Test
        @DisplayName("Debe aplicar descuento del 50% correctamente")
        void debeAplicarDescuentoDelCincuentaPorCientoCorrectamente() {
            // Given
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.50"));
            BigDecimal subtotal = new BigDecimal("200.00");
            int cantidad = 2;

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            // 200.00 * (1 - 0.50) = 200.00 * 0.50 = 100.00
            assertEquals(0, new BigDecimal("100.00").compareTo(resultado));
        }

        @Test
        @DisplayName("Debe retornar cero cuando el descuento es 100%")
        void debeRetornarCeroCuandoDescuentoEsCienPorCiento() {
            // Given
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(BigDecimal.ONE);
            BigDecimal subtotal = new BigDecimal("150.00");
            int cantidad = 3;

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            // 150.00 * (1 - 1.00) = 150.00 * 0.00 = 0.00
            assertEquals(new BigDecimal("0.00"), resultado);
        }

        @Test
        @DisplayName("Debe funcionar correctamente con descuento cero")
        void debeFuncionarCorrectamenteConDescuentoCero() {
            // Given
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(BigDecimal.ZERO);
            BigDecimal subtotal = new BigDecimal("75.50");
            int cantidad = 2;

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            // 75.50 * (1 - 0.00) = 75.50 * 1.00 = 75.50
            assertEquals(new BigDecimal("75.50"), resultado);
        }

        @Test
        @DisplayName("Debe manejar correctamente decimales precisos")
        void debeManejareCorrectamenteDecimalesPrecisos() {
            // Given
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.333"));
            BigDecimal subtotal = new BigDecimal("99.99");
            int cantidad = 1;

            // When
            BigDecimal resultado = regla.aplicar(subtotal, producto, cantidad);

            // Then
            // 99.99 * (1 - 0.333) = 99.99 * 0.667 = 66.69333
            assertEquals(0, new BigDecimal("66.69333").compareTo(resultado));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el subtotal es null")
        void debeLanzarExcepcionCuandoSubtotalEsNull() {
            // Given
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.10"));
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
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.10"));
            BigDecimal subtotal = new BigDecimal("-50.00");
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
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.10"));
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
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.10"));
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
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.10"));
            BigDecimal subtotal = new BigDecimal("100.00");
            int cantidad = -1;

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
            ReglaDescuentoPorcentaje regla = new ReglaDescuentoPorcentaje(new BigDecimal("0.20"));

            // When
            String resultado = regla.toString();

            // Then
            assertEquals("ReglaDescuentoPorcentaje{porcentajeDescuento=0.20}", resultado);
        }
    }
}
