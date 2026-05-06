import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
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

    //  NO LOGEADO → LOGIN
    if (!user) {
      return this.router.createUrlTree(['/login']);
    }

    let perfil = null;

    try {
      perfil = await this.usuarioService.consultarUsuario(user.uid);
    } catch (e) {
      console.error('Backend error:', e);
      //  si el backend falla, mejor bloquear acceso
      return this.router.createUrlTree(['/login']);
    }

    //  USUARIO SIN PERFIL → ONBOARDING
    if (!perfil) {

      // permitir entrar a onboarding
      if (state.url === '/onboarding') {
        return true;
      }

      return this.router.createUrlTree(['/onboarding']);
    }

    //  SI YA TIENE PERFIL → NO DEBE VOLVER A ONBOARDING
    if (state.url === '/onboarding') {
      return this.router.createUrlTree(
        perfil.role === 'ADMIN' ? ['/home-admin'] : ['/home']
      );
    }

    //  ROOT → REDIRECCIÓN SEGÚN ROL
    if (state.url === '/' || state.url === '') {
      return this.router.createUrlTree(
        perfil.role === 'ADMIN' ? ['/home-admin'] : ['/home']
      );
    }

    //  TODO OK → PERMITIR
    return true;
  }
}
