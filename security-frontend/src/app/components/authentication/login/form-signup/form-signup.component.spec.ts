import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormSignupComponent} from './form-signup.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {TranslateModule, TranslateStore} from "@ngx-translate/core";
import {LoginModule} from "../login.module";

describe('FormSignupComponent', () => {
  let component: FormSignupComponent;
  let fixture: ComponentFixture<FormSignupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormSignupComponent],
      imports: [
        LoginModule,
        HttpClientTestingModule,
        TranslateModule.forChild()
      ],
      providers: [
        TranslateStore
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
