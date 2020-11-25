package co.com.eam.appsEmpresariales.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.eam.appsEmpresariales.domain.Administrador;
import co.com.eam.appsEmpresariales.repository.AdministradorRepository;

@Controller
public class AdministradorController {

	private AdministradorRepository admiRepo;

	@Autowired // inyeccion de depedencias. Meter un dao

	public AdministradorController(AdministradorRepository admiRepo) {
		this.admiRepo = admiRepo;
	}

	@GetMapping("/MenuAdministrador")
	public String MenuAdministrador() {
		return "Plantilla/MenuAdministrador";
	}

	@GetMapping("/ListarAdministrador")
	public String ListarAdministrador(Model model) {
		model.addAttribute("administradores", admiRepo.findAll());
		return "ListaAdministrador";
	}

	@GetMapping("/IrAgregarAdministrador")
	public String IrAgregarAdministrador(Administrador administrador) {
		return "AgregarAdministrador";
	}

	@PostMapping("/EditarAdministrador/{id}")
	public String EditarAparato(@PathVariable("id") Integer id, @Valid Administrador administrador,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			administrador.setId(id);
			return "EditarAdministrador";
		}

		admiRepo.save(administrador);
		ListarAdministrador(model);
		return "ListaAdministrador";
	}

	@PostMapping("/AgregarAdministrador")
	public String AgregarAdministrador(@Valid Administrador administrador, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "AgregarAdministrador";
		}

		Administrador administradorPorCedula = (Administrador) admiRepo
				.BuscarAdministradorPorCedula(administrador.getCedula());
		if (administradorPorCedula != null) {
			new IllegalArgumentException(
					"Cedula duplicada " + administradorPorCedula.getCedula() + " Favor ingrese una valida.");
			return "AgregarAdministrador";
		}
		Administrador administradorPorCorreo = (Administrador) admiRepo
				.BuscarAdministradorPorCorreo(administrador.getCorreoElectronico());
		if (administradorPorCorreo != null) {
			new IllegalArgumentException(
					"Correo duplicado " + administradorPorCorreo.getCorreoElectronico() + " Favor ingrese uno valido.");
			return "AgregarAdministrador";
		}
		admiRepo.save(administrador);
		ListarAdministrador(model);
		return "ListaAdministrador";

	}

	@GetMapping("/IrEditarAdministrador/{id}")
	public String IrEditarAdministrador(@PathVariable("id") Integer id, Model model) {
		Administrador administrador = admiRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id Invalido: " + id));
		model.addAttribute("administrador", administrador);
		return "EditarAdministrador";
	}

	@GetMapping("/EliminarAdministrador/{id}")
	public String EliminarAdministrador(@PathVariable("id") Integer id, Model model) {
		Administrador administrador = admiRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id Invalido:" + id));
		admiRepo.delete(administrador);
		ListarAdministrador(model);
		return "ListaAdministrador";
	}

	@GetMapping("/BuscarAdministradorPorCedula")
	public String BuscarAdministradorPorCedula(@Valid Administrador administrador, Model m) {
		Administrador administradorPorCedula = (Administrador) admiRepo
				.BuscarAdministradorPorCedula(administrador.getCedula());
		m.addAttribute("AdministradorPorCedula", administradorPorCedula);

		return "AgregarAdministrador";

	}

}
