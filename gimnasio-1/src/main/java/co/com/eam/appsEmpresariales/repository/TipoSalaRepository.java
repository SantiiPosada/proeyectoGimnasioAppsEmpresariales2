package co.com.eam.appsEmpresariales.repository;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.Sala;
import co.com.eam.appsEmpresariales.domain.TipoSala;

@Repository
public interface TipoSalaRepository extends CrudRepository<TipoSala, Integer> {

	public TipoSala findByNombre(String nombre);

	@Query("select s from TipoSala x join Sala s Where x.nombre= :nombre")
	Iterable<Sala> BuscarSalaNombreTiposada(@Param("nombre") String nombre);
	
	@Query("select x from TipoSala x where x.id= :id")
	public TipoSala buscarTipoSalaId(@Param("id") Integer id);
	
}
