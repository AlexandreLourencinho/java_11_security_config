import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "@services/authentication/authentication.service";

@Component({
  selector: 'app-form-login',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.css']
})
export class FormLoginComponent implements OnInit {

  constructor(private authentService: AuthenticationService) { }

  ngOnInit(): void {
  }

}
