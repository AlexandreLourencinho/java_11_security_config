import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from '@app/app.component';
import {AppRoutingModule} from '@app/app-routing.module';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import { HeaderComponent } from '@components/layout/header/header.component';
import { FooterComponent } from '@components/layout/footer/footer.component';

export function createTranslateLoaderAppModule(http: HttpClient) {
  return new TranslateHttpLoader(http, '../assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoaderAppModule),
        deps: [HttpClient]
      },
      isolate: true
    }),
    MatToolbarModule,
    MatButtonModule
  ],
  providers: [],
  exports : [
    TranslateModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
