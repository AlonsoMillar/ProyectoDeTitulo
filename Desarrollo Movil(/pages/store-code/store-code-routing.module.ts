import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { StoreCodePage } from './store-code.page';

const routes: Routes = [
  {
    path: '',
    component: StoreCodePage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class StoreCodePageRoutingModule {}
