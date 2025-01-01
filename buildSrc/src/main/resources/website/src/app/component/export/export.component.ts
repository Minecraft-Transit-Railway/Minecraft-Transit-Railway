import {Component} from "@angular/core";
import {MatIconModule} from "@angular/material/icon";
import {DataService} from "../../service/data.service";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {LargeTileComponent} from "../large-button/large-tile.component";

@Component({
	selector: "app-export",
	imports: [
		MatButtonModule,
		MatInputModule,
		MatIconModule,
		ReactiveFormsModule,
		LargeTileComponent,
	],
	templateUrl: "./export.component.html",
	styleUrl: "./export.component.css",
})
export class ExportComponent {

	constructor(private readonly dataService: DataService) {
	}

	export(name: string, description: string) {
		this.dataService.export(name, description);
	}

	getExportDirectory() {
		return this.dataService.getExportDirectory();
	}
}
