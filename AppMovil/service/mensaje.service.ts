import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Mensaje {
  id?: string;
  chatId: string;
  senderUid: string;
  senderAlias: string;
  senderRole: string; // 'CLIENTE' o 'ADMIN'
  receiverUid?: string | null;
  storeCode: string; 
  contenido: string;
  createdAt?: number;
}

@Injectable({
  providedIn: 'root'
})
export class MensajeService {

  // URL Base extraída de tus registros de Cloud Run
  private urlBase = 'https://microservicio-mensajes-902749656527.us-central1.run.app/api/mensajes';

  constructor(private http: HttpClient) {}

  obtenerMensajes(chatId: string): Observable<Mensaje[]> {
    return this.http.get<Mensaje[]>(`${this.urlBase}/chat/${chatId}`);
  }

  enviarMensaje(mensaje: Mensaje): Observable<Mensaje> {
    return this.http.post<Mensaje>(`${this.urlBase}/enviar`, mensaje);
  }

  /**
   * CORRECCIÓN: 
   * Tu controlador en Java tiene: @GetMapping("/chats/{storeCode}")
   * Por lo tanto, eliminamos "/conversaciones/tienda/" que NO EXISTE en el backend.
   */
  obtenerInbox(storeCode: string): Observable<any[]> {
    // Limpiamos el código y usamos la ruta real del controlador
    const code = (storeCode || '').trim().toUpperCase();
    return this.http.get<any[]>(`${this.urlBase}/chats/${code}`);
  }
}