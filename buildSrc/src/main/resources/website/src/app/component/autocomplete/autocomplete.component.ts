import {Component, Input} from "@angular/core";
import {MatInputModule} from "@angular/material/input";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {FormGroup, ReactiveFormsModule} from "@angular/forms";

@Component({
	selector: "app-autocomplete",
	imports: [
		MatInputModule,
		MatAutocompleteModule,
		ReactiveFormsModule,
	],
	templateUrl: "./autocomplete.component.html",
	styleUrl: "./autocomplete.component.css",
})
export class AutocompleteComponent {
	@Input({required: true}) label = "";
	@Input({required: true}) parentFormGroup: FormGroup<any> = new FormGroup({});
	@Input({required: true}) childFormControlName = "";
	@Input({required: true}) lists: { label: string, list: string[] }[] = [];
	protected readonly filteredLists: { label: string, list: string[] }[] = [];

	filter(text: string) {
		this.filteredLists.length = 0;
		this.lists.forEach(({label, list}) => {
			const outputList: string[] = [];
			list.forEach(value => {
				if (value.toLowerCase().includes(text.toLowerCase())) {
					outputList.push(value);
				}
			});
			outputList.sort();
			this.filteredLists.push({label, list: outputList});
		});
	}
}
