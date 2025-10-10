package logica.Clases;


public abstract class Usuario {
    private String nickname;
    private String nombre;
    private String email;
    private String contrasena;
    private String imagen;

    public Usuario(String nickname, String nombre, String email, String contrasena, String imagen) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.imagen = imagen;
    }
    public Usuario(String nickname, String nombre, String email, String contrasena) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.imagen = null;
    }
    
    public String getNickname() {
        return this.nickname;
    }

    /*public void setNickname(String nickname) {
        this.nickname = nickname;
    }*/

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    // Si necesitas setEmail, descomenta y usa:
    // public void setEmail(String email) {
    //     this.email = email;
    // }
    
    public boolean esAsistente(Usuario u) {
    	if (u instanceof Asistente) {
    		return true;
    	}
    	else return false;
    }
    
   /* public boolean esOrganizador(Usuario u) {
    	if (u instanceof Organizador) {
    		return true;
    	}
    	else return false;
    }*/

    public Institucion getInstitucion() {
        return null;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}