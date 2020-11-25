package co.com.eam.appsEmpresariales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.Administrador;
import co.com.eam.appsEmpresariales.domain.ClasePuedenImpartirInstructor;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Rutina;

@Repository
public interface InstructorRepository extends CrudRepository<Instructor, Integer> {

	@Query("select intr from Instructor intr")
	public Iterable<Instructor> ListaInstructores();

	@Query("select intr from Instructor intr where intr.Cedula= :cedula")
	public Instructor BuscarInstructorPorCedula(@Param("cedula") String cedula);

	// se usa en el controlador Login
	@Query("select intr from Instructor intr where intr.correoelectronico= :CorreoElectronico")
	public Instructor BuscarInstructorPorCorreo(@Param("CorreoElectronico") String CorreoElectronico);

	@Query("select intr from Instructor intr where intr.nombre= :Nombre")
	public Instructor BuscarInstructorPorNombre(@Param("Nombre") String Nombre);

	@Query("Select x from Instructor x where x.correoelectronico like %?1% ")
	public List<Instructor> findByCorreoElectronico(String correoElectronico);

	@Query("select intr from Instructor intr where intr.experienciaprofesional= :experienciaprofesional")
	public Instructor BuscarInstructorPorExperienciaProfesional(
			@Param("experienciaprofesional") String experienciaprofesional);

	@Query("select intr from Instructor intr where intr.titulacion= :titulacion")
	public Instructor BuscarInstructorPorTitulacion(@Param("titulacion") String titulacion);

	@Query("select intr from Instructor intr where intr.celular= :Celular")
	public Instructor BuscarInstructorPorCelular(@Param("Celular") String Celular);

	@Query("select cls from ClasePuedenImpartirInstructor cls where cls.instructor.Cedula= :cedula")
	public Iterable<ClasePuedenImpartirInstructor> ListarClaseImpartirPorInstructor(@Param("cedula") String cedula);

	@Query("SELECT rt FROM Rutina rt where rt.instructor.Cedula= :cedula")
	public Rutina BuscarRutinaPorInstructor(@Param("cedula") String cedula);

	// Se usa en el controlador Login
	@Query("select intr from Instructor intr where intr.correoelectronico= :correo and intr.contrasena= :contrasena ")
	public Instructor loginInstructor(@Param("correo") String correo, @Param("contrasena") String contrasena);

	@Query("select x FROM Administrador x where x.CorreoElectronico= :correo")
	public Administrador BuscarAdministradorPorCorreo(@Param("correo") String correo);

	@Query("select x FROM Instructor x where x.correoelectronico= :correo")
	public Instructor BuscarInstrcutorPorCorreo(@Param("correo") String correo);
}
