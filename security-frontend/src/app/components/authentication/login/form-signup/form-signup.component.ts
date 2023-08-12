import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "@services/authentication/authentication.service";
import SignupRequestModel from "@app/models/authentication/signup-request.model";

@Component({
  selector: 'app-form-signup',
  templateUrl: './form-signup.component.html',
  styleUrls: ['./form-signup.component.css']
})
export class FormSignupComponent implements OnInit {
  password: string | undefined;
  email: string | undefined;
  username: string | undefined;
  confirmPassword: string | undefined;

  constructor(private authentService: AuthenticationService) {
  }

  ngOnInit(): void {
  }

  /**
   *
   */
  public signup() {
    if (
      this.username
      && this.password
      && this.confirmPassword
      && this.email
      && this.password.trim() === this.confirmPassword.trim()
    ) {
      console.log("username = " + this.username + " password=" + this.password + " email=" + this.email);
      const signupRequest: SignupRequestModel = new SignupRequestModel(this.username, this.password, this.email);
      this.authentService.getSignupService().signupUser(signupRequest); // TODO
    }
  }
}
