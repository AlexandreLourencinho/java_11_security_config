import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "@services/authentication/authentication.service";
import LoginRequestModel from "@app/models/authentication/login-request.model";

@Component({
  selector: 'app-form-login',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.css']
})
export class FormLoginComponent implements OnInit {

  public username: string | undefined;
  public password: string | undefined;

  constructor(private authentService: AuthenticationService) {
  }

  ngOnInit(): void {
  }

  /**
   *
   */
  public signIn(): void {
    if (this.username && this.password) {
      const loginRequest: LoginRequestModel = new LoginRequestModel(this.username, this.password);
      this.authentService.getLoginService().logInUser(loginRequest);
    }
  }
}
