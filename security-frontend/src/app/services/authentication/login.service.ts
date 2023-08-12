import {Injectable} from '@angular/core';
import LoginRequestModel from "@app/models/authentication/login-request.model";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor() {
  }

  public logInUser(loginRequest: LoginRequestModel): void {
    // this.http.post(environment.authentication.login.signing, loginRequest);
    console.info(`sending request to the back with loginRequest for user: ${loginRequest.getUsername()}`);
    // TODO REQUEST TO GET THE JWT TOKEN + storing the token (?? localStorage ? check for security level)
  }
}
