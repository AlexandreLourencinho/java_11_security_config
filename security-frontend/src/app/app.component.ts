import { Component } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'security-frontend';
  selected= 'fr';


  constructor(private translate: TranslateService, private router: Router) {
    // Configuration des langues prises en charge
    translate.addLangs(['en', 'fr']);

    // Langue par défaut
    translate.setDefaultLang('en');

    // Détecte automatiquement la langue du navigateur et utilise la langue par défaut si elle n'est pas prise en charge
    const browserLang = translate.getBrowserLang();
    translate.use(browserLang?.match(/en|fr/) ? browserLang : 'en');
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
