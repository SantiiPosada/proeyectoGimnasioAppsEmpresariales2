package co.com.eam.appsEmpresariales.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.Aparato;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Sala;
import co.com.eam.appsEmpresariales.domain.TipoSala;

@Repository
public interface SalaRepository extends CrudRepository<Sala, Integer> {
	@Query("select sla from Sala sla")
	public Iterable<Sala> ListarSalas();
	
	@Query("select x from TipoSala x Order By x.nombre")
	public Iterable<TipoSala> ListarTipoSalas();
	
	
	
	@Query("select x from TipoSala x where x.id= :id")
	public TipoSala BuscarTipoSalaPorId(@Param("id") Integer id);
	

	@Query("select sla from Sala sla where sla.tiposala.nombre= :nombre")
	public Sala BuscarSalaPorTipoSala(@Param("nombre") String nombre);

	@Query("select sla from Sala sla where sla.ubicacion= :ubicacion")
	public Sala BuscarSalaPorUbicacion(@Param("ubicacion") String ubicacion);

	@Query("select sla from Sala sla where sla.metrosCuadrados= :metrosCuadrados")
	public Sala BuscarSalaPorMetrosCuadrados(@Param("metrosCuadrados") String metrosCuadrados);

	@Query("select x from Aparato x where x.sala.id= :id")
	public Aparato BuscarAparatosPorSala(@Param("id") Integer id);
	
	

}
