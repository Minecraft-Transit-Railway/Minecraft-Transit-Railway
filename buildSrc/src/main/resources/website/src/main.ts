import {bootstrapApplication} from "@angular/platform-browser";
import {AppComponent} from "./app/app.component";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {provideHttpClient} from "@angular/common/http";

bootstrapApplication(AppComponent, {providers: [provideAnimationsAsync(), provideHttpClient()]}).catch(err => console.error(err));
