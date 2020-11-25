package co.com.eam.appsEmpresariales.domain;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the clase database table.
 * 
 */
@Data
@Entity
//@NamedQuery(name="Clase.findAll", query="SELECT c FROM Clase c")
public class Clase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull(message = "{DomainClase.description}")
	@Size(min = 2, max = 255, message = "{DomainClase.descriptionSize}")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$", message = "{DomainClase.descriptionPatterm}")
	private String descripcion;

	@Column(length = 20)
	// @NotNull(message = "Debe ingresar la fecha")
	// @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date diaHora;

	@Transient

	@NotNull(message = "{DomainClase.Date}")
	private String fechacadena;
	// bi-directional many-to-one association to Clasepuedenimpartirinstructor
	@ManyToOne
	@NotNull(message = "{DomainClase.enabledclasses}")
	@JoinColumn(name = "IdClasesImpartirInstructor")
	private ClasePuedenImpartirInstructor clasepuedenimpartirinstructor;

	// bi-directional many-to-one association to Sala
	@ManyToOne
	@NotNull(message = "{DomainClase.Room}")
	@JoinColumn(name = "IdSala")
	private Sala sala;

	// bi-directional many-to-one association to Socioclase
	@OneToMany(mappedBy = "clase")
	private List<SocioClase> socioclases;

	public SocioClase addSocioclas(SocioClase socioclas) {
		getSocioclases().add(socioclas);
		socioclas.setClase(this);

		return socioclas;
	}

	public SocioClase removeSocioclas(SocioClase socioclas) {
		getSocioclases().remove(socioclas);
		socioclas.setClase(null);

		return socioclas;
	}

}