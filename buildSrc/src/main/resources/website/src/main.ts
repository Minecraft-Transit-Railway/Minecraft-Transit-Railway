import {bootstrapApplication} from "@angular/platform-browser";
import {AppComponent} from "./app/app.component";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {provideHttpClient} from "@angular/common/http";
import {FormatFileNamePipe} from "./app/pipe/formatFileNamePipe";
import {FormatStringListPipe} from "./app/pipe/formatStringListPipe";

bootstrapApplication(AppComponent, {providers: [provideAnimationsAsync(), provideHttpClient(), FormatFileNamePipe, FormatStringListPipe]}).catch(err => console.error(err));
