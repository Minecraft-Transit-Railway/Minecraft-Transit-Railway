import {Component, inject} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {DataService} from "../../service/data.service";
import {MatFormField} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {UploaderComponent} from "../uploader/uploader.component";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTooltipModule} from "@angular/material/tooltip";
import {VehicleResource} from "../../entity/generated/vehicleResource";
import {MatDialog} from "@angular/material/dialog";
import {MatSelectModule} from "@angular/material/select";
import {VehicleWrapper} from "../../entity/generated/vehicleWrapper";
import {VehicleModel} from "../../entity/generated/vehicleModel";
import {AccordionComponent} from "../accordion/accordion.component";
import {FormatIconPipe} from "../../pipe/formatIconPipe";
import {ManageResourcesDialog} from "../manage-resources/manage-resources.dialog";
import {EditVehiclePropertiesDialog} from "../edit-vehicle-properties/edit-vehicle-properties.dialog";
import {EditVehicleModelDialog} from "../edit-vehicle-model/edit-vehicle-model.dialog";
import {FormatFileNamePipe} from "../../pipe/formatFileNamePipe";
import {FormatStringListPipe} from "../../pipe/formatStringListPipe";

export const CREATE_VEHICLE = () => new VehicleResource("my_vehicle", "My Custom Vehicle", Math.floor(Math.random() * 0xFFFFFF).toString(16).toUpperCase().padStart(6, "0"), "TRAIN", 25, 2, -8.5, 8.5, 0, 0, "This is my custom vehicle!", "", false, false, false, false, "a_train", "", 0, false, false, "", 0);

@Component({
	selector: "app-edit",
	standalone: true,
	imports: [
		MatButtonModule,
		MatIconModule,
		MatSelectModule,
		MatFormField,
		MatInputModule,
		MatExpansionModule,
		UploaderComponent,
		MatTooltipModule,
		AccordionComponent,
		FormatIconPipe,
		FormatFileNamePipe,
		FormatStringListPipe,
	],
	templateUrl: "./edit.component.html",
	styleUrl: "./edit.component.css",
})
export class EditComponent {
	private readonly dialog = inject(MatDialog);

	constructor(protected readonly dataService: DataService) {
	}

	createVehicleResourceInstance() {
		return new VehicleWrapper(CREATE_VEHICLE());
	}

	createVehicleModelInstance() {
		return new VehicleModel("", "", "", "", false);
	}

	editDetails(vehicle: VehicleResource) {
		this.dialog.open(EditVehiclePropertiesDialog, {data: vehicle});
	}

	editModel(model: VehicleModel) {
		this.dialog.open(EditVehicleModelDialog, {data: model});
	}

	scroll(target: number, content: HTMLDivElement) {
		setTimeout(() => content.scrollTo({top: target, behavior: "smooth"}), content.scrollTop < target ? 200 : 0);
	}

	getAllModels(vehicle: VehicleWrapper) {
		const vehicleResource = vehicle.vehicleResource;
		return vehicleResource.bogie1Position === vehicleResource.bogie2Position ? [vehicleResource.models, vehicleResource.bogie1Models] : [vehicleResource.models, vehicleResource.bogie1Models, vehicleResource.bogie2Models];
	}

	manageModels() {
		this.dialog.open(ManageResourcesDialog, {
			data: {
				title: "Models",
				addText: "Add Model",
				deleteText: "Delete Model",
				fileExtensions: ["bbmodel", "obj", "mtl"],
				listCustomSupplier: () => this.dataService.models().map(modelWrapper => modelWrapper.id),
				listMinecraftSupplier: () => this.dataService.minecraftModelResources().map(minecraftModelResource => minecraftModelResource.modelResource),
			},
		});
	}

	manageTextures() {
		this.dialog.open(ManageResourcesDialog, {
			data: {
				title: "Textures",
				addText: "Add Texture",
				deleteText: "Delete Texture",
				fileExtensions: ["png"],
				listCustomSupplier: () => this.dataService.textures(),
				listMinecraftSupplier: () => this.dataService.minecraftTextureResources(),
			},
		});
	}
}
