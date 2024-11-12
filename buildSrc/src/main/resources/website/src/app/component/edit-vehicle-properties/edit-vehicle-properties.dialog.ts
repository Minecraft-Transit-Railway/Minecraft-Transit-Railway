import {Component, inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatRadioModule} from "@angular/material/radio";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {MatTooltipModule} from "@angular/material/tooltip";
import {SoundComponent} from "../sound/sound.component";
import {VehicleWrapper} from "../../entity/generated/vehicleWrapper";
import {DataService} from "../../service/data.service";
import {CREATE_VEHICLE} from "../edit/edit.component";

@Component({
	standalone: true,
	imports: [
		MatDialogModule,
		MatButtonModule,
		MatInputModule,
		MatSelectModule,
		MatCheckboxModule,
		MatRadioModule,
		ReactiveFormsModule,
		MatIconModule,
		MatTooltipModule,
		SoundComponent,
	],
	templateUrl: "edit-vehicle-properties.dialog.html",
	styleUrl: "edit-vehicle-properties.dialog.css",
})
export class EditVehiclePropertiesDialog {
	private readonly dialogRef = inject(MatDialogRef<EditVehiclePropertiesDialog>);
	private readonly vehicle = inject<VehicleWrapper>(MAT_DIALOG_DATA);
	protected readonly formGroup;

	constructor(private readonly dataService: DataService) {
		const vehicleResource = this.vehicle.vehicleResource;
		this.formGroup = new FormGroup({
			id: new FormControl(vehicleResource.id),
			name: new FormControl(vehicleResource.name),
			color: new FormControl(`#${vehicleResource.color}`),
			transportMode: new FormControl(vehicleResource.transportMode),
			description: new FormControl(vehicleResource.description),
			wikipediaArticle: new FormControl(vehicleResource.wikipediaArticle),
			length: new FormControl(vehicleResource.length),
			width: new FormControl(vehicleResource.width),
			hasTwoBogies: new FormControl(vehicleResource.bogie1Position !== vehicleResource.bogie2Position),
			bogiePosition: new FormControl(vehicleResource.bogie1Position),
			bogie1Position: new FormControl(vehicleResource.bogie1Position),
			bogie2Position: new FormControl(vehicleResource.bogie2Position),
			couplingPadding1: new FormControl(vehicleResource.couplingPadding1),
			couplingPadding2: new FormControl(vehicleResource.couplingPadding2),
			hasGangway1: new FormControl(vehicleResource.hasGangway1),
			hasGangway2: new FormControl(vehicleResource.hasGangway2),
			hasBarrier1: new FormControl(vehicleResource.hasBarrier1),
			hasBarrier2: new FormControl(vehicleResource.hasBarrier2),
			soundType: new FormControl(!vehicleResource.bveSoundBaseResource && (vehicleResource.legacySpeedSoundBaseResource || vehicleResource.legacyDoorSoundBaseResource) ? "legacy" : "bve"),
			bveSoundBaseResource: new FormControl(vehicleResource.bveSoundBaseResource),
			legacySpeedSoundBaseResource: new FormControl(vehicleResource.legacySpeedSoundBaseResource),
			legacySpeedSoundCount: new FormControl(vehicleResource.legacySpeedSoundCount),
			legacyUseAccelerationSoundsWhenCoasting: new FormControl(vehicleResource.legacyUseAccelerationSoundsWhenCoasting),
			legacyConstantPlaybackSpeed: new FormControl(vehicleResource.legacyConstantPlaybackSpeed),
			legacyDoorSoundBaseResource: new FormControl(vehicleResource.legacyDoorSoundBaseResource),
			legacyDoorCloseSoundTime: new FormControl(vehicleResource.legacyDoorCloseSoundTime),
		});
	}

