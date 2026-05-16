import { Component } from '@angular/core';
import { Platform } from '@ionic/angular';
import { GoogleAuth } from '@codetrix-studio/capacitor-google-auth';
import { StatusBar, Style } from '@capacitor/status-bar'; // Importamos StatusBar

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone: false
})
export class AppComponent {

  constructor(private platform: Platform) {
    this.initializeApp();
  }

  async initializeApp() {
    await this.platform.ready();
    
    // Configuración para el "Display Infinito"
    if (this.platform.is('capacitor')) {
      // 1. Hacemos que la Status Bar sea transparente y la App flote detrás
      await StatusBar.setOverlaysWebView({ overlay: true });
      
      // 2. Definimos el estilo de los iconos (Dark para iconos blancos, Light para negros)
      // Como tu app es oscura, Dark hará que el reloj/batería se vean blancos.
      await StatusBar.setStyle({ style: Style.Dark });
    }

    this.initializeGoogle();
  }

  initializeGoogle() {
    GoogleAuth.initialize({
      clientId: '902749656527-0f8scgl1n3jfmdt11m8u6k8m8du6f923.apps.googleusercontent.com',
      scopes: ['profile', 'email'],
      grantOfflineAccess: true
    });
  }
}