import VERSION from "./version.js";
import DATA from "./data.js";
import SETTINGS from "./settings.js";
import DRAWING from "./drawing.js";

const DOCUMENT = {
	clearPanes: clearSelection => {
		if (clearSelection) {
			SETTINGS.selectedStation = 0;
			SETTINGS.selectedRoutes = [];
			SETTINGS.selectedDirectionsStations = [];
			SETTINGS.selectedDirectionsSegments = {};
			DATA.redraw();
		}
		showSettings = false;
		document.getElementById("station_info").style.display = "none";
		document.getElementById("route_info").style.display = "none";
		document.getElementById("directions").style.display = "none";
		document.getElementById("settings").style.display = "none";
	},
	onClearSearch: focus => {
		const searchBox = document.getElementById("search_box");
		searchBox.value = "";
		if (focus) {
			searchBox.focus();
		}
		document.getElementById("clear_search_icon").style.display = "none";
		// const {stations, routes} = DATA.json[SETTINGS.dimension];
		// for (const stationId in stations) {
		// 	document.getElementById(stationId).style.display = "none";
		// }
		// for (const index in routes) {
		// 	document.getElementById(routes[index]["color"]).style.display = "none";
		// }
	},
};

const getCookie = name => {
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
};
const setCookie = (name, value) => document.cookie = name + "=" + value + ";expires=Fri, 31 Dec 9999 23:59:59 GMT;path=/";

let showSettings = false;

if (getCookie("theme").includes("dark")) {
	document.body.setAttribute("class", "dark_theme");
	document.getElementById("toggle_theme_icon").innerText = "light_mode";
} else {
	document.getElementById("toggle_theme_icon").innerText = "dark_mode";
}

document.getElementById("clear_search_icon").onclick = () => DOCUMENT.onClearSearch(true);
document.getElementById("zoom_in_icon").onclick = () => DRAWING.zoom(-500, window.innerWidth / 2, window.innerHeight / 2);
document.getElementById("zoom_out_icon").onclick = () => DRAWING.zoom(500, window.innerWidth / 2, window.innerHeight / 2);
document.getElementById("toggle_text_icon").onclick = event => {
	const buttonElement = event.target;
	if (buttonElement.innerText.includes("off")) {
		buttonElement.innerText = "font_download";
		SETTINGS.showText = false;
	} else {
		buttonElement.innerText = "font_download_off";
		SETTINGS.showText = true;
	}
	DATA.redraw();
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
	DATA.redraw();
};
document.getElementById("settings_icon").onclick = () => {
	const newShowSettings = !showSettings;
	DOCUMENT.onClearSearch(false);
	DOCUMENT.clearPanes(false);
	document.getElementById("settings").style.display = newShowSettings ? "" : "none";
	showSettings = newShowSettings;
};
document.getElementById("clear_station_info_button").onclick = DOCUMENT.clearPanes;
document.getElementById("clear_route_info_button").onclick = DOCUMENT.clearPanes;
document.getElementById("version").innerText = VERSION;

export default DOCUMENT;
