import {Component, Input} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatSliderModule} from "@angular/material/slider";
import {DataService} from "../../service/data.service";

@Component({
	selector: "app-sound",
	standalone: true,
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
	private isMinecraftPaused = false;

	constructor(private readonly httpClient: HttpClient) {
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
		return this.isMinecraftPaused;
	}

	private send() {
		if (this.canSend && this.previouslySentValue != this.sliderValue) {
			this.httpClient.get<{ paused: boolean }>(DataService.getUrl(`operation/play-sound?${this.parameters}&id=${this.soundId}&value=${this.sliderValue}`)).subscribe(({paused}) => this.isMinecraftPaused = paused);
			this.canSend = false;
			this.previouslySentValue = this.sliderValue;
			setTimeout(() => {
				this.canSend = true;
				this.send();
			}, 200);
		}
	}
}
