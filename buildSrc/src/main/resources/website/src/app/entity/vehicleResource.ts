import {VehicleModel} from "./vehicleModel";

export class VehicleResource {

	constructor(
		public id: string,
		public name: string,
		public color: string,
		public transportMode: "TRAIN" | "BOAT" | "CABLE_CAR" | "AIRPLANE",
		public length: number,
		public width: number,
		public bogie1Position: number,
		public bogie2Position: number,
		public couplingPadding1: number,
		public couplingPadding2: number,
		public description: string,
		public wikipediaArticle: string,
		public tags: string[],
		public models: VehicleModel[],
		public bogie1Models: VehicleModel[],
		public bogie2Models: VehicleModel[],
		public hasGangway1: boolean,
		public hasGangway2: boolean,
		public hasBarrier1: boolean,
		public hasBarrier2: boolean,
		public bveSoundBaseResource: string,
		public legacySpeedSoundBaseResource: string,
		public legacySpeedSoundCount: number,
		public legacyUseAccelerationSoundsWhenCoasting: boolean,
		public legacyConstantPlaybackSpeed: boolean,
		public legacyDoorSoundBaseResource: string,
		public legacyDoorCloseSoundTime: number,
	) {
	}
}