	onSave() {
		const vehicleResource = this.vehicle.vehicleResource;
		const newData = this.formGroup.getRawValue();
		const defaultVehicle = CREATE_VEHICLE();
		vehicleResource.id = newData.id ?? defaultVehicle.id;
		vehicleResource.name = newData.name ?? defaultVehicle.name;
		vehicleResource.color = newData.color?.substring(1).toUpperCase() ?? defaultVehicle.color;
		vehicleResource.transportMode = newData.transportMode ?? defaultVehicle.transportMode;
		vehicleResource.description = newData.description ?? defaultVehicle.description;
		vehicleResource.wikipediaArticle = newData.wikipediaArticle ?? defaultVehicle.wikipediaArticle;
		vehicleResource.length = Math.max(0.5, newData.length ?? defaultVehicle.length);
		vehicleResource.width = Math.max(0.5, newData.width ?? defaultVehicle.width);
		vehicleResource.bogie1Position = (newData.hasTwoBogies ? newData.bogie1Position : newData.bogiePosition) ?? defaultVehicle.bogie1Position;
		vehicleResource.bogie2Position = (newData.hasTwoBogies ? newData.bogie2Position : newData.bogiePosition) ?? defaultVehicle.bogie2Position;
		vehicleResource.couplingPadding1 = Math.max(0, newData.couplingPadding1 ?? defaultVehicle.couplingPadding1);
		vehicleResource.couplingPadding2 = Math.max(0, newData.couplingPadding2 ?? defaultVehicle.couplingPadding2);
		vehicleResource.hasGangway1 = newData.hasGangway1 ?? defaultVehicle.hasGangway1;
		vehicleResource.hasGangway2 = newData.hasGangway2 ?? defaultVehicle.hasGangway2;
		vehicleResource.hasBarrier1 = newData.hasBarrier1 ?? defaultVehicle.hasBarrier1;
		vehicleResource.hasBarrier2 = newData.hasBarrier2 ?? defaultVehicle.hasBarrier2;
		vehicleResource.bveSoundBaseResource = (newData.soundType === "bve" ? newData.bveSoundBaseResource : "") ?? defaultVehicle.bveSoundBaseResource;
		vehicleResource.legacySpeedSoundBaseResource = (newData.soundType === "bve" ? "" : newData.legacySpeedSoundBaseResource) ?? defaultVehicle.legacySpeedSoundBaseResource;
		vehicleResource.legacySpeedSoundCount = Math.max(1, Math.round((newData.soundType === "bve" ? 0 : newData.legacySpeedSoundCount) ?? defaultVehicle.legacySpeedSoundCount));
		vehicleResource.legacyUseAccelerationSoundsWhenCoasting = (newData.soundType === "bve" ? false : newData.legacyUseAccelerationSoundsWhenCoasting) ?? defaultVehicle.legacyUseAccelerationSoundsWhenCoasting;
		vehicleResource.legacyConstantPlaybackSpeed = (newData.soundType === "bve" ? false : newData.legacyConstantPlaybackSpeed) ?? defaultVehicle.legacyConstantPlaybackSpeed;
		vehicleResource.legacyDoorSoundBaseResource = (newData.soundType === "bve" ? "" : newData.legacyDoorSoundBaseResource) ?? defaultVehicle.legacyDoorSoundBaseResource;
		vehicleResource.legacyDoorCloseSoundTime = Math.max(0, Math.min(1, (newData.soundType === "bve" ? 0 : newData.legacyDoorCloseSoundTime) ?? defaultVehicle.legacyDoorCloseSoundTime));
		this.dataService.update();
		this.dialogRef.close();
	}

	onCancel() {
		this.dialogRef.close();
	}

	formatWikipediaArticle(wikipediaArticleInput: HTMLInputElement) {
		const formatted = wikipediaArticleInput.value.replace(/https:\/\/\w\w\.wikipedia\.org\/wiki\//, "");
		if (wikipediaArticleInput.value != formatted) {
			wikipediaArticleInput.value = formatted;
		}
	}

	testWikipediaLink(wikipediaArticle: string) {
		if (wikipediaArticle) {
			window.open(`https://en.wikipedia.org/wiki/${wikipediaArticle}`, "_blank");
		}
	}

	getLegacyParameters(mode: string) {
		return `type=legacy&mode=${mode}&speed-sound-count=${this.formGroup.value.legacySpeedSoundCount}` +
			`&use-acceleration-sounds-when-coasting=${this.formGroup.value.legacyUseAccelerationSoundsWhenCoasting}&constant-playback-speed=${this.formGroup.value.legacyConstantPlaybackSpeed}`;
	}
}
