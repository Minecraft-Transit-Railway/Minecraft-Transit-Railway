import UTILITIES from "./utilities.js";
import VERSION from "./version.js";
import DATA from "./data.js";

const DOCUMENT = {
	clearPanes: () => {
		UTILITIES.selectedStation = 0;
		UTILITIES.selectedRoutes = [];
		UTILITIES.selectedDirectionsStations = [];
		UTILITIES.selectedDirectionsSegments = {};
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
	DATA.parseMTR(DATA.json);
};
document.getElementById("settings_icon").onclick = () => {
	const newShowSettings = !showSettings;
	DOCUMENT.onClearSearch(false);
	DOCUMENT.clearPanes();
	document.getElementById("settings").style.display = newShowSettings ? "" : "none";
	showSettings = newShowSettings;
};
document.getElementById("clear_station_info_button").onclick = DOCUMENT.clearPanes;
document.getElementById("clear_route_info_button").onclick = DOCUMENT.clearPanes;
document.getElementById("version").innerText = VERSION;

export default DOCUMENT;
