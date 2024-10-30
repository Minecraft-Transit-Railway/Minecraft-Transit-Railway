import {Injectable} from "@angular/core";
import {CustomResources} from "../entity/customResources";

@Injectable({providedIn: "root"})
export class DataService {
	private data?: CustomResources;

	public create() {
		this.data = new CustomResources([]);
	}

	public upload(data: CustomResources) {
		this.data = data;
		console.log(this.data);
	}

	public reset() {
		this.data = undefined;
	}

	public hasData() {
		return !!this.data;
	}
}
