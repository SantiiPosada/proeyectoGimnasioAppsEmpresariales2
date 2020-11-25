package co.com.eam.appsEmpresariales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.Clase;
import co.com.eam.appsEmpresariales.domain.ClaseImpartir;
import co.com.eam.appsEmpresariales.domain.ClasePuedenImpartirInstructor;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Socio;
import co.com.eam.appsEmpresariales.domain.TipoSala;

@Repository
public interface ClasePuedenImpartirInstructorRepository extends CrudRepository<ClasePuedenImpartirInstructor, Integer> {

	@Query("select x from Instructor x where x.id= :id")
	public Instructor BuscarInstructorPorId(@Param("id") Integer id);
	
	@Query("select x from ClaseImpartir x where x.id= :id")
	public ClaseImpartir BuscarClaseImpartirPorId(@Param("id") Integer id);
	
	@Query("select x from Clase x where x.id= :id")
	public Clase BuscarClasePorId(@Param("id") Integer id);
	
	@Query("select x from Instructor x Order By x.id")
	public Iterable<Instructor> ListarInstructores();
	
	@Query("select x from ClaseImpartir x Order By x.id")
	public Iterable<ClaseImpartir> ListarClasesImpartir();
	
	@Query("Select cpi from ClasePuedenImpartirInstructor cpi Order By cpi.instructor.id")
	public Iterable<ClasePuedenImpartirInstructor> ListarClasePuedenImpartirInstructorOrdenadoInstructor();
	
	@Query("Select cpi from ClasePuedenImpartirInstructor cpi where cpi.instructor.Cedula= :cedula")
	public Iterable<ClasePuedenImpartirInstructor> ListarClasePuedenImpartirInstructorPorCedulaInstructor(@Param("cedula") String cedula);
	
	@Query("Select cpi from ClasePuedenImpartirInstructor cpi where cpi.claseImpartir.nombre= :nombre")
	public Iterable<ClasePuedenImpartirInstructor> ListarClasePuedenImpartirInstructorPorNombreClaseImparir(@Param("nombre") String cedula);
	
	@Query("Select x from Instructor x where x.correoelectronico like %?1% ")
	public  List<Instructor> BuscarInstructorPorCorreoElectronico(String correoElectronico);
	
	@Query("Select cpi from ClasePuedenImpartirInstructor cpi where cpi.claseImpartir.id= :idClaseImpartir and cpi.instructor.id= :idInstrctor")
	public ClasePuedenImpartirInstructor validarNoRepetido(@Param("idClaseImpartir") Integer idClaseImpartir,@Param("idInstrctor") Integer idInstrctor);
	
	
	
}
