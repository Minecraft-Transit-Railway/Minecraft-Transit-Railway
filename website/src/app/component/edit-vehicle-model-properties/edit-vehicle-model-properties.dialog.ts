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
import {VehicleModelWrapperDTO} from "../../entity/generated/vehicleModelWrapper";
import {CREATE_MODEL} from "../edit/edit.component";
import {VehicleResourceWrapperDTO} from "../../entity/generated/vehicleResourceWrapper";

@Component({
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
	private readonly data = inject<{ vehicleResource: VehicleResourceWrapperDTO, model: VehicleModelWrapperDTO }>(MAT_DIALOG_DATA);
	private readonly customModelList: { [key: string]: number };
	private readonly minecraftModelList: { [key: string]: number };
	private readonly customTextureList: { [key: string]: number };
	private readonly minecraftTextureList: { [key: string]: number };
	protected readonly modelLists: { label: string, list: string[] }[] = [];
	protected readonly textureLists: { label: string, list: string[] }[] = [];
	protected readonly formGroup;
	protected readonly hasGangway: boolean;
	protected readonly hasBarrier: boolean;

	constructor(private readonly dataService: DataService, formatFileNamePipe: FormatFileNamePipe, formatStringListPipe: FormatStringListPipe) {
		const {vehicleResource, model} = this.data;
		this.hasGangway = vehicleResource.hasGangway1 || vehicleResource.hasGangway2;
		this.hasBarrier = vehicleResource.hasBarrier1 || vehicleResource.hasBarrier2;

		let modelKey = "";
		this.customModelList = EditVehicleModelPropertiesDialog.writeList(
			dataService.models(),
			({id}) => formatFileNamePipe.transform(id),
			() => true,
			[{
				isEqual: ({id}) => model.modelResource === (id),
				write: key => modelKey = key,
			}],
		);
		this.minecraftModelList = EditVehicleModelPropertiesDialog.writeList(
			dataService.minecraftModelResources(),
			({modelResource, modelPropertiesResource, positionDefinitionsResource}) => formatStringListPipe.transform([formatFileNamePipe.transform(modelResource), formatFileNamePipe.transform(modelPropertiesResource), formatFileNamePipe.transform(positionDefinitionsResource)], " + "),
			({modelResource, modelPropertiesResource, positionDefinitionsResource}) => !!modelResource && !!modelPropertiesResource && !!positionDefinitionsResource,
			[{
				isEqual: ({modelResource, modelPropertiesResource, positionDefinitionsResource}) => modelResource === model.modelResource && modelPropertiesResource === model.minecraftModelPropertiesResource && positionDefinitionsResource === model.minecraftPositionDefinitionsResource,
				write: key => modelKey = key,
			}],
		);
		this.modelLists.push({label: "Custom", list: Object.keys(this.customModelList)});
		this.modelLists.push({label: "Minecraft", list: Object.keys(this.minecraftModelList)});

		let textureKey = "";
		let gangwayInnerSideKey = "";
		let gangwayInnerTopKey = "";
		let gangwayInnerBottomKey = "";
		let gangwayOuterSideKey = "";
		let gangwayOuterTopKey = "";
		let gangwayOuterBottomKey = "";
		let barrierInnerSideKey = "";
		let barrierInnerTopKey = "";
		let barrierInnerBottomKey = "";
		let barrierOuterSideKey = "";
		let barrierOuterTopKey = "";
		let barrierOuterBottomKey = "";
		const writeMatchingKeys: { isEqual: (data: string) => boolean, write: (matchingKey: string) => void }[] = [{
			isEqual: texture => model.textureResource === texture,
			write: key => textureKey = key,
		}, {
			isEqual: texture => model.gangwayInnerSideResource === texture,
			write: key => gangwayInnerSideKey = key,
		}, {
			isEqual: texture => model.gangwayInnerTopResource === texture,
			write: key => gangwayInnerTopKey = key,
		}, {
			isEqual: texture => model.gangwayInnerBottomResource === texture,
			write: key => gangwayInnerBottomKey = key,
		}, {
			isEqual: texture => model.gangwayOuterSideResource === texture,
			write: key => gangwayOuterSideKey = key,
		}, {
			isEqual: texture => model.gangwayOuterTopResource === texture,
			write: key => gangwayOuterTopKey = key,
		}, {
			isEqual: texture => model.gangwayOuterBottomResource === texture,
			write: key => gangwayOuterBottomKey = key,
		}, {
			isEqual: texture => model.barrierInnerSideResource === texture,
			write: key => barrierInnerSideKey = key,
		}, {
			isEqual: texture => model.barrierInnerTopResource === texture,
			write: key => barrierInnerTopKey = key,
		}, {
			isEqual: texture => model.barrierInnerBottomResource === texture,
			write: key => barrierInnerBottomKey = key,
		}, {
			isEqual: texture => model.barrierOuterSideResource === texture,
			write: key => barrierOuterSideKey = key,
		}, {
			isEqual: texture => model.barrierOuterTopResource === texture,
			write: key => barrierOuterTopKey = key,
		}, {
			isEqual: texture => model.barrierOuterBottomResource === texture,
			write: key => barrierOuterBottomKey = key,
		}];
		this.customTextureList = EditVehicleModelPropertiesDialog.writeList(
			dataService.textures(),
			texture => texture,
			() => true,
			writeMatchingKeys,
		);
		this.minecraftTextureList = EditVehicleModelPropertiesDialog.writeList(
			dataService.minecraftTextureResources(),
			texture => texture,
			() => true,
			writeMatchingKeys,
		);
		this.textureLists.push({label: "Custom", list: Object.keys(this.customTextureList)});
		this.textureLists.push({label: "Minecraft", list: Object.keys(this.minecraftTextureList)});

		this.formGroup = new FormGroup({
			modelResource: new FormControl(modelKey),
			textureResource: new FormControl(textureKey),
			flipTextureV: new FormControl(model.flipTextureV),
			modelYOffset: new FormControl(model.modelYOffset),
			gangwayInnerSideResource: new FormControl(gangwayInnerSideKey),
			gangwayInnerTopResource: new FormControl(gangwayInnerTopKey),
			gangwayInnerBottomResource: new FormControl(gangwayInnerBottomKey),
			gangwayOuterSideResource: new FormControl(gangwayOuterSideKey),
			gangwayOuterTopResource: new FormControl(gangwayOuterTopKey),
			gangwayOuterBottomResource: new FormControl(gangwayOuterBottomKey),
			gangwayWidth: new FormControl(model.gangwayWidth),
			gangwayHeight: new FormControl(model.gangwayHeight),
			gangwayYOffset: new FormControl(model.gangwayYOffset),
			gangwayZOffset: new FormControl(model.gangwayZOffset),
			barrierInnerSideResource: new FormControl(barrierInnerSideKey),
			barrierInnerTopResource: new FormControl(barrierInnerTopKey),
			barrierInnerBottomResource: new FormControl(barrierInnerBottomKey),
			barrierOuterSideResource: new FormControl(barrierOuterSideKey),
			barrierOuterTopResource: new FormControl(barrierOuterTopKey),
			barrierOuterBottomResource: new FormControl(barrierOuterBottomKey),
			barrierWidth: new FormControl(model.barrierWidth),
			barrierHeight: new FormControl(model.barrierHeight),
			barrierYOffset: new FormControl(model.barrierYOffset),
			barrierZOffset: new FormControl(model.barrierZOffset),
		});
	}

	onSave() {
		const {model} = this.data;
		const newData = this.formGroup.getRawValue();
		const defaultModel = CREATE_MODEL();
		EditVehicleModelPropertiesDialog.matchData(newData.modelResource, this.customModelList, this.minecraftModelList, index => {
			const {id} = this.dataService.models()[index];
			model.modelResource = id;
			model.minecraftModelPropertiesResource = "";
			model.minecraftPositionDefinitionsResource = "";
		}, index => {
			const {modelResource, modelPropertiesResource, positionDefinitionsResource} = this.dataService.minecraftModelResources()[index];
			model.modelResource = modelResource;
			model.minecraftModelPropertiesResource = modelPropertiesResource;
			model.minecraftPositionDefinitionsResource = positionDefinitionsResource;
		}, () => {
			model.modelResource = "";
			model.minecraftModelPropertiesResource = "";
			model.minecraftPositionDefinitionsResource = "";
		});
		EditVehicleModelPropertiesDialog.matchData(newData.textureResource, this.customTextureList, this.minecraftTextureList, index => model.textureResource = this.dataService.textures()[index], index => model.textureResource = this.dataService.minecraftTextureResources()[index], () => model.textureResource = "");
		model.flipTextureV = newData.flipTextureV ?? defaultModel.flipTextureV;
		model.modelYOffset = newData.modelYOffset ?? defaultModel.modelYOffset;
		model.gangwayInnerSideResource = newData.gangwayInnerSideResource ?? defaultModel.gangwayInnerSideResource;
		model.gangwayInnerTopResource = newData.gangwayInnerTopResource ?? defaultModel.gangwayInnerTopResource;
		model.gangwayInnerBottomResource = newData.gangwayInnerBottomResource ?? defaultModel.gangwayInnerBottomResource;
		model.gangwayOuterSideResource = newData.gangwayOuterSideResource ?? defaultModel.gangwayOuterSideResource;
		model.gangwayOuterTopResource = newData.gangwayOuterTopResource ?? defaultModel.gangwayOuterTopResource;
		model.gangwayOuterBottomResource = newData.gangwayOuterBottomResource ?? defaultModel.gangwayOuterBottomResource;
		model.gangwayWidth = newData.gangwayWidth ?? defaultModel.gangwayWidth;
		model.gangwayHeight = newData.gangwayHeight ?? defaultModel.gangwayHeight;
		model.gangwayYOffset = newData.gangwayYOffset ?? defaultModel.gangwayYOffset;
		model.gangwayZOffset = newData.gangwayZOffset ?? defaultModel.gangwayZOffset;
		model.barrierInnerSideResource = newData.barrierInnerSideResource ?? defaultModel.barrierInnerSideResource;
		model.barrierInnerTopResource = newData.barrierInnerTopResource ?? defaultModel.barrierInnerTopResource;
		model.barrierInnerBottomResource = newData.barrierInnerBottomResource ?? defaultModel.barrierInnerBottomResource;
		model.barrierOuterSideResource = newData.barrierOuterSideResource ?? defaultModel.barrierOuterSideResource;
		model.barrierOuterTopResource = newData.barrierOuterTopResource ?? defaultModel.barrierOuterTopResource;
		model.barrierOuterBottomResource = newData.barrierOuterBottomResource ?? defaultModel.barrierOuterBottomResource;
		model.barrierWidth = newData.barrierWidth ?? defaultModel.barrierWidth;
		model.barrierHeight = newData.barrierHeight ?? defaultModel.barrierHeight;
		model.barrierYOffset = newData.barrierYOffset ?? defaultModel.barrierYOffset;
		model.barrierZOffset = newData.barrierZOffset ?? defaultModel.barrierZOffset;
		this.dataService.update(this.data.vehicleResource.id);
		this.dialogRef.close();
	}

	onCancel() {
		this.dialogRef.close();
	}

	private static writeList<T>(
		list: T[],
		keySupplier: (data: T) => string,
		isValid: (data: T) => boolean,
		writeMatchingKeys: { isEqual: (data: T) => boolean, write: (matchingKey: string) => void }[],
	): { [key: string]: number } {
		const outputList: { [key: string]: number } = {};
		for (let i = 0; i < list.length; i++) {
			const data = list[i];
			if (isValid(data)) {
				const key = keySupplier(data);
				writeMatchingKeys.forEach(({isEqual, write}) => {
					if (isEqual(data)) {
						write(key);
					}
				});
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
