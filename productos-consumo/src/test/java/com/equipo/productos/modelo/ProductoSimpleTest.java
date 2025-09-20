package com.equipo.productos.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

/**
 * Pruebas unitarias para la clase ProductoSimple.
 * Verifica que los constructores y validaciones funcionen correctamente.
 */
@DisplayName("ProductoSimple")
class ProductoSimpleTest {

    @Nested
    @DisplayName("Constructor con BigDecimal")
    class ConstructorConBigDecimal {

        @Test
        @DisplayName("Debe crear producto correctamente con parámetros válidos")
        void debeCrearProductoCorrectamente() {
            // Given
            String nombre = "Laptop Gaming";
            BigDecimal precio = new BigDecimal("1299.99");

            // When
            ProductoSimple producto = new ProductoSimple(nombre, precio);

            // Then
            assertEquals("Laptop Gaming", producto.getNombre());
            assertEquals(new BigDecimal("1299.99"), producto.getPrecioBase());
        }

        @Test
        @DisplayName("Debe limpiar espacios en blanco del nombre")
        void debeLimpiarEspaciosEnBlanco() {
            // Given
            String nombreConEspacios = "  Tablet Android  ";
            BigDecimal precio = new BigDecimal("299.99");

            // When
            ProductoSimple producto = new ProductoSimple(nombreConEspacios, precio);

            // Then
            assertEquals("Tablet Android", producto.getNombre());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el nombre es null")
        void debeLanzarExcepcionCuandoNombreEsNull() {
            // Given
            String nombre = null;
            BigDecimal precio = new BigDecimal("100.00");

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ProductoSimple(nombre, precio)
            );
            assertEquals("El nombre del producto no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el nombre está vacío")
        void debeLanzarExcepcionCuandoNombreEstaVacio() {
            // Given
            String nombre = "";
            BigDecimal precio = new BigDecimal("100.00");

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ProductoSimple(nombre, precio)
            );
            assertEquals("El nombre del producto no puede estar vacío", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el nombre solo contiene espacios")
        void debeLanzarExcepcionCuandoNombreSoloContieneEspacios() {
            // Given
            String nombre = "   ";
            BigDecimal precio = new BigDecimal("100.00");

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ProductoSimple(nombre, precio)
            );
            assertEquals("El nombre del producto no puede estar vacío", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio es null")
        void debeLanzarExcepcionCuandoPrecioEsNull() {
            // Given
            String nombre = "Producto Test";
            BigDecimal precio = null;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ProductoSimple(nombre, precio)
            );
            assertEquals("El precio base no puede ser null", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio es cero")
        void debeLanzarExcepcionCuandoPrecioEsCero() {
            // Given
            String nombre = "Producto Test";
            BigDecimal precio = BigDecimal.ZERO;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ProductoSimple(nombre, precio)
            );
            assertEquals("El precio base debe ser mayor que cero", excepcion.getMessage());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio es negativo")
        void debeLanzarExcepcionCuandoPrecioEsNegativo() {
            // Given
            String nombre = "Producto Test";
            BigDecimal precio = new BigDecimal("-50.00");

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ProductoSimple(nombre, precio)
            );
            assertEquals("El precio base debe ser mayor que cero", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor con double")
    class ConstructorConDouble {

        @Test
        @DisplayName("Debe crear producto correctamente con precio double")
        void debeCrearProductoCorrectamenteConDouble() {
            // Given
            String nombre = "Mouse Inalámbrico";
            double precio = 25.99;

            // When
            ProductoSimple producto = new ProductoSimple(nombre, precio);

            // Then
            assertEquals("Mouse Inalámbrico", producto.getNombre());
            assertEquals(BigDecimal.valueOf(25.99), producto.getPrecioBase());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio double es negativo")
        void debeLanzarExcepcionCuandoPrecioDoubleEsNegativo() {
            // Given
            String nombre = "Producto Test";
            double precio = -10.5;

            // When & Then
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> new ProductoSimple(nombre, precio)
            );
            assertEquals("El precio base debe ser mayor que cero", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Métodos equals y hashCode")
    class MetodosEqualsYHashCode {

        @Test
        @DisplayName("Dos productos con los mismos datos deben ser iguales")
        void dosProductosConLosMismosDatosDebenSerIguales() {
            // Given
            ProductoSimple producto1 = new ProductoSimple("Teclado", new BigDecimal("45.00"));
            ProductoSimple producto2 = new ProductoSimple("Teclado", new BigDecimal("45.00"));

            // When & Then
            assertEquals(producto1, producto2);
            assertEquals(producto1.hashCode(), producto2.hashCode());
        }

        @Test
        @DisplayName("Dos productos con diferentes nombres no deben ser iguales")
        void dosProductosConDiferentesNombresNoDebenSerIguales() {
            // Given
            ProductoSimple producto1 = new ProductoSimple("Teclado", new BigDecimal("45.00"));
            ProductoSimple producto2 = new ProductoSimple("Mouse", new BigDecimal("45.00"));

            // When & Then
            assertNotEquals(producto1, producto2);
        }

        @Test
        @DisplayName("Dos productos con diferentes precios no deben ser iguales")
        void dosProductosConDiferentesPreciosNoDebenSerIguales() {
            // Given
            ProductoSimple producto1 = new ProductoSimple("Teclado", new BigDecimal("45.00"));
            ProductoSimple producto2 = new ProductoSimple("Teclado", new BigDecimal("50.00"));

            // When & Then
            assertNotEquals(producto1, producto2);
        }
    }

    @Nested
    @DisplayName("Método toString")
    class MetodoToString {

        @Test
        @DisplayName("toString debe retornar representación correcta")
        void toStringDebeRetornarRepresentacionCorrecta() {
            // Given
            ProductoSimple producto = new ProductoSimple("Monitor 4K", new BigDecimal("399.99"));

            // When
            String resultado = producto.toString();

            // Then
            assertEquals("ProductoSimple{nombre='Monitor 4K', precioBase=399.99}", resultado);
        }
    }
}
