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
import {VehicleResourceWrapperDTO} from "../../entity/generated/vehicleResourceWrapper";

@Component({
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
	private readonly vehicleResourceWrapperDTO = inject<VehicleResourceWrapperDTO>(MAT_DIALOG_DATA);
	protected readonly formGroup;

	constructor(private readonly dataService: DataService) {
		this.formGroup = new FormGroup({
			id: new FormControl(this.vehicleResourceWrapperDTO.id),
			name: new FormControl(this.vehicleResourceWrapperDTO.name),
			color: new FormControl(`#${this.vehicleResourceWrapperDTO.color}`),
			transportMode: new FormControl(this.vehicleResourceWrapperDTO.transportMode),
			description: new FormControl(this.vehicleResourceWrapperDTO.description),
			wikipediaArticle: new FormControl(this.vehicleResourceWrapperDTO.wikipediaArticle),
			length: new FormControl(this.vehicleResourceWrapperDTO.length),
			width: new FormControl(this.vehicleResourceWrapperDTO.width),
			hasTwoBogies: new FormControl(this.vehicleResourceWrapperDTO.bogie1Position !== this.vehicleResourceWrapperDTO.bogie2Position),
			bogiePosition: new FormControl(this.vehicleResourceWrapperDTO.bogie1Position),
			bogie1Position: new FormControl(this.vehicleResourceWrapperDTO.bogie1Position),
			bogie2Position: new FormControl(this.vehicleResourceWrapperDTO.bogie2Position),
			couplingPadding1: new FormControl(this.vehicleResourceWrapperDTO.couplingPadding1),
			couplingPadding2: new FormControl(this.vehicleResourceWrapperDTO.couplingPadding2),
			hasGangway1: new FormControl(this.vehicleResourceWrapperDTO.hasGangway1),
			hasGangway2: new FormControl(this.vehicleResourceWrapperDTO.hasGangway2),
			hasBarrier1: new FormControl(this.vehicleResourceWrapperDTO.hasBarrier1),
			hasBarrier2: new FormControl(this.vehicleResourceWrapperDTO.hasBarrier2),
			soundType: new FormControl(!this.vehicleResourceWrapperDTO.bveSoundBaseResource && (this.vehicleResourceWrapperDTO.legacySpeedSoundBaseResource || this.vehicleResourceWrapperDTO.legacyDoorSoundBaseResource) ? "legacy" : "bve"),
			bveSoundBaseResource: new FormControl(this.vehicleResourceWrapperDTO.bveSoundBaseResource),
			legacySpeedSoundBaseResource: new FormControl(this.vehicleResourceWrapperDTO.legacySpeedSoundBaseResource),
			legacySpeedSoundCount: new FormControl(this.vehicleResourceWrapperDTO.legacySpeedSoundCount),
			legacyUseAccelerationSoundsWhenCoasting: new FormControl(this.vehicleResourceWrapperDTO.legacyUseAccelerationSoundsWhenCoasting),
			legacyConstantPlaybackSpeed: new FormControl(this.vehicleResourceWrapperDTO.legacyConstantPlaybackSpeed),
			legacyDoorSoundBaseResource: new FormControl(this.vehicleResourceWrapperDTO.legacyDoorSoundBaseResource),
			legacyDoorCloseSoundTime: new FormControl(this.vehicleResourceWrapperDTO.legacyDoorCloseSoundTime),
		});
	}

	onSave() {
		const newData = this.formGroup.getRawValue();
		const defaultVehicleResource = CREATE_VEHICLE_RESOURCE();
		this.vehicleResourceWrapperDTO.id = newData.id ?? defaultVehicleResource.id;
		this.vehicleResourceWrapperDTO.name = newData.name ?? defaultVehicleResource.name;
		this.vehicleResourceWrapperDTO.color = newData.color?.substring(1).toUpperCase() ?? defaultVehicleResource.color;
		this.vehicleResourceWrapperDTO.transportMode = newData.transportMode ?? defaultVehicleResource.transportMode;
		this.vehicleResourceWrapperDTO.description = newData.description ?? defaultVehicleResource.description;
		this.vehicleResourceWrapperDTO.wikipediaArticle = newData.wikipediaArticle ?? defaultVehicleResource.wikipediaArticle;
		this.vehicleResourceWrapperDTO.length = Math.max(0.5, newData.length ?? defaultVehicleResource.length);
		this.vehicleResourceWrapperDTO.width = Math.max(0.5, newData.width ?? defaultVehicleResource.width);
		this.vehicleResourceWrapperDTO.bogie1Position = (newData.hasTwoBogies ? newData.bogie1Position : newData.bogiePosition) ?? defaultVehicleResource.bogie1Position;
		this.vehicleResourceWrapperDTO.bogie2Position = (newData.hasTwoBogies ? newData.bogie2Position : newData.bogiePosition) ?? defaultVehicleResource.bogie2Position;
		this.vehicleResourceWrapperDTO.couplingPadding1 = Math.max(0, newData.couplingPadding1 ?? defaultVehicleResource.couplingPadding1);
		this.vehicleResourceWrapperDTO.couplingPadding2 = Math.max(0, newData.couplingPadding2 ?? defaultVehicleResource.couplingPadding2);
		this.vehicleResourceWrapperDTO.hasGangway1 = newData.hasGangway1 ?? defaultVehicleResource.hasGangway1;
		this.vehicleResourceWrapperDTO.hasGangway2 = newData.hasGangway2 ?? defaultVehicleResource.hasGangway2;
		this.vehicleResourceWrapperDTO.hasBarrier1 = newData.hasBarrier1 ?? defaultVehicleResource.hasBarrier1;
		this.vehicleResourceWrapperDTO.hasBarrier2 = newData.hasBarrier2 ?? defaultVehicleResource.hasBarrier2;
		this.vehicleResourceWrapperDTO.bveSoundBaseResource = (newData.soundType === "bve" ? newData.bveSoundBaseResource : "") ?? defaultVehicleResource.bveSoundBaseResource;
		this.vehicleResourceWrapperDTO.legacySpeedSoundBaseResource = (newData.soundType === "bve" ? "" : newData.legacySpeedSoundBaseResource) ?? defaultVehicleResource.legacySpeedSoundBaseResource;
		this.vehicleResourceWrapperDTO.legacySpeedSoundCount = Math.max(1, Math.round((newData.soundType === "bve" ? 0 : newData.legacySpeedSoundCount) ?? defaultVehicleResource.legacySpeedSoundCount));
		this.vehicleResourceWrapperDTO.legacyUseAccelerationSoundsWhenCoasting = (newData.soundType === "bve" ? false : newData.legacyUseAccelerationSoundsWhenCoasting) ?? defaultVehicleResource.legacyUseAccelerationSoundsWhenCoasting;
		this.vehicleResourceWrapperDTO.legacyConstantPlaybackSpeed = (newData.soundType === "bve" ? false : newData.legacyConstantPlaybackSpeed) ?? defaultVehicleResource.legacyConstantPlaybackSpeed;
		this.vehicleResourceWrapperDTO.legacyDoorSoundBaseResource = (newData.soundType === "bve" ? "" : newData.legacyDoorSoundBaseResource) ?? defaultVehicleResource.legacyDoorSoundBaseResource;
		this.vehicleResourceWrapperDTO.legacyDoorCloseSoundTime = Math.max(0, Math.min(1, (newData.soundType === "bve" ? 0 : newData.legacyDoorCloseSoundTime) ?? defaultVehicleResource.legacyDoorCloseSoundTime));
		this.dataService.update(this.vehicleResourceWrapperDTO.id);
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
