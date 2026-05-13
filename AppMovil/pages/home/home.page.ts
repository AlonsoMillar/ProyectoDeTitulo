import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';

import {
  UsuarioService,
  UsuarioPerfil
} from '../../service/usuario.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: false
})
export class HomePage implements OnInit {

  perfil: UsuarioPerfil | null = null;

  userAlias: string = 'Cargando...';

  userAvatar: string | null = null;

  constructor(
    private router: Router,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    this.cargarDatosUsuario();
  }

  async cargarDatosUsuario() {

    const auth = getAuth();

    const user = auth.currentUser;

    if (!user) {
      return;
    }

    this.userAvatar = user.photoURL;

    try {

      const perfil =
        await this.usuarioService.consultarUsuario(user.uid);

      if (perfil) {

        this.perfil = perfil;

        this.userAlias =
          perfil.alias ||
          user.displayName ||
          'Jugador';
      }

    } catch (err) {

      console.error(
        'Error al obtener perfil:',
        err
      );

      this.userAlias =
        user.displayName ||
        'Jugador';
    }
  }

  irA(ruta: string) {
    this.router.navigate([ruta]);
  }
}