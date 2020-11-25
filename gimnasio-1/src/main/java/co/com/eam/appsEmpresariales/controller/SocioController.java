package co.com.eam.appsEmpresariales.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.utils.ObjectUtils;
import co.com.eam.appsEmpresariales.CloudinaryConfig;
import co.com.eam.appsEmpresariales.domain.Administrador;
import co.com.eam.appsEmpresariales.domain.Instructor;
import co.com.eam.appsEmpresariales.domain.Socio;
import co.com.eam.appsEmpresariales.repository.SocioRepository;
import co.com.eam.appsEmpresariales.service.MessageByLocaleService;
import jdk.nashorn.internal.runtime.FindProperty;

@Controller
public class SocioController {

	
	@Autowired
    MessageByLocaleService messageByLocaleService;
	
	@Autowired
	private SocioRepository socioRepo;

	@Autowired
	private CloudinaryConfig cloudc;

	@Autowired // inyeccion de depedencias. Meter un dao
	public SocioController(SocioRepository socioRepo) {
		this.socioRepo = socioRepo;
	}

	@GetMapping("/ListarSocio")
	public String ListarSocio(Model model) {
		model.addAttribute("Socios", socioRepo.findAll());
		return "ListaSocio";
	}

	

	@GetMapping("/ListarClaseInscrito")
	public String ListarClaseInscrito() {

		return "ListarClaseSocio";
	}

	@GetMapping("/InscripcionClase")
	public String IncripcionClase() {

		return "InscripcionClaseSocio";
	}

	@GetMapping("/MenuSocio")
	public String MenuSocio() {
		return "Plantilla/MenuSocio";
	}

	@GetMapping("/IrAgregarSocio")
	public String IrAgregarSocio(Socio socio) {
		return "AgregarSocio";
	}

