import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from '@app/app.component';
import {AppRoutingModule} from '@app/app-routing.module';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {LayoutModule} from "@components/layout/layout.module";

export function createTranslateLoaderAppModule(http: HttpClient) {
  return new TranslateHttpLoader(http, '../assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    LayoutModule,
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
    })
  ],
  providers: [],
  exports : [
    TranslateModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
