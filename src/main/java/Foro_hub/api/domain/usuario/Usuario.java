package Foro_hub.api.domain.usuario;


import Foro_hub.api.domain.usuario.dto.ActualizarUsuario;
import Foro_hub.api.domain.usuario.dto.CrearUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
@Table(name = "usuarios")
@Entity(name = "usuarios")
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String contraseña;

    @Enumerated(EnumType.STRING)
    private Perfiles perfiles;
    private Boolean habilitado;

    public Usuario() {

    }

    public Usuario(CrearUsuario crearUsuario, String contraseña){
        this.nombre = crearUsuario.nombre();
        this.email = crearUsuario.email();
        this.contraseña = contraseña;
        this.perfiles = Perfiles.USUARIO;
        this.habilitado = true;
    }

    public void actualizarUsuarioConContraseña(ActualizarUsuario actualizarUsuario, String contraseña) {
        if (actualizarUsuario.nombre() != null) {
            this.nombre = actualizarUsuario.nombre();
        }
        if (actualizarUsuario.email() != null) {
            this.email = actualizarUsuario.email();
        }
        if (actualizarUsuario.contraseña() != null){
            this.contraseña = actualizarUsuario.contraseña();
        }
        if (actualizarUsuario.perfiles()!= null) {
            this.perfiles = actualizarUsuario.perfiles();
        }
        if (actualizarUsuario.borrarUsuario()!= null){
            this.habilitado = actualizarUsuario.borrarUsuario();
        }
    }

    public void actualizarUsuario(ActualizarUsuario actualizarUsuario){
        if (actualizarUsuario.nombre() != null) {
            this.nombre = actualizarUsuario.nombre();
        }
        if (actualizarUsuario.email() != null) {
            this.email = actualizarUsuario.email();
        }
        if (actualizarUsuario.contraseña() != null){
            this.contraseña = actualizarUsuario.contraseña();
        }
        if (actualizarUsuario.perfiles()!= null) {
            this.perfiles = actualizarUsuario.perfiles();
        }
        if (actualizarUsuario.borrarUsuario()!= null){
            this.habilitado = actualizarUsuario.borrarUsuario();
        }
    }

    public void eliminarUsuario(){
        this.habilitado = false;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.contraseña;
    }

    @Override
    public String getUsername() {
        return this.nombre;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return habilitado;
    }

    // getter


    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public Perfiles getPerfiles() {
        return perfiles;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }
}
