import {Component, OnInit} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  public language: string = "";
  public languageArray: string[] = [];

  constructor(private translate: TranslateService, private router: Router) {
  }

  ngOnInit(): void {
    this.languageArray = this.translate.getLangs();
    this.language = this.translate.currentLang;
  }

  public changeEffect(): void {
    if (this.language !== this.translate.currentLang) {
      this.translate.use(this.language);
    }
  }

  public goToLogin(): void {
    void this.router.navigate(["/login"]);
  }

  public goToHomePage(): void {
    void this.router.navigate(['']);
  }

}
