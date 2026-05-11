import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';
import { UsuarioService } from '../../service/usuario.service';
import { MensajeService, Mensaje } from '../../service/mensaje.service';

@Component({
  selector: 'app-mensajes',
  templateUrl: './mensajes.page.html',
  styleUrls: ['./mensajes.page.scss'],
  standalone: false
})
export class MensajesPage {
  nuevoTexto = '';
  mensajes: Mensaje[] = [];
  currentUserUid = '';
  currentUserAlias = '';
  currentUserStoreCode = '';
  chatId = '';
  cargando = true;

  constructor(
    private router: Router,
    private usuarioService: UsuarioService,
    private mensajeService: MensajeService
  ) {}

  ionViewWillEnter() {
    this.init();
  }

  async init() {
    this.cargando = true;
    const auth = getAuth();
    const user = auth.currentUser;

    if (!user) {
      this.cargando = false;
      return;
    }

    this.currentUserUid = user.uid;
    const perfil: any = await this.usuarioService.consultarUsuario(user.uid);
    
    this.currentUserAlias = perfil?.alias || 'Usuario';
    this.currentUserStoreCode = perfil?.storeCode || '';

    // El ID del chat debe ser consistente: TIENDA_USUARIO
    this.chatId = `${this.currentUserStoreCode}_${this.currentUserUid}`;

    this.cargarMensajes();
  }

  cargarMensajes() {
    this.mensajeService.obtenerMensajes(this.chatId).subscribe({
      next: (data) => {
        this.mensajes = data || [];
        this.cargando = false;
      },
      error: (e) => {
        console.error('ERROR AL OBTENER MENSAJES:', e);
        this.mensajes = [];
        this.cargando = false;
      }
    });
  }

  enviar() {
    if (!this.nuevoTexto.trim()) return;

    const msg: Mensaje = {
      chatId: this.chatId,
      senderUid: this.currentUserUid,
      senderAlias: this.currentUserAlias,
      senderRole: 'CLIENTE',
      storeCode: this.currentUserStoreCode,
      receiverUid: 'ADMIN', // El admin es el receptor por defecto
      contenido: this.nuevoTexto
    };

    this.mensajeService.enviarMensaje(msg).subscribe({
      next: () => {
        this.nuevoTexto = '';
        this.cargarMensajes(); // Refrescar lista
      },
      error: (e) => console.error('Error enviando mensaje:', e)
    });
  }

  irA(ruta: string) { this.router.navigate([ruta]); }
}