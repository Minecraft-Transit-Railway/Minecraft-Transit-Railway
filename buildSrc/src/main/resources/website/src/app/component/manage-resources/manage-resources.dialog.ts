import {Component, inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {UploaderComponent} from "../uploader/uploader.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatExpansionModule} from "@angular/material/expansion";
import {DataService} from "../../service/data.service";

@Component({
	imports: [
		MatDialogModule,
		MatButtonModule,
		MatIconModule,
		UploaderComponent,
		MatTooltipModule,
		MatExpansionModule,
	],
	templateUrl: "manage-resources.dialog.html",
	styleUrl: "manage-resources.dialog.css",
})
export class ManageResourcesDialog {
	private readonly dialogRef = inject(MatDialogRef<ManageResourcesDialog>);
	protected readonly data = inject<{ title: string, addText: string, deleteText: string, fileExtensions: string[], listCustomSupplier: () => string[], listMinecraftSupplier: () => string[] }>(MAT_DIALOG_DATA);
	protected listCustomCount = 0;
	protected idListCustomFlattened = "";
	protected listMinecraftCount = 0;
	protected idListMinecraftFlattened = "";

	constructor(private readonly dataService: DataService) {
		[this.listCustomCount, this.idListCustomFlattened] = ManageResourcesDialog.updateIdLists(this.data.listCustomSupplier);
		[this.listMinecraftCount, this.idListMinecraftFlattened] = ManageResourcesDialog.updateIdLists(this.data.listMinecraftSupplier);
	}

	upload() {
		[this.listCustomCount, this.idListCustomFlattened] = ManageResourcesDialog.updateIdLists(this.data.listCustomSupplier);
		[this.listMinecraftCount, this.idListMinecraftFlattened] = ManageResourcesDialog.updateIdLists(this.data.listMinecraftSupplier);
	}

	validateFiles(fileNames: [string, string][]) {
		const matchingPairsObj: string[] = [];
		const matchingPairsMtl: string[] = [];
		fileNames.forEach(([fileName, fileExtension]) => {
			if (fileExtension === "obj") {
				matchingPairsObj.push(fileName);
			} else if (fileExtension === "mtl") {
				matchingPairsMtl.push(fileName);
			}
		});

		if (JSON.stringify(matchingPairsObj.sort()) === JSON.stringify(matchingPairsMtl.sort())) {
			return undefined;
		} else {
			return "Each OBJ file must be uploaded with an MTL file of the same name!";
		}
	}

	onClose() {
		this.dialogRef.close();
	}

	private static updateIdLists(listSupplier: () => string[]): [number, string] {
		const idListMinecraft: string[] = [];
		listSupplier().forEach(id => {
			if (!idListMinecraft.includes(id)) {
				idListMinecraft.push(id);
			}
		});
		idListMinecraft.sort();
		return [idListMinecraft.length, idListMinecraft.join("\n")];
	}
}
