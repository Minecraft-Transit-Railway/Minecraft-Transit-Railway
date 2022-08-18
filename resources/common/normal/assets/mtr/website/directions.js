import UTILITIES from "./utilities.js";
import DOCUMENT from "./document.js";
import ACTIONS from "./actions.js";
import DATA from "./data.js";
import SETTINGS from "./settings.js";

// TODO get directions serverside

let stationStart = 0;
let stationEnd = 0;
let pathStations = [];
let pathRoutes = [];

const DIRECTIONS = {
	onSearch: index => {
		const data = DATA.json[SETTINGS.dimension];
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
			document.getElementById("directions_result_route").innerHTML = `<div class="info_center"><span class="material-icons large">refresh</span></div>`;
			setTimeout(findRoute, 500);
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
	calculateDistance: (stations, stationId1, stationId2) => {
		const station1 = stations[stationId1];
		const station2 = stations[stationId2];
		return Math.abs(station2["x"] - station1["x"]) + Math.abs(station2["z"] - station1["z"]);
	},
	drawDirectionsRoute: (pathStations, pathRoutes) => {
		if (pathStations.length > 0 || pathRoutes.length > 0) {
			SETTINGS.selectedRoutes = [];
		}
		SETTINGS.selectedDirectionsStations = pathStations;
		SETTINGS.selectedDirectionsSegments = {};
		for (let i = 0; i < pathRoutes.length; i++) {
			if (pathRoutes[i] != null) {
				for (let j = 0; j < pathRoutes[i].length; j++) {
					const routeId = pathRoutes[i][j]["id"];
					if (!(routeId in SETTINGS.selectedDirectionsSegments)) {
						SETTINGS.selectedDirectionsSegments[routeId] = [];
					}
					SETTINGS.selectedDirectionsSegments[routeId].push(pathStations[i] + "_" + pathStations[i + 1]);
				}
			}
		}
		DATA.redraw();
	},
};

const findRoutePart = (globalBlacklist, maxTime) => {
	const {stations, connections} = DATA.json[SETTINGS.dimension];
	if (!(stationStart in stations) || !(stationEnd in stations)) {
		return [[], [], []];
	}

	const getCloserStationWithRoute = elapsedTime => {
		const currentStation = tempPathStations[tempPathStations.length - 1];
		const stationToRoutes = {};
		let bestStation = 0;
		let bestIncrease = Number.MIN_SAFE_INTEGER;
		let bestDuration = 0;

		for (const connection of connections[currentStation]) {
			const checkStation = connection["station"];
			const route = connection["route"];
			const duration = connection["duration"];

			if (!(checkStation in stationToRoutes)) {
				stationToRoutes[checkStation] = [];
			}
			if (route != null) {
				stationToRoutes[checkStation].push(route);
			}

			if (
				duration > 0 && elapsedTime + duration < maxTime &&
				(!(checkStation in localBlacklist) || elapsedTime + duration < localBlacklist[checkStation]) &&
				(!(checkStation in globalBlacklist) || elapsedTime + duration <= globalBlacklist[checkStation])
			) {
				const increase = (DIRECTIONS.calculateDistance(stations, stationEnd, currentStation) - DIRECTIONS.calculateDistance(stations, stationEnd, checkStation)) / duration;
				globalBlacklist[checkStation] = elapsedTime + duration;

				if (increase > bestIncrease) {
					bestStation = checkStation;
					bestIncrease = increase;
					bestDuration = duration;
				}
			}
		}

		localBlacklist[bestStation] = elapsedTime + bestDuration;
		return [bestStation, stationToRoutes[bestStation], bestDuration];
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
};

const findRoute = () => {
	pathStations = [];
	pathRoutes = [];

	const millis = Date.now();
	const globalBlacklist = {};
	globalBlacklist[stationStart] = 0;
	let tries = 0;
	let totalTime = Number.MAX_SAFE_INTEGER;
	let times = [];

	while (tries < 500) {
		const path = findRoutePart(globalBlacklist, totalTime);
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
	const stations = DATA.json[SETTINGS.dimension]["stations"];
	resultElement.innerHTML = "";

	if (hasRoute) {
		const purgeRoutes = (currentIndex, oldIndex) => {
			const currentRoutes = pathRoutes[currentIndex];
			const oldRoutes = pathRoutes[oldIndex];
			if (oldRoutes.some(route => currentRoutes.includes(route))) {
				pathRoutes[oldIndex] = oldRoutes.filter(route => currentRoutes.includes(route));
				return false;
			} else {
				return true;
			}
		}
		for (let i = 1; i < pathRoutes.length; i++) {
			for (let j = i - 1; j >= 0; j--) {
				if (purgeRoutes(i, j)) {
					break;
				}
			}
		}
		for (let i = pathRoutes.length - 2; i >= 0; i--) {
			for (let j = i + 1; j < pathRoutes.length; j++) {
				if (purgeRoutes(i, j)) {
					break;
				}
			}
		}

		let routes = pathRoutes.length === 0 ? [] : pathRoutes[0];
		let station = pathStations[0];
		let totalStationCount = 0;
		let totalInterchangeCount = 0;
		let startZone = stations[station]["zone"];
		let fare = 0;
		let time = 0;
		let stationCount = 0;

		resultElement.append(UTILITIES.getDrawStationElement(createStationElement(stations[station]["name"].replace(/\|/g, " ")), null, routes.length === 0 ? null : routes[0]["color"]));

		for (let i = 1; i <= pathRoutes.length; i++) {
			const nextRoutes = i === pathRoutes.length ? [] : pathRoutes[i];
			time += times[i - 1];
			stationCount++;

			if (i === pathRoutes.length || !compareArrays(routes, nextRoutes)) {
				const nextStation = pathStations[i];
				const isWalking = routes.length === 0;
				const isNextWalking = nextRoutes.length === 0;
				const color = isWalking ? null : routes[0]["color"];

				if (isWalking) {
					const walkingElement = document.createElement("span");
					walkingElement.innerHTML = Math.round(DIRECTIONS.calculateDistance(stations, station, nextStation) / 100) / 10 + " km";
					resultElement.append(UTILITIES.getDrawLineElement("directions_walk", walkingElement, null));
					startZone = stations[nextStation]["zone"];
				} else {
					const routeIconsAndDestinations = {};
					const routeTypes = {};
					routes.forEach(route => {
						const routeName = route["name"].split("||")[0].replace(/\|/g, " ");
						if (!(routeName in routeIconsAndDestinations)) {
							routeIconsAndDestinations[routeName] = [];
						}
						const routeStations = route["stations"];
						const routeNumber = route["number"] + (route["number"] === "" ? "" : " ");
						if (routeStations.length > 0) {
							const iconAndDestination = route["circular"] === "" ? ["chevron_right", routeNumber + stations[routeStations[routeStations.length - 1].split("_")[0]]["name"]] : [route["circular"] === "cw" ? "rotate_right" : "rotate_left", routeNumber + ACTIONS.getClosestInterchangeOnRoute(route, station)];
							if (!routeIconsAndDestinations[routeName].some(iconAndDestinationCheck => iconAndDestinationCheck[0] === iconAndDestination[0] && iconAndDestinationCheck[1] === iconAndDestination[1])) {
								routeIconsAndDestinations[routeName].push(iconAndDestination);
							}
						}
						routeTypes[routeName] = route["type"];
						if (!(route["type"] in SETTINGS.selectedRouteTypes)) {
							SETTINGS.selectedRouteTypes.push(route["type"]);
						}
					});

					Object.keys(routeIconsAndDestinations).forEach(routeName => {
						const routeNameElement = document.createElement("span");
						routeNameElement.innerHTML = routeName;
						resultElement.append(UTILITIES.getDrawLineElement(UTILITIES.routeTypes[routeTypes[routeName]], routeNameElement, color));

						routeIconsAndDestinations[routeName].forEach(iconAndDestination => {
							const routeDestinationElement = document.createElement("span");
							routeDestinationElement.innerHTML = iconAndDestination[1].replace(/\|/g, " ");
							resultElement.append(UTILITIES.getDrawLineElement("&nbsp;&nbsp;&nbsp;&nbsp;" + iconAndDestination[0], routeDestinationElement, color));
						})
					})

					const stationCountElement = document.createElement("span");
					stationCountElement.innerHTML = stationCount.toString();
					resultElement.append(UTILITIES.getDrawLineElement("commit", stationCountElement, color));

					if (isNextWalking) {
						fare += Math.abs(startZone - stations[nextStation]["zone"]) + 2;
					}
				}

				const durationElement = document.createElement("span");
				durationElement.innerHTML = UTILITIES.formatTime(time / 20);
				resultElement.append(UTILITIES.getDrawLineElement("schedule", durationElement, color));
				resultElement.append(UTILITIES.getDrawStationElement(createStationElement(stations[nextStation]["name"].replace(/\|/g, " ")), color, nextRoutes.length === 0 ? null : nextRoutes[0]["color"]));

				routes = nextRoutes;
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
			`<span class="text">${UTILITIES.formatTime(totalTime / 20)}</span>` +
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
		resultElement.innerHTML = `<div class="info_center"><span class="material-icons large">warning_amber</span></div>`;
	}

	document.getElementById("directions").style.maxHeight = window.innerHeight - 80 + "px";
	DIRECTIONS.drawDirectionsRoute(pathStations, pathRoutes, true);
};

const createStationElement = stationName => {
	const stationElement = document.createElement("div");
	stationElement.innerText = stationName;
	stationElement.className = "text";
	return stationElement;
};

const compareArrays = (array1, array2) => {
	if (array1.length !== array2.length) {
		return false;
	} else {
		return array1.every(element => array2.includes(element));
	}
};

document.getElementById("directions_icon").onclick = () => {
	DOCUMENT.clearPanes(true);
	DIRECTIONS.drawDirectionsRoute(pathStations, pathRoutes);
	document.getElementById("directions").style.display = "block";
};

export default DIRECTIONS;
