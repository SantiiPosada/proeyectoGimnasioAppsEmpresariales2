package co.com.eam.appsEmpresariales.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilConvertidor {

	public Date parseFecha(String fecha) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(fecha);
			System.out.println("esta es la fecha convertida" + fechaDate.getDate());
		} catch (ParseException e) {
			System.out.println(e);
		}
		return fechaDate;
	}
	
//	public Date parseFecha(String fecha) {
//		  SimpleDateFormat formatter6 = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");//      dd/MM/yyyy
//	        try {
//	            Date date6 = formatter6.parse(fecha);
//	            return date6;
//	        } catch (Exception e) {
//	        	 System.out.println("Hubo un error al pasarlo de string a date :'(");
//	            System.out.println(e.toString());
//	        }
//
//	        return null;
//	}
	
}
