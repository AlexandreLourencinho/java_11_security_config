import {NgModule} from '@angular/core';
import {SharedModule} from "@app/shared/shared.module";
import {HomeComponent} from "@components/home/home.component";
import {HomeRoutingModule} from "@components/home/home-routing.module";


@NgModule({
  declarations: [HomeComponent],
  imports: [
    SharedModule,
    HomeRoutingModule
  ]
})
export class HomeModule {
}
