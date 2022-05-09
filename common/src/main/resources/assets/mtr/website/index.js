import FetchData from "./fetch.js";
import CANVAS from "./utilities.js";
import DIRECTIONS from "./directions.js";
import VERSION from "./version.js";
import drawMap from "./drawing.js";
import panable from "./gestures/src/gestures/pan.js";
import pinchable from "./gestures/src/gestures/pinch.js";
import tappable from "./gestures/src/gestures/tap.js";

const URL = document.location.origin + document.location.pathname.replace("index.html", "");
const SETTINGS = {
	dataUrl: URL + "data",
	infoUrl: URL + "info",
	arrivalsUrl: URL + "arrivals",
	delaysUrl: URL + "delays",
	routeTypes: {
		"train_normal": "directions_train",
		"train_light_rail": "tram",
		"train_high_speed": "train",
		"boat_normal": "sailing",
		"boat_light_rail": "directions_boat",
		"boat_high_speed": "snowmobile",
		"cable_car_normal": "airline_seat_recline_extra",
	},
	lineSize: 6,
	dimension: 0,
	selectedRouteTypes: [],
	selectedColor: -1,
	selectedStation: 0,
	selectedDirectionsStations: [],
	selectedDirectionsSegments: {},
	showText: true,
	smoothScrollScale: 100,
	isCJK: text => text.match(/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uff9f\u4e00-\u9faf\u3400-\u4dbf]/),
	getColorStyle: style => parseInt(getComputedStyle(document.body).getPropertyValue(style).replace(/#/g, ""), 16),
	onClearSearch: (data, focus) => {
		const searchBox = document.getElementById("search_box");
		searchBox.value = "";
		if (focus) {
			searchBox.focus();
		}
		document.getElementById("clear_search_icon").style.display = "none";
		const {stations, routes} = data;
		for (const stationId in stations) {
			document.getElementById(stationId).style.display = "none";
		}
		for (const index in routes) {
			document.getElementById(routes[index]["color"]).style.display = "none";
		}
	},
	clearPanes: function () {
		this.selectedStation = 0;
		this.selectedColor = -1;
		this.selectedDirectionsStations = [];
		this.selectedDirectionsSegments = {};
		showSettings = false;
		document.getElementById("station_info").style.display = "none";
		document.getElementById("route_info").style.display = "none";
		document.getElementById("directions").style.display = "none";
		document.getElementById("settings").style.display = "none";
	},
	drawDirectionsRoute: function (pathStations, pathRoutes) {
		if (pathStations.length > 0 || pathRoutes.length > 0) {
			this.selectedColor = -1;
		}
		this.selectedDirectionsStations = pathStations;
		this.selectedDirectionsSegments = [];
		for (let i = 0; i < pathRoutes.length; i++) {
			if (pathRoutes[i] != null) {
				for (let j = 0; j < pathRoutes[i].length; j++) {
					const color = pathRoutes[i][j]["color"];
					if (!(color in this.selectedDirectionsSegments)) {
						this.selectedDirectionsSegments[color] = [];
					}
					this.selectedDirectionsSegments[color].push(pathStations[i] + "_" + pathStations[i + 1]);
				}
			}
		}
		drawMap(container, json[this.dimension]);
	},
};
const WALKING_SPEED_METER_PER_SECOND = 4;
const DATA_REFRESH_INTERVAL = 60000;
const FETCH_DATA = new FetchData(() => SETTINGS.dataUrl, DATA_REFRESH_INTERVAL, false, () => true, result => {
	json = result;

	for (const dimension of json) {
		dimension["connections"] = {};
		const {routes, positions, connections, stations} = dimension;

		for (const stationId in stations) {
			stations[stationId]["horizontal"] = [];
			stations[stationId]["vertical"] = [];

			for (const stationId2 in stations) {
				if (stationId !== stationId2) {
					if (!(stationId in connections)) {
						connections[stationId] = [];
					}
					connections[stationId].push({route: null, station: stationId2, duration: DIRECTIONS.calculateDistance(stations, stationId, stationId2) * 20 / WALKING_SPEED_METER_PER_SECOND});
				}
			}
		}

		for (const routeIndex in routes) {
			const route = routes[routeIndex];
			for (let i = 1; i < route["stations"].length; i++) {
				if (i > 0) {
					const prevStationId = route["stations"][i - 1].split("_")[0];
					if (!(prevStationId in connections)) {
						connections[prevStationId] = [];
					}
					connections[prevStationId].push({route: route, station: route["stations"][i].split("_")[0], duration: route["durations"][i - 1]});
				}
			}
		}

		for (const positionKey in positions) {
			const {x, y, vertical} = positions[positionKey];
			const types = [];
			routes.filter(route => route["stations"].includes(positionKey)).forEach(route => types.push(route["type"]));
			stations[positionKey.split("_")[0]][vertical ? "vertical" : "horizontal"].push({
				x: x,
				y: y,
				color: parseInt(positionKey.split("_")[1]),
				types: types,
			});
		}
	}

	setupRouteTypeAndDimensionButtons();
	drawMap(container, json[SETTINGS.dimension]);
});
const INFO_REFRESH_INTERVAL = 5000;
const FETCH_SERVER_INFO_DATA = new FetchData(() => SETTINGS.infoUrl, INFO_REFRESH_INTERVAL, false, () => true, result => {
	const playerListElement = document.getElementById("player_list");
	playerListElement.innerText = "";

	for (let i = 0; i < result.length; i++) {
		result[i].forEach(playerData => {
			const {player, name, number, destination, circular, color} = playerData;
			const noRoute = name === "" && destination === "";
			playerListElement.innerHTML +=
				`<div class="player text">` +
				`<img src="https://mc-heads.net/avatar/${player}" alt=""/>` +
				`<span style="${noRoute ? "" : "max-width: 6em; min-width: 6em; overflow: hidden"}">&nbsp;&nbsp;&nbsp;${player}</span>` +
				`<div class="player_route" style="display: ${noRoute ? "none" : ""}; border-left: 0.3em solid ${CANVAS.convertColor(color)}">` +
				`<div class="arrival">` +
				`<span class="arrival_text">&nbsp;${number.replace(/\|/g, " ")} ${name.split("||")[0].replace(/\|/g, " ")}</span>` +
				`</div>` +
				`<div class="arrival">` +
				`<span class="material-icons small">${circular === "" ? "chevron_right" : circular === "cw" ? "rotate_right" : "rotate_left"}</span>` +
				`<span class="arrival_text">${destination.replace(/\|/g, " ")}</span>` +
				`</div>` +
				`</div>` +
				`</div>`;
		});

		if (result[i].length > 0) {
			playerListElement.innerHTML += `<div class="spacer"></div>`;
		}
	}

	document.getElementById("settings").style.maxHeight = window.innerHeight - 80 + "px";
});

const resize = () => {
	APP.renderer.resize(window.innerWidth, window.innerHeight);
	background.width = APP.screen.width;
	background.height = APP.screen.height;
	document.getElementById("search").style.maxWidth = window.innerWidth - 32 + "px";
	document.getElementById("station_info").style.maxHeight = window.innerHeight - 80 + "px";
}

const setupRouteTypeAndDimensionButtons = () => {
	const availableTypes = json[SETTINGS.dimension]["types"];
	const oneOrFewerTypes = availableTypes.length <= 1;
	document.getElementById("settings_route_types").style.display = oneOrFewerTypes ? "none" : "";
	if (oneOrFewerTypes) {
		availableTypes.forEach(routeType => SETTINGS.selectedRouteTypes.push(routeType));
	} else if (SETTINGS.selectedRouteTypes.filter(routeType => availableTypes.includes(routeType)).length === 0) {
		SETTINGS.selectedRouteTypes = [availableTypes[0]];
	}

	for (const routeType of Object.keys(SETTINGS.routeTypes)) {
		const element = document.getElementById("settings_route_type_" + routeType);
		element.className = "material-icons clickable " + (SETTINGS.selectedRouteTypes.includes(routeType) ? "selected" : "");
		element.style.display = json[SETTINGS.dimension]["types"].includes(routeType) ? "" : "none";
		element.onclick = () => {
			if (SETTINGS.selectedRouteTypes.includes(routeType)) {
				SETTINGS.selectedRouteTypes = SETTINGS.selectedRouteTypes.filter(checkType => checkType !== routeType);
			} else {
				SETTINGS.selectedRouteTypes.push(routeType);
			}
			setupRouteTypeAndDimensionButtons();
			drawMap(container, json[SETTINGS.dimension]);
		}
	}

	const settingsDimensionsButtons = document.getElementById("settings_dimensions_buttons");
	settingsDimensionsButtons.innerHTML = "";
	let dimensionButtonCount = 0;
	for (const dimensionIndex in json) {
		if (json[dimensionIndex]["routes"].length > 0) {
			const element = document.createElement("span");
			element.id = "toggle_dimension_icon_" + dimensionIndex;
			element.className = "material-icons clickable " + (SETTINGS.dimension === parseInt(dimensionIndex) ? "selected" : "");
			element.innerText = "public"
			element.onclick = () => {
				SETTINGS.dimension = parseInt(dimensionIndex);
				setupRouteTypeAndDimensionButtons();
				drawMap(container, json[SETTINGS.dimension]);
			}
			settingsDimensionsButtons.appendChild(element);
			settingsDimensionsButtons.appendChild(document.createTextNode("\n"));
			dimensionButtonCount++;
		}
	}

	document.getElementById("settings_dimensions").style.display = dimensionButtonCount <= 1 ? "none" : "";
};

const APP = new PIXI.Application({autoResize: true, antialias: true, preserveDrawingBuffer: true, resolution: window.devicePixelRatio});

let json;
let showSettings = false;

if (window.safari !== undefined) {
	PIXI.settings.PREFER_ENV = PIXI.ENV.WEBGL;
}

if (getCookie("theme").includes("dark")) {
	document.body.setAttribute("class", "dark_theme");
	document.getElementById("toggle_theme_icon").innerText = "light_mode";
} else {
	document.getElementById("toggle_theme_icon").innerText = "dark_mode";
}

document.getElementById("clear_search_icon").onclick = () => SETTINGS.onClearSearch(json[SETTINGS.dimension], true);
document.getElementById("zoom_in_icon").onclick = () => CANVAS.onZoom(-1, window.innerWidth / 2, window.innerHeight / 2, container, json[SETTINGS.dimension], false);
document.getElementById("zoom_out_icon").onclick = () => CANVAS.onZoom(1, window.innerWidth / 2, window.innerHeight / 2, container, json[SETTINGS.dimension], false);
document.getElementById("clear_directions_1_icon").onclick = () => {
	document.getElementById("directions_result").style.display = "none";
	const searchBox = document.getElementById("directions_box_1");
	searchBox.value = "";
	searchBox.focus();
};
document.getElementById("clear_directions_2_icon").onclick = () => {
	document.getElementById("directions_result").style.display = "none";
	const searchBox = document.getElementById("directions_box_2");
	searchBox.value = "";
	searchBox.focus();
};
document.getElementById("toggle_text_icon").onclick = event => {
	const buttonElement = event.target;
	if (buttonElement.innerText.includes("off")) {
		buttonElement.innerText = "font_download";
		SETTINGS.showText = false;
	} else {
		buttonElement.innerText = "font_download_off";
		SETTINGS.showText = true;
	}
	drawMap(container, json[SETTINGS.dimension]);
};
document.getElementById("toggle_theme_icon").onclick = event => {
	const buttonElement = event.target;
	if (buttonElement.innerText.includes("dark")) {
		document.body.setAttribute("class", "dark_theme");
		buttonElement.innerText = "light_mode";
		setCookie("theme", "dark");
	} else {
		document.body.removeAttribute("class");
		buttonElement.innerText = "dark_mode";
		setCookie("theme", "light");
	}
	background.tint = SETTINGS.getColorStyle("--backgroundColor");
	CANVAS.clearTextCache();
	drawMap(container, json[SETTINGS.dimension]);
};
document.getElementById("download_icon").onclick = () => {
	const link = document.createElement("a");
	link.download = document.title + ".png";
	link.href = document.getElementById("canvas").toDataURL("image/png");
	link.click();
};
document.getElementById("settings_icon").onclick = () => {
	const newShowSettings = !showSettings;
	SETTINGS.onClearSearch(json[SETTINGS.dimension], false);
	SETTINGS.clearPanes();
	document.getElementById("settings").style.display = newShowSettings ? "" : "none";
	showSettings = newShowSettings;
};
document.getElementById("clear_station_info_button").onclick = SETTINGS.clearPanes;
document.getElementById("clear_route_info_button").onclick = SETTINGS.clearPanes;
document.getElementById("version").innerText = VERSION;

window.addEventListener("resize", resize);
const background = new PIXI.Sprite(PIXI.Texture.WHITE);
resize();

background.tint = SETTINGS.getColorStyle("--backgroundColor");
panable(background);
pinchable(background, true);
tappable(background);
background.on("panmove", event => CANVAS.onCanvasMouseMove(event, container));
background.on("pinchmove", event => CANVAS.onPinch(event.data.global, event.center.x, event.center.y, container, json[SETTINGS.dimension], true));
background.on("simpletap", () => {
	SETTINGS.clearPanes();
	drawMap(container, json[SETTINGS.dimension]);
});

APP.stage.addChild(background);

const container = new PIXI.Container();
APP.stage.addChild(container);

document.body.prepend(APP.view);
APP.view.addEventListener("wheel", event => CANVAS.onZoom(Math.abs(event.deltaY) > 1 ? event.deltaY / SETTINGS.smoothScrollScale : 0, event.offsetX, event.offsetY, container, json[SETTINGS.dimension], false));
APP.view.setAttribute("id", "canvas");
APP.ticker.add(delta => CANVAS.update(delta, container));

FETCH_DATA.fetchData();
FETCH_SERVER_INFO_DATA.fetchData();

function setCookie(name, value) {
	document.cookie = name + "=" + value + ";expires=Fri, 31 Dec 9999 23:59:59 GMT;path=/";
}

function getCookie(name) {
	const nameFind = name + "=";
	const cookiesSplit = document.cookie.split(';');
	for (let cookie of cookiesSplit) {
		while (cookie.charAt(0) === " ") {
			cookie = cookie.substring(1);
		}
		if (cookie.indexOf(nameFind) === 0) {
			return cookie.substring(nameFind.length, cookie.length);
		}
	}
	return "";
}

export default SETTINGS;
