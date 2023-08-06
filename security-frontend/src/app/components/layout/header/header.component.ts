import { Component } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {


  constructor(private translate: TranslateService, private router: Router) {
  }

  goToLogin() {
    this.router.navigate(["/login"]);
  }

  translateEn() {
    this.translate.use('en')
  }

  translateFr() {
    this.translate.use('fr')
  }

}
