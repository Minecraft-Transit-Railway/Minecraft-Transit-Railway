import SETTINGS from "./index.js";
import CANVAS from "./utilities.js"

const TEXT_PADDING = 6;
const MAX_ARRIVALS = 5;
const FILTER = new PIXI.filters.BlurFilter();
const SEARCH_BOX_ELEMENT = document.getElementById("search_box");

const convertColor = color => "#" + Number(color).toString(16).padStart(6, "0");

let graphicsRoutesLayer1 = [];
let graphicsRoutesLayer2 = [];
let graphicsStationsLayer1 = [];
let graphicsStationsLayer2 = [];
let graphicsStationsTextLayer1 = [];
let graphicsStationsTextLayer2 = [];
let textStations = [];

let selecting = false;
let fetchArrivalId = 0;
let refreshArrivalId = 0;
let arrivalData = [];

let selectedColor = -1;
let selectedStation = 0;

document.getElementById("clear_station_info_button").onclick = onClearStationInfo;

const clearAndDestroy = array => {
	for (const index in array) {
		array[index].clear();
		array[index].destroy();
	}
};

const createClickable = (container, initialize, onClick) => {
	const graphics = new PIXI.Graphics();
	graphics.interactive = true;
	graphics.buttonMode = true;
	graphics.on("pointerdown", event => {
		CANVAS.onCanvasMouseDown(event);
		selecting = true;
	});
	graphics.on("pointermove", event => {
		if (CANVAS.onCanvasMouseMove(event, container)) {
			selecting = false;
		}
	});
	graphics.on("pointerup", event => {
		CANVAS.onCanvasMouseUp(event);
		if (selecting) {
			onClick();
		}
		selecting = false;
	});
	graphics.on("pointerover", () => graphics.filters = [FILTER]);
	graphics.on("pointerout", () => graphics.filters = []);
	initialize(graphics);
	initialize(new PIXI.Graphics());
};

const fetchArrivals = () => {
	if (selectedStation !== 0) {
		clearTimeout(fetchArrivalId);
		fetch(SETTINGS.arrivalsUrl + "?worldIndex=" + SETTINGS.dimension + "&stationId=" + selectedStation, {cache: "no-cache"}).then(response => response.json()).then(result => {
			arrivalData = result;
			fetchArrivalId = setTimeout(fetchArrivals, SETTINGS.refreshArrivalsInterval);
			refreshArrivals();
		});
	}
};

const refreshArrivals = () => {
	if (selectedStation !== 0) {
		clearTimeout(refreshArrivalId);
		const arrivalsHtml = {};
		for (const arrivalIndex in arrivalData) {
			const {arrival, destination, platform, color} = arrivalData[arrivalIndex];
			const currentMillis = Date.now();
			const arrivalDifference = Math.floor((arrival - currentMillis) / 1000);
			const minute = Math.floor(arrivalDifference / 60);
			const second = (arrivalDifference % 60).toString().padStart(2, "0");
			const destinationSplit = destination.split("|");
			if (typeof arrivalsHtml[color] === "undefined") {
				arrivalsHtml[color] = {html: "", count: 0};
			}
			if (arrivalsHtml[color]["count"] < MAX_ARRIVALS) {
				arrivalsHtml[color]["html"] +=
					`<div class="arrival">` +
					`<span class="arrival_text left_align" style="width: 70%">${destinationSplit[Math.floor(currentMillis / 3000) % destinationSplit.length]}</span>` +
					`<span class="arrival_text" style="width: 10%">${platform}</span>` +
					`<span class="arrival_text right_align" style="width: 20%; text-align: right">${arrivalDifference < 0 ? "" : minute + ":" + second}</span>` +
					`</div>`;
				arrivalsHtml[color]["count"]++;
			}
		}
		for (const color in arrivalsHtml) {
			const arrivalElement = document.getElementById("station_arrivals_" + color);
			if (arrivalElement != null) {
				arrivalElement.innerHTML = arrivalsHtml[color]["html"];
			}
		}
		refreshArrivalId = setTimeout(refreshArrivals, 1000);
	}
};

