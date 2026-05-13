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

    if (!user) {
      this.router.navigate(['/login'], {
        replaceUrl: true
      });

      return;
    }

    // avatar firebase
    this.userAvatar = user.photoURL;

    try {

      const perfil: any =
        await this.usuarioService.consultarUsuario(user.uid);

      // alias admin
      if (perfil?.alias) {

        this.userAlias = perfil.alias;

      } else {

        this.userAlias = 'Administrador';
      }

    } catch (err) {

      console.error(
        'Error al cargar perfil admin:',
        err
      );

      this.userAlias = 'Administrador';
    }
  }

  // =========================
  // NAVEGACIÓN
  // =========================

  irA(ruta: string) {

    this.router.navigate([ruta]);
  }

  // =========================
  // QR STORE CODE
  // =========================

  goToStoreCode() {

    this.router.navigate(
      ['/store-code'],
      {
        replaceUrl: false
      }
    );
  }

  // =========================
  // LOGOUT
  // =========================

  async logout() {

    try {

      await this.authService.logout();

      this.router.navigate(
        ['/login'],
        {
          replaceUrl: true
        }
      );

    } catch (error) {

      console.error(
        'Error al cerrar sesión:',
        error
      );
    }
  }

}