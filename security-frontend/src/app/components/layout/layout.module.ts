import {NgModule} from '@angular/core';
import {HeaderComponent} from "@components/layout/header/header.component";
import {FooterComponent} from "@components/layout/footer/footer.component";
import {SharedModule} from "@app/shared/shared.module";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {FormsModule} from "@angular/forms";


@NgModule({
  declarations:
    [
      HeaderComponent,
      FooterComponent
    ],
  exports: [
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    SharedModule,
    MatToolbarModule,
    MatButtonModule,
    FormsModule
  ]
})
export class LayoutModule {
}
