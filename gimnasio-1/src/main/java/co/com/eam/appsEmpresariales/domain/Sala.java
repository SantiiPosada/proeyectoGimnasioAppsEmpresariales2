package co.com.eam.appsEmpresariales.domain;



import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

import java.util.List;



/**
 * The persistent class for the sala database table.
 * 
 */
@Data
@Entity
//@NamedQuery(name="Sala.findAll", query="SELECT s FROM Sala s")
public class Sala implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message = "{DomainSala.urlPicture}")
	private String UrlFoto;
	
	
	@NotNull(message = "{DomainSala.squareMeter}")
	@Size(min = 1, max = 255, message = "{DomainSala.squareMeterSize}")
	@Pattern(regexp = "^[0-9]+([,][0-9]+)?$", message = "{DomainSala.squareMeterPatterm}")
	private String metrosCuadrados;
	
	@Column(unique = true)
	@NotNull(message = "{DomainSala.location}")
	@Size(min = 10, max = 255, message = "{DomainSala.locationSize}")
	@Pattern(regexp = "[a-zA-Z1-9À-ÖØ-öø-ÿ]+\\.?(( |\\-)[a-zA-Z1-9À-ÖØ-öø-ÿ]+\\.?)* (((#|[nN][oO]\\.?) ?)?\\d{1,4}(( ?[a-zA-Z0-9\\-]+)+)?)", message = "{DomainSala.locationPatterm}")
	private String ubicacion;

	
	//bi-directional many-to-one association to Aparato
	@OneToMany(mappedBy="sala")
	private List<Aparato> aparatos;
	

	//bi-directional many-to-one association to Clase
	@OneToMany(mappedBy="sala")
	private List<Clase> clases;

	//bi-directional many-to-one association to Tiposala
	@ManyToOne
	@JoinColumn(name="IdTipoSala")
	@NotNull(message = "Seleccione el tipo sala")
	
	private TipoSala tiposala;

	
	public Aparato addAparato(Aparato aparato) {
		getAparatos().add(aparato);
		aparato.setSala(this);

		return aparato;
	}

	public Aparato removeAparato(Aparato aparato) {
		getAparatos().remove(aparato);
		aparato.setSala(null);

		return aparato;
	}


	public Clase addClas(Clase clas) {
		getClases().add(clas);
		clas.setSala(this);

		return clas;
	}

	public Clase removeClas(Clase clas) {
		getClases().remove(clas);
		clas.setSala(null);

		return clas;
	}



}