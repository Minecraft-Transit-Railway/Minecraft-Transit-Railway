import {Component, inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DataService} from "../../service/data.service";
import {MatIconModule} from "@angular/material/icon";
import {ModelPropertiesPartWrapperDTO} from "../../entity/generated/modelPropertiesPartWrapper";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {CREATE_MODEL_PROPERTIES_PART} from "../edit-vehicle-model-parts/edit-vehicle-model-parts.dialog";
import {MatSelectModule} from "@angular/material/select";
import {CdkTextareaAutosize} from "@angular/cdk/text-field";
import {VehicleModelWrapperDTO} from "../../entity/generated/vehicleModelWrapper";
import {PartPositionDTO} from "../../entity/generated/partPosition";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {VehicleResourceWrapperDTO} from "../../entity/generated/vehicleResourceWrapper";

@Component({
	imports: [
		MatDialogModule,
		MatButtonModule,
		MatIconModule,
		MatTooltipModule,
		FormsModule,
		MatFormField,
		MatInput,
		MatLabel,
		ReactiveFormsModule,
		MatSelectModule,
		MatCheckboxModule,
		CdkTextareaAutosize,
	],
	templateUrl: "edit-vehicle-model-part.dialog.html",
	styleUrl: "edit-vehicle-model-part.dialog.css",
})
export class EditVehicleModelPartDialog {
	private readonly dialogRef = inject(MatDialogRef<EditVehicleModelPartDialog>);
	private readonly data = inject<{ vehicleResource: VehicleResourceWrapperDTO, model: VehicleModelWrapperDTO, modelPropertiesPart: ModelPropertiesPartWrapperDTO }>(MAT_DIALOG_DATA);
	protected readonly modelPartNames: string[] = [];
	protected readonly formGroup;

