import UTILITIES from "./utilities.js";
import DOCUMENT from "./document.js";
import ACTIONS from "./actions.js";
import DATA from "./data.js";
import SETTINGS from "./settings.js";
import FetchData from "./fetch.js";

const DATA_REFRESH_INTERVAL = 5000;
let shouldFetch = false;
let stationStart = 0;
let stationEnd = 0;
let pathStations = {};
let pathRoutes = [];

const DIRECTIONS = {
	onSearch: index => {
		shouldFetch = false;
		const stations = DATA.json[SETTINGS.dimension]["stations"];
		const searchBox = document.getElementById("directions_box_" + index);
		const search = searchBox.value.toLowerCase().replace(/\|/g, " ");
		document.getElementById("clear_directions_" + index + "_icon").innerText = search === "" ? "" : "clear";
		document.getElementById("directions_result").style.display = "none";
		index === 1 ? stationStart = 0 : stationEnd = 0;

		const resultsStations = search === "" ? [] : Object.keys(stations).filter(station => stations[station]["name"].replace(/\|/g, " ").toLowerCase().includes(search));
		for (const stationId in stations) {
			document.getElementById("directions_" + index + "_" + stationId).style.display = resultsStations.includes(stationId) ? "block" : "none";
		}

		document.getElementById("search_results_stations_" + index).style.maxHeight = (Math.max(window.innerHeight - 320, 64)) / 4 + "px";
		document.getElementById("directions").style.maxHeight = window.innerHeight - 80 + "px";
	},
	onSelectStation: (index, stationId) => {
		const data = DATA.json[SETTINGS.dimension];
		document.getElementById("clear_directions_" + index + "_icon").innerText = "clear";
		document.getElementById("directions_box_" + index).value = data["stations"][stationId]["name"].replace(/\|/g, " ");
		for (const stationId in data["stations"]) {
			document.getElementById("directions_" + index + "_" + stationId).style.display = "none";
		}
		if (index === 1) {
			stationStart = stationId;
		} else {
			stationEnd = stationId;
		}
		if (stationStart !== 0 && stationEnd !== 0) {
			document.getElementById("directions_result").style.display = "";
			document.getElementById("directions_result_route").innerHTML = `<div class="info_center"><span class="material-icons large">${stationStart === stationEnd ? "horizontal_rule" : "refresh"}</span></div>`;
			shouldFetch = true;
			FETCH_DATA.fetchData();
		}
	},
	writeStationsInResult: index => {
		const data = DATA.json[SETTINGS.dimension];
		const elementDirectionsStations = document.getElementById("search_results_stations_" + index);
		elementDirectionsStations.innerHTML = "";
		for (const stationId in data["stations"]) {
			const element = document.createElement("div");
			element.innerText = data["stations"][stationId]["name"].replace(/\|/g, " ");
			element.id = "directions_" + index + "_" + stationId;
			element.className = "text clickable";
			element.style.display = "none";
			element.onclick = () => DIRECTIONS.onSelectStation(index, stationId);
			elementDirectionsStations.append(element);
		}
	},
	calculateDistance: (x1, z1, x2, y2) => Math.abs(x2 - x1) + Math.abs(y2 - z1),
	drawDirectionsRoute: (pathStations, pathRoutes) => {
		if (pathStations.length > 0 || Object.keys(pathRoutes).length > 0) {
			SETTINGS.selectedRoutes = [];
		}
		SETTINGS.selectedDirectionsStations = pathStations;
		SETTINGS.selectedDirectionsSegments = pathRoutes;
		DATA.redraw();
	},
	stopRefresh: () => shouldFetch = false,
};

