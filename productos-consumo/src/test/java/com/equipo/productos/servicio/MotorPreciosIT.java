package com.equipo.productos.servicio;

import com.equipo.productos.modelo.Producto;
import com.equipo.productos.modelo.ProductoSimple;
import com.equipo.productos.reglas.ReglaPrecio;
import com.equipo.productos.reglas.ReglaDescuentoFijo;
import com.equipo.productos.reglas.ReglaDescuentoPorcentaje;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Pruebas de integración para la clase MotorPrecios.
 * Verifica que el motor funcione correctamente aplicando múltiples reglas
 * de precio en combinación y que el redondeo funcione adecuadamente.
 * 
 * @author Julio Martínez jmartinezm45@miumg.edu.gt
 */
@DisplayName("MotorPrecios - Pruebas de Integración")
class MotorPreciosIT {

    private Producto producto;
    private Producto productoBarato;
    private Producto productoCaro;

    @BeforeEach
    void setUp() {
        producto = new ProductoSimple("Laptop Gaming", new BigDecimal("1000.00"));
        productoBarato = new ProductoSimple("Mouse", new BigDecimal("25.00"));
        productoCaro = new ProductoSimple("Servidor", new BigDecimal("5000.00"));
    }

    @Nested
    @DisplayName("Constructor y configuración")
    class ConstructorYConfiguracion {

        @Test
        @DisplayName("Debe crear motor correctamente con lista de reglas")
        void debeCrearMotorCorrectamenteConListaDeReglas() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoFijo(new BigDecimal("50.00")),
                new ReglaDescuentoPorcentaje(new BigDecimal("0.10"))
            );

            // When
            MotorPrecios motor = new MotorPrecios(reglas);

