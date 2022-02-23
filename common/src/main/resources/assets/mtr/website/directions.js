import CANVAS from "./utilities.js";
import SETTINGS from "./index.js";

let stationStart = 0;
let stationEnd = 0;
let pathStations = [];
let pathRoutes = [];

const DIRECTIONS = {
	onSearch: (index, data) => {
		const searchBox = document.getElementById("directions_box_" + index);
		const search = searchBox.value.toLowerCase().replace(/\|/g, " ");
		document.getElementById("clear_directions_" + index + "_icon").innerText = search === "" ? "" : "clear";
		document.getElementById("directions_result").style.display = "none";
		index === 1 ? stationStart = 0 : stationEnd = 0;

		const stations = data["stations"];

		const resultsStations = search === "" ? [] : Object.keys(stations).filter(station => stations[station]["name"].replace(/\|/g, " ").toLowerCase().includes(search));
		for (const stationId in stations) {
			document.getElementById("directions_" + index + "_" + stationId).style.display = resultsStations.includes(stationId) ? "block" : "none";
		}

		document.getElementById("search_results_stations_" + index).style.maxHeight = (Math.max(window.innerHeight - 320, 64)) / 4 + "px";
		document.getElementById("directions").style.maxHeight = window.innerHeight - 80 + "px";
	},
	onSelectStation: (index, stationId, data) => {
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
			document.getElementById("directions_result_route").innerHTML = `<div class="info_center"><span class="material-icons large">refresh</span></div>`;
			setTimeout(() => findRoute(data), 500);
		}
	},
	writeStationsInResult: (index, data) => {
		const elementDirectionsStations = document.getElementById("search_results_stations_" + index);
		elementDirectionsStations.innerHTML = "";
		for (const stationId in data["stations"]) {
			const element = document.createElement("div");
			element.innerText = data["stations"][stationId]["name"].replace(/\|/g, " ");
			element.id = "directions_" + index + "_" + stationId;
			element.className = "text clickable";
			element.style.display = "none";
			element.onclick = () => DIRECTIONS.onSelectStation(index, stationId, data);
			elementDirectionsStations.append(element);
		}
	},
	calculateDistance: (stations, stationId1, stationId2) => {
		const station1 = stations[stationId1];
		const station2 = stations[stationId2];
		return Math.abs(station2["x"] - station1["x"]) + Math.abs(station2["z"] - station1["z"]);
	},
};

const findRoutePart = (data, globalBlacklist, maxTime) => {
	const {stations, connections} = data;
	if (!(stationStart in stations) || !(stationEnd in stations)) {
		return [[], [], []];
	}

	const getCloserStationWithRoute = elapsedTime => {
		const currentStation = tempPathStations[tempPathStations.length - 1];
		const currentRoute = tempPathRoutes[tempPathRoutes.length - 1];
		let bestStation = 0;
		let bestIncrease = Number.MIN_SAFE_INTEGER;
		let bestRoute = {};
		let bestDuration = 0;

		for (const connection of connections[currentStation]) {
			const checkStation = connection["station"];
			const route = connection["route"];
			const interchangePenalty = route !== currentRoute ? 10 : 0; // TODO actually calculate interchange cost
			const duration = connection["duration"] + interchangePenalty;

			if (duration > interchangePenalty && elapsedTime + duration < maxTime) {
				if (checkStation === stationEnd) {
					return [stationEnd, route, duration];
				} else if (
					(!(checkStation in localBlacklist) || elapsedTime + duration < localBlacklist[checkStation]) &&
					(!(checkStation in globalBlacklist) || elapsedTime + duration <= globalBlacklist[checkStation])
				) {
					const increase = (DIRECTIONS.calculateDistance(stations, stationEnd, currentStation) - DIRECTIONS.calculateDistance(stations, stationEnd, checkStation)) / duration;
					globalBlacklist[checkStation] = elapsedTime + duration;

					if (increase > bestIncrease) {
						bestStation = checkStation;
						bestIncrease = increase;
						bestRoute = route;
						bestDuration = duration;
					}
				}
			}
		}

		localBlacklist[bestStation] = elapsedTime + bestDuration;
		return [bestStation, bestRoute, bestDuration];
	}

	const tempPathRoutes = [];
	const tempPathStations = [stationStart];
	const times = [];
	const localBlacklist = {};

	while (!tempPathStations.includes(stationEnd)) {
		const elapsedTime = times.reduce((sum, time) => sum + time, 0);
		const newStationWithRoute = getCloserStationWithRoute(elapsedTime);

		if (newStationWithRoute[0] === 0) {
			tempPathStations.pop();
			if (tempPathStations.length === 0) {
				break;
			}
			tempPathRoutes.pop();
			times.pop();
		} else {
			tempPathStations.push(newStationWithRoute[0]);
			tempPathRoutes.push(newStationWithRoute[1]);
			times.push(newStationWithRoute[2]);
		}
	}

	return [tempPathStations, tempPathRoutes, times];
}

