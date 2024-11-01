import {Component, inject} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {DataService, ICONS} from "../../service/data.service";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {UploaderComponent} from "../uploader/uploader.component";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTooltipModule} from "@angular/material/tooltip";
import {VehicleResource} from "../../entity/generated/vehicleResource";
import {MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatRadioModule} from "@angular/material/radio";
import {SoundComponent} from "../sound/sound.component";
import {InfoComponent} from "../info/info.component";

const CREATE_VEHICLE = () => new VehicleResource("my_vehicle", "My Custom Vehicle", Math.floor(Math.random() * 0xFFFFFF).toString(16).toUpperCase().padStart(6, "0"), "TRAIN", 25, 2, -8.5, 8.5, 0, 0, "This is my custom vehicle!", "", false, false, false, false, "a_train", "", 0, false, false, "", 0);

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
	private readonly dialog = inject(MatDialog);
	private editingIndex = -1;

	constructor(protected readonly dataService: DataService) {
	}

	addVehicle(content: HTMLDivElement) {
		const vehicles = this.dataService.vehicles();
		vehicles.push(CREATE_VEHICLE());
		this.editAtIndex(vehicles.length - 1, content);
	}

	moveVehicle(content: HTMLDivElement, movePositions: number) {
		const vehicles = this.dataService.vehicles();
		const newPosition = movePositions < -1 ? 0 : movePositions > 1 ? vehicles.length - 1 : Math.max(0, Math.min(vehicles.length - 1, this.editingIndex + movePositions));
		if (this.editingIndex != newPosition) {
			const vehicleResourceToMove = vehicles[this.editingIndex];
			vehicles.splice(this.editingIndex, 1);
			vehicles.splice(newPosition, 0, vehicleResourceToMove);
			this.editAtIndex(newPosition, content);
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

	editDetails() {
		this.dialog.open(PropertiesDialog, {data: this.editingIndex});
	}

	editAtIndex(index: number, content?: HTMLDivElement) {
		this.editingIndex = index;
		if (content) {
			const target = index * 48;
			setTimeout(() => content.scrollTo({top: target, behavior: "smooth"}), content.scrollTop < target ? 200 : 0);
		}
	}

	isEditingAtIndex(index: number) {
		return this.editingIndex == index;
	}

	getIcon(transportMode: "TRAIN" | "BOAT" | "CABLE_CAR" | "AIRPLANE") {
		return ICONS[transportMode];
	}
}

@Component({
	selector: "dialog-properties",
	templateUrl: "properties.dialog.html",
	styleUrl: "edit.component.css",
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
		InfoComponent,
	],
})
export class PropertiesDialog {
	private readonly dialogRef = inject(MatDialogRef<PropertiesDialog>);
	private readonly editingIndex = inject<number>(MAT_DIALOG_DATA);
	protected readonly formGroup;

	constructor(private readonly dataService: DataService) {
		const vehicle = dataService.vehicles()[this.editingIndex];
		this.formGroup = new FormGroup({
			id: new FormControl(vehicle.id),
			name: new FormControl(vehicle.name),
			color: new FormControl(`#${vehicle.color}`),
			transportMode: new FormControl(vehicle.transportMode),
			description: new FormControl(vehicle.description),
			wikipediaArticle: new FormControl(vehicle.wikipediaArticle),
			length: new FormControl(vehicle.length),
			width: new FormControl(vehicle.width),
			hasTwoBogies: new FormControl(vehicle.bogie1Position !== vehicle.bogie2Position),
			bogiePosition: new FormControl(vehicle.bogie1Position),
			bogie1Position: new FormControl(vehicle.bogie1Position),
			bogie2Position: new FormControl(vehicle.bogie2Position),
			couplingPadding1: new FormControl(vehicle.couplingPadding1),
			couplingPadding2: new FormControl(vehicle.couplingPadding2),
			hasGangway1: new FormControl(vehicle.hasGangway1),
			hasGangway2: new FormControl(vehicle.hasGangway2),
			hasBarrier1: new FormControl(vehicle.hasBarrier1),
			hasBarrier2: new FormControl(vehicle.hasBarrier2),
			soundType: new FormControl(!vehicle.bveSoundBaseResource && (vehicle.legacySpeedSoundBaseResource || vehicle.legacyDoorSoundBaseResource) ? "legacy" : "bve"),
			bveSoundBaseResource: new FormControl(vehicle.bveSoundBaseResource),
			legacySpeedSoundBaseResource: new FormControl(vehicle.legacySpeedSoundBaseResource),
			legacySpeedSoundCount: new FormControl(vehicle.legacySpeedSoundCount),
			legacyUseAccelerationSoundsWhenCoasting: new FormControl(vehicle.legacyUseAccelerationSoundsWhenCoasting),
			legacyConstantPlaybackSpeed: new FormControl(vehicle.legacyConstantPlaybackSpeed),
			legacyDoorSoundBaseResource: new FormControl(vehicle.legacyDoorSoundBaseResource),
			legacyDoorCloseSoundTime: new FormControl(vehicle.legacyDoorCloseSoundTime),
		});
	}

