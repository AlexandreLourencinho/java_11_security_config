import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import SignupRequestModel from "@app/models/authentication/signup-request.model";

@Injectable({
  providedIn: 'root'
})
export class SignupService {

  constructor(private http: HttpClient) {
  }

  public signupUser(signupRequest: SignupRequestModel) {
    //TODO request to the backend
    console.info(`attempt to register user with mail : ${signupRequest.getMail()} with username :${signupRequest.getUsername()}`);
  }
}
