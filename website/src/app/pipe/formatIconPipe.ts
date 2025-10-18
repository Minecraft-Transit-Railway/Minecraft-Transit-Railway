import {Pipe, PipeTransform} from "@angular/core";

const ICONS = {
	"TRAIN": "directions_railway",
	"BOAT": "directions_boat",
	"CABLE_CAR": "airline_seat_recline_extra",
	"AIRPLANE": "flight",
};

@Pipe({
	name: "formatIcon",
	pure: true,
	standalone: true,
})
export class FormatIconPipe implements PipeTransform {

	transform(text: "TRAIN" | "BOAT" | "CABLE_CAR" | "AIRPLANE"): string {
		return ICONS[text];
	}
}
