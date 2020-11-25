package co.com.eam.appsEmpresariales.repository;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.Clase;

import co.com.eam.appsEmpresariales.domain.Socio;
import co.com.eam.appsEmpresariales.domain.SocioClase;


@Repository
public interface SocioClaseRepository extends CrudRepository<SocioClase, Integer> {

	@Query("select x from Socio x where x.id= :id")
	public Socio BuscarSocioPorId(@Param("id") Integer id);
	
	@Query("select x from Clase x where x.id= :id")
	public Clase BuscarClasePorId(@Param("id") Integer id);
	
	@Query("select scls FROM SocioClase scls where scls.clase.sala.tiposala.nombre= :nombre")
	public Iterable<SocioClase> ListarSocioClasesPorTipoSala(@Param("nombre") String nombre);
	
	@Query("select scls FROM SocioClase scls where scls.socio.Cedula= :cedula")
	public Iterable<SocioClase> ListarSocioClasesPorCedulaSocio(@Param("cedula") String cedula);
	
}
