package co.com.eam.appsEmpresariales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.com.eam.appsEmpresariales.domain.Aparato;
import co.com.eam.appsEmpresariales.domain.Sala;




@Repository
public interface AparatoRepository extends CrudRepository<Aparato, Integer> {
	@Query("select x from Aparato x")
	public Iterable<Aparato> ListarAparatos();
	
	@Query("select x.ubicacion,a.descripcion from Sala x inner join Aparato a on x.id=a.sala.id")
	public Iterable<Object> ListarAparatosPorSala();
	
	@Query("Select x from Aparato x where x.sala.tiposala.nombre like %?1% ")
	public  List<Aparato> BuscarAparatoPorTipoSala(String nombre);
	
	@Query("select x from Sala x Order by x.tiposala.nombre")
	public Iterable<Sala> ListarSala();
}
