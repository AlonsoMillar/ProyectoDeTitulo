import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MensajesAdminPage } from './mensajes-admin.page';

describe('MensajesAdminPage', () => {
  let component: MensajesAdminPage;
  let fixture: ComponentFixture<MensajesAdminPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(MensajesAdminPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