function drawMap(container, data) {
	const getStationElement = (color, name, id) => {
		const element = document.createElement("div");
		element.setAttribute("id", id);
		element.setAttribute("class", "clickable");
		element.setAttribute("style", "display: none");
		element.onclick = () => onClickStation(id);
		element.innerHTML =
			`<span class="station" style="background: ${convertColor(color)}"></span>` +
			`<span class="text">${name.replace(/\|/g, " ")}</span>`;
		return element;
	};

	const getRouteElement = (color, name, type, visible, id, showColor) => {
		const element = document.createElement("div");
		element.setAttribute("id", id);
		element.setAttribute("class", "clickable");
		if (!visible) {
			element.setAttribute("style", "display: none");
		}
		element.onclick = () => onClickLine(color);
		element.innerHTML =
			`<span class="line" style="background: ${convertColor(showColor ? color : SETTINGS.getColorStyle("--textColorDisabled"))}"></span>` +
			`<span class="${showColor ? "text" : "text_disabled"} material-icons tight">${SETTINGS.routeTypes[type]}</span>` +
			`<span class="${showColor ? "text" : "text_disabled"}">${name.replace(/\|/g, " ")}</span>`;
		return element;
	};

	const onClickStation = id => {
		SETTINGS.onClearSearch(data, false);
		const {name, color} = stations[id];
		const stationInfoElement = document.getElementById("station_info");
		stationInfoElement.removeAttribute("style");

		const {xMin, yMin, xMax, yMax} = data["blobs"][id];
		CANVAS.slideTo(container, -(xMin + xMax) / 2 + window.innerWidth / 2, -(yMin + yMax) / 2 + window.innerHeight / 2);

		let stationNameHtml = "";
		const nameSplit = name.split("|");
		for (const nameSplitIndex in nameSplit) {
			const namePart = nameSplit[nameSplitIndex];
			if (SETTINGS.isCJK(namePart)) {
				stationNameHtml += "<h1>" + namePart + "</h1>";
			} else {
				stationNameHtml += "<h2>" + namePart + "</h2>";
			}
		}
		document.getElementById("station_name").innerHTML = stationNameHtml;

		document.getElementById("station_line").style.backgroundColor = convertColor(color);

		const stationRoutesElement = document.getElementById("station_routes");
		stationRoutesElement.innerHTML = "";
		const addedRouteColors = [];
		for (const routeIndex in routes) {
			const route = routes[routeIndex];
			if (!addedRouteColors.includes(route["color"])) {
				for (const stationIndex in route["stations"]) {
					if (route["stations"][stationIndex].split("_")[0] === id) {
						stationRoutesElement.append(getRouteElement(route["color"], route["name"].replace(/\|/g, " "), route["type"], true, "", true, () => onClickLine(container, data, color)));
						addedRouteColors.push(route["color"]);

						const arrivalElement = document.createElement("div");
						arrivalElement.setAttribute("id", "station_arrivals_" + route["color"]);
						stationRoutesElement.append(arrivalElement);

						const spacerElement = document.createElement("div");
						spacerElement.setAttribute("class", "spacer");
						stationRoutesElement.append(spacerElement);

						break;
					}
				}
			}
		}

		if (stationRoutesElement.lastChild != null) {
			stationRoutesElement.removeChild(stationRoutesElement.lastChild);
		}

		stationInfoElement.style.maxHeight = window.innerHeight - 80 + "px";
		selectedStation = id;
		fetchArrivals();
	};

	const onClickLine = color => {
		selectedColor = selectedColor === color ? -1 : color;
		drawMap(container, data);
	};

	SEARCH_BOX_ELEMENT.onchange = () => onSearch(data);
	SEARCH_BOX_ELEMENT.onpaste = () => onSearch(data);
	SEARCH_BOX_ELEMENT.oninput = () => onSearch(data);

	clearAndDestroy(graphicsRoutesLayer1);
	clearAndDestroy(graphicsRoutesLayer2);
	clearAndDestroy(graphicsStationsLayer1);
	clearAndDestroy(graphicsStationsLayer2);
	clearAndDestroy(graphicsStationsTextLayer1);
	clearAndDestroy(graphicsStationsTextLayer2);
	graphicsRoutesLayer1 = [];
	graphicsRoutesLayer2 = [];
	graphicsStationsLayer1 = [];
	graphicsStationsLayer2 = [];
	graphicsStationsTextLayer1 = [];
	graphicsStationsTextLayer2 = [];
	textStations = [];
	container.children = [];

	data["blobs"] = {};
	const {blobs, positions, stations, routes, types} = data;

	const visitedPositions = {};
	for (const positionKey in positions) {
		const position = positions[positionKey];
		const x = CANVAS.convertX(position["x"]);
		const y = CANVAS.convertY(position["y"]);

		const stationId = positionKey.split("_")[0];
		const color = parseInt(positionKey.split("_")[1]);

		let newX = x;
		let newY = y;
		if (position["vertical"]) {
			let i = 1;
			while (typeof visitedPositions[stationId + "_x_" + newX] !== "undefined" && visitedPositions[stationId + "_x_" + newX] !== color) {
				newX = x + i * SETTINGS.lineSize;
				i = i > 0 ? -i : -i + 1;
			}
			visitedPositions[stationId + "_x_" + newX] = color;
		} else {
			let i = 1;
			while (typeof visitedPositions[stationId + "_y_" + newY] !== "undefined" && visitedPositions[stationId + "_y_" + newY] !== color) {
				newY = y + i * SETTINGS.lineSize;
				i = i > 0 ? -i : -i + 1;
			}
			visitedPositions[stationId + "_y_" + newY] = color;
		}

		position["x2"] = newX;
		position["y2"] = newY;

		const route = routes.find(route => route["color"] === color);
		if (typeof route !== "undefined" && route["type"] === types[SETTINGS.routeType]) {
			const blob = blobs[stationId];
			if (typeof blob === "undefined") {
				blobs[stationId] = {
					xMin: newX,
					yMin: newY,
					xMax: newX,
					yMax: newY,
					name: stations[stationId]["name"],
					colors: [color],
				};
			} else {
				blob["xMin"] = Math.min(blob["xMin"], newX);
				blob["yMin"] = Math.min(blob["yMin"], newY);
				blob["xMax"] = Math.max(blob["xMax"], newX);
				blob["yMax"] = Math.max(blob["yMax"], newY);
				if (!blob["colors"].includes(color)) {
					blob["colors"].push(color);
				}
			}
		}
	}

	const routeNames = {};
	const routeTypes = {};
	const sortedColors = [];
	for (const routeKey in routes) {
		const route = routes[routeKey];
		const color = route["color"];
		const shouldDraw = selectedColor < 0 || selectedColor === color;
		const routeType = route["type"];
		for (const stationIndex in route["stations"]) {
			const blob = blobs[route["stations"][stationIndex].split("_")[0]];
			if (typeof blob !== "undefined") {
				blob[routeType] = true;
			}
		}

		if (routeType === types[SETTINGS.routeType]) {
			createClickable(container, graphicsRoute => {
				(shouldDraw ? graphicsRoutesLayer2 : graphicsRoutesLayer1).push(graphicsRoute);
				graphicsRoute.beginFill(shouldDraw ? color : SETTINGS.getColorStyle("--textColorDisabled"));

				let prevX = undefined;
				let prevY = undefined;
				let prevVertical = undefined;
				for (const stationIndex in route["stations"]) {
					const id = route["stations"][stationIndex];
					const {x2, y2, vertical} = positions[id];

					if (typeof prevX !== "undefined" && typeof prevY !== "undefined") {
						CANVAS.drawLine(graphicsRoute, prevX, prevY, prevVertical, x2, y2, vertical);
					}

					prevX = x2;
					prevY = y2;
					prevVertical = vertical;
				}

				graphicsRoute.endFill();
			}, () => onClickLine(color));
		}

		routeNames[color] = route["name"];
		routeTypes[color] = routeType;
		if (!sortedColors.includes(color)) {
			sortedColors.push(color);
		}
	}
	sortedColors.sort();

	for (const stationId in blobs) {
		const blob = blobs[stationId];
		const {xMin, yMin, xMax, yMax, colors, name} = blob;
		if (blob[types[SETTINGS.routeType]]) {
			const shouldDraw = selectedColor < 0 || colors.includes(selectedColor);

			createClickable(container, graphicsStation => {
				(shouldDraw ? graphicsStationsLayer2 : graphicsStationsLayer1).push(graphicsStation);
				graphicsStation.beginFill(SETTINGS.getColorStyle("--backgroundColor"));
				graphicsStation.lineStyle(2, SETTINGS.getColorStyle(shouldDraw ? "--textColor" : "--textColorDisabled"), 1);

				if (xMin === xMax && yMin === yMax) {
					graphicsStation.drawCircle(xMin, yMin, SETTINGS.lineSize);
				} else {
					graphicsStation.drawRoundedRect(xMin - SETTINGS.lineSize, yMin - SETTINGS.lineSize, xMax - xMin + SETTINGS.lineSize * 2, yMax - yMin + SETTINGS.lineSize * 2, SETTINGS.lineSize);
				}
				graphicsStation.endFill();
			}, () => onClickStation(stationId));

			if (SETTINGS.showText && shouldDraw) {
				let icons = "";
				types.forEach(key => {
					if (typeof blob[key] !== "undefined" && key !== types[SETTINGS.routeType]) {
						icons += SETTINGS.routeTypes[key];
					}
				});
				CANVAS.drawText(textStations, name, icons, (xMin + xMax) / 2, yMax + SETTINGS.lineSize);
			}
		}
	}

	for (const index in graphicsRoutesLayer1) {
		container.addChild(graphicsRoutesLayer1[index]);
	}
	for (const index in graphicsStationsLayer1) {
		container.addChild(graphicsStationsLayer1[index]);
	}
	for (const index in graphicsRoutesLayer2) {
		container.addChild(graphicsRoutesLayer2[index]);
	}
	for (const index in graphicsStationsLayer2) {
		container.addChild(graphicsStationsLayer2[index]);
	}
	for (const index in textStations) {
		container.addChild(textStations[index]);
	}

	const elementRoutes = document.getElementById("search_results_routes");
	elementRoutes.innerHTML = "";
	for (const colorIndex in sortedColors) {
		const color = sortedColors[colorIndex];
		elementRoutes.append(getRouteElement(color, routeNames[color], routeTypes[color], false, color, selectedColor < 0 || selectedColor === color));
	}
	const elementStations = document.getElementById("search_results_stations");
	elementStations.innerHTML = "";
	for (const stationId in stations) {
		elementStations.append(getStationElement(stations[stationId]["color"], stations[stationId]["name"], stationId));
	}

	document.getElementById("loading").style.display = "none";
	onSearch(data);
}

function onSearch(data) {
	const searchBox = document.getElementById("search_box");
	const search = searchBox.value.toLowerCase();
	document.getElementById("clear_search_icon").innerText = search === "" ? "" : "clear";

	const {stations, routes} = data;

	const resultsStations = search === "" ? [] : Object.keys(stations).filter(station => stations[station]["name"].toLowerCase().includes(search));
	for (const stationId in stations) {
		document.getElementById(stationId).style.display = resultsStations.includes(stationId) ? "block" : "none";
	}

	const resultsRoutes = search === "" ? [] : Object.keys(routes).filter(route => routes[route]["name"].toLowerCase().includes(search));
	for (const routeIndex in routes) {
		document.getElementById(routes[routeIndex]["color"]).style.display = resultsRoutes.includes(routeIndex) ? "block" : "none";
	}

	const maxHeight = (window.innerHeight - 80) / 2;
	document.getElementById("search_results_stations").style.maxHeight = maxHeight + "px";
	document.getElementById("search_results_routes").style.maxHeight = maxHeight + "px";

	if (search !== "") {
		onClearStationInfo();
	}
}

function onClearStationInfo() {
	selectedStation = 0;
	document.getElementById("station_info").style.display = "none";
}

export default drawMap;
