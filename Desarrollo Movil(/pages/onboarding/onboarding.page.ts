import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoadingController, ToastController } from '@ionic/angular';
import { BarcodeScanner } from '@capacitor-mlkit/barcode-scanning';
import { UsuarioService, OnboardingRequest } from '../../service/usuario.service';
import { getAuth } from 'firebase/auth';

@Component({
  selector: 'app-onboarding',
  templateUrl: './onboarding.page.html',
  styleUrls: ['./onboarding.page.scss'],
  standalone: false
})
export class OnboardingPage {

  alias: string = '';
  role: 'ADMIN' | 'CLIENT' | null = null;
  storeCode: string = '';

  constructor(
    private router: Router,
    private loadingController: LoadingController,
    private toastController: ToastController,
    private usuarioService: UsuarioService
  ) {}

  // 🔥 Validación del formulario
  isFormValid(): boolean {
    if (!this.alias || this.alias.trim() === '') return false;
    if (!this.role) return false;
    if (this.role === 'CLIENT' && this.storeCode.trim().length !== 4) return false;
    return true;
  }

  // 📷 Escaneo QR (storeCode CLIENT)
  async scanQR() {
    try {
      const { camera } = await BarcodeScanner.requestPermissions();

      if (camera === 'granted' || camera === 'limited') {
        const { barcodes } = await BarcodeScanner.scan();

        if (barcodes.length > 0) {
          this.storeCode = barcodes[0].displayValue;
        }
      }
    } catch (error) {
      console.error('Error al escanear:', error);
    }
  }

  // 💾 Guardar perfil
 async saveProfile() {

  const auth = getAuth();
  const user = auth.currentUser;

  if (!user || !this.role || !this.alias) return;

  const profileData = {
    uid: user.uid,
    alias: this.alias.trim(),
    role: this.role
  };

  console.log('📤 guardando perfil...', profileData);

  await this.usuarioService.registrarPerfil(profileData);

  console.log('🟢 perfil guardado');

  // 🔥 CLAVE: volver al router base
  this.router.navigate(['/'], { replaceUrl: true });
}
}