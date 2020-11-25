package co.com.eam.appsEmpresariales.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

import java.util.List;

/**
 * The persistent class for the claseimpartir database table.
 * 
 */
@Data
@Entity
//@NamedQuery(name="Claseimpartir.findAll", query="SELECT c FROM Claseimpartir c")
public class ClaseImpartir implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	@NotNull(message = "{DomainClaseImpartir.name}")
	@Size(min = 2, max = 255, message = "{DomainClaseImpartir.nameSize}")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$", message = "{DomainClaseImpartir.namePatterm}")
	private String nombre;

	// bi-directional many-to-one association to Clasepuedenimpartirinstructor
	@OneToMany(mappedBy = "claseImpartir")
	private List<ClasePuedenImpartirInstructor> clasepuedenimpartirinstructors;

	public ClasePuedenImpartirInstructor addClasepuedenimpartirinstructor(
			ClasePuedenImpartirInstructor clasepuedenimpartirinstructor) {
		getClasepuedenimpartirinstructors().add(clasepuedenimpartirinstructor);
		clasepuedenimpartirinstructor.setClaseImpartir(this);

		return clasepuedenimpartirinstructor;
	}

	public ClasePuedenImpartirInstructor removeClasepuedenimpartirinstructor(
			ClasePuedenImpartirInstructor clasepuedenimpartirinstructor) {
		getClasepuedenimpartirinstructors().remove(clasepuedenimpartirinstructor);
		clasepuedenimpartirinstructor.setClaseImpartir(null);

		return clasepuedenimpartirinstructor;
	}

}