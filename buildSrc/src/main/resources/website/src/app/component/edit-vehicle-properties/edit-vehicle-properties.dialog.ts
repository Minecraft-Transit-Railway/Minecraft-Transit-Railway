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
import {DataService} from "../../service/data.service";
import {CREATE_VEHICLE_RESOURCE} from "../edit/edit.component";
import {VehicleResourceWrapper} from "../../entity/generated/vehicleResourceWrapper";

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
	private readonly vehicleResource = inject<VehicleResourceWrapper>(MAT_DIALOG_DATA);
	protected readonly formGroup;

	constructor(private readonly dataService: DataService) {
		this.formGroup = new FormGroup({
			id: new FormControl(this.vehicleResource.id),
			name: new FormControl(this.vehicleResource.name),
			color: new FormControl(`#${this.vehicleResource.color}`),
			transportMode: new FormControl(this.vehicleResource.transportMode),
			description: new FormControl(this.vehicleResource.description),
			wikipediaArticle: new FormControl(this.vehicleResource.wikipediaArticle),
			length: new FormControl(this.vehicleResource.length),
			width: new FormControl(this.vehicleResource.width),
			hasTwoBogies: new FormControl(this.vehicleResource.bogie1Position !== this.vehicleResource.bogie2Position),
			bogiePosition: new FormControl(this.vehicleResource.bogie1Position),
			bogie1Position: new FormControl(this.vehicleResource.bogie1Position),
			bogie2Position: new FormControl(this.vehicleResource.bogie2Position),
			couplingPadding1: new FormControl(this.vehicleResource.couplingPadding1),
			couplingPadding2: new FormControl(this.vehicleResource.couplingPadding2),
			hasGangway1: new FormControl(this.vehicleResource.hasGangway1),
			hasGangway2: new FormControl(this.vehicleResource.hasGangway2),
			hasBarrier1: new FormControl(this.vehicleResource.hasBarrier1),
			hasBarrier2: new FormControl(this.vehicleResource.hasBarrier2),
			soundType: new FormControl(!this.vehicleResource.bveSoundBaseResource && (this.vehicleResource.legacySpeedSoundBaseResource || this.vehicleResource.legacyDoorSoundBaseResource) ? "legacy" : "bve"),
			bveSoundBaseResource: new FormControl(this.vehicleResource.bveSoundBaseResource),
			legacySpeedSoundBaseResource: new FormControl(this.vehicleResource.legacySpeedSoundBaseResource),
			legacySpeedSoundCount: new FormControl(this.vehicleResource.legacySpeedSoundCount),
			legacyUseAccelerationSoundsWhenCoasting: new FormControl(this.vehicleResource.legacyUseAccelerationSoundsWhenCoasting),
			legacyConstantPlaybackSpeed: new FormControl(this.vehicleResource.legacyConstantPlaybackSpeed),
			legacyDoorSoundBaseResource: new FormControl(this.vehicleResource.legacyDoorSoundBaseResource),
			legacyDoorCloseSoundTime: new FormControl(this.vehicleResource.legacyDoorCloseSoundTime),
		});
	}

	onSave() {
		const newData = this.formGroup.getRawValue();
		const defaultVehicleResource = CREATE_VEHICLE_RESOURCE();
		this.vehicleResource.id = newData.id ?? defaultVehicleResource.id;
		this.vehicleResource.name = newData.name ?? defaultVehicleResource.name;
		this.vehicleResource.color = newData.color?.substring(1).toUpperCase() ?? defaultVehicleResource.color;
		this.vehicleResource.transportMode = newData.transportMode ?? defaultVehicleResource.transportMode;
		this.vehicleResource.description = newData.description ?? defaultVehicleResource.description;
		this.vehicleResource.wikipediaArticle = newData.wikipediaArticle ?? defaultVehicleResource.wikipediaArticle;
		this.vehicleResource.length = Math.max(0.5, newData.length ?? defaultVehicleResource.length);
		this.vehicleResource.width = Math.max(0.5, newData.width ?? defaultVehicleResource.width);
		this.vehicleResource.bogie1Position = (newData.hasTwoBogies ? newData.bogie1Position : newData.bogiePosition) ?? defaultVehicleResource.bogie1Position;
		this.vehicleResource.bogie2Position = (newData.hasTwoBogies ? newData.bogie2Position : newData.bogiePosition) ?? defaultVehicleResource.bogie2Position;
		this.vehicleResource.couplingPadding1 = Math.max(0, newData.couplingPadding1 ?? defaultVehicleResource.couplingPadding1);
		this.vehicleResource.couplingPadding2 = Math.max(0, newData.couplingPadding2 ?? defaultVehicleResource.couplingPadding2);
		this.vehicleResource.hasGangway1 = newData.hasGangway1 ?? defaultVehicleResource.hasGangway1;
		this.vehicleResource.hasGangway2 = newData.hasGangway2 ?? defaultVehicleResource.hasGangway2;
		this.vehicleResource.hasBarrier1 = newData.hasBarrier1 ?? defaultVehicleResource.hasBarrier1;
		this.vehicleResource.hasBarrier2 = newData.hasBarrier2 ?? defaultVehicleResource.hasBarrier2;
		this.vehicleResource.bveSoundBaseResource = (newData.soundType === "bve" ? newData.bveSoundBaseResource : "") ?? defaultVehicleResource.bveSoundBaseResource;
		this.vehicleResource.legacySpeedSoundBaseResource = (newData.soundType === "bve" ? "" : newData.legacySpeedSoundBaseResource) ?? defaultVehicleResource.legacySpeedSoundBaseResource;
		this.vehicleResource.legacySpeedSoundCount = Math.max(1, Math.round((newData.soundType === "bve" ? 0 : newData.legacySpeedSoundCount) ?? defaultVehicleResource.legacySpeedSoundCount));
		this.vehicleResource.legacyUseAccelerationSoundsWhenCoasting = (newData.soundType === "bve" ? false : newData.legacyUseAccelerationSoundsWhenCoasting) ?? defaultVehicleResource.legacyUseAccelerationSoundsWhenCoasting;
		this.vehicleResource.legacyConstantPlaybackSpeed = (newData.soundType === "bve" ? false : newData.legacyConstantPlaybackSpeed) ?? defaultVehicleResource.legacyConstantPlaybackSpeed;
		this.vehicleResource.legacyDoorSoundBaseResource = (newData.soundType === "bve" ? "" : newData.legacyDoorSoundBaseResource) ?? defaultVehicleResource.legacyDoorSoundBaseResource;
		this.vehicleResource.legacyDoorCloseSoundTime = Math.max(0, Math.min(1, (newData.soundType === "bve" ? 0 : newData.legacyDoorCloseSoundTime) ?? defaultVehicleResource.legacyDoorCloseSoundTime));
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
