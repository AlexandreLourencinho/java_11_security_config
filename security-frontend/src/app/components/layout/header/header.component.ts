import {Component, OnInit} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {


  constructor(private translate: TranslateService, private router: Router) {
  }

  ngOnInit(): void {
    this.language = this.translate.currentLang;
    console.log('this.translate.getLangs()', this.translate.getLangs(), 'at', new Date().toLocaleTimeString())
    console.log('this.translate.current', this.translate.currentLang, 'at', new Date().toLocaleTimeString())
  }

  public language: any;

  goToLogin() {
    this.router.navigate(["/login"]);
  }

  goToHomePage() {
    this.router.navigate(['']);
  }

  translateEn() {
    this.translate.use('en')
  }// TODO delete ?

  translateFr() {
    this.translate.use('fr')
  } // todo delete ?

  getTranslateService(): TranslateService {
    return this.translate;
  }

  changeEffect() {
    console.log('this.language', this.language, 'at', new Date().toLocaleTimeString());
    if(this.language !== this.translate.currentLang) {
      console.info("lang change triggered")
      this.translate.use(this.language);
      console.log('this.translate.currentLang', this.translate.currentLang, 'at', new Date().toLocaleTimeString())
    }
  }
}
