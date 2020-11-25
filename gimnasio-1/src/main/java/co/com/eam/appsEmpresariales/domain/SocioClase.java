package co.com.eam.appsEmpresariales.domain;



import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;


/**
 * The persistent class for the socioclase database table.
 * 
 */
@Data
@Entity
//@NamedQuery(name="Socioclase.findAll", query="SELECT s FROM Socioclase s")
public class SocioClase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	//bi-directional many-to-one association to Clase
	@ManyToOne
	@NotNull(message = "Debe ingresar la clase en la que va a estar el socio")
	@Size(min=1, max=255, message = "El codigo debe de tener entre 1 a 255 números ")
	@Pattern(regexp = "^[0-9]{1,255}$",message = "El codigo debe de tener al menos un número entero")
	@JoinColumn(name="IdClase")
	private Clase clase;

	//bi-directional many-to-one association to Socio
	@ManyToOne
	@NotNull(message = "Debe ingresar el socio que va a estar en la clase")
	@Size(min=1, max=255, message = "El codigo debe de tener entre 1 a 255 números ")
	@Pattern(regexp = "^[0-9]{1,255}$",message = "El codigo debe de tener al menos un número entero")
	@JoinColumn(name="IdSocio")
	private Socio socio;

	

}