import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guard/auth.guard';

const routes: Routes = [

  // 🔥 RUTA INICIAL LIMPIA (SOLO DECISIÓN DE FLUJO)
  {
    path: '',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/home/home.module').then(m => m.HomePageModule)
  },

  // 🔓 LOGIN (PÚBLICO)
  {
    path: 'login',
    loadChildren: () =>
      import('./pages/login/login.module').then(m => m.LoginPageModule)
  },

  // 🧭 ONBOARDING (PROTEGIDO POR AUTH)
  {
    path: 'onboarding',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/onboarding/onboarding.module').then(m => m.OnboardingPageModule)
  },

  // 🏠 HOME CLIENT
  {
    path: 'home',
    loadChildren: () =>
      import('./pages/home/home.module').then(m => m.HomePageModule)
  },

  // 🏠 HOME ADMIN
  {
    path: 'home-admin',
    loadChildren: () =>
      import('./pages/home-admin/home-admin.module').then(m => m.HomeAdminPageModule)
  },

  // 📦 STORE CODE (PROTEGIDO)
  {
    path: 'store-code',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pages/store-code/store-code.module').then(m => m.StoreCodePageModule)
  },

  // 🚫 FALLBACK
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