import {Component, EventEmitter, Input, Output, TemplateRef} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTooltip} from "@angular/material/tooltip";
import {NgTemplateOutlet} from "@angular/common";

@Component({
	selector: "app-accordion",
	imports: [
		MatButtonModule,
		MatIconModule,
		MatExpansionModule,
		MatTooltip,
		NgTemplateOutlet,
	],
	templateUrl: "./accordion.component.html",
	styleUrl: "./accordion.component.css",
})
export class AccordionComponent<T> {
	@Input() headerTemplate?: TemplateRef<any>;
	@Input() contentTemplate?: TemplateRef<any>;
	@Input({required: true}) list: T[] = [];
	@Input() createInstance?: () => T;
	@Output() changed = new EventEmitter<void>();
	@Output() positionChanged = new EventEmitter<number>();
	private editingIndex = -1;

	add() {
		if (this.createInstance) {
			this.list.push(this.createInstance());
			this.editAtIndex(this.list.length - 1);
			this.changed.emit();
		}
	}

	move(movePositions: number) {
		const newPosition = movePositions < -1 ? 0 : movePositions > 1 ? this.list.length - 1 : Math.max(0, Math.min(this.list.length - 1, this.editingIndex + movePositions));
		if (this.editingIndex != newPosition) {
			const dataToMove = this.list[this.editingIndex];
			this.list.splice(this.editingIndex, 1);
			this.list.splice(newPosition, 0, dataToMove);
			this.editAtIndex(newPosition);
			this.changed.emit();
		}
	}

	duplicate() {
		const dataCopy: T = JSON.parse(JSON.stringify(this.list[this.editingIndex]));
		this.list.splice(this.editingIndex + 1, 0, dataCopy);
		this.changed.emit();
	}

	delete() {
		this.list.splice(this.editingIndex, 1);
		this.editAtIndex(-1);
		this.changed.emit();
	}

	editAtIndex(index: number) {
		this.editingIndex = index;
		this.positionChanged.emit(index * 48);
	}

	isEditingAtIndex(index: number) {
		return this.editingIndex == index;
	}
}
