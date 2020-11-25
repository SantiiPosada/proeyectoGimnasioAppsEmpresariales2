package co.com.eam.appsEmpresariales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.ClaseImpartir;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Rutina;
import co.com.eam.appsEmpresariales.domain.Socio;

@Repository
public interface RutinaRepository extends CrudRepository<Rutina, Integer> {

	@Query("select rt from Rutina rt order by rt.socio.id")
	public Iterable<Rutina> ListarRutinasOrdenadaPorSocio();

	@Query("SELECT rt FROM Rutina rt where rt.socio.Cedula= :cedula")
	public Rutina BuscaRutinaPorSocio(@Param("cedula") String cedula);

	@Query("SELECT rt FROM Rutina rt where rt.instructor.Cedula= :cedula")
	public Rutina BuscarRutinaPorInstructor(@Param("cedula") String cedula);
	
	
	@Query("select x from Socio x Order By x.id")
	public Iterable<Socio> ListarSociosOrdenados();
	
	@Query("select x from Instructor x Order By x.id")
	public Iterable<Instructor> ListarInstructoresOrdenados();
	
	@Query("Select x from Rutina x where x.socio.correoElectronico like %?1% ")
	public  List<Rutina> findByCorreoElectronicoSocio(String correoElectronico);
	

	@Query("Select x from Rutina x where x.socio.correoElectronico like %?1% and x.socio.contrasena=?2")
	public  List<Rutina> findByCorreoElectronicoSocioContra(String correoElectronico,String contra);
	
	@Query("select x FROM Socio x where x.correoElectronico like %?1%")
	public List<Socio> BuscarSocioPorCorreo(@Param("correo") String correo);
	
	
	
}
