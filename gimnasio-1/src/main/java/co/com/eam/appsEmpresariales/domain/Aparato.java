package co.com.eam.appsEmpresariales.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * The persistent class for the aparato database table.
 * 
 */
@Data
@Entity
//@NamedQuery(name = "Aparato.findAll", query = "SELECT a FROM Aparato a")
public class Aparato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;
	@NotNull(message = "{DomainAparato.urlPicture}")
	private String urlFoto;

	@NotNull(message = "{DomainAparato.description}")
	@Size(min = 2, max = 255, message = "{DomainAparato.descriptionSize}")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$", message = "{DomainAparato.descriptionPatterm}")
	private String descripcion;

	// bi-directional many-to-one association to Sala
	@ManyToOne
	@NotNull(message = "{DomainAparato.room}")
	@JoinColumn(name = "IdSala")
	private Sala sala;
//comit
}