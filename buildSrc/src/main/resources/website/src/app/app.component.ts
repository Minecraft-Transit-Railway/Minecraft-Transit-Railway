import {Component} from "@angular/core";
import {MatStepperModule} from "@angular/material/stepper";
import {PrepareComponent} from "./component/prepare/prepare.component";
import {FormsModule} from "@angular/forms";
import {DataService} from "./service/data.service";

@Component({
	selector: "app-root",
	standalone: true,
	imports: [
		MatStepperModule,
		PrepareComponent,
		FormsModule,
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
