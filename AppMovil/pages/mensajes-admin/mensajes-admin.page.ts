import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';
import { UsuarioService } from '../../service/usuario.service';
import { MensajeService, Mensaje } from '../../service/mensaje.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-mensajes-admin',
  templateUrl: './mensajes-admin.page.html',
  styleUrls: ['./mensajes-admin.page.scss'],
  standalone: false
})
export class MensajesAdminPage {
  nuevoTexto = '';
  mensajes: Mensaje[] = [];
  conversaciones: any[] = [];
  chatActivo: any = null;

  currentUserUid = '';
  currentUserAlias = '';
  currentUserStoreCode = '';

  constructor(
    private router: Router,
    private usuarioService: UsuarioService,
    private mensajeService: MensajeService,
    private authService: AuthService
  ) {}

  ionViewWillEnter() {
    this.init();
  }

  async init() {
    const auth = getAuth();
    const user = auth.currentUser;
    if (!user) {
      this.router.navigate(['/login']);
      return;
    }

    this.currentUserUid = user.uid;
    
    try {
      // Obtenemos el perfil actualizado para asegurar que tenemos el storeCode correcto
      const perfil: any = await this.usuarioService.consultarUsuario(user.uid);
      
      this.currentUserAlias = perfil?.alias || 'Admin';
      
      // NORMALIZACIÓN: Limpieza de espacios y forzado a mayúsculas
      // Esto evita que busques "2yvv " cuando en la DB es "2YVV"
      this.currentUserStoreCode = (perfil?.storeCode || '').trim().toUpperCase();

      console.log('Admin StoreCode identificado:', this.currentUserStoreCode);
      
      if (this.currentUserStoreCode) {
        this.cargarInbox();
      } else {
        console.warn('El administrador no tiene un código de tienda asignado.');
      }
    } catch (error) {
      console.error('Error al inicializar la vista de admin:', error);
    }
  }

  cargarInbox() {
    // IMPORTANTE: El servicio debe llamar a /api/mensajes/chats/{code}
    // No a /api/mensajes/conversaciones/tienda/{code}
    this.mensajeService.obtenerInbox(this.currentUserStoreCode).subscribe({
      next: (data: any) => {
        console.log('Inbox cargado con éxito:', data);
        // Ajustamos según si el microservicio devuelve el Map de prueba o solo la lista
        this.conversaciones = data.chats || data || [];
      },
      error: (e) => {
        console.error('Error 404: La ruta /chats/ no fue encontrada o el código no existe.', e);
      }
    });
  }

  abrirChat(conv: any) {
    console.log('Abriendo chat:', conv.chatId);
    this.chatActivo = conv;
    this.refrescarMensajes();
  }

  refrescarMensajes() {
    if (!this.chatActivo) return;
    
    this.mensajeService.obtenerMensajes(this.chatActivo.chatId).subscribe({
      next: (data) => {
        this.mensajes = data || [];
      },
      error: (e) => console.error('Error al obtener mensajes del chat:', e)
    });
  }

  enviar() {
    if (!this.nuevoTexto.trim() || !this.chatActivo) return;

    // Lógica para obtener el receptor:
    // Si el chatId es TIENDA_CLIENTEUID, partes[1] es el UID del cliente.
    const partes = this.chatActivo.chatId.split('_');
    const receptorUid = partes.length > 1 ? partes[1] : (this.chatActivo.senderUid || '');

    const msg: Mensaje = {
      chatId: this.chatActivo.chatId,
      senderUid: this.currentUserUid,
      senderAlias: this.currentUserAlias,
      senderRole: 'ADMIN',
      storeCode: this.currentUserStoreCode,
      receiverUid: receptorUid,
      contenido: this.nuevoTexto.trim()
    };

    this.mensajeService.enviarMensaje(msg).subscribe({
      next: () => {
        this.nuevoTexto = '';
        this.refrescarMensajes(); // Actualiza la lista para ver tu propio mensaje
      },
      error: (e) => console.error('Error al enviar mensaje desde el admin:', e)
    });
  }

  irA(ruta: string) { 
    this.router.navigate([ruta]); 
  }

  async logout() {
    await this.authService.logout();
    this.router.navigate(['/login']);
  }
}