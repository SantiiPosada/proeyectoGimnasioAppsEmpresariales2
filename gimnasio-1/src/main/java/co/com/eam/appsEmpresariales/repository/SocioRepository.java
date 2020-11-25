package co.com.eam.appsEmpresariales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.Administrador;
import co.com.eam.appsEmpresariales.domain.Clase;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Rutina;
import co.com.eam.appsEmpresariales.domain.Socio;
import co.com.eam.appsEmpresariales.domain.SocioClase;


@Repository
public interface SocioRepository extends CrudRepository<Socio, Integer> {
	@Query("select x from Socio x ")
	public Iterable<Socio> ListarSocios();

	@Query("select x from Socio x where x.Cedula= :cedula")
	public Socio BuscarSocioPorCedula(@Param("cedula") String cedula);
	
	@Query("Select x from Socio x where x.correoElectronico like %?1% ")
	public  List<Socio> findByCorreoElectronico(String correoElectronico);

	@Query("select x from Socio x where x.datosbancarios= :datos")
	public Socio BuscarSocioPorCuentaBancaria(@Param("datos") String datos);
	
	@Query("select scls from SocioClase scls where scls.socio.Cedula= :cedula")
	public Iterable<SocioClase> ListarClasePorSocio(@Param("cedula") String cedula);
	
	@Query("SELECT cl FROM Clase cl")
	public Iterable<Clase> ListarClaseDisponible();

	@Query("select scls FROM SocioClase scls where scls.socio.Cedula= :cedula")
	public Iterable<SocioClase> BuscaClasePorSocio(@Param("cedula") String cedula);

	@Query("SELECT rt FROM Rutina rt where rt.socio.Cedula= :cedula")
	public Rutina BuscaRutinaPorSocio(@Param("cedula") String cedula);
	
	@Query("select x FROM Socio x where x.correoElectronico= :correo")
	public Socio BuscarSocioPorCorreo(@Param("correo") String correo);
	
	@Query("select x FROM Administrador x where x.CorreoElectronico= :correo")
	public Administrador BuscarAdministradorPorCorreo(@Param("correo") String correo);
	
	@Query("select x FROM Instructor x where x.correoelectronico= :correo")
	public Instructor BuscarInstrcutorPorCorreo(@Param("correo") String correo);
}
