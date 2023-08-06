import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from "@components/authentication/login/login.component";
import {LoginRoutingModule} from "@components/authentication/login/login-routing.module";
import {FormLoginComponent} from '@components/authentication/login/form-login/form-login.component';
import {FormSignupComponent} from '@components/authentication/login/form-signup/form-signup.component';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {HttpClient} from "@angular/common/http";
import {SharedModule} from "@app/shared/shared.module";

export function createTranslateLoader(http: HttpClient) {
  console.log('FeatureModule createTranslateLoader');
  return new TranslateHttpLoader(
    http, './assets/i18n/login/', '.json');
}

@NgModule({
  imports: [
    SharedModule,
    LoginRoutingModule,
    TranslateModule
  ],
  declarations: [
    LoginComponent,
    FormLoginComponent,
    FormSignupComponent
  ]
})
export class LoginModule {
}
