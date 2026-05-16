import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { EventoDetallePageRoutingModule } from './evento-detalle-routing.module';

import { EventoDetallePage } from './evento-detalle.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    EventoDetallePageRoutingModule
  ],
  declarations: [EventoDetallePage]
})
export class EventoDetallePageModule {}
