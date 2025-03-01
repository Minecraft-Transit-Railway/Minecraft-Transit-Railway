import {Component, inject, ViewChild} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DataService} from "../../service/data.service";
import {VehicleModelWrapperDTO} from "../../entity/generated/vehicleModelWrapper";
import {MatTable, MatTableModule} from "@angular/material/table";
import {ModelPropertiesPartWrapperDTO} from "../../entity/generated/modelPropertiesPartWrapper";
import {MatIconModule} from "@angular/material/icon";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {PositionDefinitionDTO} from "../../entity/generated/positionDefinition";
import {EditVehicleModelPartDialog} from "../edit-vehicle-model-part/edit-vehicle-model-part.dialog";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {VehicleResourceWrapperDTO} from "../../entity/generated/vehicleResourceWrapper";

const MAIN_COLUMNS: { id: string, title: string, formatData: (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => string }[] = [
	{id: "positionDefinition", title: "Model Part", formatData: modelPropertiesPart => modelPropertiesPart.positionDefinition.name},
	{id: "positions", title: "Positions", formatData: modelPropertiesPart => modelPropertiesPart.positionDefinition.positions.map(({x, y, z}) => `(${x}, ${y}, ${z})`).join("\n")},
	{id: "positionsFlipped", title: "Flipped Positions", formatData: modelPropertiesPart => modelPropertiesPart.positionDefinition.positionsFlipped.map(({x, y, z}) => `(${x}, ${y}, ${z})`).join("\n")},
	{id: "condition", title: "Condition", formatData: modelPropertiesPart => modelPropertiesPart.condition === "NORMAL" ? "" : modelPropertiesPart.condition},
	{id: "renderStage", title: "Render Stage", formatData: modelPropertiesPart => modelPropertiesPart.type === "NORMAL" ? modelPropertiesPart.renderStage : ""},
	{id: "type", title: "Type", formatData: modelPropertiesPart => modelPropertiesPart.type},
];

const hasDoorMultiplier = (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => modelPropertiesPart.doorXMultiplier !== 0 || modelPropertiesPart.doorZMultiplier !== 0;
const hasOpeningDoorTime = (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => modelPropertiesPart.renderFromOpeningDoorTime !== 0 || modelPropertiesPart.renderUntilOpeningDoorTime !== 0;
const hasClosingDoorTime = (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => modelPropertiesPart.renderFromClosingDoorTime !== 0 || modelPropertiesPart.renderUntilClosingDoorTime !== 0;
const hasFlashTime = (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => modelPropertiesPart.flashOnTime !== 0 || modelPropertiesPart.flashOffTime !== 0;
const DOOR_COLUMNS: { id: string, title: string, formatData: (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => string }[] = [
	{id: "doorXMultiplier", title: "Door X Multiplier", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) ? modelPropertiesPart.doorXMultiplier.toString() : ""},
	{id: "doorZMultiplier", title: "Door Z Multiplier", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) ? modelPropertiesPart.doorZMultiplier.toString() : ""},
	{id: "doorAnimationType", title: "Door Animation", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) ? modelPropertiesPart.doorAnimationType : ""},
	{id: "renderFromOpeningDoorTime", title: "From Opening Time", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) && hasOpeningDoorTime(modelPropertiesPart) ? modelPropertiesPart.renderFromOpeningDoorTime.toString() : ""},
	{id: "renderUntilOpeningDoorTime", title: "Until Opening Time", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) && hasOpeningDoorTime(modelPropertiesPart) ? modelPropertiesPart.renderUntilOpeningDoorTime.toString() : ""},
	{id: "renderFromClosingDoorTime", title: "From Closing Time", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) && hasClosingDoorTime(modelPropertiesPart) ? modelPropertiesPart.renderFromClosingDoorTime.toString() : ""},
	{id: "renderUntilClosingDoorTime", title: "Until Closing Time", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) && hasClosingDoorTime(modelPropertiesPart) ? modelPropertiesPart.renderUntilClosingDoorTime.toString() : ""},
	{id: "flashOnTime", title: "Flash On Time", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) && hasFlashTime(modelPropertiesPart) ? modelPropertiesPart.flashOnTime.toString() : ""},
	{id: "flashOffTime", title: "Flash Off Time", formatData: modelPropertiesPart => hasDoorMultiplier(modelPropertiesPart) && hasFlashTime(modelPropertiesPart) ? modelPropertiesPart.flashOffTime.toString() : ""},
];

