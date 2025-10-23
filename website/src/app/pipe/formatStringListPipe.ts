import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
	name: "formatStringList",
	pure: true,
	standalone: true,
})
export class FormatStringListPipe implements PipeTransform {

	transform(text: string[], separator: string): string {
		const newText = text.filter(text => text);
		return newText.length === 0 ? "(Untitled)" : newText.join(separator);
	}
}
