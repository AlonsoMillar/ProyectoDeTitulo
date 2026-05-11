import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guard/auth.guard';

const routes: Routes = [
  // 1. RUTA INICIAL (Protegida)
  {
    path: '',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/home/home.module').then(m => m.HomePageModule)
  },

  // 2. LOGIN (Público)
  {
    path: 'login',
    loadChildren: () =>
      import('./pages/login/login.module').then(m => m.LoginPageModule)
  },

  // 3. ONBOARDING (Protegido)
  {
    path: 'onboarding',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/onboarding/onboarding.module').then(m => m.OnboardingPageModule)
  },

  // 4. HOMES (Protegidos por la lógica del Guard y los componentes)
  {
    path: 'home',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/home/home.module').then(m => m.HomePageModule)
  },
  {
    path: 'home-admin',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/home-admin/home-admin.module').then(m => m.HomeAdminPageModule)
  },

  // 5. MENSAJES (Añadimos seguridad aquí también)
  {
    path: 'mensajes',
    canActivate: [AuthGuard],
    loadChildren: () => 
      import('./pages/mensajes/mensajes.module').then( m => m.MensajesPageModule)
  },
  {
    path: 'mensajes-admin',
    canActivate: [AuthGuard],
    loadChildren: () => 
      import('./pages/mensajes-admin/mensajes-admin.module').then( m => m.MensajesAdminPageModule)
  },

  // 6. STORE CODE
  {
    path: 'store-code',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/store-code/store-code.module').then(m => m.StoreCodePageModule)
  },

  // 7. FALLBACK (Siempre al final)
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
export class AppRoutingModule {}