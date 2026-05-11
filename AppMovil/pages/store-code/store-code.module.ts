import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

import { StoreCodePageRoutingModule } from './store-code-routing.module';
import { StoreCodePage } from './store-code.page';

// 1. Importamos la librería del QR
import { QRCodeComponent } from 'angularx-qrcode';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    StoreCodePageRoutingModule,
    QRCodeComponent // 2. La agregamos al arreglo de imports
  ],
  declarations: [StoreCodePage]
})
export class StoreCodePageModule {}