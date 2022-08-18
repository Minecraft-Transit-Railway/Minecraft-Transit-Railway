import UTILITIES from "./utilities.js";
import DRAWING from "./drawing.js";
import SETTINGS from "./settings.js";
import ACTIONS from "./actions.js";

const DATA = {
	parseOBA: (result, latOffset, lonOffset, zoomOffset) => {
		const {list, references} = result["data"];
		const jsonPart = {"routes": [], "positions": {}, "stations": {}, "types": []};
		const tempStations = {};
		const stationIdToName = {};
		const stationNameToFirstId = {};
		const formatId = id => id.replaceAll("_", ".");
		const formatX = lon => (lon - lonOffset) * zoomOffset;
		const formatY = lat => -(lat - latOffset) * zoomOffset;

		references["stops"].forEach(data => {
			const {name, id, lat, lon} = data;
			const newName = name.split(" ").sort().join(" ");

			if (!(newName in tempStations)) {
				tempStations[newName] = {"xPositions": [], "yPositions": []};
				stationNameToFirstId[newName] = id;
			}

			stationIdToName[id] = newName;
			tempStations[newName]["name"] = name;
			tempStations[newName]["xPositions"].push(formatX(lon));
			tempStations[newName]["yPositions"].push(formatY(lat));
		});

		Object.keys(tempStations).forEach(key => {
			const {name, xPositions, yPositions} = tempStations[key];
			jsonPart["stations"][formatId(stationNameToFirstId[key])] = {
				"name": name,
				"color": colorFromCode(key),
				"zone": 0,
				"x": listAverage(xPositions),
				"z": listAverage(yPositions),
			};
		});

		list.forEach(data => {
			const {schedule, tripId} = data;
			const {stopTimes} = schedule;
			const routeId = references["trips"].find(trip => trip["id"] === tripId)["routeId"];
			const routeReference = references["routes"].find(route => route["id"] === routeId);
			const {color, shortName, longName, description, type} = routeReference;
			const stations = [];
			const durations = [];
			const densities = [];

			for (let i = 0; i < stopTimes.length; i++) {
				const stopId = stopTimes[i]["stopId"];
				const positionId = formatId(stationNameToFirstId[stationIdToName[stopId]]) + "_" + formatId(routeId);
				stations.push(positionId);
				densities.push(0);
				if (i > 0) {
					durations.push((stopTimes[i]["arrivalTime"] - stopTimes[i - 1]["arrivalTime"]) * 20);
				}
				const stopReference = references["stops"].find(stop => stop["id"] === stopId);
				jsonPart["positions"][positionId] = {
					"x": formatX(stopReference["lon"]),
					"y": formatY(stopReference["lat"]),
					"angle": UTILITIES.directionToAngle(stopReference["direction"]),
				};
			}

			const convertedType = UTILITIES.convertGtfsRouteType(type);
			jsonPart["routes"].push({
				"id": formatId(routeId),
				"color": color === "" ? colorFromCode(routeId) : parseInt(color, 16),
				"name": longName ? longName : description,
				"number": shortName,
				"type": convertedType,
				"stations": stations,
				"durations": durations,
				"densities": densities,
				"circular": "",
			});
			if (!jsonPart["types"].includes(convertedType)) {
				jsonPart["types"].push(convertedType);
			}
		});

		DATA.parseMTR([jsonPart]);
	},
	parseMTR: result => {
		DATA.json = result;
		ACTIONS.setupRouteTypeAndDimensionButtons();
		const {routes, positions, stations} = result[SETTINGS.dimension];
		const elementRoutes = document.getElementById("search_results_routes");
		elementRoutes.innerHTML = "";
		const elementStations = document.getElementById("search_results_stations");
		elementStations.innerHTML = "";

		const routesToShow = [];
		routes.forEach(route => {
			if (!("id" in route)) {
				route["id"] = route["color"].toString();
			}
			if (SETTINGS.selectedRouteTypes.includes(route["type"])) {
				routesToShow.push(route["id"]);
			}
			elementRoutes.append(ACTIONS.getRouteElement(route["id"], route["color"], route["name"], route["number"], route["type"], false, true, "search_route_" + route["id"], true));
		});

		const tempStations = {};
		Object.keys(positions).forEach(key => {
			const nameSplit = key.split("_");
			const stationId = nameSplit[0];
			const routeId = nameSplit[1];

			if (routesToShow.includes(routeId)) {
				if (!(stationId in tempStations)) {
					tempStations[stationId] = {"xPositions": [], "yPositions": [], "routeIds": [], "types": []};
					UTILITIES.angles.forEach(angle => tempStations[stationId][`routes${angle}`] = []);
				}

				const {x, y, vertical} = positions[key];
				const angle = "angle" in positions[key] ? positions[key]["angle"] : UTILITIES.angles[vertical ? 0 : 2]; // TODO
				tempStations[stationId]["xPositions"].push(x);
				tempStations[stationId]["yPositions"].push(y);
				tempStations[stationId][`routes${angle}`].push(routeId);
			}
		});

		Object.values(tempStations).forEach(tempStation => {
			UTILITIES.angles.forEach(angle => tempStation[`routes${angle}`] = tempStation[`routes${angle}`].sort());
			tempStation["x"] = listAverage(tempStation["xPositions"]);
			tempStation["y"] = listAverage(tempStation["yPositions"]);
			delete tempStation["xPositions"];
			delete tempStation["yPositions"];
		});

		const lineQueue = {};
		routes.forEach(route => {
			const routeStations = route["stations"];
			for (let i = 0; i < routeStations.length; i++) {
				const routeId = route["id"];
				const stopId2 = routeStations[i].split("_")[0];

				if (i > 0) {
					const stopId1 = routeStations[i - 1].split("_")[0];
					if (stopId1 !== stopId2 && routesToShow.includes(routeId)) {
						const key = routeId + [stopId1, stopId2].sort().join(" ");
						if (!(key in lineQueue)) {
							lineQueue[key] = {
								"color": UTILITIES.convertColor(route["color"]),
								"segments": [getSegmentDetails(stopId1, routeId, tempStations), getSegmentDetails(stopId2, routeId, tempStations)],
								"selected": SETTINGS.selectedRoutes.length === 0 || SETTINGS.selectedRoutes.includes(routeId),
								"id": routeId,
							};
						}
					}
				}

				if (stopId2 in tempStations) {
					tempStations[stopId2]["routeIds"].push(routeId);
					tempStations[stopId2]["types"].push(route["type"]);
				}
			}
		});

		const stationQueue = {};
		for (const stationId in tempStations) {
			const routeCounts = UTILITIES.angles.map(angle => tempStations[stationId][`routes${angle}`].length);
			stationQueue[stations[stationId]["name"]] = {
				"id": stationId,
				"width": (Math.max(1, Math.max(routeCounts[0], routeCounts[1])) + 1) * SETTINGS.size * 6,
				"height": (Math.max(1, Math.max(routeCounts[2], routeCounts[3])) + 1) * SETTINGS.size * 6,
				"left": tempStations[stationId]["x"],
				"top": tempStations[stationId]["y"],
				"angle": routeCounts[1] + routeCounts[3] > routeCounts[0] + routeCounts[2] ? 45 : 0,
				"selected": SETTINGS.selectedRoutes.length === 0 || tempStations[stationId]["routeIds"].some(routeId => SETTINGS.selectedRoutes.includes(routeId)),
				"types": tempStations[stationId]["types"],
			};
			UTILITIES.angles.forEach(angle => stationQueue[stations[stationId]["name"]][`routes${angle}`] = tempStations[stationId][`routes${angle}`]);
		}

		Object.keys(stations).forEach(stationId => {
			const element = ACTIONS.getStationElement(stations[stationId]["color"], stations[stationId]["name"], stationId, "search_station_" + stationId);
			element.setAttribute("style", "display: none");
			elementStations.append(element);
		});

		DRAWING.drawMap(lineQueue, stationQueue);
	},
	redraw: () => DATA.parseMTR(DATA.json),
	json: [],
};

const getSegmentDetails = (stopId, routeId, tempStations) => {
	const tempStation = tempStations[stopId];
	const direction = UTILITIES.angles.find(angle => tempStation[`routes${angle}`].includes(routeId));
	return {
		"x": tempStation["x"],
		"y": tempStation["y"],
		"direction": direction,
		"offsetIndex": tempStation[`routes${direction}`].indexOf(routeId),
		"routeCount": tempStation[`routes${direction}`].length,
	};
};
const listAverage = list => list.length === 0 ? 0 : list.reduce((a, b) => a + b, 0) / list.length;
const colorFromCode = code => Math.round(code.replace(/[^0-9]/g, "") / 200 * 0xFFFFFF) % 0xFFFFFF;

export default DATA;
