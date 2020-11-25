package co.com.eam.appsEmpresariales.domain;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class loginForm {

@NotNull(message = "Ingrese el correo")

@NotNull(message = "Debe ingresar el correo electronico")
@Pattern(regexp = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$", message = "Debe ingresar una direccion de correo electronico valido, No puede tener Mayusculas")
private String correo;


@NotNull(message = "Debe ingresar la contraseña")
@Size(min = 8, max = 16, message = "La contraseña debe tener entre 8 y 16 caracteres")
private String contrasena;



}
