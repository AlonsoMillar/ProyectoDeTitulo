import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface UsuarioPerfil {
  uid: string;
  alias: string;
  role: string;
  storeCode?: string;
  createdAt?: number;
}

export interface OnboardingRequest {
  uid: string;
  alias: string;
  role: string;
  storeCode?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private urlBase =
    'https://microservicio-usuarios-902749656527.us-central1.run.app/api/usuarios';

  constructor(private http: HttpClient) {}

  async consultarUsuario(uid: string): Promise<UsuarioPerfil | null> {

    try {

      const url = `${this.urlBase}/${uid}`;

      return await firstValueFrom(
        this.http.get<UsuarioPerfil>(url)
      );

    } catch (error: any) {

      console.error(' ERROR GET usuario:', {
        status: error?.status,
        message: error?.message,
        error: error?.error
      });

      return null;
    }
  }

  async registrarPerfil(
    datos: OnboardingRequest
  ): Promise<UsuarioPerfil> {

    const url = `${this.urlBase}/onboarding`;

    try {

      const res = await firstValueFrom(
        this.http.post<UsuarioPerfil>(url, datos)
      );

      console.log(' POST onboarding OK:', res);

      return res;

    } catch (error: any) {

      console.error(' ERROR POST onboarding:', {
        status: error?.status,
        message: error?.message,
        error: error?.error
      });

      throw error;
    }
  }
}