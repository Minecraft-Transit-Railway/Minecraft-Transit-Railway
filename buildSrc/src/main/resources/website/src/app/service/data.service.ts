import {Injectable} from "@angular/core";
import {CustomResources} from "../entity/generated/customResources";
import {ResourceWrapper} from "../entity/generated/resourceWrapper";

export const ICONS = {
	"TRAIN": "directions_railway",
	"BOAT": "directions_boat",
	"CABLE_CAR": "airline_seat_recline_extra",
	"AIRPLANE": "flight",
};

@Injectable({providedIn: "root"})
export class DataService {
	private resourceWrapper?: ResourceWrapper;

	public create() {
		this.resourceWrapper = new ResourceWrapper(new CustomResources());
	}

	public upload(data: ResourceWrapper) {
		this.resourceWrapper = data;
		console.log(this.resourceWrapper);
	}

	public reset() {
		this.resourceWrapper = undefined;
	}

	public vehicles() {
		return this.resourceWrapper?.customResources.vehicles ?? [];
	}

	public hasData() {
		return !!this.resourceWrapper;
	}
}
