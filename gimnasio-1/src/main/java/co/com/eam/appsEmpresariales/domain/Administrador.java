package co.com.eam.appsEmpresariales.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



import lombok.Data;

/**
 * The persistent class for the administrador database table.
 * 
 */
@Data
@Entity

//@NamedQuery(name = "Administrador.findAll", query = "SELECT a FROM Administrador a")
public class Administrador  implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	
	@Column(length = 10,unique = true)
	@NotNull(message = "Debe ingresar la cedula")
	@Size(min=7, max=10, message = "La cedula debe tener entre 7 a 10 números")
	private String Cedula;
	
	
	
	@NotNull(message = "Debe ingresar el nombre")
	@Size(min=2, max=255, message = "EL nombre debe tener entre 2 y 255 letras")
	@Pattern(regexp = "^([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\']+[\\s])+([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])+[\\s]?([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])?$",message = "El nombre debe de tener letras Mayúsculas o minusculas, debe contener Un nombre y un apellido Y no puede tener espacios  antes de la primera letra")
	private String Nombre;
	
	@Column(length = 10)
	@NotNull(message = "Debe ingresar el número del celular")
	@Size(min=10, max=10, message = "EL celular debe tener 10 números")
	private String Celular;
	
	
	@Column(unique = true)
	@NotNull(message = "Debe ingresar el correo electronico")
	@Pattern(regexp = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$",message = "Debe ingresar una direccion de correo electronico valido, No puede tener Mayusculas")
	private String CorreoElectronico;
	
	@Column(length = 16)
	@NotNull(message = "Debe ingresar la contraseña")
	@Size(min=8, max=16, message = "La contraseña debe tener entre 8 y 16 caracteres")
	@Pattern(regexp = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$",message = "La contraseña debe tener al entre 8 y 16 caracteres, al menos un dígito, al menos una minúscula y al menos una mayúscula.\r\n" + 
			"NO puede tener otros símbolos.\r\n" + 
			"Ejemplo:\r\n" + 
			"w3Unpocodet0d0\r\n" + 
			"\r\n" + 
			"")
	private String Contrasena;
	
	@NotNull(message = "Debe ingresar la Jornada")
	@Size(min=2, max=255, message = "La Jornada debe tener entre 2 y 255 letras")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$",message = "La jornada debe de tener letras Mayúsculas o minusculas Y no puede tener espacios  antes de la primera letra")
	private String jornada;
	
	
}