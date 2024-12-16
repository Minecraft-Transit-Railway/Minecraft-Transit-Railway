import {Component, EventEmitter, Input, Output} from "@angular/core";
import {MatIconModule} from "@angular/material/icon";
import {MatRippleModule} from "@angular/material/core";
import {NgTemplateOutlet} from "@angular/common";

@Component({
	selector: "app-large-tile",
	imports: [
		MatIconModule,
		MatRippleModule,
		NgTemplateOutlet,
	],
	templateUrl: "./large-tile.component.html",
	styleUrl: "./large-tile.component.css",
})
export class LargeTileComponent {
	@Input({required: true}) clickable = false;
	@Input({required: true}) icon = "";
	@Input() disabled = false;
	@Output() clicked = new EventEmitter<void>();

	onClick() {
		this.clicked.emit();
	}
}
