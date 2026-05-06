import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';
import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

// --- IMPORTACIÓN NUEVA ---
import { HttpClientModule } from '@angular/common/http'; 
// -------------------------

// Importaciones de Firebase
import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyD5L0pwNAVnf5d03FKLNgDZocHWcmexouE",
  authDomain: "programwork-333af.firebaseapp.com",
  projectId: "programwork-333af",
  storageBucket: "programwork-333af.firebasestorage.app",
  messagingSenderId: "902749656527",
  appId: "1:902749656527:web:146139991fbf709cb72eba"
};

// Inicialización inmediata
const app = initializeApp(firebaseConfig);
getAuth(app);

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule, 
    IonicModule.forRoot(), 
    AppRoutingModule,
    // --- AGREGAR AQUÍ ---
    HttpClientModule 
    // -------------------
  ],
  providers: [{ provide: RouteReuseStrategy, useClass: IonicRouteStrategy }],
  bootstrap: [AppComponent],
})
export class AppModule {}