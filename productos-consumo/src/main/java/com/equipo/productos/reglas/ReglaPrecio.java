package com.equipo.productos.reglas;

import com.equipo.productos.modelo.Producto;
import java.math.BigDecimal;

public interface ReglaPrecio {

    BigDecimal aplicar(BigDecimal subtotal, Producto producto, int cantidad);
}
