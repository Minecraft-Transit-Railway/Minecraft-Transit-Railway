import UTILITIES from "./utilities.js";
import DRAWING from "./drawing.js";
import SETTINGS from "./settings.js";

const DATA = {
	parseOBA: (result, latOffset, lonOffset, zoomOffset) => {
		const {list, references} = result["data"];
		const tempStations = {};
		const stationIdMap = {};
		const stationNameMap = {};

		references["stops"].forEach(data => {
			const {name, id, direction, lat, lon, routeIds} = data;
			const newName = name.split(" ").sort().join(" ");

			if (!(newName in tempStations)) {
				tempStations[newName] = {"latValues": [], "lonValues": []};
				UTILITIES.angles.forEach(angle => tempStations[newName][`routes${angle}`] = []);
			}

			routeIds.forEach(routeId => {
				if (UTILITIES.angles.every(angle => !tempStations[newName][`routes${angle}`].includes(routeId))) {
					tempStations[newName][`routes${UTILITIES.directionToAngle(direction)}`].push(routeId);
				}
			});
			tempStations[newName]["latValues"].push((lat - latOffset) * zoomOffset);
			tempStations[newName]["lonValues"].push((lon - lonOffset) * zoomOffset);
			stationIdMap[id] = newName;
			stationNameMap[newName] = name;
		});

		processTempStations(tempStations);
		const lineQueue = {};

		list.forEach(data => {
			const {schedule, tripId} = data;
			const {stopTimes} = schedule;
			const routeId = references["trips"].find(trip => trip["id"] === tripId)["routeId"];
			const colorString = references["routes"].find(route => route["id"] === routeId)["color"];
			const color = colorString === "" ? UTILITIES.convertColor(Math.round(routeId.replace(/[^0-9]/g, "") / 200 * 0xFFFFFF) % 0xFFFFFF) : colorString;

			for (let i = 0; i < stopTimes.length - 1; i++) {
				const stopId1 = stopTimes[i]["stopId"];
				const stopId2 = stopTimes[i + 1]["stopId"];
				if (stopId1 !== stopId2) {
					const key = color + [stopId1, stopId2].sort().join(" ");
					if (!(key in lineQueue)) {
						lineQueue[key] = {
							"color": `#${color}`,
							"segments": [getSegmentDetails(stopId1, routeId, tempStations, name => stationIdMap[name]), getSegmentDetails(stopId2, routeId, tempStations, name => stationIdMap[name])],
						};
					}
				}
			}
		});

		DRAWING.drawMap(lineQueue, tempStationsToStationQueue(tempStations, name => stationNameMap[name]));
	},
	parseMTR: result => {
		const {routes, positions, stations, types} = result[SETTINGS.dimension];
		const tempStations = {};

		Object.keys(positions).forEach(key => {
			const nameSplit = key.split("_");
			const stationId = nameSplit[0];
			if (!(stationId in tempStations)) {
				tempStations[stationId] = {"lonValues": [], "latValues": []};
				UTILITIES.angles.forEach(angle => tempStations[stationId][`routes${angle}`] = []);
			}
			const {x, y, vertical} = positions[key];
			tempStations[stationId]["lonValues"].push(x);
			tempStations[stationId]["latValues"].push(-y);
			tempStations[stationId][`routes${UTILITIES.angles[vertical ? 2 : 0]}`].push(nameSplit[1]);
		});

		processTempStations(tempStations);
		const lineQueue = {};

		routes.forEach(route => {
			const routeStations = route["stations"];
			for (let i = 0; i < routeStations.length - 1; i++) {
				const stopId1 = routeStations[i].split("_")[0];
				const stopId2 = routeStations[i + 1].split("_")[0];
				const color = routeStations[i].split("_")[1];
				if (stopId1 !== stopId2) {
					const key = color + [stopId1, stopId2].sort().join(" ");
					if (!(key in lineQueue)) {
						lineQueue[key] = {
							"color": `#${UTILITIES.convertColor(color)}`,
							"segments": [getSegmentDetails(stopId1, color, tempStations, stopId => stopId), getSegmentDetails(stopId2, color, tempStations, stopId => stopId)],
						};
					}
				}
			}
		});

		DRAWING.drawMap(lineQueue, tempStationsToStationQueue(tempStations, stationId => stations[stationId]["name"]));
	},
};

const processTempStations = tempStations => {
	const listAverage = list => list.length === 0 ? 0 : list.reduce((a, b) => a + b, 0) / list.length;
	Object.values(tempStations).forEach(tempStation => {
		UTILITIES.angles.forEach(angle => tempStation[`routes${angle}`] = tempStation[`routes${angle}`].sort());
		tempStation["x"] = listAverage(tempStation["lonValues"]);
		tempStation["y"] = -listAverage(tempStation["latValues"]);
		delete tempStation["lonValues"];
		delete tempStation["latValues"];
	});
};
const tempStationsToStationQueue = (tempStations, getStationName) => {
	const stationQueue = {};
	for (const name in tempStations) {
		const routeCounts = UTILITIES.angles.map(angle => tempStations[name][`routes${angle}`].length);
		stationQueue[getStationName(name)] = {
			"width": Math.max(1, Math.max(routeCounts[0], routeCounts[1])) * UTILITIES.lineWidth + UTILITIES.lineWidth,
			"height": Math.max(1, Math.max(routeCounts[2], routeCounts[3])) * UTILITIES.lineWidth + UTILITIES.lineWidth,
			"left": tempStations[name]["x"],
			"top": tempStations[name]["y"],
			"angle": routeCounts[1] + routeCounts[3] > routeCounts[0] + routeCounts[2] ? 45 : 0,
		};
		UTILITIES.angles.forEach(angle => stationQueue[getStationName(name)] [`routes${angle}`] = tempStations[name][`routes${angle}`]);
	}
	return stationQueue;
};
const getSegmentDetails = (stopId, routeId, tempStations, getStationName) => {
	const tempStation = tempStations[getStationName(stopId)];
	const direction = UTILITIES.angles.find(angle => tempStation[`routes${angle}`].includes(routeId));
	return {
		"x": tempStation["x"],
		"y": tempStation["y"],
		"direction": direction,
		"offsetIndex": tempStation[`routes${direction}`].indexOf(routeId),
		"routeCount": tempStation[`routes${direction}`].length,
	};
};

export default DATA;
