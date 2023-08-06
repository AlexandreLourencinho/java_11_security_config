import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "@env/environment.prod";
import LoginRequestModel from "@app/models/authentication/login-request.model";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor() { }

  public logInUser(loginRequest: LoginRequestModel): void { // TODO model login request
    // this.http.post(environment.authentication.login.signing, loginRequest);
  }
}
