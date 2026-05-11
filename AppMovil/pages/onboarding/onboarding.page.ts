import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastController } from '@ionic/angular';
import { BarcodeScanner } from '@capacitor-mlkit/barcode-scanning';

import {
  UsuarioService,
  OnboardingRequest
} from '../../service/usuario.service';

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
    private toastController: ToastController,
    private usuarioService: UsuarioService
  ) {}

  //  VALIDACIÓN
  isFormValid(): boolean {

    if (!this.alias || this.alias.trim() === '') {
      return false;
    }

    if (!this.role) {
      return false;
    }

    if (
      this.role === 'CLIENT' &&
      this.storeCode.trim().length !== 4
    ) {
      return false;
    }

    return true;
  }

  //  ESCANEAR QR
  async scanQR() {

    try {

      const { camera } =
        await BarcodeScanner.requestPermissions();

      if (camera === 'granted' || camera === 'limited') {

        const { barcodes } =
          await BarcodeScanner.scan();

        if (barcodes.length > 0) {

          this.storeCode =
            barcodes[0].displayValue
              .trim()
              .toUpperCase();
        }
      }

    } catch (error) {

      console.error('Error al escanear:', error);
    }
  }

  //  GUARDAR PERFIL
  async saveProfile() {

    const auth = getAuth();

    const user = auth.currentUser;

    if (!user || !this.role || !this.alias) {
      return;
    }

    const profileData: OnboardingRequest = {

      uid: user.uid,

      alias: this.alias.trim(),

      role: this.role,

      storeCode:
        this.role === 'CLIENT'
          ? this.storeCode.trim().toUpperCase()
          : undefined
    };

    console.log(' guardando perfil...', profileData);

    try {

      await this.usuarioService
        .registrarPerfil(profileData);

      console.log(' perfil guardado');

      //  VOLVER AL ROOT
      // el guard decide destino
      this.router.navigate(['/'], {
        replaceUrl: true
      });

    } catch (error: any) {

      console.error(' ERROR onboarding:', error);

      const toast =
        await this.toastController.create({

          message:
            error?.error ||
            'Error al registrar usuario',

          duration: 3000,

          color: 'danger'
        });

      await toast.present();
    }
  }
}