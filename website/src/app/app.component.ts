import {Component} from "@angular/core";
import {MatStepperModule} from "@angular/material/stepper";
import {PrepareComponent} from "./component/prepare/prepare.component";
import {FormsModule} from "@angular/forms";
import {DataService} from "./service/data.service";
import {EditComponent} from "./component/edit/edit.component";
import {PreviewComponent} from "./component/preview/preview.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {ExportComponent} from "./component/export/export.component";
import {LargeTileComponent} from "./component/large-button/large-tile.component";

@Component({
	selector: "app-root",
	imports: [
		MatStepperModule,
		MatProgressSpinnerModule,
		MatButtonModule,
		MatIconModule,
		PrepareComponent,
		FormsModule,
		EditComponent,
		PreviewComponent,
		ExportComponent,
		LargeTileComponent,
	],
	templateUrl: "./app.component.html",
	styleUrls: ["./app.component.css"],
})
export class AppComponent {

	constructor(private dataService: DataService) {
	}

	hasData() {
		return this.dataService.hasData();
	}

	getStatus() {
		return this.dataService.getStatus();
	}

	retry() {
		document.location.reload();
	}
}
