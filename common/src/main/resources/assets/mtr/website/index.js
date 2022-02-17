import CANVAS from "./utilities.js";
import drawMap from "./drawing.js";
import panable from "./gestures/src/gestures/pan.js";
import pinchable from "./gestures/src/gestures/pinch.js";
import tappable from "./gestures/src/gestures/tap.js";

const URL = document.location.origin + document.location.pathname.replace("index.html", "");
const SETTINGS = {
	dataUrl: URL + "data",
	arrivalsUrl: URL + "arrivals",
	refreshDataInterval: 60000,
	refreshArrivalsInterval: 5000,
	routeTypes: {
		"train_normal": "directions_train",
		"train_light_rail": "tram",
		"train_high_speed": "train",
		"boat_normal": "sailing",
		"boat_light_rail": "directions_boat",
		"boat_high_speed": "snowmobile",
	},
	lineSize: 6,
	dimension: 0,
	selectedRouteTypes: [],
	selectedColor: -1,
	selectedStation: 0,
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
		document.getElementById("clear_search_icon").innerText = "";
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
		showSettings = false;
		document.getElementById("station_info").style.display = "none";
		document.getElementById("route_info").style.display = "none";
		document.getElementById("settings").style.display = "none";
	}
};

const fetchMainData = () => {
	clearTimeout(refreshDataId);
	fetch(SETTINGS.dataUrl, {cache: "no-cache"}).then(response => response.json()).then(result => {
		json = result;
		setupRouteTypeAndDimensionButtons();
		drawMap(container, json[SETTINGS.dimension]);
		refreshDataId = setTimeout(fetchMainData, SETTINGS.refreshDataInterval);
	});
}

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
		element.className = "material-icons clickable_icon " + (SETTINGS.selectedRouteTypes.includes(routeType) ? "selected" : "");
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
			element.className = "material-icons clickable_icon " + (SETTINGS.dimension === parseInt(dimensionIndex) ? "selected" : "");
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
}

const APP = new PIXI.Application({autoResize: true, antialias: true, preserveDrawingBuffer: true});

let json;
let refreshDataId = 0;
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
	SETTINGS.selectedColor = -1;
	SETTINGS.clearPanes();
	drawMap(container, json[SETTINGS.dimension]);
});

APP.stage.addChild(background);

const container = new PIXI.Container();
APP.stage.addChild(container);

document.body.appendChild(APP.view);
APP.view.addEventListener("wheel", event => CANVAS.onZoom(Math.abs(event.deltaY) > 1 ? event.deltaY / SETTINGS.smoothScrollScale : 0, event.offsetX, event.offsetY, container, json[SETTINGS.dimension], false));
APP.view.setAttribute("id", "canvas");
APP.ticker.add(delta => CANVAS.update(delta, container));

fetchMainData();

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
