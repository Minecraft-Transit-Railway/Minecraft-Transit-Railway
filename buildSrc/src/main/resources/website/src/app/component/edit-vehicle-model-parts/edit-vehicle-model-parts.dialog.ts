import {Component, inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DataService} from "../../service/data.service";
import {MatListModule} from "@angular/material/list";
import {VehicleModelWrapper} from "../../entity/generated/vehicleModelWrapper";

@Component({
	standalone: true,
	imports: [
		MatDialogModule,
		MatButtonModule,
		MatInputModule,
		MatIconModule,
		MatListModule,
		MatTooltipModule,

	],
	templateUrl: "edit-vehicle-model-parts.dialog.html",
	styleUrl: "edit-vehicle-model-parts.dialog.css",
})
export class EditVehicleModelPartsDialog {
	private readonly dialogRef = inject(MatDialogRef<EditVehicleModelPartsDialog>);
	private readonly model = inject<VehicleModelWrapper>(MAT_DIALOG_DATA);
	protected readonly modelParts: string[] = [];
	protected selectedModelPartIndex = -1;

	constructor(private readonly dataService: DataService) {
		console.log(this.model);
	}

	selectModelPart(selectedIndex: number) {
		this.selectedModelPartIndex = selectedIndex;
	}

	onClose() {
		this.dialogRef.close();
	}
}
