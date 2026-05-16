import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EventoFormPage } from './evento-form.page';

const routes: Routes = [
  {
    path: '',
    component: EventoFormPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EventoFormPageRoutingModule {}
