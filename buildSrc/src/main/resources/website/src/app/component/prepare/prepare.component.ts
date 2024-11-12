import {Component, EventEmitter, inject, Output} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatDialog, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {DataService} from "../../service/data.service";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {UploaderComponent} from "../uploader/uploader.component";
import {ResourceWrapper} from "../../entity/generated/resourceWrapper";

@Component({
	selector: "app-prepare",
	standalone: true,
	imports: [
		MatButtonModule,
		MatIconModule,
		MatFormField,
		MatInput,
		MatLabel,
		UploaderComponent,
	],
	templateUrl: "./prepare.component.html",
	styleUrl: "./prepare.component.css",
})
export class PrepareComponent {
	@Output() nextStep = new EventEmitter<void>();
	private readonly dialog = inject(MatDialog);

	constructor(private dataService: DataService) {
	}

	create() {
		this.dataService.upload(new ResourceWrapper());
		this.dataService.update();
		setTimeout(() => this.nextStep.emit(), 0);
	}

	upload(data: ResourceWrapper) {
		this.dataService.upload(data);
		setTimeout(() => this.nextStep.emit(), 0);
	}

	openResetDialog() {
		this.dialog.open(ResetDialog);
	}

	hasData() {
		return this.dataService.hasData();
	}
}

@Component({
	standalone: true,
	imports: [
		MatDialogModule,
		MatButtonModule,
	],
	templateUrl: "reset.dialog.html",
})
export class ResetDialog {
	private readonly dialogRef = inject(MatDialogRef<ResetDialog>);

	constructor(private dataService: DataService) {
	}

	onCancel() {
		this.dialogRef.close();
	}

	onReset() {
		this.dataService.reset();
		this.dialogRef.close();
	}
}
