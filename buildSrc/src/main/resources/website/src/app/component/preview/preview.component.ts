import {Component} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {DataService} from "../../service/data.service";

@Component({
	selector: "app-preview",
	standalone: true,
	imports: [
		MatButtonModule,
		MatIconModule,
	],
	templateUrl: "./preview.component.html",
	styleUrl: "./preview.component.css",
})
export class PreviewComponent {

	constructor(private dataService: DataService) {
	}

	preview() {
		this.dataService.preview();
	}
}
