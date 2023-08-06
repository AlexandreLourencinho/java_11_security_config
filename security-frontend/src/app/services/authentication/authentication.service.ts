import { Injectable } from '@angular/core';
import {LoginService} from "@services/authentication/login.service";
import {SignupService} from "@services/authentication/signup.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private loginService: LoginService, private signupService: SignupService) { }

  /**
   * @return the active instance of the {@link LoginService}
   */
  public getLoginService(): LoginService {
    return this.loginService;
  }

  /**
   * @return the instance of the {@link SignupService}
   */
  public getSignupService(): SignupService {
    return this.signupService;
  }
}
