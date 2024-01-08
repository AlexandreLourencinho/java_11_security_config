import {TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {LayoutModule} from "@components/layout/layout.module";
import {RouterModule, RouterOutlet} from "@angular/router";

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        AppComponent
      ],
      providers: [
        TranslateService, RouterOutlet
      ],
      imports: [
        TranslateModule.forRoot(),
        LayoutModule,
        RouterModule
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'security-frontend'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('security-frontend');
  });
});
