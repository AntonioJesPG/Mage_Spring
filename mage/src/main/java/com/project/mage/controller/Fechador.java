package com.project.mage.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

//Esta clase simplemente nos permite cambiar el formato de las fechas al formato más usado dentro de Europa
public class Fechador {
	public String formatDate(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String fecha = formatter.format(d);
		return fecha;
	}
}
