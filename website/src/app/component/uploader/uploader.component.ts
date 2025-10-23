import {Component, EventEmitter, Input, Output} from "@angular/core";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {DataService} from "../../service/data.service";
import {MatIconModule} from "@angular/material/icon";

@Component({
	selector: "app-uploader",
	imports: [
		MatProgressSpinnerModule,
		MatIconModule,
	],
	templateUrl: "./uploader.component.html",
	styleUrl: "./uploader.component.css",
})
export class UploaderComponent {
	@Input({required: true}) fileExtensions: string[] = [];
	@Input({required: true}) allowMultiple = false;
	@Input({required: true}) endpoint = "";
	@Input() validation: (fileNames: [string, string][]) => string | undefined = () => undefined;
	@Output() uploaded = new EventEmitter<void>();
	private fileNames?: string[];
	private error?: string;

	constructor(private readonly dataService: DataService) {
	}

	upload() {
		this.fileNames = undefined;
		this.error = undefined;
		const input = document.createElement("input");
		input.type = "file";
		input.accept = this.fileExtensions.map(fileExtension => `.${fileExtension}`).join(",");
		input.multiple = this.allowMultiple;
		input.onchange = () => {
			this.fileNames = undefined;
			this.error = undefined;

			if (!input.files || input.files.length == 0) {
				return;
			}

			const fileNames: [string, string][] = [];
			for (let i = 0; i < input.files.length; i++) {
				const fileNameSplit = input.files[i].name.split(".");
				if (fileNameSplit.length < 2) {
					this.error = "Invalid file name!";
					return;
				} else if (this.fileExtensions.every(fileExtension => fileExtension.toLowerCase() !== fileNameSplit[fileNameSplit.length - 1].toLowerCase())) {
					this.error = "Invalid file type!";
					return;
				} else {
					const newFileNameSplit = [];
					for (let i = 0; i < fileNameSplit.length - 1; i++) {
						newFileNameSplit.push(fileNameSplit[i]);
					}
					fileNames.push([newFileNameSplit.join("."), fileNameSplit[fileNameSplit.length - 1]]);
				}
			}

			if (fileNames.length == 0) {
				this.error = "No file selected!";
				return;
			}

			this.error = this.validation(fileNames);
			if (this.error) {
				return;
			}

			this.fileNames = [];
			fileNames.forEach(([fileName]) => {
				if (!this.fileNames?.includes(fileName)) {
					this.fileNames?.push(fileName);
				}
			});
			this.fileNames.sort();

			const formData = new FormData();
			for (let i = 0; i < input.files.length; i++) {
				formData.append("file", input.files[i]);
			}

			this.dataService.sendPostRequest(this.endpoint, formData, undefined, () => {
				this.fileNames = undefined;
				this.uploaded.emit();
			});
		};
		input.click();
	}

	getFileNames() {
		return this.fileNames;
	}

	getError() {
		return this.error;
	}
}
