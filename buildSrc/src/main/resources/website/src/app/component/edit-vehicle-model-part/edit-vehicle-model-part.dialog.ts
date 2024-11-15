import {Component, inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DataService} from "../../service/data.service";
import {MatIconModule} from "@angular/material/icon";
import {ModelPropertiesPartWrapper} from "../../entity/generated/modelPropertiesPartWrapper";

@Component({
	standalone: true,
	imports: [
		MatDialogModule,
		MatButtonModule,
		MatIconModule,
		MatTooltipModule,
	],
	templateUrl: "edit-vehicle-model-part.dialog.html",
	styleUrl: "edit-vehicle-model-part.dialog.css",
})
export class EditVehicleModelPartDialog {
	private readonly dialogRef = inject(MatDialogRef<EditVehicleModelPartDialog>);
	private readonly modelPropertiesPart = inject<ModelPropertiesPartWrapper>(MAT_DIALOG_DATA);

	constructor(private readonly dataService: DataService) {
		console.log(this.modelPropertiesPart);
	}

	onSave() {
		this.dialogRef.close();
	}

	onCancel() {
		this.dialogRef.close();
	}
}
