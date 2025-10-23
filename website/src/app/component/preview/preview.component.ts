import {Component} from "@angular/core";
import {MatIconModule} from "@angular/material/icon";
import {DataService} from "../../service/data.service";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatButtonModule} from "@angular/material/button";

@Component({
	selector: "app-preview",
	imports: [
		MatButtonModule,
		MatSlideToggleModule,
		MatIconModule,
	],
	templateUrl: "./preview.component.html",
	styleUrl: "./preview.component.css",
})
export class PreviewComponent {

	constructor(private readonly dataService: DataService) {
	}

	toggleDoors(openDoors: boolean) {
		this.dataService.preview(openDoors);
	}

	resumeGame() {
		this.dataService.resumeGame();
	}

	reload() {
		this.dataService.reload();
	}
}
