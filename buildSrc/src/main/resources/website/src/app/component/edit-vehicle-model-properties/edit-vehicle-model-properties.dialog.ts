import {Component, inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {DataService} from "../../service/data.service";
import {FormatStringListPipe} from "../../pipe/formatStringListPipe";
import {FormatFileNamePipe} from "../../pipe/formatFileNamePipe";
import {AutocompleteComponent} from "../autocomplete/autocomplete.component";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {VehicleModelWrapper} from "../../entity/generated/vehicleModelWrapper";

@Component({
	standalone: true,
	imports: [
		MatDialogModule,
		MatButtonModule,
		MatInputModule,
		ReactiveFormsModule,
		MatIconModule,
		AutocompleteComponent,
		MatCheckboxModule,
	],
	templateUrl: "edit-vehicle-model-properties.dialog.html",
	styleUrl: "edit-vehicle-model-properties.dialog.css",
})
export class EditVehicleModelPropertiesDialog {
	private readonly dialogRef = inject(MatDialogRef<EditVehicleModelPropertiesDialog>);
	private readonly model = inject<VehicleModelWrapper>(MAT_DIALOG_DATA);
	private readonly customModelList: { [key: string]: number };
	private readonly minecraftModelList: { [key: string]: number };
	private readonly customTextureList: { [key: string]: number };
	private readonly minecraftTextureList: { [key: string]: number };
	protected readonly modelLists: { label: string, list: string[] }[] = [];
	protected readonly textureLists: { label: string, list: string[] }[] = [];
	protected readonly formGroup;

	constructor(private readonly dataService: DataService, formatFileNamePipe: FormatFileNamePipe, formatStringListPipe: FormatStringListPipe) {
		let modelKey = "";
		this.customModelList = EditVehicleModelPropertiesDialog.writeList(
			dataService.models(),
			({id}) => formatFileNamePipe.transform(id),
			() => true,
			({id}) => this.model.modelResource === (id),
			key => modelKey = key,
		);
		this.minecraftModelList = EditVehicleModelPropertiesDialog.writeList(
			dataService.minecraftModelResources(),
			({modelResource, modelPropertiesResource, positionDefinitionsResource}) => formatStringListPipe.transform([formatFileNamePipe.transform(modelResource), formatFileNamePipe.transform(modelPropertiesResource), formatFileNamePipe.transform(positionDefinitionsResource)], " + "),
			({modelResource, modelPropertiesResource, positionDefinitionsResource}) => !!modelResource && !!modelPropertiesResource && !!positionDefinitionsResource,
			({modelResource, modelPropertiesResource, positionDefinitionsResource}) => modelResource === this.model.modelResource && modelPropertiesResource === this.model.minecraftModelPropertiesResource && positionDefinitionsResource === this.model.minecraftPositionDefinitionsResource,
			key => modelKey = key,
		);
		this.modelLists.push({label: "Custom", list: Object.keys(this.customModelList)});
		this.modelLists.push({label: "Minecraft", list: Object.keys(this.minecraftModelList)});

		let textureKey = "";
		this.customTextureList = EditVehicleModelPropertiesDialog.writeList(
			dataService.textures(),
			texture => formatFileNamePipe.transform(texture),
			() => true,
			texture => this.model.textureResource === (texture),
			key => textureKey = key,
		);
		this.minecraftTextureList = EditVehicleModelPropertiesDialog.writeList(
			dataService.minecraftTextureResources(),
			texture => formatFileNamePipe.transform(texture),
			() => true,
			texture => this.model.textureResource === (texture),
			key => textureKey = key,
		);
		this.textureLists.push({label: "Custom", list: Object.keys(this.customTextureList)});
		this.textureLists.push({label: "Minecraft", list: Object.keys(this.minecraftTextureList)});

		this.formGroup = new FormGroup({
			modelResource: new FormControl(modelKey),
			textureResource: new FormControl(textureKey),
			flipTextureV: new FormControl(this.model.flipTextureV),
		});
	}

	onSave() {
		const newData = this.formGroup.getRawValue();
		EditVehicleModelPropertiesDialog.matchData(newData.modelResource, this.customModelList, this.minecraftModelList, index => {
			const {id} = this.dataService.models()[index];
			this.model.modelResource = id;
			this.model.minecraftModelPropertiesResource = "";
			this.model.minecraftPositionDefinitionsResource = "";
		}, index => {
			const {modelResource, modelPropertiesResource, positionDefinitionsResource} = this.dataService.minecraftModelResources()[index];
			this.model.modelResource = modelResource;
			this.model.minecraftModelPropertiesResource = modelPropertiesResource;
			this.model.minecraftPositionDefinitionsResource = positionDefinitionsResource;
		}, () => {
			this.model.modelResource = "";
			this.model.minecraftModelPropertiesResource = "";
			this.model.minecraftPositionDefinitionsResource = "";
		});
		EditVehicleModelPropertiesDialog.matchData(newData.textureResource, this.customTextureList, this.customModelList, index => this.model.textureResource = this.dataService.textures()[index], index => this.model.textureResource = this.dataService.minecraftTextureResources()[index], () => this.model.textureResource = "");
		this.model.flipTextureV = newData.flipTextureV ?? true;

		this.dataService.update();
		this.dialogRef.close();
	}

	onCancel() {
		this.dialogRef.close();
	}

	private static writeList<T>(
		list: T[],
		keySupplier: (data: T) => string,
		isValid: (data: T) => boolean,
		isEqual: (data: T) => boolean,
		writeMatchingKey: (matchingKey: string) => void,
	): { [key: string]: number } {
		const outputList: { [key: string]: number } = {};
		for (let i = 0; i < list.length; i++) {
			const data = list[i];
			if (isValid(data)) {
				const key = keySupplier(data);
				if (isEqual(data)) {
					writeMatchingKey(key);
				}
				outputList[key] = i;
			}
		}
		return outputList;
	}

	private static matchData(
		key: string | null,
		customList: { [key: string]: number }, minecraftList: { [key: string]: number },
		customMatch: (index: number) => void, minecraftMatch: (index: number) => void, noneMatch: () => void,
	) {
		const index1 = key ? customList[key] : undefined;
		const index2 = key ? minecraftList[key] : undefined;
		if (index1 !== undefined) {
			customMatch(index1);
		} else if (index2 !== undefined) {
			minecraftMatch(index2);
		} else {
			noneMatch();
		}
	}
}
