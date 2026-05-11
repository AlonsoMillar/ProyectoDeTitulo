import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';

import { getAuth } from 'firebase/auth';
import { UsuarioService } from '../service/usuario.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private usuarioService: UsuarioService
  ) {}

  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean | UrlTree> {

    const auth = getAuth();

    await auth.authStateReady?.();

    const user = auth.currentUser;

    //  NO LOGEADO
    if (!user) {
      return this.router.createUrlTree(['/login']);
    }

    let perfil = null;

    try {

      perfil = await this.usuarioService
        .consultarUsuario(user.uid);

    } catch (e) {

      console.error('Backend error:', e);

      return this.router.createUrlTree(['/login']);
    }

    //  SIN PERFIL → ONBOARDING
    if (!perfil) {

      // permitir entrar al onboarding
      if (state.url === '/onboarding') {
        return true;
      }

      return this.router.createUrlTree(['/onboarding']);
    }

    //  YA TIENE PERFIL
    // NO volver a onboarding
    if (state.url === '/onboarding') {

      return this.router.createUrlTree(
        perfil.role === 'ADMIN'
          ? ['/home-admin']
          : ['/home']
      );
    }

    //  ROOT → redirección central
    if (state.url === '/' || state.url === '') {

      return this.router.createUrlTree(
        perfil.role === 'ADMIN'
          ? ['/home-admin']
          : ['/home']
      );
    }

    //  TODO OK
    return true;
  }
}