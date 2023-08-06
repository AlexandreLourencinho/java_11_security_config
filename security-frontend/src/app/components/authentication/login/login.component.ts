import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "@services/authentication/authentication.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router,
              private authenticationService: AuthenticationService,
              private translateService: TranslateService) { }

  public authenticationType: string | null = null;

  ngOnInit(): void {
    this.translateService.use(this.translateService.currentLang);
    // TODO document why this method 'ngOnInit' is empty
  }

  /**
   *
   */
  public goToSignIn(): void {
    this.authenticationType = "signin";
  }

  /**
   *
   */
  public goToSignUp(): void {
    this.authenticationType = "signup";
  }
}
