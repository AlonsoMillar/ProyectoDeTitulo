import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface Evento {
  id?: string;
  nombre: string;
  descripcion: string;
  fechaEvento: string;
  duracionMinutos: number;
  maxParticipantes: number;
  requiereJuez: boolean;
  storeCode: string;
  adminUid: string;
  adminAlias: string;
  estado?: 'BORRADOR' | 'PUBLICADO' | 'EN_CURSO' | 'FINALIZADO';
  createdAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class EventoService {
  private urlBase = 'https://evento-microservice-902749656527.us-central1.run.app/api/eventos';

  constructor(private http: HttpClient) {}

  async crearEvento(evento: Evento): Promise<Evento> {
    try {
      return await firstValueFrom(this.http.post<Evento>(this.urlBase, evento));
    } catch (error) {
      console.error('Error creando evento:', error);
      throw error;
    }
  }

  async getEventoById(id: string): Promise<Evento> {
    try {
      const url = `${this.urlBase}/${id}`;
      return await firstValueFrom(this.http.get<Evento>(url));
    } catch (error) {
      console.error('Error obteniendo evento:', error);
      throw error;
    }
  }

  async updateEvento(id: string, evento: Partial<Evento>): Promise<Evento> {
    try {
      const url = `${this.urlBase}/${id}`;
      return await firstValueFrom(this.http.put<Evento>(url, evento));
    } catch (error) {
      console.error('Error actualizando evento:', error);
      throw error;
    }
  }

  async deleteEvento(id: string): Promise<void> {
    try {
      const url = `${this.urlBase}/${id}`;
      await firstValueFrom(this.http.delete<void>(url));
    } catch (error) {
      console.error('Error eliminando evento:', error);
      throw error;
    }
  }

  async publicarEvento(id: string): Promise<Evento> {
    try {
      const url = `${this.urlBase}/${id}/publicar`;
      return await firstValueFrom(this.http.post<Evento>(url, {}));
    } catch (error) {
      console.error('Error publicando evento:', error);
      throw error;
    }
  }

  async iniciarEvento(id: string): Promise<Evento> {
    try {
      const url = `${this.urlBase}/${id}/iniciar`;
      return await firstValueFrom(this.http.post<Evento>(url, {}));
    } catch (error) {
      console.error('Error iniciando evento:', error);
      throw error;
    }
  }

  async finalizarEvento(id: string): Promise<Evento> {
    try {
      const url = `${this.urlBase}/${id}/finalizar`;
      return await firstValueFrom(this.http.post<Evento>(url, {}));
    } catch (error) {
      console.error('Error finalizando evento:', error);
      throw error;
    }
  }

  async getEventosAdmin(adminUid: string): Promise<Evento[]> {
    try {
      const url = `${this.urlBase}/admin/${adminUid}`;
      return await firstValueFrom(this.http.get<Evento[]>(url));
    } catch (error) {
      console.error('Error obteniendo eventos admin:', error);
      return [];
    }
  }

  async getEventosPublicados(): Promise<Evento[]> {
    try {
      const url = `${this.urlBase}/publicados`;
      return await firstValueFrom(this.http.get<Evento[]>(url));
    } catch (error) {
      console.error('Error obteniendo eventos publicados:', error);
      return [];
    }
  }
}