            // Then
            assertEquals(2, motor.getNumeroDeReglas());
            assertEquals(reglas, motor.getReglas());
        }

        @Test
        @DisplayName("Debe crear motor correctamente con lista vacía")
        void debeCrearMotorCorrectamenteConListaVacia() {
            // Given
            List<ReglaPrecio> reglas = new ArrayList<>();

            // When
            MotorPrecios motor = new MotorPrecios(reglas);

            // Then
            assertEquals(0, motor.getNumeroDeReglas());
            assertTrue(motor.getReglas().isEmpty());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la lista de reglas es null")
        void debeLanzarExcepcionCuandoListaDeReglasEsNull() {
            // Given
            List<ReglaPrecio> reglas = null;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new MotorPrecios(reglas)
            );
            assertEquals("La lista de reglas no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando hay una regla null en la lista")
        void debeLanzarExcepcionCuandoHayUnaReglaNullEnLaLista() {
            // Given
            List<ReglaPrecio> reglas = new ArrayList<>();
            reglas.add(new ReglaDescuentoFijo(new BigDecimal("10.00")));
            reglas.add(null);
            reglas.add(new ReglaDescuentoPorcentaje(new BigDecimal("0.15")));

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new MotorPrecios(reglas)
            );
            assertEquals("La regla en la posición 1 no puede ser null", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Cálculo de precio final sin reglas")
    class CalculoPrecioFinalSinReglas {

        @Test
        @DisplayName("Debe calcular precio correcto sin reglas aplicadas")
        void debeCalcularPrecioCorrectoSinReglasAplicadas() {
            // Given
            MotorPrecios motor = new MotorPrecios(List.of());
            int cantidad = 3;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(producto, cantidad);

            // Then
            // 1000.00 * 3 = 3000.00
            assertEquals(new BigDecimal("3000.00"), precioFinal);
        }

        @Test
        @DisplayName("Debe redondear correctamente a 2 decimales")
        void debeRedondearCorrectamenteADosDecimales() {
            // Given
            Producto productoConDecimales = new ProductoSimple("Test", new BigDecimal("33.333"));
            MotorPrecios motor = new MotorPrecios(List.of());
            int cantidad = 1;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(productoConDecimales, cantidad);

            // Then
            // 33.333 redondeado a 2 decimales = 33.33
            assertEquals(new BigDecimal("33.33"), precioFinal);
        }
    }

    @Nested
    @DisplayName("Cálculo con una sola regla")
    class CalculoConUnaSolaRegla {

        @Test
        @DisplayName("Debe aplicar correctamente solo descuento fijo")
        void debeAplicarCorrectamenteSoloDescuentoFijo() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoFijo(new BigDecimal("100.00"))
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 2;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(producto, cantidad);

            // Then
            // Subtotal: 1000.00 * 2 = 2000.00
            // Descuento fijo: 100.00 * 2 = 200.00
            // Final: 2000.00 - 200.00 = 1800.00
            assertEquals(new BigDecimal("1800.00"), precioFinal);
        }

        @Test
        @DisplayName("Debe aplicar correctamente solo descuento porcentual")
        void debeAplicarCorrectamenteSoloDescuentoPorcentual() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoPorcentaje(new BigDecimal("0.20"))
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 1;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(producto, cantidad);

            // Then
            // Subtotal: 1000.00 * 1 = 1000.00
            // Descuento 20%: 1000.00 * 0.80 = 800.00
            assertEquals(new BigDecimal("800.00"), precioFinal);
        }
    }

    @Nested
    @DisplayName("Cálculo con múltiples reglas")
    class CalculoConMultiplesReglas {

        @Test
        @DisplayName("Debe aplicar reglas en orden: descuento fijo primero, luego porcentual")
        void debeAplicarReglasEnOrdenDescuentoFijoPrimeroLuegoPorcentual() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoFijo(new BigDecimal("200.00")),
                new ReglaDescuentoPorcentaje(new BigDecimal("0.10"))
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 1;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(producto, cantidad);

            // Then
            // Subtotal inicial: 1000.00 * 1 = 1000.00
            // Después descuento fijo: 1000.00 - 200.00 = 800.00
            // Después descuento 10%: 800.00 * 0.90 = 720.00
            assertEquals(new BigDecimal("720.00"), precioFinal);
        }

        @Test
        @DisplayName("Debe aplicar reglas en orden: porcentual primero, luego fijo")
        void debeAplicarReglasEnOrdenPorcentualPrimeroLuegoFijo() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoPorcentaje(new BigDecimal("0.10")),
                new ReglaDescuentoFijo(new BigDecimal("200.00"))
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 1;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(producto, cantidad);

            // Then
            // Subtotal inicial: 1000.00 * 1 = 1000.00
            // Después descuento 10%: 1000.00 * 0.90 = 900.00
            // Después descuento fijo: 900.00 - 200.00 = 700.00
            assertEquals(new BigDecimal("700.00"), precioFinal);
        }

        @Test
        @DisplayName("Debe aplicar múltiples descuentos fijos correctamente")
        void debeAplicarMultiplesDescuentosFijosCorrectamente() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoFijo(new BigDecimal("50.00")),
                new ReglaDescuentoFijo(new BigDecimal("30.00")),
                new ReglaDescuentoFijo(new BigDecimal("20.00"))
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 2;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(producto, cantidad);

            // Then
            // Subtotal inicial: 1000.00 * 2 = 2000.00
            // Descuento 1: 2000.00 - (50.00 * 2) = 1900.00
            // Descuento 2: 1900.00 - (30.00 * 2) = 1840.00
            // Descuento 3: 1840.00 - (20.00 * 2) = 1800.00
            assertEquals(new BigDecimal("1800.00"), precioFinal);
        }

        @Test
        @DisplayName("Debe aplicar múltiples descuentos porcentuales correctamente")
        void debeAplicarMultiplesDescuentosPorcentualesCorrectamente() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoPorcentaje(new BigDecimal("0.10")), // 10%
                new ReglaDescuentoPorcentaje(new BigDecimal("0.05"))  // 5%
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 1;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(producto, cantidad);

            // Then
            // Subtotal inicial: 1000.00 * 1 = 1000.00
            // Descuento 1 (10%): 1000.00 * 0.90 = 900.00
            // Descuento 2 (5%): 900.00 * 0.95 = 855.00
            assertEquals(new BigDecimal("855.00"), precioFinal);
        }
    }

    @Nested
    @DisplayName("Casos especiales y límite")
    class CasosEspecialesYLimite {

        @Test
        @DisplayName("Debe manejar correctamente cuando el descuento fijo es mayor al subtotal")
        void debeManejareCorrectamenteCuandoDescuentoFijoEsMayorAlSubtotal() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoFijo(new BigDecimal("50.00")) // 50 * 1 = 50 > 25
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 1;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(productoBarato, cantidad);

            // Then
            // Subtotal: 25.00 * 1 = 25.00
            // Descuento fijo sería 50.00, pero se limita a 0.00
            assertEquals(new BigDecimal("0.00"), precioFinal);
        }

        @Test
        @DisplayName("Debe manejar correctamente descuento 100%")
        void debeManejareCorrectamenteDescuentoCienPorCiento() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoPorcentaje(BigDecimal.ONE) // 100%
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 3;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(productoCaro, cantidad);

            // Then
            // Subtotal: 5000.00 * 3 = 15000.00
            // Descuento 100%: 15000.00 * 0.00 = 0.00
            assertEquals(new BigDecimal("0.00"), precioFinal);
        }

        @Test
        @DisplayName("Debe redondear correctamente con cálculos complejos")
        void debeRedondearCorrectamenteConCalculosComplejos() {
            // Given
            Producto productoDecimal = new ProductoSimple("Test", new BigDecimal("33.33"));
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoPorcentaje(new BigDecimal("0.333")) // 33.3%
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            int cantidad = 1;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(productoDecimal, cantidad);

            // Then
            // Subtotal: 33.33 * 1 = 33.33
            // Descuento 33.3%: 33.33 * 0.667 = 22.23111
            // Redondeado: 22.23
            assertEquals(new BigDecimal("22.23"), precioFinal);
        }
    }

    @Nested
    @DisplayName("Cálculo con subtotal inicial")
    class CalculoConSubtotalInicial {

        @Test
        @DisplayName("Debe calcular correctamente usando subtotal inicial")
        void debeCalcularCorrectamenteUsandoSubtotalInicial() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoPorcentaje(new BigDecimal("0.20"))
            );
            MotorPrecios motor = new MotorPrecios(reglas);
            BigDecimal subtotalInicial = new BigDecimal("500.00");
            int cantidad = 1;

            // When
            BigDecimal precioFinal = motor.calcularPrecioFinal(subtotalInicial, producto, cantidad);

            // Then
            // Subtotal inicial: 500.00 (ignora precio base del producto)
            // Descuento 20%: 500.00 * 0.80 = 400.00
            assertEquals(new BigDecimal("400.00"), precioFinal);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando subtotal inicial es null")
        void debeLanzarExcepcionCuandoSubtotalInicialEsNull() {
            // Given
            MotorPrecios motor = new MotorPrecios(List.of());
            BigDecimal subtotalInicial = null;
            int cantidad = 1;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> motor.calcularPrecioFinal(subtotalInicial, producto, cantidad)
            );
            assertEquals("El subtotal inicial no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando subtotal inicial es negativo")
        void debeLanzarExcepcionCuandoSubtotalInicialEsNegativo() {
            // Given
            MotorPrecios motor = new MotorPrecios(List.of());
            BigDecimal subtotalInicial = new BigDecimal("-100.00");
            int cantidad = 1;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> motor.calcularPrecioFinal(subtotalInicial, producto, cantidad)
            );
            assertEquals("El subtotal inicial no puede ser negativo", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de parámetros")
    class ValidacionesDeParametros {

        @Test
        @DisplayName("Debe lanzar excepción cuando el producto es null")
        void debeLanzarExcepcionCuandoProductoEsNull() {
            // Given
            MotorPrecios motor = new MotorPrecios(List.of());
            Producto productoNull = null;
            int cantidad = 1;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> motor.calcularPrecioFinal(productoNull, cantidad)
            );
            assertEquals("El producto no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la cantidad es cero")
        void debeLanzarExcepcionCuandoCantidadEsCero() {
            // Given
            MotorPrecios motor = new MotorPrecios(List.of());
            int cantidad = 0;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> motor.calcularPrecioFinal(producto, cantidad)
            );
            assertEquals("La cantidad debe ser mayor que cero", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la cantidad es negativa")
        void debeLanzarExcepcionCuandoCantidadEsNegativa() {
            // Given
            MotorPrecios motor = new MotorPrecios(List.of());
            int cantidad = -5;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> motor.calcularPrecioFinal(producto, cantidad)
            );
            assertEquals("La cantidad debe ser mayor que cero", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Métodos equals, hashCode y toString")
    class MetodosEqualsHashCodeYToString {

        @Test
        @DisplayName("Dos motores con las mismas reglas deben ser iguales")
        void dosMotoresConLasMismasReglasDebenSerIguales() {
            // Given
            List<ReglaPrecio> reglas1 = List.of(
                new ReglaDescuentoFijo(new BigDecimal("10.00")),
                new ReglaDescuentoPorcentaje(new BigDecimal("0.15"))
            );
            List<ReglaPrecio> reglas2 = List.of(
                new ReglaDescuentoFijo(new BigDecimal("10.00")),
                new ReglaDescuentoPorcentaje(new BigDecimal("0.15"))
            );

            MotorPrecios motor1 = new MotorPrecios(reglas1);
            MotorPrecios motor2 = new MotorPrecios(reglas2);

            // When & Then
            assertEquals(motor1, motor2);
            assertEquals(motor1.hashCode(), motor2.hashCode());
        }

        @Test
        @DisplayName("toString debe retornar representación correcta")
        void toStringDebeRetornarRepresentacionCorrecta() {
            // Given
            List<ReglaPrecio> reglas = List.of(
                new ReglaDescuentoFijo(new BigDecimal("50.00")),
                new ReglaDescuentoPorcentaje(new BigDecimal("0.10"))
            );
            MotorPrecios motor = new MotorPrecios(reglas);

            // When
            String resultado = motor.toString();

            // Then
            assertEquals("MotorPrecios{reglas=2}", resultado);
        }
    }
}