	constructor(private readonly dataService: DataService) {
		const {model, modelPropertiesPart} = this.data;
		this.dataService.models().find(({id}) => id === model.modelResource)?.modelParts.forEach(name => this.modelPartNames.push(name));

		this.formGroup = new FormGroup({
			name: new FormControl(modelPropertiesPart.positionDefinition.name),
			positions: new FormControl(EditVehicleModelPartDialog.positionsToString(modelPropertiesPart.positionDefinition.positions)),
			positionsFlipped: new FormControl(EditVehicleModelPartDialog.positionsToString(modelPropertiesPart.positionDefinition.positionsFlipped)),
			condition: new FormControl(modelPropertiesPart.condition),
			renderStage: new FormControl(modelPropertiesPart.renderStage),
			type: new FormControl(modelPropertiesPart.type),
			displayXPadding: new FormControl(modelPropertiesPart.displayXPadding),
			displayYPadding: new FormControl(modelPropertiesPart.displayYPadding),
			displayColorCjk: new FormControl(`#${modelPropertiesPart.displayColorCjk}`),
			displayColor: new FormControl(`#${modelPropertiesPart.displayColor}`),
			displayMaxLineHeight: new FormControl(modelPropertiesPart.displayMaxLineHeight),
			displayCjkSizeRatio: new FormControl(modelPropertiesPart.displayCjkSizeRatio),
			displayPadZeros: new FormControl(modelPropertiesPart.displayPadZeros),
			displayType: new FormControl(modelPropertiesPart.displayType),
			displayDefaultText: new FormControl(modelPropertiesPart.displayDefaultText),
			doorXMultiplier: new FormControl(modelPropertiesPart.doorXMultiplier),
			doorZMultiplier: new FormControl(modelPropertiesPart.doorZMultiplier),
			doorAnimationType: new FormControl(modelPropertiesPart.doorAnimationType),
			renderFromOpeningDoorTime: new FormControl(modelPropertiesPart.renderFromOpeningDoorTime),
			renderUntilOpeningDoorTime: new FormControl(modelPropertiesPart.renderUntilOpeningDoorTime),
			renderFromClosingDoorTime: new FormControl(modelPropertiesPart.renderFromClosingDoorTime),
			renderUntilClosingDoorTime: new FormControl(modelPropertiesPart.renderUntilClosingDoorTime),
			flashOnTime: new FormControl(modelPropertiesPart.flashOnTime),
			flashOffTime: new FormControl(modelPropertiesPart.flashOffTime),
			displayOptionSingleLine: new FormControl(modelPropertiesPart.displayOptions.includes("SINGLE_LINE")),
			displayOptionUpperCase: new FormControl(modelPropertiesPart.displayOptions.includes("UPPER_CASE")),
			displayOptionSpaceCjk: new FormControl(modelPropertiesPart.displayOptions.includes("SPACE_CJK")),
			displayOptionScrollNormal: new FormControl(modelPropertiesPart.displayOptions.includes("SCROLL_NORMAL")),
			displayOptionScrollLightRail: new FormControl(modelPropertiesPart.displayOptions.includes("SCROLL_LIGHT_RAIL")),
			displayOptionSevenSegment: new FormControl(modelPropertiesPart.displayOptions.includes("SEVEN_SEGMENT")),
			displayOptionAlignLeftCjk: new FormControl(modelPropertiesPart.displayOptions.includes("ALIGN_LEFT_CJK")),
			displayOptionAlignRightCjk: new FormControl(modelPropertiesPart.displayOptions.includes("ALIGN_RIGHT_CJK")),
			displayOptionAlignLeft: new FormControl(modelPropertiesPart.displayOptions.includes("ALIGN_LEFT")),
			displayOptionAlignRight: new FormControl(modelPropertiesPart.displayOptions.includes("ALIGN_RIGHT")),
			displayOptionAlignTop: new FormControl(modelPropertiesPart.displayOptions.includes("ALIGN_TOP")),
			displayOptionAlignBottom: new FormControl(modelPropertiesPart.displayOptions.includes("ALIGN_BOTTOM")),
			displayOptionCycleLanguages: new FormControl(modelPropertiesPart.displayOptions.includes("CYCLE_LANGUAGES")),
		});

		this.formGroup.valueChanges.subscribe(() => {
			const newData = this.formGroup.getRawValue();
			const defaultModelPropertiesPart = CREATE_MODEL_PROPERTIES_PART();
			modelPropertiesPart.positionDefinition.name = newData.name ?? defaultModelPropertiesPart.positionDefinition.name;
			modelPropertiesPart.positionDefinition.positions.length = 0;
			if (newData.positions) {
				EditVehicleModelPartDialog.stringToPositions(newData.positions).forEach(position => modelPropertiesPart.positionDefinition.positions.push(position));
			}
			modelPropertiesPart.positionDefinition.positionsFlipped.length = 0;
			if (newData.positionsFlipped) {
				EditVehicleModelPartDialog.stringToPositions(newData.positionsFlipped).forEach(position => modelPropertiesPart.positionDefinition.positionsFlipped.push(position));
			}
			modelPropertiesPart.condition = newData.condition ?? defaultModelPropertiesPart.condition;
			modelPropertiesPart.renderStage = newData.renderStage ?? defaultModelPropertiesPart.renderStage;
			modelPropertiesPart.type = newData.type ?? defaultModelPropertiesPart.type;
			modelPropertiesPart.displayXPadding = newData.displayXPadding ?? defaultModelPropertiesPart.displayXPadding;
			modelPropertiesPart.displayYPadding = newData.displayYPadding ?? defaultModelPropertiesPart.displayYPadding;
			modelPropertiesPart.displayColorCjk = newData.displayColorCjk?.substring(1).toUpperCase() ?? defaultModelPropertiesPart.displayColorCjk;
			modelPropertiesPart.displayColor = newData.displayColor?.substring(1).toUpperCase() ?? defaultModelPropertiesPart.displayColor;
			modelPropertiesPart.displayMaxLineHeight = Math.max(0, newData.displayMaxLineHeight ?? defaultModelPropertiesPart.displayMaxLineHeight);
			modelPropertiesPart.displayCjkSizeRatio = Math.max(0, newData.displayCjkSizeRatio ?? defaultModelPropertiesPart.displayCjkSizeRatio);
			modelPropertiesPart.displayPadZeros = Math.max(0, Math.round(newData.displayPadZeros ?? defaultModelPropertiesPart.displayPadZeros));
			modelPropertiesPart.displayType = newData.displayType ?? defaultModelPropertiesPart.displayType;
			modelPropertiesPart.displayDefaultText = newData.displayDefaultText ?? defaultModelPropertiesPart.displayDefaultText;
			modelPropertiesPart.doorXMultiplier = newData.doorXMultiplier ?? defaultModelPropertiesPart.doorXMultiplier;
			modelPropertiesPart.doorZMultiplier = newData.doorZMultiplier ?? defaultModelPropertiesPart.doorZMultiplier;
			modelPropertiesPart.doorAnimationType = newData.doorAnimationType ?? defaultModelPropertiesPart.doorAnimationType;
			modelPropertiesPart.renderFromOpeningDoorTime = newData.renderFromOpeningDoorTime ?? defaultModelPropertiesPart.renderFromOpeningDoorTime;
			modelPropertiesPart.renderUntilOpeningDoorTime = newData.renderUntilOpeningDoorTime ?? defaultModelPropertiesPart.renderUntilOpeningDoorTime;
			modelPropertiesPart.renderFromClosingDoorTime = newData.renderFromClosingDoorTime ?? defaultModelPropertiesPart.renderFromClosingDoorTime;
			modelPropertiesPart.renderUntilClosingDoorTime = newData.renderUntilClosingDoorTime ?? defaultModelPropertiesPart.renderUntilClosingDoorTime;
			modelPropertiesPart.flashOnTime = newData.flashOnTime ?? defaultModelPropertiesPart.flashOnTime;
			modelPropertiesPart.flashOffTime = newData.flashOffTime ?? defaultModelPropertiesPart.flashOffTime;
			modelPropertiesPart.displayOptions.length = 0;
			if (newData.displayOptionSingleLine) {
				modelPropertiesPart.displayOptions.push("SINGLE_LINE");
			}
			if (newData.displayOptionUpperCase) {
				modelPropertiesPart.displayOptions.push("UPPER_CASE");
			}
			if (newData.displayOptionSpaceCjk) {
				modelPropertiesPart.displayOptions.push("SPACE_CJK");
			}
			if (newData.displayOptionScrollNormal) {
				modelPropertiesPart.displayOptions.push("SCROLL_NORMAL");
			}
			if (newData.displayOptionScrollLightRail) {
				modelPropertiesPart.displayOptions.push("SCROLL_LIGHT_RAIL");
			}
			if (newData.displayOptionSevenSegment) {
				modelPropertiesPart.displayOptions.push("SEVEN_SEGMENT");
			}
			if (newData.displayOptionAlignLeftCjk) {
				modelPropertiesPart.displayOptions.push("ALIGN_LEFT_CJK");
			}
			if (newData.displayOptionAlignRightCjk) {
				modelPropertiesPart.displayOptions.push("ALIGN_RIGHT_CJK");
			}
			if (newData.displayOptionAlignLeft) {
				modelPropertiesPart.displayOptions.push("ALIGN_LEFT");
			}
			if (newData.displayOptionAlignRight) {
				modelPropertiesPart.displayOptions.push("ALIGN_RIGHT");
			}
			if (newData.displayOptionAlignTop) {
				modelPropertiesPart.displayOptions.push("ALIGN_TOP");
			}
			if (newData.displayOptionAlignBottom) {
				modelPropertiesPart.displayOptions.push("ALIGN_BOTTOM");
			}
			if (newData.displayOptionCycleLanguages) {
				modelPropertiesPart.displayOptions.push("CYCLE_LANGUAGES");
			}
			this.dataService.update(this.data.vehicleResource.id, true);
		});
	}

