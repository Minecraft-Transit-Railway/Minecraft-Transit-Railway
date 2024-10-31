import {Component} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {DataService, ICONS} from "../../service/data.service";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {UploaderComponent} from "../uploader/uploader.component";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTooltipModule} from "@angular/material/tooltip";
import {VehicleResource} from "../../entity/generated/vehicleResource";

@Component({
	selector: "app-edit",
	standalone: true,
	imports: [
		MatButtonModule,
		MatIconModule,
		MatFormField,
		MatInput,
		MatLabel,
		MatExpansionModule,
		UploaderComponent,
		MatTooltipModule,
	],
	templateUrl: "./edit.component.html",
	styleUrl: "./edit.component.css",
})
export class EditComponent {
	private editingIndex = -1;

	constructor(protected dataService: DataService) {
	}

	addVehicle() {
		const vehicles = this.dataService.vehicles();
		vehicles.push(new VehicleResource("vehicle", "Untitled", "000000", "TRAIN", 25, 2, -8.5, 8.5, 0, 0, "My Custom Vehicle", "", false, false, false, false, "", "", 0, false, false, "", 0));
		this.editingIndex = vehicles.length - 1;
	}

	moveVehicle(movePositions: number) {
		const vehicles = this.dataService.vehicles();
		const newPosition = movePositions < -1 ? 0 : movePositions > 1 ? vehicles.length - 1 : Math.max(0, Math.min(vehicles.length - 1, this.editingIndex + movePositions));
		if (this.editingIndex != newPosition) {
			const vehicleResourceToMove = vehicles[this.editingIndex];
			vehicles.splice(this.editingIndex, 1);
			vehicles.splice(newPosition, 0, vehicleResourceToMove);
			this.editAtIndex(newPosition);
		}
	}

	duplicateVehicle() {
		const vehicles = this.dataService.vehicles();
		const newVehicleResource: VehicleResource = JSON.parse(JSON.stringify(vehicles[this.editingIndex]));
		newVehicleResource.name = `${newVehicleResource.name} (Copy)`;
		vehicles.splice(this.editingIndex + 1, 0, newVehicleResource);
	}

	deleteVehicle() {
		this.dataService.vehicles().splice(this.editingIndex, 1);
		this.editAtIndex(-1);
	}

	editAtIndex(index: number) {
		this.editingIndex = index;
	}

	isEditingAtIndex(index: number) {
		return this.editingIndex == index;
	}

	getIcon(transportMode: "TRAIN" | "BOAT" | "CABLE_CAR" | "AIRPLANE") {
		return ICONS[transportMode];
	}
}
