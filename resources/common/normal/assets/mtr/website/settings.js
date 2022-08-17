import UTILITIES from "./utilities.js";

const SETTINGS = {
	dimension: 0,
	selectedRoutes: [],
	selectedStation: 0,
	selectedDirectionsStations: [],
	selectedDirectionsSegments: {},
	selectedRouteTypes: [Object.keys(UTILITIES.routeTypes)[0]],
	size: 1,
};

export default SETTINGS;
