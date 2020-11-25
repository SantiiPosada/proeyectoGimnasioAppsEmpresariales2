package co.com.eam.appsEmpresariales.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

import java.util.List;

/**
 * The persistent class for the instructor database table.
 * 
 */
@Data
@Entity
//@NamedQuery(name = "Instructor.findAll", query = "SELECT i FROM Instructor i")
public class Instructor implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 10, unique = true)
	@NotNull(message = "{DomainInstructor.cedula}")
	@Size(min = 7, max = 10, message = "{DomainInstructor.cedulaSize}")
	@Pattern(regexp = "^([0-9])*$", message = "{DomaiInstructor.cedulaPatterm}")
	private String Cedula;

	@NotNull(message = "{DomainInstructor.nombre}")
	@Size(min = 2, max = 255, message = "{DomainInstructor.nameSize}")
	@Pattern(regexp = "^([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\']+[\\s])+([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])+[\\s]?([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])?$", message = "{DomainInstructor.namePatterm}")
	private String nombre;

	@Column(length = 10)
	@NotNull(message = "{DomainInstructor.phone}")
	@Size(min = 10, max = 10, message = "{DomainInstructor.phoneSize}")
	@Pattern(regexp = "^([0-9])*$", message = "{DomainInstructor.phonePatterm}")
	private String celular;

	@Column(unique = true)
	@NotNull(message = "{DomainInstructor.email}")
	@Pattern(regexp = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$", message = "{DomainInstructor.emailPatterm}")
	private String correoelectronico;

	@Column(length = 16)
	@NotNull(message = "{DomainInstructor.password}")
	@Size(min = 8, max = 16, message = "{DomainInstructor.passwordSize}")
	@Pattern(regexp = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$", message = "{DomainInstructor.passwordPatterm}")
	private String contrasena;

	@Transient
	private String repContrasena;

	@Column(length = 10)
	@NotNull(message = "{DomainInstructor.experience}")
	@Size(min = 1, max = 2, message = "{DomainInstructor.experienceSize}")
	private String experienciaprofesional;

	@NotNull(message = "{DomainInstructor.urlPicture}")
	private String urlFoto;

	@NotNull(message = "{DomainInstructor.title}")
	@Size(min = 2, max = 255, message = "{DomainInstructor.titleSize}")
	@Pattern(regexp = "^([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\']+[\\s])+([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])+[\\s]?([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])?$", message = "{DomainInstructor.titlePatterm}")
	private String titulacion;

	// bi-directional many-to-one association to Clasepuedenimpartirinstructor
	@OneToMany(mappedBy = "instructor")
	private List<ClasePuedenImpartirInstructor> clasepuedenimpartirinstructors;

	// bi-directional many-to-one association to Rutina
	@OneToMany(mappedBy = "instructor")
	private List<Rutina> rutinas;

	public ClasePuedenImpartirInstructor addClasepuedenimpartirinstructor(
			ClasePuedenImpartirInstructor clasepuedenimpartirinstructor) {
		getClasepuedenimpartirinstructors().add(clasepuedenimpartirinstructor);
		clasepuedenimpartirinstructor.setInstructor(this);

		return clasepuedenimpartirinstructor;
	}

	public ClasePuedenImpartirInstructor removeClasepuedenimpartirinstructor(
			ClasePuedenImpartirInstructor clasepuedenimpartirinstructor) {
		getClasepuedenimpartirinstructors().remove(clasepuedenimpartirinstructor);
		clasepuedenimpartirinstructor.setInstructor(null);

		return clasepuedenimpartirinstructor;
	}

	public Rutina addRutina(Rutina rutina) {
		getRutinas().add(rutina);
		rutina.setInstructor(this);

		return rutina;
	}

	public Rutina removeRutina(Rutina rutina) {
		getRutinas().remove(rutina);
		rutina.setInstructor(null);

		return rutina;
	}

}