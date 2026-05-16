import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';

import {
  UsuarioService,
  UsuarioPerfil,
  OnboardingRequest
} from '../../service/usuario.service';

import { AuthService } from '../../service/auth.service';

import {
  ToastController,
  LoadingController,
  Platform,
  NavController
} from '@ionic/angular';

import { BarcodeScanner } from '@capacitor-mlkit/barcode-scanning';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.page.html',
  styleUrls: ['./perfil.page.scss'],
  standalone: false
})
export class PerfilPage implements OnInit {

  perfil: UsuarioPerfil = {
    uid: '',
    alias: 'Cargando...',
    role: 'CLIENT',
    storeCode: ''
  };

  userAvatar: string | null = null;
  nuevoAlias: string = '';
  nuevoStoreCode: string = '';
  editMode: boolean = false;

  constructor(
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router,
    private navCtrl: NavController, // Inyectado para manejo de roles al volver
    private toastController: ToastController,
    private loadingController: LoadingController,
    private platform: Platform
  ) {}

  ngOnInit() {
    this.cargarDatos();
  }

  // Detecta el rol y redirige al home correcto
  volver() {
    const rutaHome = this.perfil?.role === 'ADMIN' ? '/home-admin' : '/home';
    this.navCtrl.navigateBack(rutaHome);
  }

  async cargarDatos() {
    const auth = getAuth();
    const user = auth.currentUser;

    if (!user) {
      return;
    }

    this.userAvatar = user.photoURL;

    try {
      const data = await this.usuarioService.consultarUsuario(user.uid);
      if (data) {
        this.perfil = data;
        this.nuevoAlias = data.alias || '';
        this.nuevoStoreCode = data.storeCode || '';
      }
    } catch (error) {
      console.error('Error al obtener datos del perfil:', error);
    }
  }

  toggleEdit() {
    this.editMode = true;
  }

  cancelarEdicion() {
    this.editMode = false;
    this.nuevoAlias = this.perfil.alias;
    this.nuevoStoreCode = this.perfil.storeCode || '';
  }

  async cambiarTiendaQR() {
    if (!this.platform.is('capacitor')) {
      this.mostrarToast('El escaneo QR solo funciona en dispositivos reales', 'warning');
      return;
    }

    try {
      const { camera } = await BarcodeScanner.requestPermissions();
      if (camera === 'granted' || camera === 'limited') {
        const { barcodes } = await BarcodeScanner.scan();
        if (barcodes.length > 0) {
          this.nuevoStoreCode = barcodes[0].displayValue.trim().toUpperCase();
        }
      }
    } catch (error) {
      console.error('Error QR:', error);
    }
  }

  async guardarCambios() {
    if (!this.nuevoAlias.trim()) {
      this.mostrarToast('El alias no puede estar vacío', 'warning');
      return;
    }

    const loading = await this.loadingController.create({
      message: 'Guardando cambios...'
    });
    await loading.present();

    try {
      const updateData: OnboardingRequest = {
        uid: this.perfil.uid,
        alias: this.nuevoAlias.trim(),
        role: this.perfil.role,
        storeCode: this.perfil.role === 'CLIENT' ? this.nuevoStoreCode.toUpperCase() : undefined
      };

      await this.usuarioService.registrarPerfil(updateData);
      
      this.perfil.alias = this.nuevoAlias;
      if (this.perfil.role === 'CLIENT') {
        this.perfil.storeCode = this.nuevoStoreCode;
      }

      this.editMode = false;
      this.mostrarToast('Perfil actualizado con éxito', 'success');
    } catch (error) {
      this.mostrarToast('Error al actualizar perfil', 'danger');
    } finally {
      await loading.dismiss();
    }
  }

  async logout() {
    await this.authService.logout();
    this.router.navigate(['/login'], { replaceUrl: true });
  }

  irA(ruta: string) {
    this.router.navigate([ruta]);
  }

  async mostrarToast(message: string, color: 'success' | 'danger' | 'warning') {
    const toast = await this.toastController.create({
      message,
      duration: 2000,
      color
    });
    await toast.present();
  }
}