package co.com.eam.appsEmpresariales.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.Clase;
import co.com.eam.appsEmpresariales.domain.ClasePuedenImpartirInstructor;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Sala;
import co.com.eam.appsEmpresariales.domain.Socio;

@Repository
public interface ClaseRepository extends CrudRepository<Clase, Integer> {

	@Query("select x from Socio x where x.id= :id")
	public Socio BuscarSocioPorId(@Param("id") Integer id);

	@Query("select x from Instructor x where x.id= :id")
	public Instructor BuscarInstructorPorId(@Param("id") Integer id);

	@Query("select x from Sala x Order by x.tiposala.nombre")
	public Iterable<Sala> ListarSala();

	@Query("select x from ClasePuedenImpartirInstructor x Order by x.claseImpartir.nombre")
	public Iterable<ClasePuedenImpartirInstructor> ListarClaseHabilitadas();

	@Query("select x from Instructor x join ClasePuedenImpartirInstructor cp on cp.instructor.id=x.id where cp.id= :id")
	public Instructor BuscarInstructorPorCodigoClasePuedeImpartir(@Param("id") Integer id);

	Iterable<Clase> findByDiaHora(Date diaHora);

	Iterable<Clase> findByDiaHoraBetween(Date diaHora1, Date diaHora2);

	@Query("select x from Clase x where x.sala.tiposala.nombre= :nombre")
	Iterable<Clase> LisarClasesPorTipoSala(@Param("nombre") String nombre);

	@Query("select x from Clase x where x.clasepuedenimpartirinstructor.instructor.Cedula= :cedula")
	Iterable<Clase> ListarCLasesPorCedulaInstructor(@Param("cedula") String cedula);

	@Query("select x from Clase x where x.clasepuedenimpartirinstructor.instructor.correoelectronico= :correo")
	Iterable<Clase> ListarCLasesPorCorreoInstructor(@Param("correo") String correo);
}
