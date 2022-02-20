import CANVAS from "./utilities.js";
import SETTINGS from "./index.js";

let stationStart = 0;
let stationEnd = 0;
let pathStations = [];
let pathRoutes = [];
let globalBlacklistedStations = [];

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
};

const findRoutePart = (data, reverse, maxTime) => {
	const newStationStart = reverse ? stationEnd : stationStart;
	const newStationEnd = reverse ? stationStart : stationEnd;
	const {routes, positions, stations} = data;
	if (!(newStationStart in stations) || !(newStationEnd in stations)) {
		return [[], []];
	}
	const positionEnd = positions[Object.keys(positions).find(position => position.split("_")[0] === newStationEnd)];

	const getCloserStationWithRoute = () => {
		const currentStation = pathStations[pathStations.length - 1];
		const currentRoute = pathRoutes[pathRoutes.length - 1];
		let closestStation = 0;
		let biggestIncrease = Number.MIN_SAFE_INTEGER;
		let routeUsed = {};
		let duration = 0;
		let availableRoutes = 0;

		for (const route of routes) {
			const routeStations = route["stations"].map(station => station.split("_")[0]);
			const index = routeStations.indexOf(currentStation);
			if (index >= (reverse ? 1 : 0) && index < routeStations.length - (reverse ? 0 : 1)) {
				const checkStation = routeStations[index + (reverse ? -1 : 1)];
				const time = route["durations"][index];
				if (time > 0) {
					if (checkStation === newStationEnd) {
						return [newStationEnd, route, time, false];
					} else if (!globalBlacklistedStations.includes(checkStation + "_" + route["name"]) && !blacklistedStations.includes(checkStation)) {
						const currentPosition = positions[route["stations"][index]];
						const currentDistance = Math.abs(positionEnd["x"] - currentPosition["x"]) + Math.abs(positionEnd["y"] - currentPosition["y"]);
						const newPosition = positions[route["stations"][index + (reverse ? -1 : 1)]];
						const newDistance = Math.abs(positionEnd["x"] - newPosition["x"]) + Math.abs(positionEnd["y"] - newPosition["y"]);
						const interchangePenalty = route !== currentRoute ? 100 : 0; // TODO actually calculate interchange cost
						const increase = (currentDistance - newDistance) / (time + interchangePenalty);
						if (increase > biggestIncrease) {
							closestStation = checkStation;
							biggestIncrease = increase;
							routeUsed = route;
							duration = time + interchangePenalty;
						}
						availableRoutes++;
					}
				}
			}
		}

		return [closestStation, routeUsed, duration, availableRoutes > 1];
	}

	const pathRoutes = [];
	const pathStations = [newStationStart];
	const times = [];
	const blacklistedStations = [newStationStart];
	let notBlacklistedOneStation = true;

	while (!pathStations.includes(newStationEnd)) {
		const newStationWithRoute = getCloserStationWithRoute();

		if (newStationWithRoute[0] === 0) {
			pathStations.pop();
			if (pathStations.length === 0) {
				break;
			}
			pathRoutes.pop();
			times.pop();
		} else {
			pathStations.push(newStationWithRoute[0]);
			pathRoutes.push(newStationWithRoute[1]);
			times.push(newStationWithRoute[2]);
			blacklistedStations.push(newStationWithRoute[0]);
			if (notBlacklistedOneStation && newStationWithRoute[3]) {
				notBlacklistedOneStation = false;
				globalBlacklistedStations.push(newStationWithRoute[0] + "_" + newStationWithRoute[1]["name"]);
			}
			if (times.reduce((sum, time) => sum + time, 0) > maxTime) {
				break;
			}
		}
	}

	if (reverse) {
		pathStations.reverse();
		pathRoutes.reverse();
		times.reverse();
	}

	return [pathStations, pathRoutes, times];
}

const findRoute = data => {
	pathStations = [];
	pathRoutes = [];
	globalBlacklistedStations = [];

	const millis = Date.now();
	let reverse = false;
	let failedTimes = 0;
	let tries = 0;
	let totalTime = Number.MAX_SAFE_INTEGER;
	let previousTime1 = 0;
	let previousTime2 = 0;
	let repeatedTime = 0;
	let times = [];

	while (failedTimes < 10 && repeatedTime < 10 && tries < 500) {
		const path = findRoutePart(data, reverse, totalTime);
		const time = path[2].reduce((sum, time) => sum + time, 0);
		tries++;

		if (time === 0) {
			failedTimes++;
		} else {
			if (time < totalTime) {
				totalTime = time;
				pathStations = path[0];
				pathRoutes = path[1];
				times = path[2];
			}

			failedTimes = 0;

			if (time === previousTime1 || time === previousTime2) {
				repeatedTime++;
			} else {
				repeatedTime = 0;
			}
		}

		reverse = !reverse;
		previousTime2 = previousTime1;
		previousTime1 = time;
	}

	if (times.length > 0) {
		console.log(`Found the best path on attempt ${tries} in ${Date.now() - millis} ms`);
	}

	const hasRoute = pathStations.length > 0 && pathStations.length > pathRoutes.length && times.length === pathRoutes.length;
	const resultElement = document.getElementById("directions_result_route");
	resultElement.innerHTML = "";

	if (hasRoute) {
		let route = null;
		for (let i = 0; i < pathRoutes.length; i++) {
			if (route !== pathRoutes[i]) {
				let tempRoute = pathRoutes[i];
				let stationCount = 0;
				let time = 0;
				while (tempRoute === pathRoutes[i]) {
					time += times[i + stationCount];
					stationCount++;
					tempRoute = pathRoutes[i + stationCount];
				}

				const newColor = pathRoutes[i]["color"];
				resultElement.append(CANVAS.getDrawStationElement(createStationElement(data["stations"][pathStations[i]]["name"].replace(/\|/g, " ")), route == null ? null : route["color"], newColor));

				const routeNameElement = document.createElement("span");
				routeNameElement.innerHTML = pathRoutes[i]["name"].split("||")[0].replace(/\|/g, " ");
				resultElement.append(CANVAS.getDrawLineElement(SETTINGS.routeTypes[pathRoutes[i]["type"]], routeNameElement, newColor));

				const stationCountElement = document.createElement("span");
				stationCountElement.innerHTML = stationCount.toString();
				resultElement.append(CANVAS.getDrawLineElement("commit", stationCountElement, newColor));

				const durationElement = document.createElement("span");
				durationElement.innerHTML = CANVAS.formatTime(time / 20);
				resultElement.append(CANVAS.getDrawLineElement("schedule", durationElement, newColor));

				route = pathRoutes[i];
			}
		}

		const firstStation = data["stations"][pathStations[0]];
		const lastStation = data["stations"][pathStations[pathStations.length - 1]];
		resultElement.append(CANVAS.getDrawStationElement(createStationElement(lastStation["name"].replace(/\|/g, " ")), route == null ? null : route["color"], null));

		const infoElement = document.createElement("div");
		infoElement.className = "info_center";
		infoElement.innerHTML +=
			`<div class="info_middle">` +
			`<span class="material-icons small">schedule</span>` +
			`<span class="text">${CANVAS.formatTime(totalTime / 20)}</span>` +
			"&nbsp;&nbsp;&nbsp;" +
			`<span class="material-icons small">confirmation_number</span>` +
			`<span class="text">$${Math.abs(lastStation["zone"] - firstStation["zone"]) + 2}</span>` +
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
