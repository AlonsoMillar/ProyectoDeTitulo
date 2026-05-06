import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { UsuarioService } from '../../service/usuario.service';
import { LoadingController, ToastController } from '@ionic/angular';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: false
})
export class LoginPage {

  constructor(
    private authService: AuthService,
    private usuarioService: UsuarioService,
    private router: Router,
    private loadingController: LoadingController,
    private toastController: ToastController
  ) {}

  async signInWithGoogle() {
    const loading = await this.loadingController.create({
      message: 'Autenticando...',
      spinner: 'crescent'
    });

    await loading.present();

    try {
      console.log("Iniciando login con Google y Firebase...");

      // 🔥 1. Login Firebase
      const user: any = await this.authService.login();
      console.log("Usuario autenticado:", user?.email);

      // 🔥 2. Consultar backend
      const usuarioExistente: any = await this.usuarioService.consultarUsuario(user.uid);
      console.log("Usuario DB:", usuarioExistente);

      await loading.dismiss();

      // 🔥 3. Navegación correcta
      if (usuarioExistente) {
        console.log("Usuario existente → dejamos que el guard decida");
        
        // 🚨 CLAVE: NO mandar a /home directamente
        this.router.navigate(['/'], { replaceUrl: true });

      } else {
        console.log("Usuario nuevo → onboarding");
        this.router.navigate(['/onboarding'], { replaceUrl: true });
      }

    } catch (error: any) {
      await loading.dismiss();
      console.error("Error en login:", error);

      let mensajeError = 'Error desconocido al iniciar sesión.';

      if (error?.error === 'popup_closed_by_user' || error?.message?.includes('popup_closed')) {
        mensajeError = 'Cancelaste el inicio de sesión.';
      }
      else if (error?.message?.includes('12500')) {
        mensajeError = 'Error de Google Sign-In (SHA-1 o Client ID incorrecto).';
      }
      else if (error?.message?.includes('10')) {
        mensajeError = 'Error de configuración (DEVELOPER_ERROR).';
      }
      else if (error?.message?.toLowerCase().includes('network_error')) {
        mensajeError = 'Error de red al conectar con Google.';
      }
      else if (error?.code === 'auth/network-request-failed') {
        mensajeError = 'Error de red con Firebase.';
      }
      else if (error?.code === 'auth/invalid-credential') {
        mensajeError = 'Credencial inválida.';
      }
      else if (error?.code === 'auth/user-disabled') {
        mensajeError = 'Cuenta deshabilitada.';
      }
      else if (error?.message) {
        mensajeError = error.message;
      }

      const toast = await this.toastController.create({
        message: mensajeError,
        duration: 4000,
        position: 'bottom',
        color: 'danger'
      });

      await toast.present();
    }
  }
}