const isDisplay = (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => modelPropertiesPart.type === "DISPLAY";
const isRouteColorDisplay = (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => modelPropertiesPart.displayType === "ROUTE_COLOR" || modelPropertiesPart.displayType === "ROUTE_COLOR_ROUNDED";
const isDepartureIndexDisplay = (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => modelPropertiesPart.displayType === "DEPARTURE_INDEX";
const DISPLAY_COLUMNS: { id: string, title: string, formatData: (modelPropertiesPart: ModelPropertiesPartWrapperDTO) => string }[] = [
	{id: "displayXPadding", title: "X Padding", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) ? modelPropertiesPart.displayXPadding.toString() : ""},
	{id: "displayYPadding", title: "Y Padding", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) ? modelPropertiesPart.displayYPadding.toString() : ""},
	{id: "displayColorCjk", title: "CJK Text Colour", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) && !isRouteColorDisplay(modelPropertiesPart) ? modelPropertiesPart.displayColorCjk : ""},
	{id: "displayColor", title: "Text Colour", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) && !isRouteColorDisplay(modelPropertiesPart) ? modelPropertiesPart.displayColor : ""},
	{id: "displayMaxLineHeight", title: "Max Line Height", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) && !isRouteColorDisplay(modelPropertiesPart) ? modelPropertiesPart.displayMaxLineHeight.toString() : ""},
	{id: "displayCjkSizeRatio", title: "CJK Size Ratio", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) && !isDepartureIndexDisplay(modelPropertiesPart) ? modelPropertiesPart.displayCjkSizeRatio.toString() : ""},
	{id: "displayPadZeros", title: "Pad Zeros", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) && isDepartureIndexDisplay(modelPropertiesPart) ? modelPropertiesPart.displayPadZeros.toString() : ""},
	{id: "displayType", title: "Type", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) ? modelPropertiesPart.displayType : ""},
	{id: "displayDefaultText", title: "Default Text", formatData: modelPropertiesPart => isDisplay(modelPropertiesPart) && !isRouteColorDisplay(modelPropertiesPart) ? modelPropertiesPart.displayDefaultText : ""},
];

export const CREATE_MODEL_PROPERTIES_PART = () => new ModelPropertiesPartWrapperDTO(
	new PositionDefinitionDTO(""),
	"NORMAL", "EXTERIOR", "NORMAL",
	0, 0, "FF9900", "FF9900", 1.5, 2, 0, "DESTINATION", "Not In Service",
	0, 0, "STANDARD", 0, 0, 0, 0, 0, 0,
);

@Component({
	imports: [
		MatDialogModule,
		MatButtonModule,
		MatIconModule,
		MatTableModule,
		MatCheckboxModule,
		MatTooltipModule,
		ReactiveFormsModule,
	],
	templateUrl: "edit-vehicle-model-parts.dialog.html",
	styleUrl: "edit-vehicle-model-parts.dialog.css",
})
export class EditVehicleModelPartsDialog {
	@ViewChild(MatTable) table?: MatTable<ModelPropertiesPartWrapperDTO>;
	private readonly dialogRef = inject(MatDialogRef<EditVehicleModelPartsDialog>);
	private readonly data = inject<{ vehicleResource: VehicleResourceWrapperDTO, model: VehicleModelWrapperDTO }>(MAT_DIALOG_DATA);
	private readonly dialog = inject(MatDialog);
	protected readonly modelPartNames: string[] = [];
	protected readonly allColumns = [...MAIN_COLUMNS, ...DOOR_COLUMNS, ...DISPLAY_COLUMNS];
	protected readonly displayedColumnNames: string[] = [];
	protected readonly dataSource: ModelPropertiesPartWrapperDTO[] = [];
	protected readonly formGroup;
	protected hasNormal = false;
	protected hasFloorOrDoorway = false;
	protected hasSeat = false;
	protected hasDisplay = false;

	constructor(private readonly dataService: DataService) {
		this.dataService.models().find(({id}) => id === this.data.model.modelResource)?.modelParts.forEach(name => this.modelPartNames.push(name));
		this.formGroup = new FormGroup({
			showNormalParts: new FormControl(true),
			showFloorsAndDoorways: new FormControl(true),
			showSeats: new FormControl(true),
			showDisplays: new FormControl(true),
		});
		this.filterData();
	}

	filterData() {
		this.dataSource.length = 0;
		this.hasNormal = false;
		this.hasFloorOrDoorway = false;
		this.hasSeat = false;
		this.hasDisplay = false;
		let showDoorColumns = false;
		let showDisplayColumns = false;
		const newData = this.formGroup.getRawValue();
		this.data.model.parts.forEach(modelPropertiesPart => {
			if (this.modelPartNames.includes(modelPropertiesPart.positionDefinition.name)) {
				const isNormal = modelPropertiesPart.type === "NORMAL";
				const isFloorOrDoorway = modelPropertiesPart.type === "FLOOR" || modelPropertiesPart.type === "DOORWAY";
				const isSeat = modelPropertiesPart.type === "SEAT";
				if (isNormal && newData.showNormalParts || isFloorOrDoorway && newData.showFloorsAndDoorways || isSeat && newData.showSeats || isDisplay(modelPropertiesPart) && newData.showDisplays) {
					this.dataSource.push(modelPropertiesPart);
					if (hasDoorMultiplier(modelPropertiesPart)) {
						showDoorColumns = true;
					}
					if (isDisplay(modelPropertiesPart)) {
						showDisplayColumns = true;
					}
				}
				if (isNormal) {
					this.hasNormal = true;
				}
				if (isFloorOrDoorway) {
					this.hasFloorOrDoorway = true;
				}
				if (isSeat) {
					this.hasSeat = true;
				}
				if (isDisplay(modelPropertiesPart)) {
					this.hasDisplay = true;
				}
			}
		});

		this.displayedColumnNames.length = 0;
		MAIN_COLUMNS.forEach(({id}) => this.displayedColumnNames.push(id));
		if (showDoorColumns) {
			DOOR_COLUMNS.forEach(({id}) => this.displayedColumnNames.push(id));
		}
		if (showDisplayColumns) {
			DISPLAY_COLUMNS.forEach(({id}) => this.displayedColumnNames.push(id));
		}
		this.displayedColumnNames.push("controls");

		this.table?.renderRows();
	}

	add() {
		const modelPropertiesPart = CREATE_MODEL_PROPERTIES_PART();
		this.data.model.parts.push(modelPropertiesPart);
		this.edit(modelPropertiesPart);
	}

	edit(modelPropertiesPart: ModelPropertiesPartWrapperDTO) {
		this.dialog.open(EditVehicleModelPartDialog, {data: {vehicleResource: this.data.vehicleResource, model: this.data.model, modelPropertiesPart}}).afterClosed().subscribe(() => this.filterData());
	}

	delete(modelPropertiesPart: ModelPropertiesPartWrapperDTO) {
		modelPropertiesPart.positionDefinition.name = "";
		this.filterData();
		this.dataService.update(this.data.vehicleResource.id, true);
	}

	onClose() {
		this.dataService.update(this.data.vehicleResource.id);
		this.dialogRef.close();
	}
}