	onSave() {
		const vehicle = this.dataService.vehicles()[this.editingIndex];
		const newData = this.formGroup.getRawValue();
		const defaultVehicle = CREATE_VEHICLE();
		vehicle.id = newData.id ?? defaultVehicle.id;
		vehicle.name = newData.name ?? defaultVehicle.name;
		vehicle.color = newData.color?.substring(1).toUpperCase() ?? defaultVehicle.color;
		vehicle.transportMode = newData.transportMode ?? defaultVehicle.transportMode;
		vehicle.description = newData.description ?? defaultVehicle.description;
		vehicle.wikipediaArticle = newData.wikipediaArticle ?? defaultVehicle.wikipediaArticle;
		vehicle.length = Math.max(0.5, newData.length ?? defaultVehicle.length);
		vehicle.width = Math.max(0.5, newData.width ?? defaultVehicle.width);
		vehicle.bogie1Position = (newData.hasTwoBogies ? newData.bogie1Position : newData.bogiePosition) ?? defaultVehicle.bogie1Position;
		vehicle.bogie2Position = (newData.hasTwoBogies ? newData.bogie2Position : newData.bogiePosition) ?? defaultVehicle.bogie2Position;
		vehicle.couplingPadding1 = Math.max(0, newData.couplingPadding1 ?? defaultVehicle.couplingPadding1);
		vehicle.couplingPadding2 = Math.max(0, newData.couplingPadding2 ?? defaultVehicle.couplingPadding2);
		vehicle.hasGangway1 = newData.hasGangway1 ?? defaultVehicle.hasGangway1;
		vehicle.hasGangway2 = newData.hasGangway2 ?? defaultVehicle.hasGangway2;
		vehicle.hasBarrier1 = newData.hasBarrier1 ?? defaultVehicle.hasBarrier1;
		vehicle.hasBarrier2 = newData.hasBarrier2 ?? defaultVehicle.hasBarrier2;
		vehicle.bveSoundBaseResource = (newData.soundType === "bve" ? newData.bveSoundBaseResource : "") ?? defaultVehicle.bveSoundBaseResource;
		vehicle.legacySpeedSoundBaseResource = (newData.soundType === "bve" ? "" : newData.legacySpeedSoundBaseResource) ?? defaultVehicle.legacySpeedSoundBaseResource;
		vehicle.legacySpeedSoundCount = Math.max(1, Math.round((newData.soundType === "bve" ? 0 : newData.legacySpeedSoundCount) ?? defaultVehicle.legacySpeedSoundCount));
		vehicle.legacyUseAccelerationSoundsWhenCoasting = (newData.soundType === "bve" ? false : newData.legacyUseAccelerationSoundsWhenCoasting) ?? defaultVehicle.legacyUseAccelerationSoundsWhenCoasting;
		vehicle.legacyConstantPlaybackSpeed = (newData.soundType === "bve" ? false : newData.legacyConstantPlaybackSpeed) ?? defaultVehicle.legacyConstantPlaybackSpeed;
		vehicle.legacyDoorSoundBaseResource = (newData.soundType === "bve" ? "" : newData.legacyDoorSoundBaseResource) ?? defaultVehicle.legacyDoorSoundBaseResource;
		vehicle.legacyDoorCloseSoundTime = Math.max(0, Math.min(1, (newData.soundType === "bve" ? 0 : newData.legacyDoorCloseSoundTime) ?? defaultVehicle.legacyDoorCloseSoundTime));
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
