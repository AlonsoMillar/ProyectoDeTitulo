import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StoreCodePage } from './store-code.page';

describe('StoreCodePage', () => {
  let component: StoreCodePage;
  let fixture: ComponentFixture<StoreCodePage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(StoreCodePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
