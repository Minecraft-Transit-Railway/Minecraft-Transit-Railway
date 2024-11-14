import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
	name: "formatFileName",
	pure: true,
	standalone: true,
})
export class FormatFileNamePipe implements PipeTransform {

	transform(fileName: string): string {
		if (fileName) {
			const fileNameSplit = fileName.split("/");
			return fileNameSplit[fileNameSplit.length - 1];
		} else {
			return "";
		}
	}
}
