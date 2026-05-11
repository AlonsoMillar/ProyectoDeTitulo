import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';
import { AuthService } from '../../service/auth.service';
import { UsuarioService } from '../../service/usuario.service';
import { BarcodeScanner } from '@capacitor-mlkit/barcode-scanning';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: false
})
export class HomePage implements OnInit {
  userAlias: string = 'Cargando...';
  userAvatar: string | null = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    this.cargarDatosUsuario();
  }

  async cargarDatosUsuario() {
    const auth = getAuth();
    const user = auth.currentUser;
    
    if (user) {
      this.userAvatar = user.photoURL;

      try {
        const perfil: any = await this.usuarioService.consultarUsuario(user.uid);

        if (perfil && perfil.alias) {
          this.userAlias = perfil.alias;
        } else {
          this.userAlias = user.displayName || 'Usuario';
        }

      } catch (err: any) {
        console.error('Error al obtener perfil del microservicio', err);
        this.userAlias = user.displayName || 'Usuario';
      }
    }
  }

  async scanQR() {
    try {
      const { camera } = await BarcodeScanner.requestPermissions();
      if (camera === 'granted' || camera === 'limited') {
        const { barcodes } = await BarcodeScanner.scan();
        if (barcodes.length > 0) {
          alert(`Código de tienda: ${barcodes[0].displayValue}`);
        }
      }
    } catch (error) {
      console.error('Error al escanear:', error);
    }
  }

  async logout() {
    await this.authService.logout();
    this.router.navigate(['/login'], { replaceUrl: true });
  }

  // 🔥 NUEVO: Función para la barra de navegación inferior
  irA(ruta: string) {
    this.router.navigate([ruta]);
  }
}