import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FormLoginComponent} from './form-login.component';
import {AuthenticationService} from "@services/authentication/authentication.service";

import {HttpClientTestingModule} from "@angular/common/http/testing";
import {TranslateModule, TranslateStore} from "@ngx-translate/core";
import {LoginModule} from "@components/authentication/login/login.module";

describe('FormLoginComponent', () => {
  let component: FormLoginComponent;
  let fixture: ComponentFixture<FormLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormLoginComponent],
      providers: [
        AuthenticationService,
        TranslateStore
      ],
      imports: [
        HttpClientTestingModule,
        LoginModule,
        TranslateModule.forChild(),
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
