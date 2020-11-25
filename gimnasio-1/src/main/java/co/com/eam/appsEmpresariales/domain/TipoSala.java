package co.com.eam.appsEmpresariales.domain;



import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

import java.util.List;


/**
 * The persistent class for the tiposala database table.
 * 
 */
@Data
@Entity
//@NamedQuery(name="Tiposala.findAll", query="SELECT t FROM Tiposala t")
public class TipoSala implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	
	@NotNull(message = "{DomainTipoSala.name}")
	@Size(min=2, max=255, message = "{DomainTipoSala.nameSize}")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$",message = "{DomainTipoSala.namePattern}")
	private String nombre;

	//bi-directional many-to-one association to Sala
	@OneToMany(mappedBy="tiposala")
	private List<Sala> salas;
	

	

	public Sala addSala(Sala sala) {
		getSalas().add(sala);
		sala.setTiposala(this);

		return sala;
	}

	public Sala removeSala(Sala sala) {
		getSalas().remove(sala);
		sala.setTiposala(null);

		return sala;
	}

}