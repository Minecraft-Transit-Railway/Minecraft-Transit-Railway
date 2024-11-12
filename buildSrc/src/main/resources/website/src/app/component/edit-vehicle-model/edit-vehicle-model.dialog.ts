import {Component, inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {MatTooltipModule} from "@angular/material/tooltip";
import {SoundComponent} from "../sound/sound.component";
import {DataService} from "../../service/data.service";
import {VehicleModel} from "../../entity/generated/vehicleModel";
import {FormatStringListPipe} from "../../pipe/formatStringListPipe";
import {FormatFileNamePipe} from "../../pipe/formatFileNamePipe";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatListModule, MatListOption} from "@angular/material/list";

@Component({
	standalone: true,
	imports: [
		MatDialogModule,
		MatButtonModule,
		MatInputModule,
		MatAutocompleteModule,
		ReactiveFormsModule,
		MatIconModule,
		MatListModule,
		MatTooltipModule,
		SoundComponent,
		FormatStringListPipe,
		FormatFileNamePipe,
	],
	templateUrl: "edit-vehicle-model.dialog.html",
	styleUrl: "edit-vehicle-model.dialog.css",
})
export class EditVehicleModelDialog {
	private readonly dialogRef = inject(MatDialogRef<EditVehicleModelDialog>);
	private readonly vehicleModel = inject<VehicleModel>(MAT_DIALOG_DATA);
	private readonly customModelList: { [key: string]: number } = {};
	private readonly minecraftModelList: { [key: string]: number } = {};
	private readonly customTextureList: { [key: string]: number } = {};
	private readonly minecraftTextureList: { [key: string]: number } = {};
	protected readonly filteredCustomModelList: string[] = [];
	protected readonly filteredMinecraftModelList: string[] = [];
	protected readonly filteredCustomTextureList: string[] = [];
	protected readonly filteredMinecraftTextureList: string[] = [];
	protected readonly modelParts: string[] = [];
	protected readonly formGroup;

	constructor(private readonly dataService: DataService, private readonly formatFileNamePipe: FormatFileNamePipe, private readonly formatStringListPipe: FormatStringListPipe) {
		let modelKey = "";

		for (let i = 0; i < dataService.models().length; i++) {
			const {id} = dataService.models()[i];
			const key = formatFileNamePipe.transform(id);
			if (id === this.vehicleModel.modelResource) {
				modelKey = key;
			}
			this.customModelList[key] = i;
		}

		for (let i = 0; i < dataService.minecraftModelResources().length; i++) {
			const {modelResource, modelPropertiesResource, positionDefinitionsResource} = dataService.minecraftModelResources()[i];
			if (modelResource && modelPropertiesResource && positionDefinitionsResource) {
				const key = formatStringListPipe.transform([formatFileNamePipe.transform(modelResource), formatFileNamePipe.transform(modelPropertiesResource), formatFileNamePipe.transform(positionDefinitionsResource)], " + ");
				if (modelResource === this.vehicleModel.modelResource && modelPropertiesResource === this.vehicleModel.modelPropertiesResource && positionDefinitionsResource === this.vehicleModel.positionDefinitionsResource) {
					modelKey = key;
				}
				this.minecraftModelList[key] = i;
			}
		}

		let textureKey = "";

		for (let i = 0; i < dataService.textures().length; i++) {
			const texture = dataService.textures()[i];
			const key = formatFileNamePipe.transform(texture);
			if (texture === this.vehicleModel.textureResource) {
				textureKey = key;
			}
			this.customTextureList[key] = i;
		}

		for (let i = 0; i < dataService.minecraftTextureResources().length; i++) {
			const texture = dataService.minecraftTextureResources()[i];
			const key = formatFileNamePipe.transform(texture);
			if (texture === this.vehicleModel.textureResource) {
				textureKey = key;
			}
			this.minecraftTextureList[key] = i;
		}

		this.formGroup = new FormGroup({
			modelResource: new FormControl(modelKey),
			textureResource: new FormControl(textureKey),
		});

		this.updateModelParts();
	}

	filterModels(text: string) {
		EditVehicleModelDialog.filterAndSort(text, this.customModelList, this.filteredCustomModelList);
		EditVehicleModelDialog.filterAndSort(text, this.minecraftModelList, this.filteredMinecraftModelList);
		this.updateModelParts();
	}

	filterTextures(text: string) {
		EditVehicleModelDialog.filterAndSort(text, this.customTextureList, this.filteredCustomTextureList);
		EditVehicleModelDialog.filterAndSort(text, this.minecraftTextureList, this.filteredMinecraftTextureList);
	}

	selectModelPart(selectedOptions: MatListOption[]) {
		const index = parseInt(selectedOptions[0]?.value);
		console.log(index);
	}

	onSave() {
		const newData = this.formGroup.getRawValue();

		const modelIndex1 = newData.modelResource ? this.customModelList[newData.modelResource] : undefined;
		const modelIndex2 = newData.modelResource ? this.minecraftModelList[newData.modelResource] : undefined;
		if (modelIndex1 !== undefined) {
			const {id} = this.dataService.models()[modelIndex1];
			this.vehicleModel.modelResource = id;
			this.vehicleModel.modelPropertiesResource = "";
			this.vehicleModel.positionDefinitionsResource = "";
		} else if (modelIndex2 !== undefined) {
			const {modelResource, modelPropertiesResource, positionDefinitionsResource} = this.dataService.minecraftModelResources()[modelIndex2];
			this.vehicleModel.modelResource = modelResource;
			this.vehicleModel.modelPropertiesResource = modelPropertiesResource;
			this.vehicleModel.positionDefinitionsResource = positionDefinitionsResource;
		} else {
			this.vehicleModel.modelResource = "";
			this.vehicleModel.modelPropertiesResource = "";
			this.vehicleModel.positionDefinitionsResource = "";
		}

		const textureIndex1 = newData.textureResource ? this.customTextureList[newData.textureResource] : undefined;
		const textureIndex2 = newData.textureResource ? this.minecraftTextureList[newData.textureResource] : undefined;
		if (textureIndex1) {
			this.vehicleModel.textureResource = this.dataService.textures()[textureIndex1];
		} else if (textureIndex2) {
			this.vehicleModel.textureResource = this.dataService.minecraftTextureResources()[textureIndex2];
		} else {
			this.vehicleModel.textureResource = "";
		}

		this.dataService.update();
		this.dialogRef.close();
	}

	onCancel() {
		this.dialogRef.close();
	}

	private updateModelParts() {
		this.modelParts.length = 0;
		const modelResource = this.formGroup.getRawValue().modelResource;
		const modelIndex = modelResource ? this.customModelList[modelResource] : undefined;
		if (modelIndex !== undefined) {
			const {id} = this.dataService.models()[modelIndex];
			this.dataService.models().find(model => model.id == id)?.modelParts.forEach(modelPart => this.modelParts.push(modelPart));
		}
	}

	private static filterAndSort(text: string, inputList: { [key: string]: number }, outputList: string[]) {
		outputList.length = 0;
		Object.keys(inputList).forEach(value => {
			if (value.toLowerCase().includes(text.toLowerCase())) {
				outputList.push(value);
			}
		});
		outputList.sort();
	}
}
