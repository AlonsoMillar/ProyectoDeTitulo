import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';
import { UsuarioService } from '../../service/usuario.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.page.html',
  styleUrls: ['./home-admin.page.scss'],
  standalone: false
})
export class HomeAdminPage implements OnInit {
  userAlias: string = 'Cargando...'; 
  userAvatar: string | null = null;

  constructor(
    private router: Router,
    private usuarioService: UsuarioService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.cargarDatosAdmin();
  }

  async cargarDatosAdmin() {
    const auth = getAuth();
    const user = auth.currentUser;
    
    if (user) {
      this.userAvatar = user.photoURL;

      try {
        const perfil: any = await this.usuarioService.consultarUsuario(user.uid);

        if (perfil && perfil.alias) {
          this.userAlias = perfil.alias;
        } else {
          this.userAlias = 'Administrador';
        }

      } catch (err: any) {
        console.error('Error al cargar perfil de admin', err);
        this.userAlias = 'Administrador';
      }
    }
  }

  // navegar a código
  goToStoreCode() {
    console.log('CLICK DETECTADO');
    this.router.navigate(['/store-code'], { replaceUrl: true });
  }

  // logout igual que en home normal
  async logout() {
    await this.authService.logout();
    this.router.navigate(['/login'], { replaceUrl: true });
  }

  // 🔥 NUEVO: Función para la barra de navegación inferior
  irA(ruta: string) {
    this.router.navigate([ruta]);
  }
}