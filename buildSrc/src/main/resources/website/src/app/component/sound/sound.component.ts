import {Component, Input} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatSliderModule} from "@angular/material/slider";
import {DataService} from "../../service/data.service";

@Component({
	selector: "app-sound",
	imports: [
		MatButtonModule,
		MatIconModule,
		MatSliderModule,
	],
	templateUrl: "./sound.component.html",
	styleUrl: "./sound.component.css",
})
export class SoundComponent {
	@Input({required: true}) buttonLabel = "";
	@Input({required: true}) timeout = 0;
	@Input({required: true}) soundId = "";
	@Input({required: true}) parameters = "";
	@Input() sliderMax = 0;
	@Input() sliderLabelSuffix = "";
	protected sliderValue = 0;
	private previouslySentValue = -1;
	private canSend = true;
	private playingTimeoutId?: number;

	constructor(private readonly dataService: DataService) {
	}

	playSound(valueString?: string) {
		if (valueString) {
			this.sliderValue = parseInt(valueString);
		}

		this.send();
		clearTimeout(this.playingTimeoutId);
		this.playingTimeoutId = setTimeout(() => this.previouslySentValue = -1, this.timeout) as unknown as number;
	}

	updateSound() {
		if (this.getIsPlaying()) {
			this.previouslySentValue = -1;
			this.playSound();
		}
	}

	getIsPlaying() {
		return this.previouslySentValue >= 0;
	}

	getIsMinecraftPaused() {
		return this.dataService.isMinecraftPaused();
	}

	private send() {
		if (this.canSend && this.previouslySentValue != this.sliderValue) {
			this.dataService.sendGetRequest(`operation/play-sound?${this.parameters}&id=${this.soundId}&value=${this.sliderValue}`);
			this.canSend = false;
			this.previouslySentValue = this.sliderValue;
			setTimeout(() => {
				this.canSend = true;
				this.send();
			}, 200);
		}
	}
}
