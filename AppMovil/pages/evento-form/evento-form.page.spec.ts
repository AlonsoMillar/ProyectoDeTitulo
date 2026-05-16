import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventoFormPage } from './evento-form.page';

describe('EventoFormPage', () => {
  let component: EventoFormPage;
  let fixture: ComponentFixture<EventoFormPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(EventoFormPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
