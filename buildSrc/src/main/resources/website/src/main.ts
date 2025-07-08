import {bootstrapApplication} from "@angular/platform-browser";
import {AppComponent} from "./app/app.component";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {FormatFileNamePipe} from "./app/pipe/formatFileNamePipe";
import {FormatStringListPipe} from "./app/pipe/formatStringListPipe";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {importProvidersFrom} from "@angular/core";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

bootstrapApplication(AppComponent, {
    providers: [
        provideAnimationsAsync(),
        provideHttpClient(),
        FormatFileNamePipe,
        FormatStringListPipe,
        importProvidersFrom(TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: createTranslateLoader,
                deps: [HttpClient]
            }
        }))
    ]
}).catch(err => console.error(err));
