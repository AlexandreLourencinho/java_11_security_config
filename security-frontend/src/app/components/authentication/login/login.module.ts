import {NgModule} from '@angular/core';
import {LoginComponent} from "@components/authentication/login/login.component";
import {LoginRoutingModule} from "@components/authentication/login/login-routing.module";
import {FormLoginComponent} from '@components/authentication/login/form-login/form-login.component';
import {FormSignupComponent} from '@components/authentication/login/form-signup/form-signup.component';
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {HttpClient} from "@angular/common/http";
import {SharedModule} from "@app/shared/shared.module";
import {LogoutComponent} from './logout/logout.component';
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {FormsModule} from "@angular/forms";

export function createTranslateLoader(http: HttpClient) {
  console.log('FeatureModule createTranslateLoader');
  return new TranslateHttpLoader(
    http, './assets/i18n/login/', '.json');
}

@NgModule({
  imports: [
    SharedModule,
    LoginRoutingModule,
    MatButtonModule,
    MatCardModule,
    FormsModule
  ],
  declarations: [
    LoginComponent,
    FormLoginComponent,
    FormSignupComponent,
    LogoutComponent
  ]
})
export class LoginModule {
}