	@PostMapping("/AgregarSocio")
	public String AgregarSocio(@Valid Socio socio, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file, RedirectAttributes attribute) {
		if (result.hasErrors()) {
			return "AgregarSocio";
		}
		
		if (!socio.getContrasena().equals(socio.getRepContrasena())) {
			new IllegalArgumentException( messageByLocaleService.getMessage("SocioController.passwordWarning") );
			model.addAttribute("warning", messageByLocaleService.getMessage("SocioController.passwordWarning"));
			return "AgregarSocio";
		}
		
		Administrador administradorPorCorreo=socioRepo.BuscarAdministradorPorCorreo(socio.getCorreoElectronico());
		
		Instructor instrcutorPorCorreo=socioRepo.BuscarInstrcutorPorCorreo(socio.getCorreoElectronico());
		
		if(administradorPorCorreo!=null || instrcutorPorCorreo!=null) {
			new IllegalArgumentException( messageByLocaleService.getMessage("SocioController.emailWarning") );
			model.addAttribute("warning", messageByLocaleService.getMessage("SocioController.emailWarning"));
			return "AgregarSocio";
		}
		
		
		Socio socioPorCedula = (Socio) socioRepo.BuscarSocioPorCedula(socio.getCedula());

		if (socioPorCedula != null) {
			new IllegalArgumentException(messageByLocaleService.getMessage("SocioController.duplicateCeduladWarning") );
			model.addAttribute("warning",messageByLocaleService.getMessage("SocioController.duplicateCeduladWarning"));
			return "AgregarSocio";
		}
		Socio socioPorCorreo = (Socio) socioRepo.BuscarSocioPorCorreo(socio.getCorreoElectronico());
		if (socioPorCorreo != null) {
			new IllegalArgumentException(messageByLocaleService.getMessage("SocioController.duplicateEmailWarning"));
			model.addAttribute("warning",messageByLocaleService.getMessage("SocioController.duplicateEmailWarning"));
			return "AgregarSocio";
		}
		Socio socioPorCuentaBancaria = (Socio) socioRepo.BuscarSocioPorCuentaBancaria(socio.getDatosbancarios());
		if (socioPorCuentaBancaria != null) {
			new IllegalArgumentException(messageByLocaleService.getMessage("SocioController.duplicateBankAccountWarning") );
			model.addAttribute("warning", messageByLocaleService.getMessage("SocioController.duplicateBankAccountWarning") );
			return "AgregarSocio";
		}
		try {
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
			System.out.println(uploadResult.get("url").toString());
			socio.setUrlFoto(uploadResult.get("url").toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			new IllegalArgumentException(messageByLocaleService.getMessage("SocioController.pictureWarning") );
			model.addAttribute("warning",messageByLocaleService.getMessage("SocioController.pictureWarning") );
			return "AgregarSocio";
		}
		
		socioRepo.save(socio);
		model.addAttribute("Socios", socioRepo.findAll());
		attribute.addFlashAttribute("success",messageByLocaleService.getMessage("SocioController.savedPartnerSucces"));
		return "redirect:/ListarSocio";
	}

	@GetMapping("/IrEditarSocio/{id}")
	public String IrEditarSocio(@PathVariable("id") Integer id, Model model) {
		Socio socio = socioRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido: " + id));
		model.addAttribute("socio", socio);
		return "EditarSocio";
	}

	@PostMapping("/EditarSocio/{id}")
	public String EditarSocio(@PathVariable("id") Integer id, @Valid Socio socio, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl,
			RedirectAttributes attribute) {
		if (result.hasErrors()) {
			socio.setId(id);
			return "EditarSocio";
		}
		Socio socioPorId = socioRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido: " + id));
		if (!socio.getContrasena().equals(socio.getRepContrasena())) {
			new IllegalArgumentException(  messageByLocaleService.getMessage("SocioController.passwordWarning") );
			model.addAttribute("warning", messageByLocaleService.getMessage("SocioController.passwordWarning"));
			socio.setId(id);
			return "EditarSocio";
		}


		if (!socioPorId.getCedula().equals(socio.getCedula())) {
			new IllegalArgumentException( messageByLocaleService.getMessage("SocioController.tryChangeCedulaWarning") );
			model.addAttribute("warning", messageByLocaleService.getMessage("SocioController.tryChangeCedulaWarning"));
			socio.setId(id);
			return "EditarSocio";
		}

		if (!socioPorId.getCorreoElectronico().equals( socio.getCorreoElectronico())) {
			new IllegalArgumentException(messageByLocaleService.getMessage("SocioController.tryChangeEmailWarning") );
			model.addAttribute("warning", messageByLocaleService.getMessage("SocioController.tryChangeEmailWarning"));
			socio.setId(id);
			return "EditarSocio";
		}

		if (socioPorId.getDatosbancarios().equals(socio.getDatosbancarios())) {

		} else {
			Socio socioPorCuentaBancaria = (Socio) socioRepo.BuscarSocioPorCuentaBancaria(socio.getDatosbancarios());
			if (socioPorCuentaBancaria != null) {
				new IllegalArgumentException(messageByLocaleService.getMessage("SocioController.duplicateBankAccountWarning") );
				model.addAttribute("warning", messageByLocaleService.getMessage("SocioController.duplicateBankAccountWarning"));
				socio.setId(id);
				return "EditarSocio";
			}

		}

		if (cambioUrl) {
			try {
				Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
				System.out.println(uploadResult.get("url").toString());
				socio.setUrlFoto(uploadResult.get("url").toString());

			} catch (Exception e) {
				System.out.println(e.getMessage());
				new IllegalArgumentException(messageByLocaleService.getMessage("SocioController.pictureWarning"));
				model.addAttribute("warning",messageByLocaleService.getMessage("SocioController.pictureWarning"));
				socio.setId(id);
				return "EditarSocio";
			}
		}
		socioRepo.save(socio);
		model.addAttribute("Socios", socioRepo.findAll());
		attribute.addFlashAttribute("info",messageByLocaleService.getMessage("SocioController.modifyPartnerSucces"));
		return "redirect:/ListarSocio";
	}

	@GetMapping("/EliminarSocio/{id}")
	public String EliminarSocio(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		Socio socio = socioRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id Invalido:" + id));
		socioRepo.delete(socio);
		model.addAttribute("Socios", socioRepo.findAll());
		attribute.addFlashAttribute("warning",messageByLocaleService.getMessage("SocioController.removePartnerSucces") );
		return "redirect:/ListarSocio";
	}

	@PostMapping("**/socidoFiltradoCorreo")
	public String SocioFiltrado(@RequestParam("correoFiltrar") String correoFiltrar, Model model) {
		model.addAttribute("Socios", socioRepo.findByCorreoElectronico(correoFiltrar));
		return "ListaSocio";
	}

}
