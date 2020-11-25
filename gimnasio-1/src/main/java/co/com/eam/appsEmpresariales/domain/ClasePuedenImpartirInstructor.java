package co.com.eam.appsEmpresariales.domain;



import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

import java.util.List;


/**
 * The persistent class for the clasepuedenimpartirinstructor database table.
 * 
 */
@Data
@Entity
//@NamedQuery(name="Clasepuedenimpartirinstructor.findAll", query="SELECT c FROM Clasepuedenimpartirinstructor c")
public class ClasePuedenImpartirInstructor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	//bi-directional many-to-one association to Clase
	@OneToMany(mappedBy="clasepuedenimpartirinstructor")
	private List<Clase> clases;

	//bi-directional many-to-one association to Claseimpartir
	@ManyToOne
	@NotNull(message = "{DomainCpii.classImpart}")
	
	@JoinColumn(name="IdClasesImpartir")
	private ClaseImpartir claseImpartir;

	//bi-directional many-to-one association to Instructor
	@ManyToOne
	@NotNull(message = "{DomainCpii.instructor}")
	
	@JoinColumn(name="IdInstructor")
	private Instructor instructor;

	

	public Clase addClas(Clase clas) {
		getClases().add(clas);
		clas.setClasepuedenimpartirinstructor(this);

		return clas;
	}

	public Clase removeClas(Clase clas) {
		getClases().remove(clas);
		clas.setClasepuedenimpartirinstructor(null);

		return clas;
	}



}