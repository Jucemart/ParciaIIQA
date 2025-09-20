# Sistema de Gestión de Productos con Motor de Precios

**Autores:** Julio Martínez & Alvaro Pur - 2025

## 📋 Descripción del Proyecto

Este proyecto implementa un **sistema de gestión de productos con motor de precios** desarrollado en Java 17 utilizando Maven. El sistema permite calcular precios finales de productos aplicando diferentes reglas de descuento de forma flexible y extensible.

### 🎯 Características Principales

- **Modelo de Dominio Robusto**: Interfaz `Producto` con implementación `ProductoSimple`
- **Motor de Reglas de Precios**: Sistema extensible basado en el patrón Strategy
- **Reglas de Descuento Implementadas**:
  - **Descuento Fijo**: Descuenta un monto fijo por unidad
  - **Descuento Porcentual**: Descuenta un porcentaje del subtotal (0-100%)
- **Validaciones Exhaustivas**: Manejo robusto de parámetros inválidos
- **Precisión Decimal**: Uso de `BigDecimal` para cálculos monetarios exactos
- **Arquitectura Limpia**: Separación clara de responsabilidades

## 🏗️ Arquitectura del Sistema

### Estructura de Paquetes

```
com.equipo.productos/
├── modelo/                    # Entidades del dominio
│   ├── Producto.java         # Interfaz del producto
│   └── ProductoSimple.java   # Implementación básica
├── reglas/                   # Reglas de precio (Strategy Pattern)
│   ├── ReglaPrecio.java      # Interfaz común
│   ├── ReglaDescuentoFijo.java      # Descuento fijo por unidad
│   └── ReglaDescuentoPorcentaje.java # Descuento porcentual
└── servicio/                 # Orquestación
    └── MotorPrecios.java     # Aplicador de reglas en cadena
```

### Patrones de Diseño Utilizados

1. **Strategy Pattern**: Las reglas de precio son intercambiables
2. **Template Method**: Estructura común de validaciones
3. **Value Object**: `ProductoSimple` como objeto inmutable
4. **Composite**: El motor compone múltiples reglas

## 🚀 Funcionalidades

### Motor de Precios

El `MotorPrecios` es el componente central que:

1. **Calcula el subtotal base**: `precio_base × cantidad`
2. **Aplica reglas en secuencia**: Cada regla modifica el subtotal
3. **Redondea el resultado**: A 2 decimales usando HALF_UP
4. **Maneja casos límite**: Evita precios negativos

### Reglas de Descuento

#### ReglaDescuentoFijo
- Descuenta un monto fijo por cada unidad del producto
- Ejemplo: $10 de descuento por unidad × 3 unidades = $30 de descuento total
- Garantiza que el precio final no sea negativo

#### ReglaDescuentoPorcentaje  
- Descuenta un porcentaje del subtotal actual
- Rango válido: 0.0 a 1.0 (0% a 100%)
- Ejemplo: 15% de descuento (0.15) sobre $100 = $85 final

### Ejemplo de Uso

```java
// Crear un producto
Producto laptop = new ProductoSimple("Laptop Gaming", new BigDecimal("1000.00"));

// Definir reglas de descuento
List<ReglaPrecio> reglas = List.of(
    new ReglaDescuentoFijo(new BigDecimal("50.00")),      // $50 por unidad
    new ReglaDescuentoPorcentaje(new BigDecimal("0.10"))   // 10% adicional
);

// Crear el motor
MotorPrecios motor = new MotorPrecios(reglas);

// Calcular precio final
BigDecimal precioFinal = motor.calcularPrecioFinal(laptop, 2);
// Resultado: (1000×2) - (50×2) = 1900, luego 1900×0.90 = 1710.00
```

## 🧪 Cómo Probar el Sistema

### Prerrequisitos

- **Java 17** o superior
- **Maven 3.6+**
- IDE compatible (IntelliJ IDEA, Eclipse, VS Code)

### Compilación

```bash
# Limpiar y compilar el proyecto
mvn clean compile

# Compilar también las pruebas
mvn test-compile
```

### Ejecución de Pruebas

#### 1. Orden Recomendado de Pruebas

```bash
# Paso 1: Pruebas del modelo base
mvn test -Dtest=ProductoSimpleTest

# Paso 2: Pruebas de reglas individuales  
mvn test -Dtest=ReglaDescuentoFijoTest
mvn test -Dtest=ReglaDescuentoPorcentajeTest

# Paso 3: Pruebas de integración
mvn test -Dtest=MotorPreciosIT

# Paso 4: Todas las pruebas
mvn test
```

#### 2. Pruebas por Categoría

```bash
# Solo pruebas del modelo
mvn test -Dtest="**/modelo/**"

# Solo pruebas de reglas
mvn test -Dtest="**/reglas/**"  

# Solo pruebas de servicio
mvn test -Dtest="**/servicio/**"
```

