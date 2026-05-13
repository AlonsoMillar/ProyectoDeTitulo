import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';
import { UsuarioService } from '../../service/usuario.service';
import { MensajeService, Mensaje } from '../../service/mensaje.service';

@Component({
  selector: 'app-mensajes-admin',
  templateUrl: './mensajes-admin.page.html',
  styleUrls: ['./mensajes-admin.page.scss'],
  standalone: false
})
export class MensajesAdminPage {
  conversaciones: any[] = [];
  mensajes: Mensaje[] = [];
  chatActivo: any = null;
  nuevoTexto = '';

  currentUserUid = '';
  currentUserStoreCode = '';
  currentUserAlias = '';

  constructor(
    private router: Router,
    private usuarioService: UsuarioService,
    private mensajeService: MensajeService
  ) {}

  ionViewWillEnter() {
    this.inicializarAdmin();
  }

  async inicializarAdmin() {
    const auth = getAuth();
    const user = auth.currentUser;
    if (!user) {
      this.router.navigate(['/login']);
      return;
    }

    this.currentUserUid = user.uid;

    try {
      // Replicamos la lógica del Home para obtener el perfil del Admin
      const perfil: any = await this.usuarioService.consultarUsuario(user.uid);
      if (perfil) {
        this.currentUserAlias = perfil.alias || 'Admin';
        this.currentUserStoreCode = (perfil.storeCode || '').trim().toUpperCase();
        
        if (this.currentUserStoreCode) {
          this.cargarBandejaEntrada();
        }
      }
    } catch (error) {
      console.error('Error al cargar perfil admin:', error);
    }
  }

  cargarBandejaEntrada() {
    this.mensajeService.obtenerInbox(this.currentUserStoreCode).subscribe({
      next: async (data: any) => {
        const chats = data.chats || data || [];

        // ENRIQUECIMIENTO: Buscamos la foto de cada cliente igual que en el Home
        this.conversaciones = await Promise.all(chats.map(async (conv: any) => {
          try {
            // Extraer UID del cliente del chatId (TIENDA_CLIENTEUID)
            const partes = conv.chatId.split('_');
            const clienteUid = partes.length > 1 ? partes[1] : conv.senderUid;

            // LLAMADA AL SERVICIO (Igual que en tu Home)
            const perfilCliente = await this.usuarioService.consultarUsuario(clienteUid);
            
            return {
              ...conv,
              userAvatar: perfilCliente?.photoUrl || null // Guardamos la URL para el HTML
            };
          } catch (e) {
            return conv;
          }
        }));
      },
      error: (e) => console.error('Error al cargar inbox:', e)
    });
  }

  abrirChat(conv: any) {
    this.chatActivo = conv;
    this.mensajeService.obtenerMensajes(conv.chatId).subscribe(data => {
      this.mensajes = data || [];
    });
  }

  enviar() {
    if (!this.nuevoTexto.trim() || !this.chatActivo) return;

    const partes = this.chatActivo.chatId.split('_');
    const receptorUid = partes.length > 1 ? partes[1] : this.chatActivo.senderUid;

    const msg: Mensaje = {
      chatId: this.chatActivo.chatId,
      senderUid: this.currentUserUid,
      senderAlias: this.currentUserAlias,
      senderRole: 'ADMIN',
      storeCode: this.currentUserStoreCode,
      receiverUid: receptorUid,
      contenido: this.nuevoTexto.trim()
    };

    this.mensajeService.enviarMensaje(msg).subscribe(() => {
      this.nuevoTexto = '';
      this.abrirChat(this.chatActivo);
    });
  }

  irA(ruta: string) { this.router.navigate([ruta]); }
}