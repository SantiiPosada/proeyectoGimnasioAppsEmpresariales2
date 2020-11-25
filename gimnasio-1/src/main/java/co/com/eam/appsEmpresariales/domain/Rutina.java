package co.com.eam.appsEmpresariales.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * The persistent class for the rutinas database table.
 * 
 */
@Data
@Entity
@Table(name = "rutinas")
//@NamedQuery(name="Rutina.findAll", query="SELECT r FROM Rutina r")
public class Rutina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message = "{DomainRutina.urlFoto}")
	private String urlFoto;

	@NotNull(message = "{DomainRutina.nameDescription}")
	@Size(min = 2, max = 255, message = "{DomainRutina.descriptionSize}")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$", message = "{DomainRutina.descriptionSizePattern}")
	private String descripcion;

	// bi-directional many-to-one association to Instructor
	@ManyToOne
	@NotNull(message = "{DomainRutina.nameInstrcutor}")
	@JoinColumn(name = "IdInstructor")
	private Instructor instructor;

	// bi-directional many-to-one association to Socio
	@ManyToOne
	@NotNull(message = "{DomainRutina.namePartner}")
	@JoinColumn(name = "IdSocio")
	private Socio socio;

}