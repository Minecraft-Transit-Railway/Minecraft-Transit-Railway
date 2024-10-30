import {Component, EventEmitter, Input, Output} from "@angular/core";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {HttpClient} from "@angular/common/http";
import {catchError, EMPTY} from "rxjs";

@Component({
	selector: "app-uploader",
	standalone: true,
	imports: [
		MatProgressSpinnerModule,
	],
	templateUrl: "./uploader.component.html",
	styleUrl: "./uploader.component.css",
})
export class UploaderComponent<T> {
	@Input({required: true}) fileExtension = "";
	@Input({required: true}) endpoint = "";
	@Output() uploaded = new EventEmitter<T>();
	private fileName?: string;
	private error?: string;

	constructor(private readonly httpClient: HttpClient) {
	}

	upload() {
		const input = document.createElement("input");
		input.type = "file";
		input.accept = this.fileExtension;
		input.onchange = () => {
			this.fileName = undefined;
			this.error = undefined;
			if (input.files) {
				const file = input.files[0];
				if (file) {
					this.fileName = file.name;
					const formData = new FormData();
					formData.append("file", file);
					this.httpClient.post<T>(UploaderComponent.getUrl(this.endpoint), formData).pipe(catchError(error => {
						this.fileName = undefined;
						this.error = error;
						return EMPTY;
					})).subscribe(data => {
						this.fileName = undefined;
						this.uploaded.emit(data);
					});
				}
			}
		};
		input.click();
	}

	getFileName() {
		return this.fileName;
	}

	public static getUrl(endpoint: string) {
		return `${document.location.origin}${document.location.pathname}mtr/api/creator/${endpoint}`;
	}
}