#### 3. Pruebas Específicas

```bash
# Probar solo constructores
mvn test -Dtest="**/*Test*Constructor*"

# Probar solo validaciones
mvn test -Dtest="**/*Test*Validar*"

# Probar solo casos límite
mvn test -Dtest="**/*Test*Limite*"
```

### Casos de Prueba Incluidos

#### ProductoSimpleTest
- ✅ Constructores con `BigDecimal` y `double`
- ✅ Validaciones de nombre (null, vacío, espacios)
- ✅ Validaciones de precio (null, negativo, cero)
- ✅ Métodos `equals`, `hashCode` y `toString`

#### ReglaDescuentoFijoTest  
- ✅ Constructores y validaciones
- ✅ Aplicación correcta de descuentos
- ✅ Manejo de descuentos mayores al subtotal
- ✅ Validación de parámetros

#### ReglaDescuentoPorcentajeTest
- ✅ Validación de rango [0,1] para porcentajes
- ✅ Cálculos con diferentes porcentajes (0%, 15%, 50%, 100%)
- ✅ Manejo de decimales precisos
- ✅ Casos límite y validaciones

#### MotorPreciosIT (Integración)
- ✅ Configuración del motor con múltiples reglas
- ✅ Aplicación secuencial de reglas
- ✅ Combinaciones de descuentos fijos y porcentuales
- ✅ Redondeo correcto a 2 decimales
- ✅ Casos especiales (descuentos 100%, productos baratos)

### Interpretación de Resultados

#### ✅ Éxito
```
[INFO] Tests run: 45, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

#### ❌ Fallos Comunes

1. **Errores de Compilación**
   ```
   [ERROR] COMPILATION ERROR
   ```
   **Solución**: Verificar Java 17 y dependencias Maven

2. **Fallos de Validación**
   ```
   IllegalArgumentException: El precio base debe ser mayor que cero
   ```
   **Causa**: Datos de prueba inválidos

3. **Errores de Precisión**
   ```
   Expected: 85.00, Actual: 85.0000
   ```
   **Causa**: Comparación incorrecta de `BigDecimal`

## 🔧 Configuración del Entorno

### Maven (pom.xml)

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <junit.version>5.10.0</junit.version>
</properties>
```

### Dependencias

- **JUnit 5**: Framework de pruebas unitarias
- **Java 17**: Versión mínima requerida

## 📊 Métricas de Calidad

- **Cobertura de Pruebas**: 100% de métodos públicos
- **Casos de Prueba**: 45+ escenarios diferentes
- **Validaciones**: Manejo exhaustivo de casos inválidos
- **Documentación**: Javadoc completo en todas las clases

## 🔍 Casos de Uso Reales

### Escenario 1: Tienda de Electrónicos
```java
Producto laptop = new ProductoSimple("Laptop Gaming", new BigDecimal("1500.00"));
List<ReglaPrecio> reglas = List.of(
    new ReglaDescuentoFijo(new BigDecimal("100.00")),     // Descuento fijo
    new ReglaDescuentoPorcentaje(new BigDecimal("0.05"))   // 5% descuento adicional
);
// Precio final: (1500-100) × 0.95 = 1330.00
```

### Escenario 2: Promoción Especial
```java
Producto mouse = new ProductoSimple("Mouse Gaming", new BigDecimal("50.00"));
List<ReglaPrecio> reglas = List.of(
    new ReglaDescuentoPorcentaje(new BigDecimal("0.20")),  // 20% descuento
    new ReglaDescuentoFijo(new BigDecimal("5.00"))         // $5 adicional
);
// Para 3 unidades: (150 × 0.80) - 15 = 105.00
```

## 🚀 Extensibilidad

El sistema está diseñado para ser fácilmente extensible:

### Agregar Nuevas Reglas
```java
public class ReglaDescuentoGraduado implements ReglaPrecio {
    // Descuento que aumenta según la cantidad
}
```

### Nuevos Tipos de Producto
```java
public class ProductoPremium implements Producto {
    // Productos con características especiales
}
```

## 📝 Notas de Desarrollo

- **Inmutabilidad**: Todos los objetos son inmutables
- **Validación Defensiva**: Validación exhaustiva en constructores
- **Precisión Decimal**: Uso consistente de `BigDecimal`
- **Patrones de Prueba**: Given-When-Then en todas las pruebas
- **Documentación**: Javadoc completo con ejemplos

## 🏆 Autores

**Julio Martínez & Alvaro Pur - 2025**

*Proyecto desarrollado como parte del curso de Calidad de Software*

---

**¿Necesitas ayuda?** Revisa la documentación Javadoc en el código fuente o ejecuta las pruebas unitarias para ver ejemplos de uso detallados.
