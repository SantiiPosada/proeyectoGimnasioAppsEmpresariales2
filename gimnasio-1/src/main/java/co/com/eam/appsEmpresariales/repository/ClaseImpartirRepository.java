package co.com.eam.appsEmpresariales.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.ClaseImpartir;
import co.com.eam.appsEmpresariales.domain.ClasePuedenImpartirInstructor;

@Repository
public interface ClaseImpartirRepository extends CrudRepository<ClaseImpartir, Integer> {

	@Query("select ctr from ClaseImpartir ctr")
	public Iterable<ClaseImpartir> ListarClaseImpartir();

	@Query("select ctr from ClaseImpartir ctr where ctr.nombre= :nombre")
	public ClaseImpartir buscarClaseImpartirPorNombre(@Param("nombre") String nombre);

	@Query("select x from ClasePuedenImpartirInstructor x where x.claseImpartir.id= :id")
	public ClasePuedenImpartirInstructor buscarClaseImpartirPorId(@Param("id") int id);
	
}
