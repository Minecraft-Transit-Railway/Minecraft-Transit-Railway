import {Component, inject} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {DataService} from "../../service/data.service";
import {MatInputModule} from "@angular/material/input";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatDialog} from "@angular/material/dialog";
import {MatSelectModule} from "@angular/material/select";
import {AccordionComponent} from "../accordion/accordion.component";
import {FormatIconPipe} from "../../pipe/formatIconPipe";
import {ManageResourcesDialog} from "../manage-resources/manage-resources.dialog";
import {EditVehiclePropertiesDialog} from "../edit-vehicle-properties/edit-vehicle-properties.dialog";
import {FormatFileNamePipe} from "../../pipe/formatFileNamePipe";
import {FormatStringListPipe} from "../../pipe/formatStringListPipe";
import {EditVehicleModelPropertiesDialog} from "../edit-vehicle-model-properties/edit-vehicle-model-properties.dialog";
import {EditVehicleModelPartsDialog} from "../edit-vehicle-model-parts/edit-vehicle-model-parts.dialog";
import {VehicleResourceWrapperDTO} from "../../entity/generated/vehicleResourceWrapper";
import {VehicleModelWrapperDTO} from "../../entity/generated/vehicleModelWrapper";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";

export const CREATE_VEHICLE_RESOURCE = () => new VehicleResourceWrapperDTO("my_vehicle", "My Custom Vehicle", Math.floor(Math.random() * 0xFFFFFF).toString(16).toUpperCase().padStart(6, "0"), "TRAIN", 25, 2, -8.5, 8.5, 0, 0, "This is my custom vehicle!", "", false, false, false, false, 0, "a_train", "", 0, false, false, "", 0);
export const CREATE_MODEL = () => new VehicleModelWrapperDTO("", "", "", "", true, 1, "", "", "", "", "", "", 1.5, 2.25, 1, 0.5, "", "", "", "", "", "", 2.25, 1, 1.25, 0.25);

@Component({
    selector: "app-edit",
    imports: [
        MatButtonModule,
        MatIconModule,
        MatSelectModule,
        MatInputModule,
        MatExpansionModule,
        MatTooltipModule,
        AccordionComponent,
        FormatIconPipe,
        FormatFileNamePipe,
        FormatStringListPipe,
        TranslatePipe,
    ],
    templateUrl: "./edit.component.html",
    styleUrl: "./edit.component.css",
})
export class EditComponent {
    private readonly dialog = inject(MatDialog);

    constructor(protected readonly dataService: DataService, protected translate: TranslateService, protected sanitizer: DomSanitizer) {
    }

    createVehicleResourceInstance() {
        return CREATE_VEHICLE_RESOURCE();
    }

    createVehicleModelInstance() {
        return CREATE_MODEL();
    }

    editDetails(vehicleResourceWrapperDTO: VehicleResourceWrapperDTO) {
        this.dialog.open(EditVehiclePropertiesDialog, {data: vehicleResourceWrapperDTO});
    }

    editModelProperties(vehicleResourceWrapperDTO: VehicleResourceWrapperDTO, vehicleModelWrapperDTO: VehicleModelWrapperDTO) {
        this.dialog.open(EditVehicleModelPropertiesDialog, {
            data: {
                vehicleResource: vehicleResourceWrapperDTO,
                model: vehicleModelWrapperDTO
            }
        });
    }

    editModelParts(vehicleResourceWrapperDTO: VehicleResourceWrapperDTO, vehicleModelWrapperDTO: VehicleModelWrapperDTO) {
        this.dialog.open(EditVehicleModelPartsDialog, {
            data: {
                vehicleResource: vehicleResourceWrapperDTO,
                model: vehicleModelWrapperDTO
            }, maxWidth: "90vw", maxHeight: "90vh"
        });
    }

    canEditModelParts(vehicleModelWrapperDTO: VehicleModelWrapperDTO) {
        return this.dataService.minecraftModelResources().some(minecraftModel => vehicleModelWrapperDTO.modelResource === minecraftModel.modelResource);
    }

    scroll(target: number, content: HTMLDivElement) {
        setTimeout(() => content.scrollTo({top: target, behavior: "smooth"}), content.scrollTop < target ? 200 : 0);
    }

    getAllModelsPropertiesAndDefinitions(vehicleResourceWrapperDTO: VehicleResourceWrapperDTO) {
        return vehicleResourceWrapperDTO.bogie1Position === vehicleResourceWrapperDTO.bogie2Position ? [vehicleResourceWrapperDTO.models, vehicleResourceWrapperDTO.bogie1Models] : [vehicleResourceWrapperDTO.models, vehicleResourceWrapperDTO.bogie1Models, vehicleResourceWrapperDTO.bogie2Models];
    }

    manageModels() {
        this.dialog.open(ManageResourcesDialog, {
            data: {
                title: this.translate.instant("design.models"),
                addText: this.translate.instant("design.add_model"),
                deleteText: this.translate.instant("design.delete_model"),
                fileExtensions: ["bbmodel", "obj", "mtl"],
                listCustomSupplier: () => this.dataService.models().map(modelWrapper => modelWrapper.id),
                listMinecraftSupplier: () => this.dataService.minecraftModelResources().map(minecraftModelResource => minecraftModelResource.modelResource),
            },
        });
    }

    manageTextures() {
        this.dialog.open(ManageResourcesDialog, {
            data: {
                title: this.translate.instant("design.textures"),
                addText: this.translate.instant("design.add_texture"),
                deleteText: this.translate.instant("design.delete_texture"),
                fileExtensions: ["png"],
                listCustomSupplier: () => this.dataService.textures(),
                listMinecraftSupplier: () => this.dataService.minecraftTextureResources(),
            },
        });
    }

    bold(text: string): string {
        return `<b>${text}</b>`;
    }
}
