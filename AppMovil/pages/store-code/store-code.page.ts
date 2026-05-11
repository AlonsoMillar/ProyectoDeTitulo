import { Component, OnInit } from '@angular/core';
import { Share } from '@capacitor/share';
import { getAuth } from 'firebase/auth';
import { UsuarioService } from '../../service/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-store-code',
  templateUrl: './store-code.page.html',
  styleUrls: ['./store-code.page.scss'],
  standalone: false
})
export class StoreCodePage implements OnInit {

  storeCode: string = 'Cargando...';

  constructor(
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarCodigo();
  }

  async cargarCodigo() {
    const auth = getAuth();
    const user = auth.currentUser;

    if (user) {
      try {
        const perfil: any = await this.usuarioService.consultarUsuario(user.uid);

        if (perfil && perfil.storeCode) {
          this.storeCode = perfil.storeCode;
        } else {
          this.storeCode = 'Sin código';
        }

      } catch (err) {
        console.error('Error al obtener código de tienda', err);
        this.storeCode = 'Error al cargar';
      }
    }
  }

  goBack() {
    this.router.navigate(['/home-admin'], { replaceUrl: true });
  }

  async shareCode() {
    try {
      await Share.share({
        title: 'Únete a mi Gremio',
        text: `¡Hola! Únete a mi tienda en Guild Letter para participar en los eventos de Magic. Mi código es: ${this.storeCode}`,
        dialogTitle: 'Compartir código de tienda',
      });

    } catch (error) {
      console.error('Error al compartir el código:', error);
    }
  }
}