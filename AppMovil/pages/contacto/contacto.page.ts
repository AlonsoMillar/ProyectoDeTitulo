import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastController, Platform, NavController } from '@ionic/angular';
import { getAuth } from 'firebase/auth';
import { UsuarioService, UsuarioPerfil } from '../../service/usuario.service';

@Component({
  selector: 'app-contacto',
  templateUrl: './contacto.page.html',
  styleUrls: ['./contacto.page.scss'],
  standalone: false
})
export class ContactoPage implements OnInit {
  
  perfil: UsuarioPerfil | null = null;
  contactoEmail: string = 'pprogramwork@gmail.com';

  constructor(
    private router: Router,
    private navCtrl: NavController, // Cambiado para mejor manejo de historial
    private toastController: ToastController,
    private usuarioService: UsuarioService,
    private platform: Platform
  ) {}

  ngOnInit() {
    this.cargarPerfil();
  }

  async cargarPerfil() {
    const auth = getAuth();
    const user = auth.currentUser;
    if (user) {
      try {
        this.perfil = await this.usuarioService.consultarUsuario(user.uid);
      } catch (error) {
        console.error('Error al cargar perfil en contacto', error);
      }
    }
  }

  // Corregir el bug de redirección al dar atrás
  volver() {
    const rutaHome = this.perfil?.role === 'ADMIN' ? '/home-admin' : '/home';
    this.navCtrl.navigateBack(rutaHome);
  }

  getHomeRoute(): string {
    return this.perfil?.role === 'ADMIN' ? '/home-admin' : '/home';
  }

  getMensajesRoute(): string {
    return this.perfil?.role === 'ADMIN' ? '/mensajes-admin' : '/mensajes';
  }

  enviarCorreo() {
    const subject = encodeURIComponent('Soporte Gremio App - Consulta');
    const mailtoUrl = `mailto:${this.contactoEmail}?subject=${subject}`;
    
    if (this.platform.is('capacitor') || this.platform.is('android') || this.platform.is('ios')) {
      window.open(mailtoUrl, '_system');
    } else {
      window.location.href = mailtoUrl;
    }
  }

  async copiarEmail() {
    navigator.clipboard.writeText(this.contactoEmail);
    const toast = await this.toastController.create({
      message: '✨ Email copiado al portapapeles',
      duration: 2000,
      cssClass: 'custom-toast',
      position: 'bottom'
    });
    toast.present();
  }

  irA(ruta: string) {
    this.router.navigate([ruta]);
  }
}