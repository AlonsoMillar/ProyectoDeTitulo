import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

// 1. Revisa que el nombre importado sea el que tiene "Routing"
import { HomeAdminPageRoutingModule } from './home-admin-routing.module';
import { HomeAdminPage } from './home-admin.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    HomeAdminPageRoutingModule // 2. Y que aquí también coincida
  ],
  declarations: [HomeAdminPage]
})
export class HomeAdminPageModule {} // <--- Esta es la clase del módulo principal