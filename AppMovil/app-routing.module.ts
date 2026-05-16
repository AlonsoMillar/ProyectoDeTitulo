import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guard/auth.guard';

const routes: Routes = [

  // =========================================================
  // REDIRECT PRINCIPAL
  // =========================================================
  {
    path: '',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/redirect/redirect.module').then(
        m => m.RedirectPageModule
      )
  },

  // =========================================================
  // LOGIN
  // =========================================================
  {
    path: 'login',
    loadChildren: () =>
      import('./pages/login/login.module').then(
        m => m.LoginPageModule
      )
  },

  // =========================================================
  // ONBOARDING
  // =========================================================
  {
    path: 'onboarding',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/onboarding/onboarding.module').then(
        m => m.OnboardingPageModule
      )
  },

  // =========================================================
  // HOME CLIENTE
  // =========================================================
  {
    path: 'home',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/home/home.module').then(
        m => m.HomePageModule
      )
  },

  // =========================================================
  // HOME ADMIN
  // =========================================================
  {
    path: 'home-admin',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/home-admin/home-admin.module').then(
        m => m.HomeAdminPageModule
      )
  },

  // =========================================================
  // MENSAJES CLIENTE
  // =========================================================
  {
    path: 'mensajes',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/mensajes/mensajes.module').then(
        m => m.MensajesPageModule
      )
  },

  // =========================================================
  // MENSAJES ADMIN
  // =========================================================
  {
    path: 'mensajes-admin',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/mensajes-admin/mensajes-admin.module').then(
        m => m.MensajesAdminPageModule
      )
  },

  // =========================================================
  // EVENTOS
  // =========================================================
  {
    path: 'eventos',
    canActivate: [AuthGuard],
    loadChildren: () => import('./pages/eventos/eventos.module').then(m => m.EventosPageModule)
  },
  {
    path: 'evento-detalle',
    canActivate: [AuthGuard],
    loadChildren: () => import('./pages/evento-detalle/evento-detalle.module').then(m => m.EventoDetallePageModule)
  },
  {
    path: 'evento-form',
    canActivate: [AuthGuard],
    loadChildren: () => import('./pages/evento-form/evento-form.module').then(m => m.EventoFormPageModule)
  },

  // =========================================================
  // PERFIL
  // =========================================================
  {
    path: 'perfil',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/perfil/perfil.module').then(
        m => m.PerfilPageModule
      )
  },

  // =========================================================
  // STORE CODE
  // =========================================================
  {
    path: 'store-code',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/store-code/store-code.module').then(
        m => m.StoreCodePageModule
      )
  },

  // =========================================================
  // CONTACTO
  // =========================================================
  {
    path: 'contacto',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/contacto/contacto.module').then(
        m => m.ContactoPageModule
      )
  },

  // =========================================================
  // FALLBACK
  // =========================================================
  {
    path: '**',
    redirectTo: ''
  }

];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      preloadingStrategy: PreloadAllModules
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }