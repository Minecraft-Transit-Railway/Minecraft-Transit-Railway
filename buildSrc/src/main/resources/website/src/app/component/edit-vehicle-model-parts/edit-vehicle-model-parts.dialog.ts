import {Component, inject, ViewChild} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DataService} from "../../service/data.service";
import {VehicleModelWrapper} from "../../entity/generated/vehicleModelWrapper";
import {MatTable, MatTableModule} from "@angular/material/table";
import {ModelPropertiesPartWrapper} from "../../entity/generated/modelPropertiesPartWrapper";
import {MatIconModule} from "@angular/material/icon";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {PositionDefinition} from "../../entity/generated/positionDefinition";
import {EditVehicleModelPartDialog} from "../edit-vehicle-model-part/edit-vehicle-model-part.dialog";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";

const MAIN_COLUMNS: { id: string, title: string, formatData: (modelPropertiesPart: ModelPropertiesPartWrapper) => string }[] = [
	{id: "positionDefinition", title: "Model Part", formatData: modelPropertiesPart => modelPropertiesPart.positionDefinition.name},
	{id: "positions", title: "Positions", formatData: modelPropertiesPart => modelPropertiesPart.positionDefinition.positions.map(({x, y, z}) => `(${x}, ${y}, ${z})`).join("\n")},
	{id: "positionsFlipped", title: "Flipped Positions", formatData: modelPropertiesPart => modelPropertiesPart.positionDefinition.positionsFlipped.map(({x, y, z}) => `(${x}, ${y}, ${z})`).join("\n")},
	{id: "condition", title: "Condition", formatData: modelPropertiesPart => modelPropertiesPart.condition === "NORMAL" ? "" : modelPropertiesPart.condition},
	{id: "renderStage", title: "Render Stage", formatData: modelPropertiesPart => modelPropertiesPart.type === "NORMAL" ? modelPropertiesPart.renderStage : ""},
	{id: "type", title: "Type", formatData: modelPropertiesPart => modelPropertiesPart.type},
];

const DOOR_COLUMNS: { id: string, title: string, formatData: (modelPropertiesPart: ModelPropertiesPartWrapper) => string }[] = [
	{id: "doorXMultiplier", title: "Door X Multiplier", formatData: modelPropertiesPart => modelPropertiesPart.doorXMultiplier !== 0 || modelPropertiesPart.doorZMultiplier !== 0 ? modelPropertiesPart.doorXMultiplier.toString() : ""},
	{id: "doorZMultiplier", title: "Door Z Multiplier", formatData: modelPropertiesPart => modelPropertiesPart.doorXMultiplier !== 0 || modelPropertiesPart.doorZMultiplier !== 0 ? modelPropertiesPart.doorZMultiplier.toString() : ""},
	{id: "doorAnimationType", title: "Door Animation", formatData: modelPropertiesPart => modelPropertiesPart.doorXMultiplier !== 0 || modelPropertiesPart.doorZMultiplier !== 0 ? modelPropertiesPart.doorAnimationType : ""},
];

const DISPLAY_COLUMNS: { id: string, title: string, formatData: (modelPropertiesPart: ModelPropertiesPartWrapper) => string }[] = [
	{id: "displayXPadding", title: "X Padding", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayXPadding.toString() : ""},
	{id: "displayYPadding", title: "Y Padding", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayYPadding.toString() : ""},
	{id: "displayColorCjk", title: "CJK Text Colour", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayColorCjk : ""},
	{id: "displayColor", title: "Text Colour", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayColor : ""},
	{id: "displayMaxLineHeight", title: "Max Line Height", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayMaxLineHeight.toString() : ""},
	{id: "displayCjkSizeRatio", title: "CJK Size Ratio", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayCjkSizeRatio.toString() : ""},
	{id: "displayPadZeros", title: "Pad Zeros", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayPadZeros.toString() : ""},
	{id: "displayType", title: "Type", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayType : ""},
	{id: "displayDefaultText", title: "Default Text", formatData: modelPropertiesPart => modelPropertiesPart.type === "DISPLAY" ? modelPropertiesPart.displayDefaultText : ""},
];

export const CREATE_MODEL_PROPERTIES_PART = () => new ModelPropertiesPartWrapper(
	new PositionDefinition(""),
	"NORMAL", "EXTERIOR", "NORMAL",
	0, 0, "FF9900", "FF9900", 1.5, 2, 0, "DESTINATION", "Not In Service",
	0, 0, "STANDARD",
);

@Component({
	standalone: true,
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
	@ViewChild(MatTable) table?: MatTable<ModelPropertiesPartWrapper>;
	private readonly dialogRef = inject(MatDialogRef<EditVehicleModelPartsDialog>);
	private readonly model = inject<VehicleModelWrapper>(MAT_DIALOG_DATA);
	private readonly dialog = inject(MatDialog);
	protected readonly modelPartNames: string[] = [];
	protected readonly allColumns = [...MAIN_COLUMNS, ...DOOR_COLUMNS, ...DISPLAY_COLUMNS];
	protected readonly displayedColumnNames: string[] = [];
	protected readonly dataSource: ModelPropertiesPartWrapper[] = [];
	protected readonly formGroup;
	protected hasNormal = false;
	protected hasFloorOrDoorway = false;
	protected hasSeat = false;
	protected hasDisplay = false;

	constructor(private readonly dataService: DataService) {
		this.dataService.models().find(({id}) => id === this.model.modelResource)?.modelParts.forEach(name => this.modelPartNames.push(name));
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
		this.model.parts.forEach(modelPropertiesPart => {
			if (this.modelPartNames.includes(modelPropertiesPart.positionDefinition.name)) {
				const isNormal = modelPropertiesPart.type === "NORMAL";
				const isFloorOrDoorway = modelPropertiesPart.type === "FLOOR" || modelPropertiesPart.type === "DOORWAY";
				const isSeat = modelPropertiesPart.type === "SEAT";
				const isDisplay = modelPropertiesPart.type === "DISPLAY";
				if (isNormal && newData.showNormalParts || isFloorOrDoorway && newData.showFloorsAndDoorways || isSeat && newData.showSeats || isDisplay && newData.showDisplays) {
					this.dataSource.push(modelPropertiesPart);
					if (modelPropertiesPart.doorXMultiplier !== 0 || modelPropertiesPart.doorZMultiplier !== 0) {
						showDoorColumns = true;
					}
					if (isDisplay) {
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
				if (isDisplay) {
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
		this.model.parts.push(modelPropertiesPart);
		this.edit(modelPropertiesPart);
	}

	edit(modelPropertiesPart: ModelPropertiesPartWrapper) {
		this.dialog.open(EditVehicleModelPartDialog, {data: {model: this.model, modelPropertiesPart}}).afterClosed().subscribe(() => this.filterData());
	}

	delete(modelPropertiesPart: ModelPropertiesPartWrapper) {
		modelPropertiesPart.positionDefinition.name = "";
		this.filterData();
		this.dataService.update();
	}

	onClose() {
		this.dataService.update();
		this.dialogRef.close();
	}
}
