import {Component} from "@angular/core";
import {MatStepperModule} from "@angular/material/stepper";
import {PrepareComponent} from "./component/prepare/prepare.component";
import {FormsModule} from "@angular/forms";
import {DataService} from "./service/data.service";
import {EditComponent} from "./component/edit/edit.component";

@Component({
	selector: "app-root",
	standalone: true,
	imports: [
		MatStepperModule,
		PrepareComponent,
		FormsModule,
		EditComponent,
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
}
