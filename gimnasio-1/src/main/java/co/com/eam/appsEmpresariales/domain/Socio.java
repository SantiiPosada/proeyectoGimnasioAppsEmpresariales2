package co.com.eam.appsEmpresariales.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

import java.util.List;

/**
 * The persistent class for the socio database table.
 * 
 */
@Data
@Entity



//@NamedQuery(name = "Socio.findAll", query = "SELECT s FROM Socio s")
public class Socio implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 10, unique = true)
	@NotNull(message = "{DomainSocio.cedula}")
	@Size(min = 7, max = 10, message = "{DomainSocio.cedulaSize}")
	@Pattern(regexp = "^([0-9])*$", message = "{DomainSocio.cedulaPatterm}")
	private String Cedula;

	@NotNull(message = "{DomainSocio.name}")
	@Size(min = 2, max = 255, message = "{DomainSocio.nameSize}")
	@Pattern(regexp = "^([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\']+[\\s])+([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])+[\\s]?([A-Za-zÁÉÍÓÚñáéíóúÑ]{0}?[A-Za-zÁÉÍÓÚñáéíóúÑ\\'])?$", message =  "{DomainSocio.namePatterm}")
	private String nombre;

	@Column(length = 10)
	@NotNull(message = "{DomainSocio.phone}")
	@Size(min = 10, max = 10, message = "{DomainSocio.phoneSize}")
	@Pattern(regexp = "^([0-9])*$", message = "{DomainSocio.phonePatterm}")
	private String celular;

	@Column(unique = true)
	@NotNull(message = "{DomainSocio.email}")
	@Pattern(regexp = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$", message = "{DomainSocio.emailPatterm}")
	private String correoElectronico;

	@Column(length = 16)
	@NotNull(message = "{DomainSocio.password}")
	@Size(min = 8, max = 16, message = "{DomainSocio.passwordSize}")
	@Pattern(regexp = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$", message = "{DomainSocio.passwordPatterm}")
	private String contrasena;
	
	@Transient
	private String repContrasena;

	@Column(length = 19, unique = true)
	@NotNull(message =  "{DomainSocio.bankData}")
	@Size(min = 13, max = 19, message =  "{DomainSocio.bankDataSize}")
	@Pattern(regexp = "^(?:4\\d([\\- ])?\\d{6}\\1\\d{5}|(?:4\\d{3}|5[1-5]\\d{2}|6011)([\\- ])?\\d{4}\\2\\d{4}\\2\\d{4})$", message =  "{DomainSocio.bankDataPatterm}")
	
	private String datosbancarios;

	@NotNull(message = "{DomainSocio.address}")
	@Pattern(regexp = "[a-zA-Z1-9À-ÖØ-öø-ÿ]+\\.?(( |\\-)[a-zA-Z1-9À-ÖØ-öø-ÿ]+\\.?)* (((#|[nN][oO]\\.?) ?)?\\d{1,4}(( ?[a-zA-Z0-9\\-]+)+)?)", message = "{DomainSocio.addressPatterm}")
	@Size(min = 10, max = 255, message = "{DomainSocio.addressSize}")
	private String direccion;

	@NotNull(message = "{DomainSocio.profession}")
	@Size(min = 2, max = 255, message = "{DomainSocio.professionSize}")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$", message = "{DomainSocio.professionPatterm}")
	private String profesion;
	
	@NotNull(message = "{DomainSocio.urlPicture}")
	private String urlFoto;

	// bi-directional many-to-one association to Rutina
	@OneToMany(mappedBy = "socio")
	private List<Rutina> rutinas;

	// bi-directional many-to-one association to Socioclase
	@OneToMany(mappedBy = "socio")
	private List<SocioClase> socioclases;

	public Rutina addRutina(Rutina rutina) {
		getRutinas().add(rutina);
		rutina.setSocio(this);

		return rutina;
	}

	public Rutina removeRutina(Rutina rutina) {
		getRutinas().remove(rutina);
		rutina.setSocio(null);

		return rutina;
	}

	public SocioClase addSocioclas(SocioClase socioclas) {
		getSocioclases().add(socioclas);
		socioclas.setSocio(this);

		return socioclas;
	}

	public SocioClase removeSocioclas(SocioClase socioclas) {
		getSocioclases().remove(socioclas);
		socioclas.setSocio(null);

		return socioclas;
	}

}