	hasDoorMultiplier() {
		const newData = this.formGroup.getRawValue();
		return newData.doorXMultiplier !== 0 || newData.doorZMultiplier !== 0;
	}

	isDisplay() {
		return this.formGroup.getRawValue().type === "DISPLAY";
	}

	isNormal() {
		return this.formGroup.getRawValue().type === "NORMAL";
	}

	isDepartureIndexDisplay() {
		return this.isDisplay() && this.formGroup.getRawValue().displayType === "DEPARTURE_INDEX";
	}

	isRouteColorDisplay() {
		const displayType = this.formGroup.getRawValue().displayType;
		return this.isDisplay() && (displayType === "ROUTE_COLOR" || displayType === "ROUTE_COLOR_ROUNDED");
	}

	formatPositions() {
		const newData = this.formGroup.getRawValue();
		if (newData.positions) {
			this.formGroup.controls.positions.setValue(EditVehicleModelPartDialog.formatPositions(newData.positions));
		}
		if (newData.positionsFlipped) {
			this.formGroup.controls.positionsFlipped.setValue(EditVehicleModelPartDialog.formatPositions(newData.positionsFlipped));
		}
	}

	onClose() {
		this.dialogRef.close();
	}

	private static positionsToString(positions: PartPositionDTO[]) {
		return positions.map(position => `${position.x}, ${position.y}, ${position.z}`).join("\n");
	}

	private static stringToPositions(positionsString: string) {
		return positionsString.split("\n").map(positionString => {
			const coordinates = positionString.split(",").map(coordinateString => {
				const coordinate = parseFloat(coordinateString.replace(/[^\d-.]/g, ""));
				return Number.isNaN(coordinate) ? 0 : coordinate;
			});
			return new PartPositionDTO(coordinates[0] ?? 0, coordinates[1] ?? 0, coordinates[2] ?? 0);
		});
	}

	private static formatPositions(positionsString: string) {
		const positions: PartPositionDTO[] = [];
		EditVehicleModelPartDialog.stringToPositions(positionsString).forEach(position => positions.push(position));
		return EditVehicleModelPartDialog.positionsToString(positions);
	}
}
