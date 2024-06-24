import UTILITIES from "./utilities.js";

const SETTINGS = {
	dimension: 0,
	selectedRoutes: [],
	selectedStation: 0,
	selectedDirectionsStations: [],
	selectedDirectionsSegments: {},
	selectedRouteTypes: [Object.keys(UTILITIES.routeTypes)[0]],
	densityView: 0,
	showText: true,
	showLegend: true,
	size: 1,
	url: document.location.origin + document.location.pathname.replace("index.html", ""),
};

export default SETTINGS;