const findRoute = data => {
	pathStations = [];
	pathRoutes = [];

	const millis = Date.now();
	const globalBlacklist = {};
	globalBlacklist[stationStart] = 0;
	let tries = 0;
	let totalTime = Number.MAX_SAFE_INTEGER;
	let times = [];

	while (tries < 500) {
		const path = findRoutePart(data, globalBlacklist, totalTime);
		tries++;

		if (path[0].length === 0) {
			break;
		} else {
			const time = path[2].reduce((sum, time) => sum + time, 0);
			if (time < totalTime) {
				totalTime = time;
				pathStations = path[0];
				pathRoutes = path[1];
				times = path[2];
			}
		}
	}

	if (times.length > 0) {
		console.log(`Found the best path on attempt ${tries} in ${Date.now() - millis} ms`);
	}

	const hasRoute = pathStations.length > 0 && pathStations.length > pathRoutes.length && times.length === pathRoutes.length;
	const resultElement = document.getElementById("directions_result_route");
	const stations = data["stations"];
	resultElement.innerHTML = "";

	if (hasRoute) {
		let route = pathRoutes[0];
		let station = pathStations[0];
		let totalStationCount = 0;
		let totalInterchangeCount = 0;
		let startZone = stations[station]["zone"];
		let fare = 0;
		let time = 0;
		let stationCount = 0;

		resultElement.append(CANVAS.getDrawStationElement(createStationElement(stations[station]["name"].replace(/\|/g, " ")), null, route == null ? null : route["color"]));

		for (let i = 1; i <= pathRoutes.length; i++) {
			const nextRoute = pathRoutes[i];
			time += times[i - 1];
			stationCount++;

			if (route !== nextRoute) {
				const nextStation = pathStations[i];
				const isWalking = route == null;
				const isNextWalking = nextRoute == null;
				const color = isWalking ? null : route["color"];
				const routeNameElement = document.createElement("span");
				routeNameElement.innerHTML = isWalking ? Math.round(DIRECTIONS.calculateDistance(stations, station, nextStation) / 100) / 10 + " km" : route["name"].split("||")[0].replace(/\|/g, " ");
				resultElement.append(CANVAS.getDrawLineElement(isWalking ? "directions_walk" : SETTINGS.routeTypes[route["type"]], routeNameElement, color));

				if (isWalking) {
					startZone = stations[nextStation]["zone"];
				} else {
					const stationCountElement = document.createElement("span");
					stationCountElement.innerHTML = stationCount.toString();
					resultElement.append(CANVAS.getDrawLineElement("commit", stationCountElement, color));

					if (isNextWalking) {
						fare += Math.abs(startZone - stations[nextStation]["zone"]) + 2;
					}
				}

				const durationElement = document.createElement("span");
				durationElement.innerHTML = CANVAS.formatTime(time / 20);
				resultElement.append(CANVAS.getDrawLineElement("schedule", durationElement, color));
				resultElement.append(CANVAS.getDrawStationElement(createStationElement(stations[nextStation]["name"].replace(/\|/g, " ")), color, nextRoute == null ? null : nextRoute["color"]));

				route = nextRoute;
				station = nextStation;
				totalStationCount += stationCount;
				time = 0;
				stationCount = 0;
				totalInterchangeCount++;
			}
		}

		const infoElement = document.createElement("div");
		infoElement.className = "info_center";
		infoElement.innerHTML +=
			`<div class="info_middle">` +
			`<span class="material-icons small">schedule</span>` +
			`<span class="text">${CANVAS.formatTime(totalTime / 20)}</span>` +
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
	} else {
		resultElement.innerHTML = `<div class="info_center"><span class="material-icons large">remove_road</span></div>`;
	}

	document.getElementById("directions").style.maxHeight = window.innerHeight - 80 + "px";
	SETTINGS.drawDirectionsRoute(pathStations, pathRoutes, true);
};

const createStationElement = stationName => {
	const stationElement = document.createElement("div");
	stationElement.innerText = stationName;
	stationElement.className = "text";
	return stationElement;
}

document.getElementById("directions_icon").onclick = () => {
	SETTINGS.clearPanes();
	SETTINGS.drawDirectionsRoute(pathStations, pathRoutes);
	document.getElementById("directions").style.display = "block";
};

export default DIRECTIONS;
