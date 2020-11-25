package co.com.eam.appsEmpresariales.repository;





import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import co.com.eam.appsEmpresariales.domain.Administrador;





@Repository
public interface AdministradorRepository extends CrudRepository<Administrador, Integer> {
	
	//Iterable<Administrador> findByNombre(String nombre);
	
	@Query("select x from Administrador x where x.Cedula= :cedula")
	public Administrador BuscarAdministradorPorCedula(@Param("cedula") String cedula);
	
	@Query("select x from Administrador x where x.CorreoElectronico= :correo")
	public Administrador BuscarAdministradorPorCorreo(@Param("correo") String correo);

	//hola
}
