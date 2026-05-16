import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getAuth } from 'firebase/auth';
import { EventoService } from '../../service/evento.service';
import { UsuarioService, UsuarioPerfil } from '../../service/usuario.service';

@Component({
  selector: 'app-evento-form',
  templateUrl: './evento-form.page.html',
  styleUrls: ['./evento-form.page.scss'],
  standalone: false
})
export class EventoFormPage implements OnInit {
  eventoForm: FormGroup;
  perfil: UsuarioPerfil | null = null;
  guardando = false;

  constructor(
    private fb: FormBuilder,
    private eventoService: EventoService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {
    this.eventoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      descripcion: ['', [Validators.required, Validators.maxLength(500)]],
      fechaEvento: [new Date().toISOString(), Validators.required],
      duracionMinutos: [120, [Validators.required, Validators.min(10)]],
      maxParticipantes: [16, [Validators.required, Validators.min(2)]],
      requiereJuez: [false]
    });
  }

  async ngOnInit() {
    await this.cargarDatosUsuario();
  }

  async cargarDatosUsuario() {
    const auth = getAuth();
    const user = auth.currentUser;
    if (!user) return;
    try {
      this.perfil = await this.usuarioService.consultarUsuario(user.uid);
    } catch (err) {
      console.error('Error al obtener perfil:', err);
    }
  }

  volver() {
    this.router.navigate(['/eventos']);
  }

  async guardarEvento() {
    if (this.eventoForm.invalid || !this.perfil) return;
    this.guardando = true;

    try {
      const nuevoEvento = {
        ...this.eventoForm.value,
        storeCode: this.perfil.storeCode || 'DEFAULT_STORE',
        adminUid: this.perfil.uid,
        adminAlias: this.perfil.alias || 'Admin',
      };

      await this.eventoService.crearEvento(nuevoEvento);
      this.volver();
    } catch (error) {
      console.error('Error al crear evento:', error);
    } finally {
      this.guardando = false;
    }
  }
}
