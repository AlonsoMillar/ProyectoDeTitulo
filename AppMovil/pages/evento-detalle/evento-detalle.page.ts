import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { getAuth } from 'firebase/auth';
import { EventoService, Evento } from '../../service/evento.service';
import { UsuarioService, UsuarioPerfil } from '../../service/usuario.service';

@Component({
  selector: 'app-evento-detalle',
  templateUrl: './evento-detalle.page.html',
  styleUrls: ['./evento-detalle.page.scss'],
  standalone: false
})
export class EventoDetallePage implements OnInit {
  evento: Evento | null = null;
  perfil: UsuarioPerfil | null = null;
  cargando = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventoService: EventoService,
    private usuarioService: UsuarioService
  ) {}

  async ngOnInit() {
    await this.cargarDatosUsuario();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      await this.cargarEvento(id);
    } else {
      this.volver();
    }
  }

  async cargarDatosUsuario() {
    const auth = getAuth();
    const user = auth.currentUser;
    if (user) {
      try {
        this.perfil = await this.usuarioService.consultarUsuario(user.uid);
      } catch (err) {
        console.error('Error al obtener perfil:', err);
      }
    }
  }

  async cargarEvento(id: string) {
    this.cargando = true;
    try {
      this.evento = await this.eventoService.getEventoById(id);
    } catch (error) {
      console.error('Error al cargar evento', error);
      this.volver();
    } finally {
      this.cargando = false;
    }
  }

  get esCreador(): boolean {
    return this.perfil?.uid === this.evento?.adminUid;
  }

  volver() {
    this.router.navigate(['/eventos']);
  }

  async cambiarEstado(accion: 'publicar' | 'iniciar' | 'finalizar') {
    if (!this.evento?.id) return;
    try {
      if (accion === 'publicar') {
        this.evento = await this.eventoService.publicarEvento(this.evento.id);
      } else if (accion === 'iniciar') {
        this.evento = await this.eventoService.iniciarEvento(this.evento.id);
      } else if (accion === 'finalizar') {
        this.evento = await this.eventoService.finalizarEvento(this.evento.id);
      }
    } catch (error) {
      console.error(`Error al ${accion} evento:`, error);
    }
  }

  async eliminarEvento() {
    if (!this.evento?.id) return;
    if (confirm('¿Estás seguro de eliminar este evento?')) {
      try {
        await this.eventoService.deleteEvento(this.evento.id);
        this.volver();
      } catch (error) {
        console.error('Error al eliminar evento:', error);
      }
    }
  }
}