const FETCH_DATA = new FetchData(() => `${SETTINGS.url}route?dimension=${SETTINGS.dimension}&startStation=${stationStart}&endStation=${stationEnd}`, DATA_REFRESH_INTERVAL, false, () => shouldFetch && stationStart !== 0 && stationEnd !== 0 && stationStart !== stationEnd, data => {
	const {start, directions} = data;
	const resultElement = document.getElementById("directions_result_route");
	resultElement.innerHTML = "";
	pathStations = [];
	pathRoutes = [];

	const getData = (index, key1, key2) => {
		if (index < directions.length) {
			const direction = directions[index];
			if (key1 in direction) {
				return direction[key1][key2];
			}
		}
		return null;
	};
	const getStationData = (stationId, key) => {
		const stations = DATA.json[SETTINGS.dimension]["stations"];
		if (stationId in stations) {
			return stations[stationId][key];
		} else {
			return null;
		}
	};
	const getTimeString = time => new Date(Date.now() + time * 50).toLocaleTimeString();
	const getRouteDestination = (routeId, stationId1, stationId2) => {
		const routes = DATA.json[SETTINGS.dimension]["routes"];
		const route = routes.find(route => {
			const routeStations = route["stations"];
			let passed = -1;
			for (let i = 0; i < routeStations.length; i++) {
				if (routeStations[i] === `${stationId1}_${routeId}`) {
					passed = i + 1;
				} else if (passed === i && routeStations[i] === `${stationId2}_${routeId}`) {
					return true;
				}
			}
			return false;
		});
		if (route) {
			if (route["circular"] === "") {
				const routeStations = route["stations"];
				return getStationData(routeStations[routeStations.length - 1].split("_")[0], "name").replace(/\|/g, " ");
			} else {
				return ACTIONS.getClosestInterchangeOnRoute(route, stationId1).replace(/\|/g, " ");
			}
		} else {
			return null;
		}
	};

	let totalStationCount = 0;
	let totalInterchangeCount = 0;
	let fare = 0;
	let zone = null;
	let totalDuration = 0;
	let posX = start["pos"][0];
	let posZ = start["pos"][2];
	let stationId = start["station"]["id"];
	const firstColor = getData(0, "route", "color");
	const initialWait = getData(firstColor == null ? 1 : 0, "route", "wait");

	const drawStartStation = getData(0, "station", "id") !== stationId;
	if (drawStartStation) {
		pathStations.push(stationId);
		const stationElement = document.createElement("div");
		stationElement.className = "route_station_name_inner";
		stationElement.innerHTML =
			`<div class="route_segment_name">${start["station"]["name"].replace(/\|/g, " ")}</div>` +
			`<div class="route_segment_time">${getTimeString(totalDuration + initialWait)}</div>`;
		resultElement.append(UTILITIES.getDrawStationElement(stationElement, null, firstColor));
	}

	for (let i = 0; i < directions.length; i++) {
		const direction = directions[i];
		const duration = direction["duration"];
		const newX = direction["pos"][0];
		const newZ = direction["pos"][2];
		const hasRoute = "route" in direction;
		const thisStationId = direction["station"]["id"];
		const isInterchange = thisStationId === stationId;
		let routeColor = null;
		pathStations.push(thisStationId);

		if (i > 0 || drawStartStation) {
			let waitTime = 0;

			if (hasRoute) {
				const route = direction["route"];
				const routeStations = route["stations"];
				const routeType = route["type"];
				const stationCount = routeStations.length;
				totalStationCount += stationCount;
				totalInterchangeCount++;
				routeColor = route["color"];
				waitTime = route["wait"];

				for (let j = 0; j < routeStations.length; j++) {
					const passingStationId = routeStations[j];
					pathStations.push(passingStationId);
					if (!(routeColor in pathRoutes)) {
						pathRoutes[routeColor] = [];
					}
					if (j === 0) {
						pathRoutes[routeColor].push(`${stationId}_${routeStations[j]}`);
					} else {
						pathRoutes[routeColor].push(`${routeStations[j - 1]}_${routeStations[j]}`);
					}
				}

				if (!SETTINGS.selectedRouteTypes.includes(routeType)) {
					SETTINGS.selectedRouteTypes.push(routeType);
				}

				resultElement.append(UTILITIES.getDrawLineElement("commit", createTextElement(stationCount.toString()), routeColor));
			} else {
				resultElement.append(UTILITIES.getDrawLineElement(isInterchange ? "transfer_within_a_station" : "directions_walk", createTextElement(isInterchange ? "" : `${Math.round(DIRECTIONS.calculateDistance(posX, posZ, newX, newZ) / 100) / 10} km`), null));
				totalStationCount++;
			}

			if (!isInterchange) {
				resultElement.append(UTILITIES.getDrawLineElement("schedule", createTextElement(UTILITIES.formatTime((duration - waitTime) / 20)), routeColor));
			}
		}

		totalDuration += duration;

		const nextColor = getData(i + 1, "route", "color");
		const stationElement = document.createElement("div");
		stationElement.className = "route_station_name_inner";
		stationElement.innerHTML =
			`<div class="route_segment_name">${direction["station"]["name"].replace(/\|/g, " ")}</div>` +
			`<div class="route_segment_time">${getTimeString(totalDuration + (nextColor == null ? 0 : getData(i + 1, "route", "wait")))}</div>`;
		resultElement.append(UTILITIES.getDrawStationElement(stationElement, routeColor, nextColor));

		const platform = direction["platform"];
		const nextStationId = getData(i + 1, "station", "id");
		if (!hasRoute && nextColor != null && platform) {
			resultElement.append(UTILITIES.getDrawLineElement(UTILITIES.routeTypes[getData(i + 1, "route", "type")], createTextElement(getData(i + 1, "route", "name").split("||")[0].replace(/\|/g, " ")), nextColor));
			const circular = getData(i + 1, "route", "circular");
			const routeNumber = getData(i + 1, "route", "number").replace(/\|/g, " ");
			resultElement.append(UTILITIES.getDrawLineElement(circular === "" ? "" : circular === "cw" ? "rotate_right" : "rotate_left", createTextElement((routeNumber ? routeNumber + " " : "") + getRouteDestination(nextColor, thisStationId, getData(i + 1, "route", "stations")[0])), nextColor, `<span class="platform_circle" style="background: ${UTILITIES.convertColor(nextColor)}">${platform}</span>`));
		}

		const thisZone = getStationData(thisStationId, "zone");
		if (nextColor != null && zone == null) {
			zone = thisZone;
		} else if (nextColor == null && zone != null && nextStationId !== thisStationId) {
			fare += Math.abs(zone - thisZone) + 2;
			zone = null;
		}

		posX = newX;
		posZ = newZ;
		stationId = thisStationId;
	}

	const infoElement = document.createElement("div");
	infoElement.className = "info_center";
	infoElement.innerHTML +=
		`<div class="info_middle">` +
		`<span class="material-icons small">schedule</span>` +
		`<span class="text">${UTILITIES.formatTime((totalDuration - initialWait) / 20)}</span>` +
		"&nbsp;&nbsp;&nbsp;" +
		`<span class="material-icons small">confirmation_number</span>` +
		`<span class="text">$${fare}</span>` +
		"&nbsp;&nbsp;&nbsp;" +
		`<span class="material-icons small">commit</span>` +
		`<span class="text">${totalStationCount}</span>` +
		"&nbsp;&nbsp;&nbsp;" +
		`<span class="material-icons small">transfer_within_a_station</span>` +
		`<span class="text">${Math.max(0, totalInterchangeCount - 1)}</span>` +
		`</div>`;
	resultElement.append(infoElement);

	document.getElementById("directions").style.maxHeight = window.innerHeight - 80 + "px";
	DIRECTIONS.drawDirectionsRoute(pathStations, pathRoutes);
});

const createTextElement = text => {
	const element = document.createElement("div");
	element.innerText = text;
	return element;
};

document.getElementById("directions_icon").onclick = () => {
	DOCUMENT.clearPanes(true);
	DIRECTIONS.drawDirectionsRoute(pathStations, pathRoutes);
	document.getElementById("directions").style.display = "block";
	shouldFetch = true;
	FETCH_DATA.fetchData();
};

export default DIRECTIONS;
