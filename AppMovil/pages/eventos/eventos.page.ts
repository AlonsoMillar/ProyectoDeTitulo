import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';
import { EventoService, Evento } from '../../service/evento.service';
import { UsuarioService, UsuarioPerfil } from '../../service/usuario.service';

@Component({
  selector: 'app-eventos',
  templateUrl: './eventos.page.html',
  styleUrls: ['./eventos.page.scss'],
  standalone: false
})
export class EventosPage implements OnInit {
  eventos: Evento[] = [];
  perfil: UsuarioPerfil | null = null;
  cargando = true;

  constructor(
    private router: Router,
    private eventoService: EventoService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    this.cargarDatosUsuario();
  }

  ionViewWillEnter() {
    if (this.perfil) {
      this.cargarEventos();
    }
  }

  async cargarDatosUsuario() {
    const auth = getAuth();
    const user = auth.currentUser;
    
    if (!user) {
      // Si no hay usuario (caso de preview/testing), cargamos los publicados
      await this.cargarEventos();
      return;
    }

    try {
      const perfil = await this.usuarioService.consultarUsuario(user.uid);
      if (perfil) {
        this.perfil = perfil;
        await this.cargarEventos();
      }
    } catch (err) {
      console.error('Error al obtener perfil:', err);
      // Intentamos cargar eventos de todas formas
      await this.cargarEventos();
    }
  }

  async cargarEventos() {
    this.cargando = true;
    try {
      if (this.perfil?.role === 'ADMIN') {
        this.eventos = await this.eventoService.getEventosAdmin(this.perfil.uid);
      } else {
        this.eventos = await this.eventoService.getEventosPublicados();
      }
    } catch (error) {
      console.error('Error al cargar eventos', error);
    } finally {
      this.cargando = false;
    }
  }

  irA(ruta: string) {
    this.router.navigate([ruta]);
  }

  verDetalle(id?: string) {
    if (id) {
      this.router.navigate(['/evento-detalle', { id }]);
    }
  }

  crearEvento() {
    this.router.navigate(['/evento-form']);
  }
}
